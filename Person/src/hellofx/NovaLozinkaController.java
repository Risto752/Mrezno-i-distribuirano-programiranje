package hellofx;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NovaLozinkaController {
	
	@FXML
	TextField novaLozinkaID;
	
	public void potvrdi() {
		
		
		ClientState.setPassword(novaLozinkaID.getText());
		
		novaLozinkaID.getScene().getWindow().hide();
		
		
	}
	

}
