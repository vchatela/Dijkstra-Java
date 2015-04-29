package core;

import base.Readarg;

import java.awt.*;
import java.io.PrintStream;

public class PccStar extends Pcc_Generique<Label_Star> {

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
        super(gr, sortie, readarg);

        String chaine = (choix == 0) ? "distance" : "temps";
        sortieAlgo = "Carte: " + graphe.getNomCarte() + "\n" + "Dijkstra A STAR de " + origine + " -> " + destination + " en " + chaine;
        this.graphe.getDessin().setColor(Color.CYAN);// TODO : pourquoi ????
    }

    public void initialisation() {
        //Associe des Label_Star correspondant aux noeuds et le stocke dans la map !
        for (Node node : this.graphe.getArrayList()) {
            Label_Star l = new Label_Star(node, this.graphe.getArrayList().get(destination));
            mapLabel.put(node, l);
            if ((node.getNum() == origine) && (graphe.getZone() == zoneOrigine)) {
                l.setCout(0);
                //Initialisation du tas avec le Label_Dijkstra sommet origine
                tas.insert(l);
            }
            lab.add(l);

            // sii on demande un calcul en temps on prendra une vitesse max car :
            // ce doit etre obligatoirement une borne INFERIEURE
            if (choix == 1)
                l.setCout_oiseau(60.0f * l.getCout_oiseau() / (130.0f * 1000.0f));

            //Noeud destinataire
            if ((node.getNum() == destination) && (graphe.getZone() == zoneDestination)) {
                dest = l;
            }
        }
    }

}
