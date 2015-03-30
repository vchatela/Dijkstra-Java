package core;

import java.util.ArrayList;

public class Node {
	private float longitude;
	private float latitude;
	private ArrayList<Arc> listArc;
	private int numberArc;
	
	public Node(){
		this.longitude = 0;
		this.latitude = 0;
		this.listArc = new ArrayList<Arc>();
		this.numberArc = 0;
	}
	
	public Node(float longitude, float latitude){
		this.longitude=longitude;
		this.latitude=latitude;
		this.listArc = new ArrayList<Arc>();
		this.numberArc = 0;
	}
	
	public void setLongitude(float f){
		this.longitude = f;
	}
	
	public void setLatitude(float f){
		this.latitude = f;
	}
	
	public float getLongitude(){
		return this.longitude;
	}
	
	public float getLatitude(){
		return this.latitude;
	}
	
	public ArrayList<Arc> getArrayListArc(){
		return this.listArc;
	}
	
	public void setNumberArc(int numberArc){
		this.numberArc = numberArc;
	}
}
