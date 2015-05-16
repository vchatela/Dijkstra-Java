package core;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Connexite extends Algo {

    protected BinaryHeap<Label> tas;           // Le tas
    protected ArrayList<Label> labels;         // Liste de tous les Labels
    protected HashMap<Node, Label> mapLabel;   // Correspondre un noeud à un Label

    public Connexite(Graphe gr, boolean affichageDeroulementAlgo, int origine, int destination) {
        super(gr);
        this.affichageDeroulementAlgo = affichageDeroulementAlgo;

        this.zoneOrigine = gr.getZone();
        this.origine = origine;
        this.zoneDestination = gr.getZone();
        this.destination = destination;

        this.graphe.getDessin().setColor(Color.cyan);
    }

    /**
     * Initialisation de l'algo de Connexite
     */
    public void initialisation() {
        labels = new ArrayList<>();
        tas = new BinaryHeap<>();
        mapLabel = new HashMap<>();

        //Associe des Labels correspondant aux noeuds et le stocke dans la map !
        for (Node node : graphe.getArrayList()) {
            Label l = new Label(node);
            mapLabel.put(node, l);
            labels.add(l);

            // Noeud origine
            if ((node.getNum() == origine) && (graphe.getZone() == zoneOrigine)) {
                l.setCout(0);
                //Initialisation du tas avec le Label sommet origine
                tas.insert(l);
            }

            // Noeud destinataire
            if ((node.getNum() == destination) && (graphe.getZone() == zoneDestination)) {
                dest = l;
            }
        }
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
            System.out.println("Lancement de l'algorithme Connexité de (zone,noeud) : (" + zoneOrigine + "," + origine + ") vers (" + zoneDestination + "," + destination + ")");

            // Initialisation de nos champs
            double new_cout = 0;
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
            Label min, succ;
            Node node_suc;

            while (!((this.tas.isEmpty() || dest.isMarque()))) {
                min = this.tas.deleteMin();
                ((Label_Generique) min).setMarque(true);
                // pour chaque successeurs / arc
                for (Arc arc : this.graphe.getArrayList().get(((Label_Generique) min).getNum_node()).getArrayListArc()) {

                    node_suc = this.graphe.getArrayList().get(arc.getNum_dest());
                    // Label correspondant au noeud destinataire
                    succ = mapLabel.get(node_suc);
                    // si le noeud n'est pas marque
                    if (!(succ.isMarque())) {
                        // on met alors le cout a jour
                        new_cout = (arc.getLg_arete() + min.getCout());
                        // on verifie alors que ce cout est bien inferieur au precedent
                        if (new_cout < succ.getCout()) {
                            succ.setCout(new_cout);
                            succ.setPere(min.getNum_node());
                        }
                        // maintenant si le sommet n'est pas dans le tas il faut l'ajouter
                        if (this.tas.getMap().get(succ) == null) {
                            // on insere le sommet dans le tas
                            this.tas.insert(succ);
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
            }

            // On enregistre le temps d'execution de l'algorithme
            duree = (System.currentTimeMillis() - duree);

            connexes = dest.isMarque();

            // Mise à jour du résultat pour affichage et fichier de sortie
            ArrayList resultat = new ArrayList();
            if(connexes)
                resultat.add("Les points sont connexes");
            else
                resultat.add("Les points ne sont pas connexes");
            resultat.add(duree);

            return resultat;
        }
        return null;
    }

    @Override
    public ArrayList getLabels() {
        return labels;
    }
}
