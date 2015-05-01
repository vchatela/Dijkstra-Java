package core;

import base.Descripteur;

import java.util.ArrayList;

public class Arc {

	/*
     * Declaration des variables
	*/

    private int num_zone;            //Numero de la zone
    private int num_dest;            //Numero du destinataire
    private int num_descripteur;    //Numero du descripteur
    private int lg_arete;            //Longueur de l'arete
    private int nb_segments;        //Nombre de segments representant l'arc (affichage)
    private Node node_source;        //Noeud source de l'arc
    private Descripteur descripteur;        //Descripteur de l'arc
    private ArrayList<Segment> listSegment;    //Liste des segments representant l'arc (affichage)

	
	
	/*
	 * Constructeurs
	*/

    public Arc() {
        this.num_zone = 0;
        this.num_dest = 0;
        this.num_descripteur = 0;
        this.lg_arete = 0;
        this.nb_segments = 0;
        this.node_source = null;
        this.descripteur = null;
        this.setListSegment(new ArrayList<Segment>());
    }

    public Arc(int nb_zone, int num_dest, int num_descripteur, int lg_arete, int nb_segments, Descripteur descripteur, Node node_source) {
        this.num_zone = nb_zone;
        this.num_dest = num_dest;
        this.num_descripteur = num_descripteur;
        this.lg_arete = lg_arete;
        this.nb_segments = nb_segments;
        this.node_source = node_source;
        this.descripteur = descripteur;
        this.setListSegment(new ArrayList<Segment>());
    }
	
	
	
	/*
	 * Methodes
	*/

    //Ajouter le descripteur de l'arc
    public void addDescripteur(Descripteur descripteur) {
        this.descripteur = descripteur;
    }

    //Affichage : Ajouter un segment pour la representation de l'arc -> un arc peut contenir plusieurs segments
    public void addSegment(Segment segment) {
        this.listSegment.add(segment);
    }

    //Recuperer tous les segments caracterisant un arc
    public ArrayList<Segment> getListSegment() {
        return listSegment;
    }

    //Initialisation de la liste des segments de l'arc
    public void setListSegment(ArrayList<Segment> listSegment) {
        this.listSegment = listSegment;
    }

    //GETTEURS ET SETTEURS GENERES :
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
