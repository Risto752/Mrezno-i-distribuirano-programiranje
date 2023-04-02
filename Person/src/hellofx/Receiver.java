package hellofx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.TextArea;



public class Receiver extends Thread {

	private static BufferedReader in;
	private static PrintWriter out;
	private static Socket sock;
	private boolean exit = false;
	private TextArea area;
	
	
	Receiver(BufferedReader in, PrintWriter out, Socket sock, TextArea area){
		
		this.in = in; 
		this.out = out;
		this.sock = sock;
		this.area = area;
		
		start();
		
		
	}
	
	public void run() {
		
		
		
		while(!exit) {
			
		try {
			String message = in.readLine();
			
			Platform.runLater(() -> {
				
				area.appendText("Medicinar: " + message);
				area.appendText(System.getProperty("line.separator"));
				
			});
			
			
			
			if("kraj".equals(message)) {
				
				
				
				out.println("kraj");
				
				in.close();
				out.close();
				sock.close();
			
				exit = true;
				
				ClientState.isConnectedToMedic = false;
			
				
			}
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
			
			
			
		}
		
		
		
		
	}
	
	
	
}
