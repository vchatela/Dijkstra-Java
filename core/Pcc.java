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
	protected Label dest ;
	//en temps (choix=1),  en distance (choix=0)
	protected int choix;
	//Afficher ou non le déroulement de l'algo
	protected int choixAffichage;
	//fait correspondre un noeud à un label
	protected HashMap< Node,Label> mapLab;
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

		/*Algorithme (à améliorer)
		 * On part du noeud d'origine
		 * On parcourt tous ses successeurs
		 * Si ils sont pas marqué alors on met a jour leur valeur du cout : valeur du cout du noeud + cout de l'arc
		 * Si ils sont déjà marqué, alors testé si cette valeur est inférieure à la valeur qu'a déjà ce noeud
		 * 			si < alors update sinon rien
		 * 
		 * 
		 * A réfléchir : est il possible qu'un noeud déjà traité soit modifié et qu'il faille modifier les cout
		 * de tous les noeuds qui utilise sur leur chemin ce noeud ?
		 */
    }

}
