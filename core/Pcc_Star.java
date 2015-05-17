package core;

import java.awt.*;

public class Pcc_Star extends Pcc_Generique<Label_Star> {

    public Pcc_Star(Graphe gr, int origine, int dest, boolean affichageDeroulementAlgo, int choixCout, boolean TOUS, boolean pieton) {
        super(gr, origine, dest, affichageDeroulementAlgo, choixCout, TOUS, pieton);

        this.graphe.getDessin().setColor(Color.red);
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
            labels.add(l);

            // si on demande un calcul en temps on prendra une vitesse max car :
            // ce doit etre obligatoirement une borne INFERIEURE
            if (choixCout == 1)
                l.setCout_oiseau(60.0f * l.getCout_oiseau() / (130.0f * 1000.0f));

            //Noeud destinataire
            if ((node.getNum() == destination) && (graphe.getZone() == zoneDestination)) {
                dest = l;
            }
        }
    }

}
