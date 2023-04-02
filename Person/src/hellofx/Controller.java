package hellofx;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import DefaultNamespace.Token_server;
import DefaultNamespace.Token_serverServiceLocator;

public class Controller {

   public void logIn() {  // Prijava na token server
	   
	   
	   
	   if(ClientState.isLoggedIn() == false) {
	   
		   FXMLHelper helper = new FXMLHelper();
		   helper.loadForm("prijava.fxml");
		   
		   
	   
	   }else {
		   
		   FXMLHelper helper = new FXMLHelper();
		   helper.loadForm("lozinka.fxml");
		   
	   }
   
   }
   
   
   public void logOut() {  // Odjava sa token servera
	   
	   if(ClientState.isLoggedIn() == false) {
		   
		   FXMLHelper helper = new FXMLHelper();
			helper.loadForm("VecOdjavljen.fxml");
		   
		   
	   }else {
		   
		   ClientState.setLoggedIn(false);   
		   ClientState.setToken("default");
		   
		   FXMLHelper helper = new FXMLHelper();
			helper.loadForm("UspjesnaOdjava.fxml");
		   
		   
		   					
	   }
	   
	   
	   
   }
   
  
}