package hellofx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.TextArea;

public class ReceiverMulticast extends Thread {
	
	
	private Controller controllerMed;
	
	
	public ReceiverMulticast(Controller controller) {
		
		
		controllerMed = controller;
		
	}
	
	
	
	public void run() {
		
		
		while(true) {
			
			byte[] buf = new byte[1024];

			
			
			try {
				DatagramPacket packet = new DatagramPacket(buf,buf.length);
				
				ClientState.multSocket.receive(packet);
				
				String received = new String(packet.getData(),0,packet.getLength());
				
				controllerMed.updateAreaMed(received);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
			
			
			
			
			
		}
		

	}
	
	
	

}
