package core;

import java.util.ArrayList;

public class Node {

    /*
     * Declaration des variables
    */
    private int num;                //Numero du noeud
    private float longitude;        //Longitude du noeud
    private float latitude;            //Latidute du noeud
    private ArrayList<Arc> listArc;    //Liste des arcs du noeuds -> ses successeurs
    private int numberArc;            //Nombre d'arcs du noeuds

	
	
	/*
     * Constructeurs
	*/

    public Node() {
        this.num = 0;
        this.longitude = 0;
        this.latitude = 0;
        this.listArc = new ArrayList<Arc>();
        this.numberArc = 0;
    }

    public Node(int num, float longitude, float latitude, int numberArc) {
        this.num = num;
        this.longitude = longitude;
        this.latitude = latitude;
        this.listArc = new ArrayList<Arc>();
        this.numberArc = numberArc;
    }

	

	/*
	 * Methodes
	*/

    //Returne la liste des arcs successeurs du noeud
    public ArrayList<Arc> getArrayListArc() {
        return this.listArc;
    }

    public float getLongitude() {
        return this.longitude;
    }

    //GETTEURS ET SETTEURS GENERES :
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getNumberArc() {
        return this.numberArc;
    }

    public void setNumberArc(int numberArc) {
        this.numberArc = numberArc;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
