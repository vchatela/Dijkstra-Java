package core;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Classe abstraite representant un algorithme (connexite, plus court chemin, etc.)
 */
public abstract class Algo<E extends Comparable<E>> {

    protected Graphe graphe;

    // Numero des sommets origine et destination
    protected int zoneOrigine;
    protected int origine;
    protected int zoneDestination;
    protected int destination;
    protected Label_Generique dest;        // Label destinataire
    protected boolean affichageDeroulementAlgo;      // Affichage du deroulement de l'algo
    protected double duree;                  // Duree d'execution
    protected boolean connexes;            // Les noeuds origine et destination sont-ils connexes

    protected Algo(Graphe gr) {
        this.graphe = gr;
    }

    public abstract ArrayList run();

    public abstract ArrayList getLabels();
}
