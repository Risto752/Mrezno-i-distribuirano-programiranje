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

public class PrikaziSesije implements Initializable {
	
	@FXML
    public TableView<Session> tableView;

    @FXML
    public TableColumn<Session, Integer> SesijaID;

    @FXML
    public TableColumn<Session, String> Pocetak;

    @FXML
    public TableColumn<Session, String> Kraj;
    
    @FXML
    public TableColumn<Session,Integer> Trajanje;
    
    
    ObservableList<Session>  sessions = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {


           readFromArrayList();

        SesijaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        Pocetak.setCellValueFactory(new PropertyValueFactory<>("sessionStart"));
        Kraj.setCellValueFactory(new PropertyValueFactory<>("sessionEnd"));
        Trajanje.setCellValueFactory(new PropertyValueFactory<>("duration"));
       


            tableView.setItems(sessions);

    }
	
	
	public void readFromArrayList() {
		
		
		for(Session session : ClientState.getSessions()) {
			
			sessions.add(session);
			
		}
		
		
	}
    
    

}
