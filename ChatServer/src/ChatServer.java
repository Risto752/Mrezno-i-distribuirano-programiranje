import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class ChatServer {
	
	
	
	public static final int PORT = 8443;
	public static final String KEY_STORE_PATH = "." + File.separator + "keystore.jks";
	public static final String KEY_STORE_PASSWORD = "securemdp";
	
	private static BufferedReader in;
	private static PrintWriter out;
	public static Queue<SSLSocket> medicalStaff = new LinkedList<SSLSocket>();

	public static void main(String[] args) {
		
		new MulticastServer().start();
		
		System.setProperty("javax.net.ssl.keyStore", KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PASSWORD);
		
		SSLServerSocketFactory ssf =  (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
		
		
		try {
			ServerSocket ss = ssf.createServerSocket(PORT);
			System.out.println("Server running...");
			while (true) {
				
				SSLSocket s = (SSLSocket)ss.accept();
				
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			String firstMessage = in.readLine();
			
			
				
					if("osoba".equals(firstMessage)) {
						
						
						out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);

						
						if(medicalStaff.isEmpty()) {
							
							out.println("Nema medicinara");
							
						}else {
						
						SSLSocket medicalSocket = medicalStaff.poll();
						
						new ServerThread(s,medicalSocket,false);
						new ServerThread(medicalSocket,s,true);
						
							out.println("Ima medicinara");
						
						}
						
					}else if("medicinar".equals(firstMessage)){
						
						medicalStaff.add(s);
						
					}
			
				
			
			
			}
		} catch (Exception e) {
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
	}
	
	
	
	
	
	
	

}
