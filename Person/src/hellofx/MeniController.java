package hellofx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import org.json.JSONArray;
import org.json.JSONObject;

import DefaultNamespace.Token_server;
import DefaultNamespace.Token_serverServiceLocator;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MeniController {
	
	@FXML
	Button buttonForm;
	
	public void lozinka() {
		
		if(ClientState.getLocalyLoggedIn() == true) {
		
		FXMLHelper helper = new FXMLHelper();
		helper.loadForm("NovaLozinka.fxml");
		
		buttonForm.getScene().getWindow().hide();
		
		}else {
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("PonovnaPrijava.fxml");
			
			
		}
		
		
	}
	
	
	
	
	public void pregledKoristenja() {
		
		if(ClientState.getLocalyLoggedIn() == true) {
		
		FXMLHelper helper = new FXMLHelper();
		helper.loadForm("PregledKoristenja.fxml");
		
		buttonForm.getScene().getWindow().hide();
		
		}else {
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("PonovnaPrijava.fxml");
			
		}
		
	}
	
	
	public void prikazPozicija() { // Dobijamo sve evidentirane pozicije u matrici
		
		
		if(GlavnaFormaController.isBlocked()) {
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("Blokiran.fxml");
			
		}else {
		
	
		if(ClientState.getLocalyLoggedIn() == true) {
			
							
			
							// Moramo uputiti rest poziv centralnom registru kako bi dobili sve lokacije
			
			boolean verifyToken = false;

			Token_serverServiceLocator locator = new Token_serverServiceLocator();
			
			try {
				Token_server tokenServer = locator.getToken_server();
				
				
				verifyToken = tokenServer.verifyToken(ClientState.getToken());
				
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
			
			
			if(!verifyToken) {
				
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("TokenNevazeci.fxml");
				
				
			}else {
			
				
			try {
				InputStream is = new URL("http://localhost:8080/Centralni_registar/api/lokacije/" + ClientState.getToken()).openStream();
				
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				
				StringBuilder sb = new StringBuilder();
				int cp;
				while ((cp = rd.read()) != -1) {
					sb.append((char) cp);
				}
				
				String jsonText = sb.toString();
				
				JSONArray jsonArr = new JSONArray(jsonText);
				
				ArrayList<Location> arrList = new ArrayList<Location>();
				
				for (int i = 0; i < jsonArr.length(); i++) {
					JSONObject obj = jsonArr.getJSONObject(i);
					arrList.add(new Location(obj.getInt("x"),obj.getInt("y")));
				}
				
				
				// Ovdje ide logika za GUI matricu :D
				
				GridPane root = new GridPane();
				
				Properties prop = new Properties();
				InputStream inputStream = getClass().getClassLoader().getResourceAsStream("hellofx/config.properties");
				
					prop.load(inputStream);
				
					int n = Integer.parseInt(prop.getProperty("n"));
					int m = Integer.parseInt(prop.getProperty("m"));
				
				for(int x = 0; x < n ; x++){
	                for(int y = 0; y < m; y++){
	                	
	             
	                	Button button = new Button();
	                	
	                	 button.setPrefHeight(50);
	                     button.setPrefWidth(50);
	                     button.setAlignment(Pos.CENTER);
	                     button.setDisable(false);
	                     
	                     for(Location loc : arrList) {
	                     
	                    	 if(x == loc.getX() && y == loc.getY()) {
	                    	 
	                     button.setStyle("-fx-background-color: #0000ff");
	                     
	                    	 }
	                	
	                     }
	                     
	                     root.setRowIndex(button,x);
	                     root.setColumnIndex(button,y);    
	                     root.getChildren().add(button);
	                     
	                     buttonForm.getScene().getWindow().hide();
	                	
	                }
				}
				
				Stage stage = new  Stage();
				
				Scene scene = new Scene(root, 500, 500); 
				stage.setScene(scene);
				stage.show();
				
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
			
			}
				
			}else {
				
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("PonovnaPrijava.fxml");
				
			}
		
		}
		
	}

}
