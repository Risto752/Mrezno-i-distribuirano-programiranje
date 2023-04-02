package hellofx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ActiveTokenController implements Initializable {
	
	@FXML
    public TableView<Token> tableView;
	
	 @FXML
	  public TableColumn<Token, String> tokenID;
	 
	 
	 ObservableList<Token> tokens = FXCollections.observableArrayList();
	 
	 @Override
	 public void initialize(URL url, ResourceBundle rb) {

		 	
		 
	           readFromArrayList();

	        tokenID.setCellValueFactory(new PropertyValueFactory<>("token"));
	        


	            tableView.setItems(tokens);

	    }
		
		
		public void readFromArrayList() {
			
			
			for(Token token : ClientState.activeTokens) {
			
				tokens.add(token);
				
			}
			
			
		}
	 
	 

}
