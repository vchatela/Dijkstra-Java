package core;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Pcc_Generique<E extends Comparable<E>> extends Algo {

    protected int choixCout;        // Cout en temps (1) ou en distance (0)
    protected int maxTas;           // Nombre maximum d'elemnt dans le tas
    protected int nb_elements_tas;  // Nombre d'element explores
    protected boolean TOUS;         // Si on souhaite parcourir tous les sommets
    protected BinaryHeap<E> tas;           // Le tas
    protected ArrayList<E> labels;         // Liste de tous les Labels
    protected HashMap<Node, E> mapLabel;   // Correspondre un noeud à un Label

    public Pcc_Generique(Graphe gr, int choixCout, boolean affichageDeroulementAlgo, int origine, int destination, boolean TOUS) {
        super(gr);
        this.choixCout = choixCout;
        this.affichageDeroulementAlgo = affichageDeroulementAlgo;
        this.TOUS = TOUS;

        this.zoneOrigine = gr.getZone();
        this.origine = origine;
        this.zoneDestination = gr.getZone();
        this.destination = destination;
    }

    /**
     * Initialisation de l'algo de Dijikstra
     */
    public void initialisation() {
        // implementees dans les sous classes
    }

    public ArrayList run() {

        // On verifie que nos noeuds existent sur la carte
        if ((origine <= 0) || (origine > graphe.getArrayList().size())) {
            JOptionPane.showMessageDialog(null, "Le numero de sommet d'origine saisi : " + origine + " n'appartient pas au graphe");
        }
        else if ((destination <= 0) || (destination > graphe.getArrayList().size())) {
            JOptionPane.showMessageDialog(null, "Le numero de sommet de destination saisi : " + origine + " n'appartient pas au graphe");
        }
        else {
            // a noter que : si booleen TOUS alors message de confirmation de vers tout le monde

            System.out.println("Lancement de l'algorithme PCC de (zone,noeud) : (" + zoneOrigine + "," + origine + ") vers (" + zoneDestination + "," + destination + ")");

            // Initialisation de nos champs
            labels = new ArrayList<>();
            tas = new BinaryHeap<>();
            mapLabel = new HashMap<>();
            // Nombre max des elements et ceux explores
            maxTas = tas.size();
            nb_elements_tas = 1;
            double new_cout;
            // afin de mesurer le temps d'execution on mettra une duree
            duree = System.currentTimeMillis();

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
            E min, succ;
            Node node_suc;
            while (!((this.tas.isEmpty() || dest.isMarque()) && !TOUS) && !(TOUS && this.tas.isEmpty())) {
                min = (E) this.tas.deleteMin();
                ((Label_Generique) min).setMarque(true);
                // pour chaque successeurs / arc
                for (Arc arc : this.graphe.getArrayList().get(((Label_Generique) min).getNum_node()).getArrayListArc()) {

                    //TODO pour pfrance.x : si on sort de la map, ca plante
                    node_suc = this.graphe.getArrayList().get(arc.getNum_dest());
                    // Label correspondant au noeud destinataire
                    succ = (E) mapLabel.get(node_suc);
                    // si le noeud n'est pas marque
                    if (!(((Label_Generique) succ).isMarque())) {
                        // on met alors le cout a jour
                        // TODO : verifier temps !
                        new_cout = (choixCout == 0) ? (arc.getLg_arete() + ((Label_Generique) min).getCout()) : (((60.0f * ((float) arc.getLg_arete())) / (1000.0f * (float) arc.getDescripteur().getVitMax())) + ((Label_Generique) min).getCout());
                        // on verifie alors que ce cout est bien inferieur au precedent
                        if (new_cout < ((Label_Generique) succ).getCout()) {
                            ((Label_Generique) succ).setCout(new_cout);
                            ((Label_Generique) succ).setPere(((Label_Generique) min).getNum_node());
                        }
                        // maintenant si le sommet n'est pas dans le tas il faut l'ajouter
                        if (this.tas.getMap().get(succ) == null) {
                            // on insere le sommet dans le tas
                            this.tas.insert(succ);
                            nb_elements_tas++;
                            // On peut afficher le sommet sur la carte
                            if (affichageDeroulementAlgo) {
                                this.graphe.getDessin().drawPoint(node_suc.getLongitude(), node_suc.getLatitude(), 3);
                            }
                        }
                        // sinon il ne faut pas oublier de mettre a jour le tas !
                        else {
                            this.tas.update(succ);
                        }
                    }
                }
                // on met a jours la valeur max du tas
                if (maxTas < tas.size()) {
                    maxTas = tas.size();
                }
            }

            // Tracer le chemin si le cout n'est pas infiny
            if (!(dest.getCout() == Float.POSITIVE_INFINITY))
                chemin();

            // On enregistre le temps d'execution de l'algorithme
            this.duree = (System.currentTimeMillis() - duree);

            connexes = dest.isMarque();

            // Mise à jour du résultat pour affichage et fichier de sortie
            ArrayList resultat = new ArrayList<>();
            if(connexes)
                resultat.add("Les points sont connexes");
            else
                resultat.add("Les points ne sont pas connexes");
            resultat.add(duree);
            if (choixCout == 0)
                resultat.add(dest.getCout() / 1000);
            else
                resultat.add(dest.getCout());
            resultat.add(maxTas);
            resultat.add(nb_elements_tas);

            return resultat;
        }
        return null;
    }

    @Override
    public ArrayList getLabels() {
        return labels;
    }

    public void chemin() {
        // on construit le chemin du dest->origine
        Chemin chemin = new Chemin(origine, destination);
        chemin.addNode(this.graphe.getArrayList().get(destination));
        E en_cours = (E) dest;
        Node node;
        // On remonte avec l'aide du pere !
        // Tant qu'on n'atteint pas le sommet d'origine qui a pour pere -1
        while (((Label_Generique) en_cours).getPere() != -1) {
            node = this.graphe.getArrayList().get(((Label_Generique) en_cours).getPere());
            chemin.addNode(node);
            en_cours = (E) mapLabel.get(node);
        }
        // cout et affichage du chemin
        Collections.reverse(chemin.getListNode());
        //TODO : traiter la difference suivant le choix ?
        System.out.println();
        chemin.tracerChemin(this.graphe.getDessin());
    }
}
