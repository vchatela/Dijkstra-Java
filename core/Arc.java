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
        this.setListSegment(new ArrayList<>());
    }

    public Arc(int nb_zone, int num_dest, int num_descripteur, int lg_arete, int nb_segments, Descripteur descripteur, Node node_source) {
        this.num_zone = nb_zone;
        this.num_dest = num_dest;
        this.num_descripteur = num_descripteur;
        this.lg_arete = lg_arete;
        this.nb_segments = nb_segments;
        this.node_source = node_source;
        this.descripteur = descripteur;
        this.setListSegment(new ArrayList<>());
    }
	

	/*
	 * Methodes
	*/

    //Affichage : Ajouter un segment pour la representation de l'arc -> un arc peut contenir plusieurs segments
    public void addSegment(Segment segment) {
        this.listSegment.add(segment);
    }

    //Initialisation de la liste des segments de l'arc
    public void setListSegment(ArrayList<Segment> listSegment) {
        this.listSegment = listSegment;
    }

    public Descripteur getDescripteur() {
        return descripteur;
    }

    public int getLg_arete() {
        return lg_arete;
    }

    public int getNum_dest() {
        return num_dest;
    }
}
