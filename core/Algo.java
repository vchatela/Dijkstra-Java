package core;

import base.Readarg;

import java.io.PrintStream;

/**
 * Classe abstraite representant un algorithme (connexite, plus court chemin, etc.)
 */
public abstract class Algo {

    protected PrintStream sortie;
    protected Graphe graphe;

    protected Algo(Graphe gr, PrintStream fichierSortie, Readarg readarg) {
        this.graphe = gr;
        this.sortie = fichierSortie;
    }

    public abstract int run();

}
