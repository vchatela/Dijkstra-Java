package core ;

import java.io.* ;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;
import javax.swing.JOptionPane;

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
	
	//Afficher ou non le deroulement de l'algo
	protected int choixAffichage;
	
	//fait correspondre un noeud a un label
	protected HashMap<Node,Label> mapLabel;
	//duree d'execution
	protected long duree;
	//Nombre maximum d'elemnt dans le tas
	protected int maxTas;
	//Nombre d'element explores
	protected int nb_elements_tas;
	//contient le resultat a enregister dans un fichier
	protected String sortieAlgo; 
	
	
    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
		// On veut stocker le resultat dans le fichier de sortie de la forme
		/*Carte: france
				Dijsktra de 12 -> 11111 en temps
				cout: 59659.546845 mn
				Temps de Calcul: 261 ms
				Nombre maximum d'elements dans le tas: 25235
				Nombre d'elements explores: 54653
		*/
		mapLabel = new HashMap<Node,Label>();
		// a voir si on demande la zone ou le sommet directement
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
	// Si le numero des noeuds n'est pas dans le graphe on doit arreter l'algo
	if((origine<=0)||(origine>graphe.getArrayList().size())){
	    System.out.println(" Le numero de sommet saisi n'appartient pas au graphe");
	    System.exit(-1);
	}
	if((destination<=0)||(destination>graphe.getArrayList().size())){
	    System.out.println(" Le numero de sommet saisi n'appartient pas au graphe");
	    System.exit(-1);
	}
	
	// Enfin on demande le type choisi : temps ou distance  - TODO : A ameliorer
	this.choix = Integer.parseInt(JOptionPane.showInputDialog("Plus court en:\n0 : Distance\n1 : Temps"));
	String chaine=(choix==0)?"distance":"temps";
	sortieAlgo="Carte: "+graphe.getNomCarte()+"\n"+"Dijkstra de " +origine+ " -> "+destination+" en "+chaine;
	//L'affichage du deroulement de l'algo est faite en vert
	this.graphe.getDessin().setColor(Color.magenta);
		
	// a voir si on fait le choix de l'affichage avec choixAffichage
	choixAffichage=JOptionPane.showConfirmDialog(null, "Voulez vous afficher le deroulement de l'algo","Choix de l'affichage", JOptionPane.YES_NO_OPTION);
}

	 /**
	 * Initialisation de l'algo de Dijikstra
	 */
	public void initialisation(){
			//Associe des labels correspondant aux noeuds et le stocke dans la map !
			for(Node node:this.graphe.getArrayList()){
				Label l=new Label(node);
				mapLabel.put(node,l);
					if((node.getNum()==origine)&&(graphe.getZone()==zoneOrigine)){
						l.setCout(0);
						//Initialisation du tas avec le label sommet origine 
						tas.insert(l);
					}
					lab.add(l);
					
					//Noeud destinataire
					if((node.getNum()==destination)&&(graphe.getZone()==zoneDestination)){
						dest= l;
					}
			}
	}

    public void run() {

		System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
// Initialisation de nos champs
		this.lab = new ArrayList<Label>();
		this.tas = new BinaryHeap<Label>();
		// Nombre max des elements et ceux explores
		this.maxTas = tas.size();
		nb_elements_tas=1;
		double new_cout = 0;
		// afin de mesurer le temps d'execution on mettra une duree
		this.duree = System.currentTimeMillis();
		
		// Il faut Initialiser l'algo ou pas
		initialisation();
		
		/*Algorithme (a ameliorer)
		 * On part du noeud d'origine
		 * On parcourt tous ses successeurs
		 * Si ils sont pas marque alors on met a jour leur valeur du cout : valeur du cout du noeud + cout de l'arc
		 * Si ils sont deja marque, alors teste si cette valeur est inferieure a la valeur qu'a deja ce noeud
		 * 			si < alors update sinon rien
		 * 
		 * 
		 * A reflechir : est il possible qu'un noeud deja traite soit modifie et qu'il faille modifier les cout
		 * de tous les noeuds qui utilise sur leur chemin ce noeud ?
		 */
		 
		/* Boucle principale
		  TODO : condition tres moche : comment faire mieux ?
		  TODO : nom des fonctions a verifier .... de memoire sans eclipse
		*/
		Label min , label_succ;
		Node node_suc;
		while(!(this.tas.isEmpty() || dest.isMarque())){
		    min = this.tas.deleteMin();
		    min.setMarque(true);
		    // pour chaque successeurs / arc
		    for(Arc arc:this.graphe.getArrayList().get(min.getNum_node()).getArrayListArc()){
			node_suc = this.graphe.getArrayList().get(arc.getNum_dest());
			// Label correspondant au noeud destinataire
			label_succ=mapLabel.get(node_suc);
			// si le noeud n'est pas marque
			if (!(label_succ.isMarque())){
			    // on met alors le cout a jour
			    new_cout= (choix==0)?arc.getLg_arete()+ min.getCout():60.0f*((float)arc.getLg_arete())/(1000.0f*(float)arc.getDescripteur().getVitMax())+ min.getCout();
			    // on verifie alors que ce cout est bien inferieur au precedent
			    if(new_cout<label_succ.getCout()){
						label_succ.setCout(new_cout);
						label_succ.setPere(min.getNum_node());
			    }
			    // maintenant si le sommet n'est pas dans le tas il faut l'ajouter
			    if (!(this.tas.getMap().get(label_succ)!=null)){
                    // on insere le sommet dans le tas
                    this.tas.insert(label_succ);
                    nb_elements_tas++;
                    // On peut afficher le sommet sur la carte
                    if(choixAffichage==JOptionPane.OK_OPTION) {
                        graphe.getDessin().setColor(Color.magenta);
                        this.graphe.getDessin().drawPoint(node_suc.getLongitude(), node_suc.getLatitude(), 3);
                    }
			    }
			    // sinon il ne faut pas oublier de mettre a jour le tas !
			    else {
				this.tas.update(label_succ);
			    }
			}
		    }
		// on met a jours la valeur max du tas
		if (maxTas<tas.size()){
		    maxTas=tas.size();
		}
    }
    // pour terminer on affichera le temps de calcul
    
    duree = (System.currentTimeMillis() - duree);
    System.out.println("Duree= "+duree+" ms");
    //Afficher le resultat du calcul - ou rediriger sur fichier ?
    // Fonction de retour a faire ? Avec JOptionPane ?
    //comme ca ?
    if(choix==0){
    		JOptionPane.showMessageDialog(null, "Le cout est de "+ dest.getCout()/1000+ "km\n"+
    				"Temps de Calcul: "+duree+ " ms\n"+
    				"Nb max d'element: "+maxTas+"\nNb elements explores: "+nb_elements_tas);
    }
    else {
        JOptionPane.showMessageDialog(null, "Le cout est de "+ dest.getCout()+ "min\n"+
                "Temps de Calcul: "+duree+ " ms\n"+
                "Nb max d'element: "+maxTas+"\nNb elements explores: "+nb_elements_tas);
    }

    // on pourra aussi tracer le chemin ! cf en dessous
    chemin();
    
}

public void chemin(){
  // on construit le chemin du dest->origine
  Chemin chemin=new Chemin(origine, destination);
  chemin.addNode(this.graphe.getArrayList().get(destination));
  Label label_en_cours = dest;
  Node node;
  // On remonte avec l'aide du pere !
  // Tant qu'on n'atteint pas le sommet d'origine qui a pour pere -1 
  while (label_en_cours.getPere()!=-1){
      node = this.graphe.getArrayList().get(label_en_cours.getPere());
      chemin.addNode(node);
      label_en_cours = mapLabel.get(node);
  }
  // Il faut inverser l'ordre des elements dans le chemin !
  // comment faire un reverse ? Collections ?
  
  // cout et affichage du chemin
//  chemin.cout_chemin_distance(); // je crois que c'est ca ...)
  chemin.tracerChemin(this.graphe.getDessin()); // ca doit exister aussi ...
}
}
