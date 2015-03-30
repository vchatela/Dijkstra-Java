package core;

import java.util.ArrayList;

import base.Descripteur;

public class Arc {

	/*
	 * Declaration des variables
	*/
	
	private int num_zone;
	private int num_dest;
	private int num_descripteur;
	private int lg_arete;
	private int nb_segments;
	private Node node_source;
	private Descripteur descripteur;
	private ArrayList<Segment> listSegment;
	
	/*
	 * Constructeurs
	*/
	public Arc(){
		this.listSegment = new ArrayList<Segment>();
	}
	
	/*
	 * Méthodes
	*/
	public void addSegment() {
	
	}
	
	public void addDescripteur() {
	
	}
}
