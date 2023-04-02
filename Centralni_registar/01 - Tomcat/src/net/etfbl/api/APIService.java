package net.etfbl.api;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.rpc.ServiceException;

import DefaultNamespace.Token_server;
import DefaultNamespace.Token_serverServiceLocator;
import net.etfbl.model.LocationData;
import net.etfbl.model.Mark;
import net.etfbl.model.Token;
import redis.clients.jedis.Jedis;




@Path("/lokacije")
public class APIService {

	LocationService service;

	public APIService() {
		service = new LocationService();
	}

	@GET
	@Path("/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllLocations(@PathParam("token") String token) {
		
		int id = 0;
		
		Token_serverServiceLocator locator = new Token_serverServiceLocator();
		
		try {
			Token_server tokenServer = locator.getToken_server();
			
			id = tokenServer.getId(token);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		if(id == -1) {

			return Response.status(401).build();
			
		}else {
			
		return	 Response.status(200).entity(service.getAllLocations(id)).build();
			
		}
		
				
	}
	
	@POST
	@Path("/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewLocation(LocationData locData, @PathParam("token") String token) {
		
		int id = 0;
		
		Token_serverServiceLocator locator = new Token_serverServiceLocator();
		
		try {
			Token_server tokenServer = locator.getToken_server();
			
			id = tokenServer.getId(token);
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		service.addLocation(id,locData.getX(),locData.getY(),locData.getStartTime(),locData.getEndTime());
		
		return Response.status(200).build();
		
		
	}
	
	@GET
	@Path("/tokeni")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllTokens() {
		
		String[] tokens = null;
		ArrayList<Token> arrList = new ArrayList<Token>();
 		
		Token_serverServiceLocator locator = new Token_serverServiceLocator();
		
		try {
			Token_server tokenServer = locator.getToken_server();
			
			tokens = tokenServer.getAllTokens();
			
			for(String token : tokens) {
				
				if(token.equals("Nema aktivnih tokena")) {
					
					
					return Response.status(200).entity(arrList).build();
					
				}
				
			}
			
			
			for(String token : tokens) {
				
				arrList.add(new Token(token));
			}
			
		
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		return Response.status(200).entity(arrList).build();
		
		
	}
	
	@PUT
	@Path("/{token}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response markPerson(Mark mark,@PathParam("token") String token) {
		

		Token_serverServiceLocator locator = new Token_serverServiceLocator();
		
		try {
			
			
			
			Token_server tokenServer = locator.getToken_server();
			
			
			boolean verifyToken = false;
			
			verifyToken = tokenServer.verifyToken(token);
			
			if(!verifyToken) {
				
				return Response.status(401).build();
				
			}

			
			
			
			int id = tokenServer.getId(token);
			
			
			try(Jedis jedis = new Jedis()){
				
				Set<String> keys = jedis.keys("osoba*");
				
				
				for(String key : keys) {
					
					List<String> person = jedis.hmget(key,"id");
					
					
					String data = null;
					
					for(String atribute : person) {
					
					
					 data = atribute;	
						
						
					}
					
					if(id == Integer.parseInt(data)){
						
						if(mark.getMark().equals("blokiran") || mark.getMark().equals("odblokiran")) {
							
							
							Map<String, String> map = new HashMap<String,String>();
							
							if(mark.getMark().equals("blokiran")) {
							
							map.put("blokiran", "da");
							
							}else {
								
								map.put("blokiran", "ne");
								
							}
							
							jedis.hmset(key,map);
							
							jedis.save();
							
							return Response.status(200).build();
							
							
						}else {
						
						
						Map<String, String> map = new HashMap<String,String>();
						map.put("status", mark.getMark());
						
						jedis.hmset(key,map);
						
						jedis.save();
						
						return Response.status(200).build();
						
						}
						
					}
					
					
				}
				
				
			}
			
			
			
			
			
			
			 
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
	
		return Response.status(401).build();
		
	}
	
	
	@GET
	@Path("/blokiran/{token}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isBlocked(@PathParam("token") String token) {
		
		
			Token_serverServiceLocator locator = new Token_serverServiceLocator();
			
			Token_server tokenServer;
			
			int id = -1;
			try {
				
				tokenServer = locator.getToken_server();
				
				boolean verifyToken = false;
				
				verifyToken = tokenServer.verifyToken(token);
				
				if(!verifyToken) {
					
					return Response.status(401).build();
					
				}
				
				
				
				 id = tokenServer.getId(token);
				
				
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
			
			
			try(Jedis jedis = new Jedis()){
				
				Set<String> keys = jedis.keys("osoba*");
				
				
				for(String key : keys) {
					
					
					List<String> person = jedis.hmget(key,"blokiran");
					List<String> idPerson = jedis.hmget(key,"id");
					
					for(String listID : idPerson) {
						
						
						if(id == Integer.parseInt(listID)) {
							
							for(String blocked : person) {
								
								if(blocked.equals("da")){
									
									return Response.status(403).build();
									
								}
								
								if(blocked.equals("ne")) {
									
									
									return Response.status(200).build();
									
								}
								
							}
							
							
							
						}
						
						
					}
					
					
					
					
					
				}
				
			}
				
			return Response.status(401).build();
			
		
	}
	
}
