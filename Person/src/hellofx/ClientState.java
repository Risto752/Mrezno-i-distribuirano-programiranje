package hellofx;

import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.crypto.Data;
import javax.xml.rpc.ServiceException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import DefaultNamespace.Token_server;
import DefaultNamespace.Token_serverServiceLocator;
import javafx.scene.control.TextArea;

public class ClientState {
	
	private static String token = "default";			
	private static boolean loggedIn = false;
	private static String password = "firstTime";			// Podaci o log in
	private static boolean localyLoggedIn = false;
	public static boolean isConnectedToMedic = false;
	
	
	private static SSLSocket socket;
	private static final String HOST = "localhost";			// SSL stvari
	private static final int PORT = 8443;
	private static final String KEY_STORE_PATH = "." + File.separator + "keystore.jks";
	private static final String KEY_STORE_PASSWORD = "securemdp";
	private static BufferedReader in;
	private static PrintWriter out;
	
	private static GlavnaFormaController controller = null;
	private static final String EXCHANGE_NAME = "person";
	public static ArrayList<String> notifications = new ArrayList<>();
	
	
	private static int serialization = 0;
	public static ArrayList<File> files = new ArrayList<>();
	
	public static void connectRabbitMQ(GlavnaFormaController contr) {
		
		
		controller = contr;
		
		
		try {
			
			
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
			Connection connection = factory.newConnection();
			 Channel channel = connection.createChannel();
			
			 channel.exchangeDeclare(EXCHANGE_NAME, "direct");
			   String queueName = channel.queueDeclare().getQueue();
			  
			   int id = 0;
			   
				Token_serverServiceLocator locator = new Token_serverServiceLocator();
				
				try {
					Token_server tokenServer = locator.getToken_server();
					
					  id = tokenServer.getId(ClientState.getToken());
					
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				}
				
				
			  
			   channel.queueBind(queueName, EXCHANGE_NAME, "" + id );
			 
			 
			   Consumer consumer = new DefaultConsumer(channel) {
					@Override
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
							byte[] body) throws IOException {
						String message = new String(body, "UTF-8");
					
						controller.redLabel();
						
						notifications.add(message);
						
						Random rand = new Random();
						
						if((serialization % 4) == 0) {
							
							Gson gson = new Gson();
							

							try {
								FileWriter out = new FileWriter(new File("gson" + rand.nextInt(100000) + ".out"));
								out.write(gson.toJson(message));
								out.close();
							} catch (IOException e) {
								Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
							}
								
						}
						
						if((serialization % 4) == 1) {
							
							Kryo kryo = new Kryo();
							kryo.register(Data.class);
							try {
								Output out = new Output(new FileOutputStream(new File("kryo" + rand.nextInt(100000) + ".out")));
								kryo.writeClassAndObject(out, message);
								out.close();
							} catch (Exception e) {
								Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
							}
							
						}
						
						if((serialization % 4) == 2) {
							
							try {
								FileOutputStream fileOut = new FileOutputStream("java" + rand.nextInt(100000) + ".out");
								ObjectOutputStream out = new ObjectOutputStream(fileOut);
								out.writeObject(message);
								out.close();
							} catch (Exception e) {
								Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
							}
							
							
							
							
						}
						
						if((serialization % 4) == 3) {
							
							try {
								XMLEncoder encoder = new XMLEncoder(new FileOutputStream(new File("xml" + rand.nextInt(100000) + ".out")));
								encoder.writeObject(message);
								encoder.close();
							} catch (Exception e) {
								Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
							}
							
							
							
							
						}
						
						
						
						serialization++;
			
					}
				};
			   
			    channel.basicConsume(queueName, true, consumer);
			   
			   
			   
			   
			   
			   
			   
			   
			   
			 
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
	   
		
		
		
		
	}
	
	
	
	public static void connect(TextArea area) {  // Metoda povezuje osobu na chat server
		
		System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
		
		SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
		
		
		try {
			
			socket = (SSLSocket)sf.createSocket(HOST,PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			
			out.println("osoba");
			
			String response = in.readLine();
			
			if(response.equals("Ima medicinara")) {
				
				new Receiver(in,out,socket,area);
				isConnectedToMedic = true;
				
			}
			
			if(response.equals("Nema medicinara")) {
				
				
				area.appendText(response);
				area.appendText(System.getProperty("line.separator"));

				
			}
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
	}
	
	
	private static String sessionStart = "";
	private static long millisStart = 0;			// Podaci o sesijama
	private static ArrayList<Session> sessions = new ArrayList<>();
	
	
	public static String getToken() {
		
		return token;
	}
	
	
	public static boolean isLoggedIn() {
		
		return loggedIn;
	}
	
	
	public static void setToken(String tokenParam) {
		
		token = tokenParam;
	}
	
	public static void setLoggedIn(boolean param) {
		
		loggedIn = param;
	}
	
	public static String getPassword() {
		
		return password;
	}
	
	public static void setPassword(String pass) {
		
		password = pass;
	}
	
	public static void setLocalyLoggedIn(boolean param) {
		
		localyLoggedIn = param;
	}
	
	public static boolean getLocalyLoggedIn() {
		
		return localyLoggedIn;
	}
	
	public static String getSessionStart() {
		
		return sessionStart;
	}
	
	
	
	public static void setSessionStart(String param) {
		
		sessionStart = param;
	}
	
	
	
	public static void setMillisStart(long param) {
		
		millisStart = param;
	}
	
	
	
	public static long getMillisStart() {
		
		return millisStart;
	}
	
	
	
	
	
	public static ArrayList<Session> getSessions(){
		
		
		return sessions;
	}
	
	public static void addSession(String sessionStart, String sessionEnd, int duration) {
		
			sessions.add(new Session(sessionStart, sessionEnd, duration));
		
	}


	public static Socket getSocket() {
		return socket;
	}


	public static void setSocket(SSLSocket socket) {
		ClientState.socket = socket;
	}


	public static BufferedReader getIn() {
		return in;
	}


	public static void setIn(BufferedReader in) {
		ClientState.in = in;
	}


	public static PrintWriter getOut() {
		return out;
	}


	public static void setOut(PrintWriter out) {
		ClientState.out = out;
	}
	
	

}
