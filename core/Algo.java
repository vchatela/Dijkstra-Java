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

    public abstract int getNb_elements_tas();

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }

    public abstract ArrayList run();

    public abstract ArrayList getLabels();

    public String AffichageTempsHeureMin(double min) {
        int heure = 0;
        double minute;

        if (min >= 60) {
            heure = (int) min / 60;
            minute = min % 60;
            return (heure + " heure(s) et " + minute + " minute(s) ");
        }
        return (min + "min ");
    }

    public abstract HashMap<Node, Label_Generique> getMapLabel();

    public abstract void setMapLabel(HashMap<Node, E> mapLabel);
    public String AffichageTempsCalcul(double ms) {
        int sec = 0;
        double milli;

        if (ms >= 1000) {
            sec = (int) ms / 1000;
            milli = ms % 1000;
            return (sec + " seconde(s) " + milli);
        }
        return (ms + "ms");
    }
}
