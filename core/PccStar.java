package core;

import base.Readarg;

import java.io.PrintStream;

public class PccStar extends Pcc_Generique<Label_Star> {

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
        super(gr, sortie, readarg);
    }

    //Recherche des noeuds les plus proches -> plus efficace
    public void run() {

        System.out.println("Run PCC-Star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination);

        // A vous d'implementer la recherche de plus court chemin A*
    }

}
