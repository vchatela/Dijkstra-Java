package core;

import java.util.ArrayList;

public class Node {

	/*
	 * Declaration des variables
	*/
	
	private float longitude;		//Longitude du noeud
	private float latitude;			//Latidute du noeud
	private ArrayList<Arc> listArc;	//Liste des arcs du noeuds -> ses successeurs
	private int numberArc;			//Nombre d'arcs du noeuds
	
	
	
	/*
	 * Constructeurs
	*/
	
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
	
	

	/*
	 * Méthodes
	*/
	
	//Returne la liste des arcs successeurs du noeud
	public ArrayList<Arc> getArrayListArc(){
		return this.listArc;
	}
	
	//GETTEURS ET SETTEURS GENERES :
	public void setLongitude(float longitude){
		this.longitude = longitude;
	}
	
	public float getLongitude(){
		return this.longitude;
	}
	
	public void setLatitude(float latitude){
		this.latitude = latitude;
	}
	
	public float getLatitude(){
		return this.latitude;
	}
	
	public void setNumberArc(int numberArc){
		this.numberArc = numberArc;
	}
	public int getNumberArc(){
		return this.numberArc;
	}
}
