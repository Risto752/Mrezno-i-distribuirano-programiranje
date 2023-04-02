package hellofx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLHelper {
	
	
	public  void loadForm(String fxmlFile) {
		
		
		 Parent root;
		   
			try {
				
				root = FXMLLoader.load(getClass().getResource(fxmlFile));
				 Stage stage = new Stage();
			       stage.setScene(new Scene(root));
			       stage.show();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger("").log(Level.SEVERE, e.getClass().getName());
			}
		
		
		
	}

}
