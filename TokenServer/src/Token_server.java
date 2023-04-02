import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Token_server {
	
	
	public String generateToken(String name, String surname, String JMB) {
		
		JedisPool pool = new JedisPool("localhost");
		
				try (Jedis jedis = pool.getResource()) {
					
				Set<String>	keys = jedis.keys("token*");
					
			
				for(String key : keys) {
						
					List<String> person = jedis.hmget(key,"ime","prezime", "JMB"); 
					
					int index = 0;
					String[] data = new String[3]; 
					
					for(String atribute : person) {
					
					
					data[index++] = atribute;	
						
						
					}
					
					if(data[0].equals(name) && data[1].equals(surname) && data[2].equals(JMB)) {
						
						boolean exists = false;
						String token = "";
						
						do {
							token = UUID.randomUUID().toString();  // generisanje tokena
						
							for(String keyValue : keys) {
								
								List<String> tokenValue = jedis.hmget(keyValue,"token");
								
								for(String tokenX : tokenValue) {
									
								
									if(token.equals(tokenX)) {
										
									exists = true;
										
									}
									
								}
								
								
							}
							
							
						
						}while(exists == true);
						
						
						Map<String, String> map = new HashMap<String,String>();
						map.put("token", token);
						
						jedis.hmset(key,map);
						
						jedis.save();
						
						return token;
						
					}
					
					
				}
				
			
				}
				
				pool.close();
				
				return "Not registered";
				
	
	}
	
	public Boolean verifyToken(String tokenParam) {
		
		
		JedisPool pool = new JedisPool("localhost");
		
		try (Jedis jedis = pool.getResource()) {
		
			Set<String>	keys = jedis.keys("token*");
			
			for(String key : keys) {
				
				List<String> tokenValue = jedis.hmget(key,"token");
				
				for(String tokenX : tokenValue) {
					
					
					if(tokenX.equals(tokenParam)) {
						
					return true;
						
					}
					
				}
				
			}
			
			

		}
		
		pool.close();
		
		return false;
		
		
		
		
	}
	
	
	public int getId(String tokenParam) {
		

		JedisPool pool = new JedisPool("localhost");
		
		try (Jedis jedis = pool.getResource()) {
		
			Set<String>	keys = jedis.keys("token*");
			
			for(String key : keys) {
				
				List<String> tokenValue = jedis.hmget(key,"token","id");
				
				int index = 0;
				String[] data = new String[2]; 
				
				for(String atribute : tokenValue) {
				
				
				data[index++] = atribute;	
					
					
				}
				
				if(data[0].equals(tokenParam)){
					
					return Integer.parseInt(data[1]);
					
				}
				
				
				
				
			}
			
			

		}
		
		pool.close();
		return -1;
		
	}
	
	public String[] getAllTokens() {
		
		JedisPool pool = new JedisPool("localhost");
		
		String[] tokens;
		
		try (Jedis jedis = pool.getResource()) {
			
			Set<String>	keys = jedis.keys("token*");
			
			int counter = 0;
			
			for(String key : keys) {
				
				
				List<String> tokenValue = jedis.hmget(key,"token");
				
				for(String attribute : tokenValue) {
				
					if(attribute.equals("")) {
						
					}else {
						counter++;
					}
				
				}
				
				
			}
			
			if(counter != 0) {
			
			 tokens = new String[counter];
			 
			}else {
				
				String[] ret = new String[1];
				ret[0] = "Nema aktivnih tokena";
				
				return ret;
				
			}
			
			int index = 0;
			
			for(String key : keys) {
				
				List<String> tokenValue = jedis.hmget(key,"token");
				
				
				for(String attribute : tokenValue) {
					
					if(attribute.equals("")) {
						
					}else {
						
						tokens[index] = attribute;
						index++;
						
					}
					
					
					
				}
				
				
			}
			
			
			
		}
		pool.close();
		
		return tokens;
		
		
	}
	
	public String getToken(int id) {
		
		
		try(Jedis jedis = new Jedis("localhost")){
			
			Set<String>	keys = jedis.keys("token*");
			
			for(String key : keys) {
				
				if(Integer.parseInt(jedis.hget(key, "id")) == id) {
					
					
					return jedis.hget(key, "token");
					
				}
				
				
				
				
			}
			
			
		}
		
		return "unknown";
		
		
	}
	
	
	
	
}


