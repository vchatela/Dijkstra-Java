package core;

import base.Readarg;

import javax.swing.*;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Pcc_Generique<E extends Comparable<E>> extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine;
    protected int origine;

    protected int zoneDestination;
    protected int destination;


    //liste de tous les Label_Dijkstras
    protected ArrayList<E> labels;
    //Le tas
    protected BinaryHeap<E> tas;
    //Label_Dijkstra destinataire
    protected E dest;
    //Afficher ou non le deroulement de l'algo
    protected int choixAffichage;
    //en temps (choix=1),  en distance (choix=0)
    protected int choix;
    //fait correspondre un noeud a un Label_Dijkstra
    protected HashMap<Node, E> mapLabel;
    //duree d'execution
    protected long duree;
    //Nombre maximum d'elemnt dans le tas
    protected int maxTas;
    //Nombre d'element explores
    protected int nb_elements_tas;
    //contient le resultat a enregister dans un fichier
    protected boolean TOUS;

    public Pcc_Generique(Graphe gr, int choixCout, int affichageDeroulementAlgo, int origine, int dest, boolean TOUS) {
        super(gr);
        this.choix = choixCout;
        this.choixAffichage = affichageDeroulementAlgo;
        this.TOUS = TOUS;

        mapLabel = new HashMap<Node, E>();
        this.zoneOrigine = gr.getZone();
        this.origine = origine;
        this.zoneDestination = gr.getZone();
        this.destination = dest;

        if ((origine <= 0) || (origine > graphe.getArrayList().size())) {
            System.out.println(" Le numero de sommet saisi : " + origine + " n'appartient pas au graphe");
            return;
        }
        if ((destination <= 0) || (destination > graphe.getArrayList().size())) {
            System.out.println(" Le numero de sommet saisi : " + origine + " n'appartient pas au graphe");
            return;
        }
    }

    public ArrayList<E> getLabels() {
        return labels;
    }

    public void setLab(ArrayList<E> lab) {
        this.labels = lab;
    }

    /**
     * Initialisation de l'algo de Dijikstra
     */
    public void initialisation() {
        // implementees dans les sous classes
    }

    public ArrayList run() {
        if (((origine <= 0) || (origine > graphe.getArrayList().size()) || (destination <= 0) || (destination > graphe.getArrayList().size()))) {
            JOptionPane.showMessageDialog(null, "Un des sommets n'appartient pas au graphe.");
            return null;
        }
        // a noter que : si booleen TOUS alors message de confirmation de vers tout le monde

        System.out.println();
        System.out.println("Lancement de l'algorithme de (zone,noeud) : (" + zoneOrigine + "," + origine + ") vers (" + zoneDestination + "," + destination + ")");
// Initialisation de nos champs
        this.labels = new ArrayList<E>();
        this.tas = new BinaryHeap<E>();
        // Nombre max des elements et ceux explores
        this.maxTas = tas.size();
        nb_elements_tas = 1;
        double new_cout = 0;
        // afin de mesurer le temps d'execution on mettra une duree
        this.duree = System.currentTimeMillis();

        // Il faut Initialiser l'algo
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

		/* Boucle principale*/
        E min, E_succ;
        Node node_suc;
        while (!((this.tas.isEmpty() || ((Label) dest).isMarque()) && !TOUS) && !(TOUS && this.tas.isEmpty())) {
            min = this.tas.deleteMin();
            ((Label) min).setMarque(true);
            // pour chaque successeurs / arc
            for (Arc arc : this.graphe.getArrayList().get(((Label) min).getNum_node()).getArrayListArc()) {

                //TODO pour pfrance.x : si on sort de la map, ca plante
                node_suc = this.graphe.getArrayList().get(arc.getNum_dest());
                // Label_Dijkstra correspondant au noeud destinataire
                E_succ = mapLabel.get(node_suc);
                // si le noeud n'est pas marque
                if (!(((Label) E_succ).isMarque())) {
                    // on met alors le cout a jour
                    // TODO : verifier temps !
                    new_cout = (choix == 0) ? arc.getLg_arete() + ((Label) min).getCout() : 60.0f * ((float) arc.getLg_arete()) / (1000.0f * (float) arc.getDescripteur().getVitMax()) + ((Label) min).getCout();
                    // on verifie alors que ce cout est bien inferieur au precedent
                    if (new_cout < ((Label) E_succ).getCout()) {
                        ((Label) E_succ).setCout(new_cout);
                        ((Label) E_succ).setPere(((Label) min).getNum_node());
                    }
                    // maintenant si le sommet n'est pas dans le tas il faut l'ajouter
                    if (this.tas.getMap().get(E_succ) == null) {
                        // on insere le sommet dans le tas
                        this.tas.insert(E_succ);
                        nb_elements_tas++;
                        // On peut afficher le sommet sur la carte
                        if (choixAffichage == 1) {
                            //graphe.getDessin().setColor(Color.magenta);
                            this.graphe.getDessin().drawPoint(node_suc.getLongitude(), node_suc.getLatitude(), 3);
                        }
                    }
                    // sinon il ne faut pas oublier de mettre a jour le tas !
                    else {
                        this.tas.update(E_succ);
                    }
                }
            }
            // on met a jours la valeur max du tas
            if (maxTas < tas.size()) {
                maxTas = tas.size();
            }
        }
        // pour terminer on affichera le temps de calcul

        duree = (System.currentTimeMillis() - duree);
        System.out.println("Duree= " + duree + " ms");
        //Afficher le resultat du calcul - ou rediriger sur fichier
        chemin();

        // Mise à jour du résultat pour affichage et fichier de sortie
        ArrayList resultat = new ArrayList();
        if (choix == 0)
            resultat.add(((Label) dest).getCout() / 1000);
        else
            resultat.add(((Label) dest).getCout());
        resultat.add(duree);
        resultat.add(maxTas);
        resultat.add(nb_elements_tas);

        return resultat;
    }

    public void chemin() {
        // on construit le chemin du dest->origine
        Chemin chemin = new Chemin(origine, destination);
        chemin.addNode(this.graphe.getArrayList().get(destination));
        E E_en_cours = dest;
        Node node;
        // On remonte avec l'aide du pere !
        // Tant qu'on n'atteint pas le sommet d'origine qui a pour pere -1
        while (((Label) E_en_cours).getPere() != -1) {
            node = this.graphe.getArrayList().get(((Label) E_en_cours).getPere());
            chemin.addNode(node);
            E_en_cours = mapLabel.get(node);
        }
        // cout et affichage du chemin
        Collections.reverse(chemin.getListNode());
        //TODO : traiter la difference suivant le choix ?
        System.out.println();
        System.out.println("Calcul sur le Chemin");
        chemin.cout_chemin_distance();
        chemin.cout_chemin_temps();
        chemin.tracerChemin(this.graphe.getDessin());
    }
}
