package hellofx;

import javafx.beans.property.SimpleStringProperty;

public class Token {
	
	public SimpleStringProperty token;
	
	public Token(String tokenParam) {
		super();
		
		token = new SimpleStringProperty(tokenParam);
		
	}
	
	public String getToken() {
		
		return token.get();
	}
	
	public SimpleStringProperty tokenProperty() {
		
		return token;
	}
	
	public void setToken(String tokenParam) {
		
		
		this.token.set(tokenParam);
	}
	
	

}
