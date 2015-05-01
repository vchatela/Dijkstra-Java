package core;

import base.Readarg;

import java.awt.*;
import java.io.PrintStream;

/**
 * Created by valentin on 4/29/15.
 */
public class Pcc_Dijkstra extends Pcc_Generique<Label_Dijkstra> {

    public Pcc_Dijkstra(Graphe gr, PrintStream sortie, Readarg readarg, boolean test, int origine, int dest) {
        super(gr, sortie, readarg, test, origine, dest);

        String chaine = (choix == 0) ? "distance" : "temps";
        sortieAlgo = "Carte: " + graphe.getNomCarte() + "\n" + "Dijkstra de " + origine + " -> " + destination + " en " + chaine;
        //L'affichage du deroulement de l'algo est faite en magenta
        this.graphe.getDessin().setColor(Color.magenta);
    }
    public Pcc_Dijkstra(Graphe gr, PrintStream sortie, Readarg readarg) {
        super(gr, sortie, readarg);

        String chaine = (choix == 0) ? "distance" : "temps";
        sortieAlgo = "Carte: " + graphe.getNomCarte() + "\n" + "Dijkstra de " + origine + " -> " + destination + " en " + chaine;
        //L'affichage du deroulement de l'algo est faite en magenta
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
            lab.add(l);

            //Noeud destinataire
            if ((node.getNum() == destination) && (graphe.getZone() == zoneDestination)) {
                dest = l;
            }
        }
    }

}
