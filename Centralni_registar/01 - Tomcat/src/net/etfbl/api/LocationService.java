package net.etfbl.api;

import java.util.ArrayList;

import net.etfbl.model.DataSource;
import net.etfbl.model.Location;

public class LocationService {
	
	
	public ArrayList<Location> getAllLocations(int id) {
		
		DataSource dataSource = new DataSource();
		
		
		return dataSource.getAllLocations(id);
		
	}
	
	public void addLocation(int id, int x, int y, int start, int end) {
		
		
		DataSource data = new DataSource();
		data.addLocation(id,x,y,start,end);
		
		
		
	}
	

}
