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
	//protected int choixAffichage;
	
	//fait correspondre un noeud à un label
	protected HashMap<Node,Label> mapLabel;
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
		mapLabel = new HashMap<Node,Label>();
		// a voir si on demande la zone ou le sommet directement
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
	// Si le numéro des noeuds n'est pas dans le graphe on doit arréter l'algo
	if((origine<=0)||(origine>graphe.getNode().size())){
	    System.out.println(" Le numero de sommet saisi n'appartient pas au graphe");
	    System.exit(-1);
	}
	if((destination<=0)||(destination>graphe.getNode().size())){
	    System.out.println(" Le numero de sommet saisi n'appartient pas au graphe");
	    System.exit(-1);
	}
	
	// Enfin on demande le type choisi : temps ou distance  - TODO : A améliorer
	this.choix = Integer.parseInt(JOptionPane.showInputDialog("Plus court en:\n0 : Distance\n1 : Temps"));
	// a voir si on fait le choxi de l'affichage avec choixAffichage
	//choixAffichage=JOptionPane.showConfirmDialog(null, "Voulez vous afficher le deroulement de l'algo","Choix de l'affichage", JOptionPane.YES_NO_OPTION);
}

    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
// Initialisation de nos champs
		this.lab = new ArrayList<Label>();
		this.tas = new BinaryHeap<Label>();
		// Nombre max des éléments et ceux explorés
		this.maxTas = tas.getCurrentSize();
		nb_elements_tas=1;
		double new_cout = 0;
		// afin de mesurer le temps d'éxécution on mettra une durée
		this.duree = System.currentTimeMillis();
		
		// A verifier s'il faut Initialiser l'algo ou pas
		
		
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
		 
		/* Boucle principale
		  TODO : condition très moche : comment faire mieux ?
		  TODO : nom des fonctions a vérifier .... de mémoire sans éclipse
		*/
		Label min , label_succ;
		Node node_suc;
		while(!(this.tas.isEmpty() || dest.isMarque())){
		    min = this.tas.deleteMin();
		    min.setMarque(true);
		    // pour chaque successeurs / arc
		    for(Arc arc:this.graphe.getNode().get(min).getNum_node().getArcs()){
			node_suc = this.graphe.getNode().get(arc.getNum_dest());
			// Label correspondant au noeud destinataire
			label_succ=mapLabel.get(node_suc);
			// si le noeud n'est pas marqué
			if (!(label_succ.isMarque())){
			    // on met alors le cout a jour
			    new_cout= (choix==0)?succ.getLg_arete()+ min.getCout():60.0f*((float)succ.getLg_arete())/(1000.0f*(float)succ.getDescripteur().getVitMax())+ min.getCout();
			    // on vérifie alors que ce cout est bien inférieur au précédent
			    if(new_cout<lab_succ.getCout()){
						lab_succ.setCout(new_cout);
						lab_succ.setPere(min.getNum_node());
			    }
			    // maintenant si le sommet n'est pas dans le tas il faut l'ajouter
			    if (!(this.tas.getMap().get(lab_succ)!=null)){
				// on insère le sommet dans le tas 
				this.tas.insert(label_succ);
				nb_elements_tas++;
				// On peut afficher le sommet sur la carte 
				/*if(choixAffichage==JOptionPane.OK_OPTION){
							graphe.getDessin().setColor(Color.magenta);
							this.graphe.getDessin().drawPoint(noeud_succ.getLongitude(),noeud_succ.getLatitude(), 3);
						}
				*/
			    }
			    // sinon il ne faut pas oublier de mettre a jour le tas !
			    else {
				this.tas.update(label_succ);
			    }
			}
		    }
		// on met a jours la valeur max du tas
		if (maxTas<tas.getCurrentSize();
		    maxTas=tas.getCurrentSize();
		}
    }
    // pour terminer on affichera le temps de calcul
    
    duree = (System.currentTimeMillis() - duree);
    System.out.println("Duree= "+duree+" ms");
    //Afficher le résultat du calcul - ou rediriger sur fichier ?
    // Fonction de retour a faire ? Avec JOptionPane ?
    /* comme ca ?
    if(choix==0){
    		JOptionPane.showMessageDialog(null, "Le coût est de "+ dest.getCout()/1000+ "km\n"+
    				"Temps de Calcul: "+duree+ " ms\n"+
    				"Nb max d'element: "+maxTas+"\nNb élements explorés: "+nb_elements_tas);
    }
    else ...
    */
    // on pourra aussi tracer le chemin ! cf en dessous
    chemin();
    
}

public void chemin(){
  // on construit le chemin du dest->origine
  Chemin chemin=new Chemin(origine, destination);
  chemin.addNode(this.graphe.getNode().get(destination));
  Label label_en_cours = dest;
  Node node;
  // On remonte avec l'aide du pere !
  // Tant qu'on n'atteint pas le sommet d'origine qui a pour père -1 
  while (label_en_cours.getPere()!=-1){
      node = this.graphe.getNode().get(label_en_cours.getPere());
      chemin.addNode(node);
      label_en_cours = mapLabel.get(node);
  }
  // Il faut inverser l'ordre des éléments dans le chemin !
  // comment faire un reverse ? Collections ?
  
  // cout et affichage du chemin
  chemin.cout_chemin_distance(); // je crois que c'est ca ...)
  chemin.tracerChemin(this.graphe.getDessin()); // ca doit exister aussi ...
}
