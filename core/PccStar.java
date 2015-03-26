package core ;

import java.io.* ;
import base.Readarg ;

public class PccStar extends Pcc {

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
    }

	//Recherche des noeuds les plus proches -> plus efficace
    public void run() {

		System.out.println("Run PCC-Star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

		// A vous d'implementer la recherche de plus court chemin A*
    }

}
