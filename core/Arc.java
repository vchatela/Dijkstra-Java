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
		this.setListSegment(new ArrayList<Segment>());
	}
	
	/*
	 * Méthodes
	*/
	public void addSegment() {
	
	}
	
	public void addDescripteur() {
	
	}

	public ArrayList<Segment> getListSegment() {
		return listSegment;
	}

	public void setListSegment(ArrayList<Segment> listSegment) {
		this.listSegment = listSegment;
	}

	public Descripteur getDescripteur() {
		return descripteur;
	}

	public void setDescripteur(Descripteur descripteur) {
		this.descripteur = descripteur;
	}

	public Node getNode_source() {
		return node_source;
	}

	public void setNode_source(Node node_source) {
		this.node_source = node_source;
	}

	public int getNb_segments() {
		return nb_segments;
	}

	public void setNb_segments(int nb_segments) {
		this.nb_segments = nb_segments;
	}

	public int getLg_arete() {
		return lg_arete;
	}

	public void setLg_arete(int lg_arete) {
		this.lg_arete = lg_arete;
	}

	public int getNum_descripteur() {
		return num_descripteur;
	}

	public void setNum_descripteur(int num_descripteur) {
		this.num_descripteur = num_descripteur;
	}

	public int getNum_dest() {
		return num_dest;
	}

	public void setNum_dest(int num_dest) {
		this.num_dest = num_dest;
	}

	public int getNum_zone() {
		return num_zone;
	}

	public void setNum_zone(int num_zone) {
		this.num_zone = num_zone;
	}
}
