package core;

public class Segment {

	/*
	 * Declaration des variables
	*/
	
	private float delta_long;
	private float delta_lat;
	
	/*
	 * Constructeurs
	*/
	public Segment(){
		this.delta_long = 0;
		this.delta_lat = 0;
	}
	
	public Segment(float delta_long, float delta_lat){
		this.delta_long = delta_long;
		this.delta_lat = delta_lat;
	}
	
	/*
	 * Methodes
	*/
	public float getDeltaLong() {
		return this.delta_long;
	}
	
	public float getDeltaLat() {
		return this.delta_lat;
	}
}
