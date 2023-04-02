package net.etfbl.model;

public class LocationData {
	
	public LocationData() {
		
	}
	
	public LocationData(int startTime, int endTime, int x, int y) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.x = x;
		this.y = y;
	}

	private int startTime, endTime, x, y;

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
	

}
