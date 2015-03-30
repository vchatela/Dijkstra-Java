package core;

import java.util.ArrayList;

public class Node {
	private float longitude;
	private float latitude;
	private int number_Successeur;
	private ArrayList<Successeur> listSuccesseur;
	
	public Node(){
		this.listSuccesseur = new ArrayList<Arc>();
	}
	
	public Node(float longitude, float latitude){
		this.longitude=longitude;
		this.latitude=latitude;
		this.number_Successeur = 0;
		this.listSuccesseur = new ArrayList<Successeur>();
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
	
	public void setNumberSuccesseur(int i){
		this.number_Successeur=i;
	}
	
	public int getNumberSuccesseur(){
		return this.number_Successeur;
	}
	
	public ArrayList<Successeur> getArrayListSuccesseur(){
		return this.listSuccesseur;
	}
}
