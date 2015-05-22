package core;

import java.awt.*;
import java.util.Collections;

/**
 * Created by valentin on 4/29/15.
 */
public class Pcc_Dijkstra extends Pcc_Generique<Label> {


    public Pcc_Dijkstra(Graphe gr, int origine, int dest, int choixCout, boolean TOUS, boolean pieton, double tempsAttenteMaxPieton, boolean affichageDeroulementAlgo, boolean afficherChemin) {

        super(gr, origine, dest, choixCout, TOUS, pieton, tempsAttenteMaxPieton, affichageDeroulementAlgo, afficherChemin);

        this.graphe.getDessin().setColor(Color.red);
    }

    public void initialisation() {

        // Associe des Labels correspondant aux noeuds et le stocke dans la map !
        for (Node node : this.graphe.getArrayList()) {
            Label l = new Label(node);
            mapLabel.put(node, l);
            if ((node.getNum() == origine) && (graphe.getZone() == zoneOrigine)) {
                l.setCout(0);
                //Initialisation du tas avec le Label sommet origine
                tas.insert(l);
            }
            labels.add(l);

            //Noeud destinataire
            if ((node.getNum() == destination) && (graphe.getZone() == zoneDestination)) {
                dest = l;
            }
        }
    }

    public void chemin(int origine, int dest) {
        // on construit le chemin du dest->origine
        Chemin chemin = new Chemin(origine, dest);
        chemin.addNode(this.graphe.getArrayList().get(dest));
        Label_Generique labeldest = mapLabel.get(this.graphe.getArrayList().get(dest));
        //dest doit être label_generique

        Label E_en_cours = (Label) labeldest;

        Node node;
        // On remonte avec l'aide du pere !
        // Tant qu'on n'atteint pas le sommet d'origine qui a pour pere -1
        while (E_en_cours.getPere() != -1) {
            node = this.graphe.getArrayList().get(E_en_cours.getPere());
            chemin.addNode(node);
            E_en_cours = mapLabel.get(node);
        }
        // cout et affichage du chemin
        Collections.reverse(chemin.getListNode());
        chemin.tracerChemin(this.graphe.getDessin());
    }

}