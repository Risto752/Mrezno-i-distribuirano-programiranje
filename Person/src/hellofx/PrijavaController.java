package hellofx;



import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;
import DefaultNamespace.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class PrijavaController {
	@FXML
	TextField imeID;
	
	@FXML
	TextField prezimeID;
	
	@FXML
	TextField jmbID;
	
	
	public void logIn() {
		
		
	
		
		String name = imeID.getText();
		String surname = prezimeID.getText();
		String JMB = jmbID.getText();
		
		Token_serverServiceLocator locator = new Token_serverServiceLocator();
		
		try {
			Token_server tokenServer = locator.getToken_server();
			
			  String newToken = tokenServer.generateToken(name, surname, JMB);
			
			
			if(!newToken.equals("Not registered")){
			
			ClientState.setToken(newToken);
			ClientState.setLoggedIn(true);
			
			
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("lozinka.fxml");
				
				imeID.getScene().getWindow().hide();
				
			
			}else {
				
					FXMLHelper helper = new FXMLHelper();
					helper.loadForm("NisteZavedeni.fxml");
				
					
			}
			
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		
		
		
		
		
		
	}

}
