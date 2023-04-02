





import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import DefaultNamespace.Token_server;
import DefaultNamespace.Token_serverServiceLocator;

import redis.clients.jedis.Jedis;


public class MQSender {

	private static final String EXCHANGE_NAME = "medics";
	
	private static final String EXCHANGE_NAME2 = "person";
	
	
public static void calculate(Map<String,String> infected,Map<String,String> notInfected, Channel channel) {
		
	int minutes = 0;
	int meters = 0;
		
	
		try {
			InputStream is = new FileInputStream("." + File.separator + "config.properties");
			
			Properties prop = new Properties();
			prop.load(is);
			
		 minutes = Integer.parseInt(prop.getProperty("p"));
		 meters = Integer.parseInt(prop.getProperty("k"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
	
	
		
		
		int x1,y1, x2,y2;
		
		x1 = Integer.parseInt(infected.get("X"));
		y1 = Integer.parseInt(infected.get("Y"));
		
		x2 = Integer.parseInt(notInfected.get("X"));
		y2 = Integer.parseInt(notInfected.get("Y"));
		
		
		double distance = Math.sqrt(Math.pow(x1-x2, 2)+ Math.pow(y1-y2, 2));
		
		if(distance > meters) {
			
			return;
			
		}
		
		int startA = Integer.parseInt(infected.get("VrijemeDolaska"));		
		int endA = Integer.parseInt(infected.get("VrijemeNapustanja"));
		
		int startB = Integer.parseInt(notInfected.get("VrijemeDolaska"));
		int endB = Integer.parseInt(notInfected.get("VrijemeNapustanja"));
		
		
		if(startA <= endB && endA >= startB) {
			
			
		int minEnd = Math.min(endA, endB);
		int maxStart = Math.max(startA, startB);
		
		int resultTime = minEnd - maxStart;
		
		if(resultTime < minutes) {
			
			return;
			
		}
		
			Token_serverServiceLocator locator = new Token_serverServiceLocator();
		
			String infectedToken = null;
			String notInfectedToken = null;
		try {
			
			Token_server tokenServer = locator.getToken_server();
			
			//   infectedToken = tokenServer.getToken(Integer.parseInt(infected.get("id")));
			   notInfectedToken = tokenServer.getToken(Integer.parseInt(notInfected.get("id")));
		
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		
		
		
		String message = "Token: " + notInfectedToken + " je potencijalno zaražen";
		String messagePerson = notInfected.get("X") + "|" + notInfected.get("Y") + "-" + infected.get("X") + "|" + infected.get("Y");
		
		try(Jedis jedis = new Jedis("localhost")){
			
			
			jedis.hset("osoba" + notInfected.get("id"),"status", "potencijalno");
			
			jedis.save();
			
		}
			
		
	      try {
	    	  
			channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
			channel.basicPublish(EXCHANGE_NAME2,notInfected.get("id"),null,messagePerson.getBytes("UTF-8"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}

		
		
		}
		
		return;
	
		
		
	}


	public static void algorithm(Channel channel) {
		
		try(Jedis jedis = new Jedis("localhost")){
			
			 Set<String> keys = jedis.keys("lokacija*");
			 int size = keys.size();
			
			 ArrayList<Map<String,String>> arrList = new ArrayList<>();
			 
			
			 
			 for(String key : keys) {
				 
			 Map<String,String> map  =  jedis.hgetAll(key);
				 
			 	arrList.add(map);
				 
			 }
			 
			 for(int i = 0;i < size - 1 ; i++) {
				 
					Map<String,String> map1 = arrList.get(i);
				 
					String infected1 = jedis.hget("osoba" + map1.get("id"), "status");
					
				 for(int j = i+1; j < size ; j++) {
					 
				
					Map<String,String> map2 = arrList.get(j);
					
					String infected2 = jedis.hget("osoba" + map2.get("id"), "status");

					 
					 if(infected1.equals("nezarazen") && infected2.equals("zarazen")) {
						 
						 calculate(map2,map1,channel);
						 
					 }
					 
					 if(infected2.equals("nezarazen") && infected1.equals("zarazen")){
						 
						 calculate(map1,map2,channel);
					 }
					 
					 
				 }
			 }
			
			
		}
		
		
		
	}
	
	
	public static void main(String[] args)  {
		
		
		 ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    try (Connection connection = factory.newConnection()){
		    	
		    	Channel channel = connection.createChannel();
		        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		        channel.exchangeDeclare(EXCHANGE_NAME2, "direct");
		        
		        int seconds = 0;
		        

				try {
					InputStream is = new FileInputStream("." + File.separator + "config.properties");
					
					Properties prop = new Properties();
					prop.load(is);
					
				 seconds = Integer.parseInt(prop.getProperty("n"));
				
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				}
		        
		        
		       
		        while(true) {
			    	
		        	Thread.sleep(seconds*1000);
	
		        	algorithm(channel);
		        
				    
			    }
			    
		        
		        
		        
		    }catch(Exception e) {
		    	
		    	Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		    	
		    }
		    
		 
		    
		    
		    
		    
		  }
		
		
		
		

	}


