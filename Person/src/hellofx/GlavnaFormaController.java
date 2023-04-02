package hellofx;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import org.json.JSONObject;


import DefaultNamespace.Token_server;
import DefaultNamespace.Token_serverServiceLocator;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GlavnaFormaController {
	
	@FXML
	TextField X;
	
	@FXML
	TextField Y;
	
	@FXML
	TextField start;
	
	@FXML
	TextField end;
	
	@FXML
	TextField poruka;
	
	@FXML
	TextArea area;
	
	@FXML
	Label notification;
	
	
	public void chooseFiles() {
		
	
		FileChooser fileChooser = new FileChooser();
		
		File selectedFile = fileChooser.showOpenDialog(area.getScene().getWindow());
		
		
		
		if(ClientState.files.size() > 5) {
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("FajlPet.fxml");
			
		}else {
			
			
			
			ClientState.files.add(selectedFile);
			
		}
		
		
		
	}
	
	public void sendFiles() {
		
		System.setProperty("java.security.policy", "client_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		Registry registry;
		try {
			
			String name = "FS";
			registry = LocateRegistry.getRegistry(1099);
			FSInterface fileServer = (FSInterface) registry.lookup(name);
		
			
			FileObject[] files = new FileObject[ClientState.files.size()];
			int index = 0;
			
			
			
			for(File file : ClientState.files) {
				
			
				
				
				
				try {
					
					files[index] = new FileObject( Files.readAllBytes(file.toPath()),file.getName());
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				}
				
				index++;
				
			}
			
			if(ClientState.files.size() != 0) {
			
			fileServer.upload(files, ClientState.getToken());
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("FajloviPoslani.fxml");
			
			ClientState.files.clear();
			
			}else {
				
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("NemaFajlova.fxml");
				
			}
			
			
			
			
			
			
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		

		
		
		
		
		
		
	}
	
	
	
	public void notification() {
		
		for(String notif: ClientState.notifications) {
			
			
		 String[] parts = notif.split("-");
			
		 String notInfected = parts[0];
		 String infected = parts[1];
		 
		 String[] help1 = notInfected.split("\\|");
		 
		 int x1 = Integer.parseInt(help1[0]);
		 int y1 = Integer.parseInt(help1[1]);
		 
		 String[] help2 = infected.split("\\|");
		 
		 int x2 = Integer.parseInt(help2[0]);
		 int y2 = Integer.parseInt(help2[1]);
		 
		 
		 GridPane root = new GridPane();
			
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("hellofx/config.properties");
			
				try {
					prop.load(inputStream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				}
			
				int n = Integer.parseInt(prop.getProperty("n"));
				int m = Integer.parseInt(prop.getProperty("m"));
			
			for(int x = 0; x < n ; x++){
             for(int y = 0; y < m; y++){
             	
          
             	Button button = new Button();
             	
             	 button.setPrefHeight(50);
                  button.setPrefWidth(50);
                  button.setAlignment(Pos.CENTER);
                  button.setDisable(false);
                  
              
                  
                 	 if(x == x1 && y == y1) {
                 	 
                  button.setStyle("-fx-background-color: #0000ff");
                  
                 	 }
                 	 
                 	 if(x == x2 && y == y2) {
                     	 
                         button.setStyle("-fx-background-color: #00ff00");
                         
                        	 }
             	
                  
                  
                  root.setRowIndex(button,x);
                  root.setColumnIndex(button,y);    
                  root.getChildren().add(button);
                  
                  
             	
             }
			}
			
			Stage stage = new  Stage();
			
			Scene scene = new Scene(root, 500, 500); 
			stage.setScene(scene);
			stage.show();
			
			
			
			
			
			
			
		}
		
		notification.setTextFill(Color.web("#000000"));
		ClientState.notifications.clear();
		
		
		
	}
	
	
	
	public void redLabel() {
		
		
		notification.setTextFill(Color.web("#ff0000"));
		
	}
	
	
	
	
	public void meni() {	// Pozivanje menija
		
		
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("Meni.fxml");

		
	}
	
	public void posalji() { // Slanje podataka centralnom registru
		
				
		if(isBlocked()) {
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("Blokiran.fxml");
			
		}else {
		
		

		
		int x,y,startVar,endVar;
		
		
		try {
			
			x = Integer.parseInt(X.getText());
			y = Integer.parseInt(Y.getText());
			startVar = Integer.parseInt(start.getText());
			endVar = Integer.parseInt(end.getText());
			
			X.setText("");
			Y.setText("");
			start.setText("");
			end.setText("");
			
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("hellofx/config.properties");
			
				prop.load(inputStream);
			
				int n = Integer.parseInt(prop.getProperty("n"));
				int m = Integer.parseInt(prop.getProperty("m"));
			
			
			
			if(x >= 0 && x < n && y >= 0 && y < m && startVar >= 0 && startVar <= 60 && endVar >= 0 && endVar <= 60 && startVar < endVar) {
				
				// slanje post zahtjeva
				
				String jsonStringLocation = new String("{\"startTime\":\"" + startVar +  "\",\"endTime\":\"" + endVar + "\",\"x\":\"" + x + "\",\"y\":\""+y+"\"}");
				
				
				
				URL url = new URL("http://localhost:8080/Centralni_registar/api/lokacije/" + ClientState.getToken());
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST"); 
				conn.setRequestProperty("Content-Type", "application/json");
				
				JSONObject input = new JSONObject(jsonStringLocation);
				
				OutputStream os = conn.getOutputStream();
				os.write(input.toString().getBytes());
				os.flush();
				
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}
				
				
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}
				os.close();
				br.close();
				conn.disconnect();
				
				
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("PodaciPoslani.fxml");
				
			}else {
				
				throw new NumberFormatException();
				
			}
			
			
			
		}catch(NumberFormatException nre) {
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("NeispravanUnos.fxml");
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		
		
		
		}
		
	}
	
	public static boolean isBlocked() {
		
		boolean blocked = false;
		URL url;
		try {
			url = new URL("http://localhost:8080/Centralni_registar/api/lokacije/blokiran/" + ClientState.getToken());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			
			if(conn.getResponseCode() == 401) {
				
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("TokenNevazeci.fxml");
				
			}
			
			
			if(conn.getResponseCode() == 403) {
				
				blocked = true;
				
			}
			
			
			if(conn.getResponseCode() == 200) {
				
				blocked = false;
				
			}
			
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		
		return blocked;
		
	}
	
	
	public void poslajiChat() {
		
		if(isBlocked()) {
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("Blokiran.fxml");
			
		}else {

		
		String message = poruka.getText();
		
		poruka.setText("");
		
		if(!ClientState.isConnectedToMedic) {
			
			ClientState.connect(area);
			
		}
			
		if(ClientState.isConnectedToMedic) {
			
			ClientState.getOut().println(message);
			
			area.appendText("Ja: " + message );
			area.appendText(System.getProperty("line.separator"));
			
		}

		}
	
	}
	
	
	public void odjava() {
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		
		ClientState.addSession(ClientState.getSessionStart(),sdfDate.format(now) , (int)((now.getTime()- ClientState.getMillisStart())/1000));
		
		ClientState.setLocalyLoggedIn(false);
		
		start.getScene().getWindow().hide();
		
		
	}
	
	

}
