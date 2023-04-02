package hellofx;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;


import DefaultNamespace.Token_server;
import DefaultNamespace.Token_serverServiceLocator;

public class FileServer implements FSInterface {
	
	
	

	@Override
	public FileObject[] download(String token) throws RemoteException {
		
		FileObject[] arr = null;
		
		Token_serverServiceLocator locator = new Token_serverServiceLocator();
		
		int id = 0;
	
		try {
			Token_server tokenServer = locator.getToken_server();
			
			  id = tokenServer.getId(token);
			
			  if(id == -1) {
				  
				  return arr;
				  
			  }
			  
			  
			  File folder = new File("" + id);
			  
			  if(folder.exists()) {
			  
				 int counter = 0;
				  
			  for(File file : folder.listFiles()) {
				  
				  
				  counter++;
				  
				  
			  }
			  
			  arr = new FileObject[counter];
			  
			  int index = 0;
			  
			  for(File file : folder.listFiles()) {
				  
				  
					try {
						arr[index] = new FileObject( Files.readAllBytes(file.toPath()),file.getName());
						
						 index++;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
					}
				  
				  
				  
				 
			  }
			  
			  return arr;
			  
			  }else {
				  
				  return arr;
				  
			  }
			  
			  
			  
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		
		
		
		
		return null;
	}

	@Override
	public void upload(FileObject[] files, String token) throws RemoteException {
		
	Token_serverServiceLocator locator = new Token_serverServiceLocator();
		
		int id = 0;
	
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
		
		File file = new File("" + id);
		
		file.mkdir();
		
		for(FileObject fileOb : files) {
		
		
		try (FileOutputStream stream = new FileOutputStream(id + File.separator + fileOb.fileName)) {
		    stream.write(fileOb.content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		
		}
		
		
		
	}
	
	
	public static void main(String args[]) {
		System.setProperty("java.security.policy", "server_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			FileServer fileServer = new FileServer();
			FSInterface stub = (FSInterface)UnicastRemoteObject.exportObject(fileServer, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("FS", stub);
			System.out.println("Server started.");
		} catch (Exception e) {
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
	}
	
	
	
	
	
	

}
