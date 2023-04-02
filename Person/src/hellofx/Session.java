package hellofx;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Session {

	public static int counterID = 0;
	
	public SimpleIntegerProperty id;
	public SimpleStringProperty sessionStart, sessionEnd;
	public SimpleIntegerProperty duration;
	
	
	
	public Session( String sessionStart, String sessionEnd,
			int duration) {
		super();
		this.id = new SimpleIntegerProperty(++counterID);
		this.sessionStart = new SimpleStringProperty(sessionStart);
		this.sessionEnd = new SimpleStringProperty(sessionEnd);
		this.duration = new SimpleIntegerProperty(duration);
	}



	public int getId() {
		return id.get();
	}


	public SimpleIntegerProperty idProperty() {
		
		return id;
	}

	public String getSessionStart() {
		return sessionStart.get();
	}
	
	public SimpleStringProperty sessionStartProperty() {
		return sessionStart;
	}

	
	public String getSessionEnd() {
		
		return sessionEnd.get();
	}


	public SimpleStringProperty sessionEndProperty() {
		return sessionEnd;
	}

	public int getDuration() {
		
		return duration.get();
	}


	public SimpleIntegerProperty durationProperty() {
		return duration;
	}


	public void setId(int id) {
		
		this.id.set(id);
	}
	
	
	public void setSessionStart(String param) {
		
		this.sessionStart.set(param);
	}
	
	public void setSessionEnd(String param) {
		
		this.sessionEnd.set(param);
	}

	public void setDuration(int duration) {
		this.duration.set(duration);
	}
	
	
}
