package core;

import java.awt.*;

/**
 * Created by valentin on 4/29/15.
 */
public class Pcc_Dijkstra extends Pcc_Generique<Label_Dijkstra> {


    public Pcc_Dijkstra(Graphe gr, int choixCout, int affichageDeroulementAlgo, int origine, int dest, boolean TOUS, boolean pieton) {
        super(gr, choixCout, affichageDeroulementAlgo, origine, dest, TOUS, pieton);

        this.pieton = pieton;
        this.graphe.getDessin().setColor(Color.magenta);
    }

    public void initialisation() {
        //Associe des Label_Dijkstras correspondant aux noeuds et le stocke dans la map !
        for (Node node : this.graphe.getArrayList()) {
            Label_Dijkstra l = new Label_Dijkstra(node);
            mapLabel.put(node, l);
            if ((node.getNum() == origine) && (graphe.getZone() == zoneOrigine)) {
                l.setCout(0);
                //Initialisation du tas avec le Label_Dijkstra sommet origine
                tas.insert(l);
            }
            labels.add(l);

            //Noeud destinataire
            if ((node.getNum() == destination) && (graphe.getZone() == zoneDestination)) {
                dest = l;
            }
        }
    }

}