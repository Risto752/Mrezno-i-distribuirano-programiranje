package hellofx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.TextArea;

public class ReceiverMedical extends Thread {

	private static BufferedReader in;
	private static PrintWriter out;
	private static Controller controller;
	
	ReceiverMedical(BufferedReader in, PrintWriter out, Controller controller){
		
		this.in = in; 
		this.out = out;
		this.controller = controller;
		
		start();
	}
	
	public void run() {
		
		
		while(true) {
			
		try {
			
			String message = in.readLine();
			
			
			if(!message.equals("kraj")) {
			
				controller.updateArea(message);
		
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
			
			
			
		}
		
		
		
		
	}
	
	
	
}
