package core ;

import java.io.* ;
import java.util.ArrayList;
import java.util.HashMap;

import base.Readarg ;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;

    
  //liste de tous les labels
    protected ArrayList <Label> lab;
    //Le tas
	protected  BinaryHeap <Label> tas;
	//label destinataire
	protected E dest ;
	//en temps (choix=1),  en distance (choix=0)
	protected int choix;
	//Afficher ou non le déroulement de l'algo
	protected int choixAffichage;
	//fait correspondre un noeud à un label
	protected HashMap< Node,E> mapLab;
	//durée d'exécution
	protected long duree;
	//Nombre maximum d'elemnt dans le tas
	protected int maxTas;
	//Nombre d'element explorés
	protected int nb_elements_tas;
	//contient le résultat à enregister dans un fichier
	protected String sortieAlgo; 
	
	
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;

		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
    }

    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

		// A vous d'implementer la recherche de plus court chemin.
    }

}
