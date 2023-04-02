import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MulticastServer extends Thread {
	
	private static final int PORT = 20000;
	private static final String HOST = "224.0.0.11";
	private static MulticastSocket socket ;
	private static InetAddress address;
	
	static {
		
		try {
			socket = new MulticastSocket(PORT);
			address = InetAddress.getByName(HOST);
			socket.joinGroup(address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
	}
	
	
	public void run() {
		
		
		
		while(true) {
			
			
			try {
				byte[] buffer = new byte[256];
				
				DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
				socket.receive(packet);
				
			
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
				}
				packet = new DatagramPacket(buffer,buffer.length,address,PORT+1);				
				socket.send(packet);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
		
			
			
			
			
		}
		
		
	}
	
	
	
	
	
	
	

}
