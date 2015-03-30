package core;
package base;

public class Segment {

	/*
	 * Declaration des variables
	*/
	
	private float delta_long;
	private float delta_lat;
	
	/*
	 * Constructeurs
	*/
	public Arc(){
		this.delta_long = 0;
		this.delta_lat = 0;
	}
	
	public Arc(float delta_long, float delta_lat){
		this.delta_long = delta_long;
		this.delta_lat = delta_lat;
	}
	
	/*
	 * Méthodes
	*/
	public float getDeltaLong() {
		return this.delta_long;
	}
	
	public float getDeltaLat() {
		return this.delta_lat;
	}
}
