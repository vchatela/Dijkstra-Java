package core;

import java.util.ArrayList;

/**
 * Classe abstraite representant un algorithme (connexite, plus court chemin, etc.)
 */
public abstract class Algo {

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

    public abstract ArrayList run();

    public abstract ArrayList getLabels();

    public String AffichageTempsHeureMin(double min) {
        int heures, minutes, sec;
        double totalSecondes2 = min*60;
        int totalSecondes = (int) Math.round(totalSecondes2);

        minutes = (totalSecondes / 60) % 60;
        heures = (totalSecondes / (60 * 60));
        sec = (totalSecondes - 60*minutes - 3600*heures);

        System.out.println("Cout : " + min);

        if (min >= 60)
            return (heures + " heure(s), " + minutes + " minute(s) et " + sec + " sec");
        else if (minutes > 0)
            return  (minutes + " minute(s) et " + sec + " sec");
        else return (sec + " sec");
    }

    public String AffichageTempsCalcul(double ms) {
        int sec;
        double milli;

        if (ms >= 1000) {
            sec = (int) ms / 1000;
            milli = ms % 1000;
            return (sec + " seconde(s) " + milli);
        }
        return (ms + "ms");
    }
}
