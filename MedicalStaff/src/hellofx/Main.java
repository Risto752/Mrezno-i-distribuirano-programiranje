package hellofx;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
	
	
	@Override
    public void start(Stage primaryStage) throws Exception{
	
        
      FXMLLoader loader = new FXMLLoader(getClass().getResource("hellofx.fxml"));
      
      Parent root = loader.load();
      Controller controller = loader.getController();
      primaryStage.setScene(new Scene(root));
      primaryStage.show();
      
    
      ClientState.connectMulticast();
      new ReceiverMulticast(controller).start();
      ClientState.connect(controller);
      ClientState.connectRabbitMQ(controller);
        
        
    }

	
	
	public static void main(String[] args) {
		launch(args);
	}
}
