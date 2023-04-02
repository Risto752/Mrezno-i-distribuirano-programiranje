package hellofx;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FSInterface extends Remote {
	
	FileObject[] download(String token) throws RemoteException;
	void  upload(FileObject[] files, String token) throws RemoteException;
	
	

}
