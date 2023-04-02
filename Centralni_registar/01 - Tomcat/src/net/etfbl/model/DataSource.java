package net.etfbl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class DataSource {

	

	
	public ArrayList<Location> getAllLocations(int id) {
		
		 ArrayList<Location> locations = new ArrayList<>();
		
		 try(Jedis jedis = new Jedis("localhost")){
			 
			 Set<String> keys = jedis.keys("lokacija*");
			 
			 for(String key : keys) {
				 
				 
				 List<String> location = jedis.hmget(key,"X","Y","id");
				 
				 int index = 0;
					String[] data = new String[3]; 
					
					for(String atribute : location) {
					
					
					data[index++] = atribute;	
						
						
					}
				 
					if(Integer.parseInt(data[2]) == id) {
			
						Location locationObj = new Location(Integer.parseInt(data[0]),Integer.parseInt(data[1]));
						
						locations.add(locationObj);
						
						
					}
					
				 
			 }
			 
			 
		 }
		 
		 
		 
		 
		 return locations;
	}
	
	
	public void addLocation(int id,int x, int y, int start, int end) {
		
		int locationCount = 0;
		
		try(Jedis jedis = new Jedis("localhost")){
			
			 Set<String> keys = jedis.keys("lokacija*");
			 
			 for(String key : keys) {
				 
				 locationCount++;
				 
			 }
			
			 locationCount++;
			 
			 HashMap<String, String> obj = new HashMap<>();
				
				obj.put("id","" + id);
				obj.put("X","" + x);
				obj.put("Y","" + y);
				obj.put("VrijemeDolaska", "" + start);
				obj.put("VrijemeNapustanja", "" + end);
				
				jedis.hmset("lokacija" + locationCount, obj);
				
				jedis.save();
			 
			 
			
			
		}
		
		
		
	}
	
	
	
}
