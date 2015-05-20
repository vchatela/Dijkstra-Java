package core;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Connexite extends Algo {

        protected BinaryHeap<Label> tas;           // Le tas
        protected ArrayList<Label> labels;         // Liste de tous les Labels
        protected HashMap<Node, Label> mapLabel;   // Correspondre un noeud à un Label

        public Connexite(Graphe gr, int origine, int destination, boolean affichageDeroulementAlgo) {
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

    @Override
    public int getNb_elements_tas() {
        return 0;
    }

    @Override
    public double getCoutMinTemps() {
        return 0;
    }

    public ArrayList run() {

        if ((origine <= 0) || (origine > graphe.getArrayList().size())) {
            JOptionPane.showMessageDialog(null, "Le numero de sommet d'origine saisi : " + origine + " n'appartient pas au graphe");
        }
        else if ((destination <= 0) || (destination > graphe.getArrayList().size())) {
            JOptionPane.showMessageDialog(null, "Le numero de sommet de destination saisi : " + origine + " n'appartient pas au graphe");
        }

        else {
            System.out.println("Lancement de l'algorithme Connexité de (zone,noeud) : (" + zoneOrigine + "," + origine + ") vers (" + zoneDestination + "," + destination + ")");

            // Mesurer le temps d'execution de l'algorithme
            duree = System.currentTimeMillis();

            // Il faut Initialiser l'algo
            initialisation();

            Label current, succ;
            Node node_succ;
            double new_cout;

            while (!((tas.isEmpty() || dest.isMarque()))) {

                // On récupère un élément du tas que l'on marque comme visité
                current = tas.deleteMin();
                current.setMarque(true);

                // Pour chaque successeur / arc
                for (Arc arc : graphe.getArrayList().get(current.getNum_node()).getArrayListArc()) {

                    // On récupère le noeud successeur de l'arc en cours
                    node_succ = graphe.getArrayList().get(arc.getNum_dest());

                    // Label correspondant au noeud destinataire
                    succ = mapLabel.get(node_succ);

//                    // On verifie si le cout est inferieur au precedent
//                    new_cout = arc.getLg_arete() + current.getCout();
//                    if (new_cout < succ.getCout()) {
//                        // Si oui, on met à jour les valeurs des couts
//                        succ.setCout(new_cout);
//                        succ.setPere(((Label_Generique) current).getNum_node());
//                    }

                    // Si le sommet n'est pas marque ET qu'il n'est pas dans le tas
                    if (!(succ.isMarque()) && tas.getMap().get(succ) == null) {

                        // On insere le sommet dans le tas
                        tas.insert(succ);

                        // On peut afficher le sommet sur la carte
                        if (affichageDeroulementAlgo) {
                            graphe.getDessin().drawPoint(node_succ.getLongitude(), node_succ.getLatitude(), 3);
                        }
                    }
                }
            }

            // Origine et Destination sont-ils connexes ?
            connexes = dest.isMarque();

            // On enregistre le temps d'execution de l'algorithme
            duree = (System.currentTimeMillis() - duree);

            // Mise à jour du résultat pour affichage et fichier de sortie
            ArrayList<String> resultat = new ArrayList<>();
            if(connexes)    resultat.add("connexes");
            else            resultat.add("non connexes");
            resultat.add(AffichageTempsCalcul(duree));

            return resultat;
        }
        return null;
    }

    @Override
    public ArrayList getLabels() {
        return labels;
    }

    @Override
    public HashMap getMapLabel() {
        return null;
    }

    @Override
    public void setMapLabel(HashMap mapLabel) {

    }

}