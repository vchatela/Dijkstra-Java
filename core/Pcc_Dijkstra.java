package core;

import java.awt.*;

/**
 * Created by valentin on 4/29/15.
 */
public class Pcc_Dijkstra extends Pcc_Generique<Label> {

    public Pcc_Dijkstra(Graphe gr, int choixCout, boolean affichageDeroulementAlgo, int origine, int destination, boolean TOUS) {
        super(gr, choixCout, affichageDeroulementAlgo, origine, destination, TOUS);

        this.graphe.getDessin().setColor(Color.magenta);
    }

    public void initialisation() {
        //Associe des Labels correspondant aux noeuds et le stocke dans la map !
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

}