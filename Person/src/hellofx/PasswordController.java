package hellofx;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PasswordController {

	@FXML
	TextField lozinkaID;
	
	
	public void process() {
		
		if(ClientState.getLocalyLoggedIn() == false){
		
		if(ClientState.getPassword().equals("firstTime")) {
			
			ClientState.setPassword(lozinkaID.getText());
				
				SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
				Date now = new Date();
				ClientState.setSessionStart(sdfDate.format(now));
				ClientState.setMillisStart(now.getTime());
				
				ClientState.setLocalyLoggedIn(true);
				
				//FXMLHelper helper = new FXMLHelper();
				//helper.loadForm("GlavnaForma.fxml");
				
				  FXMLLoader loader = new FXMLLoader(getClass().getResource("GlavnaForma.fxml"));
			      
			      Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				}
			      GlavnaFormaController controller = loader.getController();
			      Stage stage = new Stage();
			      stage.setScene(new Scene(root));
			      stage.show();
			      
			      
			      ClientState.connectRabbitMQ(controller);
			    
				
				
				
				lozinkaID.getScene().getWindow().hide();
			
		}else {
			
			if(ClientState.getPassword().equals(lozinkaID.getText())) {
				
				
				SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
				Date now = new Date();
				ClientState.setSessionStart(sdfDate.format(now));
				ClientState.setMillisStart(now.getTime());
				
				
				ClientState.setLocalyLoggedIn(true);
				
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("GlavnaForma.fxml");
				
				lozinkaID.getScene().getWindow().hide();
			}else {
				
				
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("NeispravnaLozika.fxml");
				
				
			}
			
			
			
		}
		
	}else {
		
		FXMLHelper helper = new FXMLHelper();
		helper.loadForm("VecLokalno.fxml");
		
		
	}
		
	}
	
	
}
