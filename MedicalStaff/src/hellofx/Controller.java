package hellofx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Controller {
	

	@FXML
	TextField poruka;
	
	@FXML
	TextField token;
		
	@FXML
	 TextArea area;
	
	@FXML
	TextField porukamed;
	
	@FXML
	 TextArea areamed;
	
	@FXML
	 TextArea areanotif;
	
	
	public void download() {
		
String likeSearch = token.getText();
		
		
		String likeSearch2 = likeSearch.replace("%", ".*");
		
		String regex = likeSearch2.replace("_", ".");
		
		ArrayList<String> matches = new ArrayList<String>();
		
		
		for(Token token : ClientState.activeTokens) {
			
			if(token.getToken().matches(regex)) {
				
				matches.add(token.getToken());
				
				
			}
			
			
		}
		
		if(matches.size() == 1) {
			
			
			String token = null;
			
			for(String tok : matches) {
				
				token = tok;
			}
			
			
			
			System.setProperty("java.security.policy", "client_policyfile.txt");
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}
			
			Registry registry;
		
				
				try {
					String name = "FS";
					registry = LocateRegistry.getRegistry(1099);
					FSInterface fileServer = (FSInterface) registry.lookup(name);

					
					FileObject[] response = fileServer.download(token);
					
					if(response == null) {
						
						FXMLHelper helper = new FXMLHelper();
						helper.loadForm("NemaDownload.fxml");
						
						return;
						
					}
					
					
					File file = new File(token);
					file.mkdir();
					
					for(FileObject fileOb : response) {
						
						
						try (FileOutputStream stream = new FileOutputStream(token + File.separator + fileOb.fileName)) {
						    stream.write(fileOb.content);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
						}
						
						
						}
					
					
					FXMLHelper helper = new FXMLHelper();
					helper.loadForm("UspjesanDownload.fxml");
					
					
					return;
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				} catch (NotBoundException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				}
			
			
			
			
			
			
			
			
			
		}else {
				
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("JedanToken.fxml");
				
			}
		
		
		
	}
	
	
	public void updateArea(String message) {
		
		area.appendText("Osoba: " + message);
		area.appendText(System.getProperty("line.separator"));	
		
		
	}
	
	public void updateAreaMed(String message) {
		
		areamed.appendText("Medicinar: " + message);
		areamed.appendText(System.getProperty("line.separator"));	
		
		
	}
	
	public void updateAreaNotif(String message) {
		
		
		areanotif.appendText(message);
		areanotif.appendText(System.getProperty("line.separator"));	
		
		
	}
	
	
	
	
	public void activeTokens() {
		
		
		
		new Thread(() -> {
		
			InputStream is = null;
			try {
				is = new URL("http://localhost:8080/Centralni_registar/api/lokacije/tokeni").openStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			
			StringBuilder sb = new StringBuilder();
			int cp;
			try {
				while ((cp = rd.read()) != -1) {
					sb.append((char) cp);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
			
			String jsonText = sb.toString();
			
			JSONArray jsonArr = new JSONArray(jsonText);
			
			ArrayList<String> arrList = new ArrayList<String>();
			
			ClientState.activeTokens.clear();
			
			for (int i = 0; i < jsonArr.length(); i++) {
				
				JSONObject obj = jsonArr.getJSONObject(i);
			
				ClientState.activeTokens.add(new Token(obj.getString("value")));
				
				
			
			}
			
			Platform.runLater(() -> {
				
				FXMLHelper helper = new FXMLHelper();
				helper.loadForm("AktivniTokeni.fxml");
				
			});
			
			
		
		}).start();
		
		
	}
	
	
	
	public void posaljiMed() {
		
		
		String message = porukamed.getText();
		porukamed.setText("");
	
		
		byte[] buf = new byte[1024];
		
		buf = message.getBytes();
		
		DatagramPacket packet = new DatagramPacket(buf,buf.length,ClientState.address,ClientState.MULTICAST_PORT-1);
		try {
			
			ClientState.multSocket.send(packet);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
	}
	
	
	
	
	public void posaljiChat() {
		
		
		String message = poruka.getText();
		poruka.setText("");
		
	
		
		area.appendText("Ja: " + message);
		area.appendText(System.getProperty("line.separator"));
		
		ClientState.getOut().println(message);
		
	
			
	}
	
	public void terminate() {
		
	
		
		String message = "kraj";
		
		area.appendText("Ja: Kraj komunikacije.");
		area.appendText(System.getProperty("line.separator"));
		
		ClientState.getOut().println(message);
		
	
		
	}
	
	
	
	public void markInfected() {
		
		marking("zarazen");
		
	}
	
	
	public void markNotInfected(){
		
		marking("nezarazen");
		
	}
	
	
	public void markPotentialyInfected() {
		
		marking("potencijalno");
		
	}
	
	public void block() {
		
		
		marking("blokiran");
	}
	
	public void unblock() {
		
		
		marking("odblokiran");
		
	}
	
	

	public void marking(String mark) {
		
	
		
		
		String likeSearch = token.getText();
		
		
		String likeSearch2 = likeSearch.replace("%", ".*");
		
		String regex = likeSearch2.replace("_", ".");
		
		ArrayList<String> matches = new ArrayList<String>();
		
		
		for(Token token : ClientState.activeTokens) {
			
			if(token.getToken().matches(regex)) {
				
				matches.add(token.getToken());
				
				
			}
			
			
		}
		
		if(matches.size() == 1) {
			
			
			String token = null;
			
			for(String tok : matches) {
				
				token = tok;
			}
			
			
			
			
			URL url;
			try {
				
				String jsonStringLocation = new String("{\"mark\":\"" + mark + "\"}");
				
				
				url = new URL("http://localhost:8080/Centralni_registar/api/lokacije/" + token);
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("PUT"); 
				conn.setRequestProperty("Content-Type", "application/json"); //text/plain
				
				JSONObject input = new JSONObject(jsonStringLocation);
				
				OutputStream os = conn.getOutputStream();
				os.write(input.toString().getBytes());
				os.flush();
				
				if(conn.getResponseCode() == 401) {
					
					FXMLHelper helper = new FXMLHelper();
					helper.loadForm("TokenNevazeci.fxml");
					
				}
				
				if(conn.getResponseCode() == 200) {
					
					
					if(mark.equals("zarazen") || mark.equals("potencijalno") || mark.equals("nezarazen")) {
						
						FXMLHelper helper = new FXMLHelper();
						helper.loadForm("UspjesnaOznaka.fxml");
						
					}else if(mark.equals("blokiran")){
						
						FXMLHelper helper = new FXMLHelper();
						helper.loadForm("UspjesanBlok.fxml");
						
					}else {
						
						FXMLHelper helper = new FXMLHelper();
						helper.loadForm("UspjesnoOdblok.fxml");
						
					}
					
				}
				
				
				
				
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				while ((output = br.readLine()) != null) {
					
					
				}
				os.close();
				br.close();
				conn.disconnect();
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
			
			
			
			
			
			
		}else {
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("JedanToken.fxml");
			
		}
		
		
		
		
		
	}
	
	
	
	public void getLocations() { 
		
		
		
		
		String likeSearch = token.getText();
		
		
		String likeSearch2 = likeSearch.replace("%", ".*");
		
		String regex = likeSearch2.replace("_", ".");
		
		ArrayList<String> matches = new ArrayList<String>();
		
		
		for(Token token : ClientState.activeTokens) {
			
			if(token.getToken().matches(regex)) {
				
				matches.add(token.getToken());
				
				
			}
			
			
		}
		
		if(matches.size() == 1) {
			
			
			String token = null;
			
			for(String tok : matches) {
				
				token = tok;
			}
			
			
			try {
				
				URL url = new URL("http://localhost:8080/Centralni_registar/api/lokacije/" + token);
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				conn.setRequestMethod("GET");
				
				
				if(conn.getResponseCode() == 401) {
					
					FXMLHelper helper = new FXMLHelper();
					helper.loadForm("TokenNevazeci.fxml");
					
				}
				
				if(conn.getResponseCode() == 200) {
					
					BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
					
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
	                	
	                }
				}
				
				Stage stage = new  Stage();
				
				Scene scene = new Scene(root, 500, 500); 
				stage.setScene(scene);
				stage.show();
				
				}
				
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
			
			
			
			
			
			
		}else {
			
			FXMLHelper helper = new FXMLHelper();
			helper.loadForm("JedanToken.fxml");
			
		}
		
	
	
	}
	
	
}
