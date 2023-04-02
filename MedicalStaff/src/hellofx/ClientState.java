package hellofx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import javafx.scene.control.TextArea;

public class ClientState {
	
	
	private static SSLSocket socket;
	private static final String HOST = "localhost"; 		// SSL stvari
	private static final int PORT = 8443;
	private static final String KEY_STORE_PATH = "." + File.separator + "keystore.jks";
	private static final String KEY_STORE_PASSWORD = "securemdp";
	private static BufferedReader in;
	private static PrintWriter out;
	

	private static boolean activate = false;		// Tokeni...
	public static ArrayList<Token> activeTokens = new ArrayList<Token>();
	public static boolean firstTime = true;

	
	public static final int MULTICAST_PORT = 20001;		// Multikast komunikacija
	public static final String MULTICAST_HOST = "224.0.0.11";
	public static MulticastSocket multSocket = null;
	public static InetAddress address;
	public static boolean multicast = false;
	
	 private static final String EXCHANGE_NAME = "medics";
	
	public static Controller controllerNotif; 
	
	public static void connectRabbitMQ(Controller controller){
		
		controllerNotif = controller;
		
			try {
		
		 ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		
		    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		    String queueName = channel.queueDeclare().getQueue();
		    channel.queueBind(queueName, EXCHANGE_NAME, "");
		    
		    Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String message = new String(body, "UTF-8");
				
					ClientState.controllerNotif.updateAreaNotif(message);
					
				}
			};
		   
		    channel.basicConsume(queueName, true, consumer);
		
			}catch(TimeoutException e) {
				
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
		
	}
	
	
	
	public static void connectMulticast() {
		
		try {
			multSocket = new MulticastSocket(MULTICAST_PORT);
			address =  InetAddress.getByName(MULTICAST_HOST); // Multikast klijent
			multSocket.joinGroup(address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
	
		
		
	}
	
	
	public static void connect(Controller controller) { // povezivanje na chat server
		
		System.setProperty("javax.net.ssl.trustStore", KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASSWORD);
		
		SSLSocketFactory sf = (SSLSocketFactory)SSLSocketFactory.getDefault();
		
		
		
		try {
			
			socket = (SSLSocket)sf.createSocket(HOST,PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			
			out.println("medicinar");
			
			new ReceiverMedical(in,out,controller);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
		}
		
		
		
		
	}
	

	public static Socket getSocket() {
		return socket;
	}

	public static void setSocket(SSLSocket socket) {
		ClientState.socket = socket;
	}

	public static BufferedReader getIn() {
		return in;
	}

	public static void setIn(BufferedReader in) {
		ClientState.in = in;
	}

	public static PrintWriter getOut() {
		return out;
	}

	public static void setOut(PrintWriter out) {
		ClientState.out = out;
	}

	public static boolean isActivate() {
		return activate;
	}

	public static void setActivate(boolean activate) {
		ClientState.activate = activate;
	}
	
	
	
	

}
