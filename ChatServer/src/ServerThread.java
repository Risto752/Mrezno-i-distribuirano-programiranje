import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;

public class ServerThread extends Thread {

	private SSLSocket sock1;
	private SSLSocket sock2;
	private boolean exit = false;
	private boolean sock1Med;
	
	private BufferedReader in;
	private PrintWriter out;
	
	
	ServerThread(SSLSocket sock1, SSLSocket sock2, boolean flag){
		
		this.sock1 = sock1;
		this.sock2 = sock2;
		this.sock1Med = flag;
		
		try {
			
			in = new BufferedReader(new InputStreamReader(sock1.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock2.getOutputStream())), true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		start();
		
		
		
	}
	
	
	public void run() {
		
		String message;
		
		while(!exit) {
			
			try {
				
				message = in.readLine();
				
				if("kraj".equals(message)){
					exit = true;
					
					if(sock1Med) {
						
						ChatServer.medicalStaff.add(sock1);
						
						
					}
					
					
					
				} 
			
			
				out.println(message);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
		
			
			
		}
		
		
	}
	
	
	

	
	
	
}