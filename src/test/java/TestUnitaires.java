package src.test.java;

import src.main.java.base.Dessin;
import src.main.java.base.DessinInvisible;
import src.main.java.base.DessinVisible;
import src.main.java.base.Openfile;
import src.main.java.core.*;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.DataInputStream;
import java.util.ArrayList;

/**
 * Created by Famille on 22/09/2015.
 *
 */
public class TestUnitaires extends TestCase {

    private final boolean isTest = true;
    private String nomCarte;                   // Nom de la carte a charger
    private DataInputStream mapdata;
    private Dessin dessinPanel;
    private Graphe graphe;                     // La map


    protected void setUp() {
        nomCarte = "midip";
        mapdata = Openfile.open(nomCarte);
        dessinPanel = new DessinVisible(800, 600);
        graphe = new Graphe(nomCarte, mapdata, dessinPanel, false);
    }

    @Test
    public final void testConnexite(){
        int origine;
        int dest;
        if (nomCarte.equals("midip")) {
            origine = 127072;
            dest = 588;
            Algo al = new Connexite(graphe, origine, dest, false);
            if (al.run(isTest).equals("non connexes"))
                assert false;
            al = new Connexite(graphe, dest, origine, false);
            assert !al.run(isTest).equals("connexes");
        } else {
            // pour le moment on ne test que sur midip
            assert true;
        }
    }


    @Test     
    public final void testCarte() {
        nomCarte = "midip";
        mapdata = Openfile.open(nomCarte);
        assert mapdata != null;
    }

    @Test     
    public final void testDessin() {
        Dessin dessinInvisible = new DessinInvisible();
        dessinPanel = new DessinVisible(800, 600);
        assert !(dessinPanel == null || dessinInvisible == null);
    }

    @Test     
    public final void testGraphe() {
        graphe = null;
        mapdata = Openfile.open(nomCarte);
        graphe = new Graphe(nomCarte, mapdata, dessinPanel, false);
        assert graphe != null;
    }

    @Test     
    public final void testDijkstra() {
        Algo test;
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);
        // test noeud inconnu
        test = new Pcc_Dijkstra(graphe, -1, -1, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) != null)
            assert false;

        // test en distance
        test = new Pcc_Dijkstra(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;

        // test noeud inconnu
        test = new Pcc_Dijkstra(graphe, -1, -1, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) != null)
            assert false;

        // test en temps
        test = new Pcc_Dijkstra(graphe, origine, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;


        origine = (int) (Math.random() * graphe.max);
        dest = (int) (Math.random() * graphe.max);

        // test cout limite en distance
        test = new Pcc_Dijkstra(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;

        //test pieton
        test = new Pcc_Dijkstra(graphe, origine, dest, 0, false, true, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;

        // test TOUS
        test = new Pcc_Dijkstra(graphe, origine, dest, 0, true, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;

        // test midip environ = a 200 minutes
        test = new Pcc_Dijkstra(graphe, 119963, 96676, 1, true, false, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double cout = ((Pcc_Generique) test).getCout();
        // on verifie les resultats
        if (!(cout > 200 && cout < 201))
            assert false;
        ((Pcc_Generique) test).chemin();

        assert true;
    }


    @Test     
    public final void testStar() {
        Algo test;
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);
        // test noeud inconnu
        test = new Pcc_Star(graphe, -1, -1, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) != null)
            assert false;

        // test en distance
        test = new Pcc_Star(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;

        // test noeud inconnu
        test = new Pcc_Star(graphe, -1, -1, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) != null)
            assert false;

        // test en temps
        test = new Pcc_Star(graphe, origine, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;


        origine = (int) (Math.random() * graphe.max);
        dest = (int) (Math.random() * graphe.max);

        // test cout limite en distance
        test = new Pcc_Star(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;

        //test pieton
        test = new Pcc_Star(graphe, origine, dest, 0, false, true, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;

        // test TOUS
        test = new Pcc_Star(graphe, origine, dest, 0, true, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run(isTest) == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            assert false;

        // test midip environ = a 200 minutes
        test = new Pcc_Star(graphe, 119963, 96676, 1, true, false, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double cout = ((Pcc_Generique) test).getCout();
        // on verifie les resultats
        if (!(cout > 200 && cout < 201))
            assert false;
        ((Pcc_Generique) test).chemin();

        assert true;
    }

    @Test     
    public final void testChemin() {
        // On recupere le nom de la carte
        String s = "chemin_midip";
        if (graphe.verifierChemin(Openfile.open(s), s) == -1)
            assert false;

        graphe.verifierChemin(Openfile.open(s), s);
        graphe.getChemin().tracerChemin(graphe.getDessin());
        graphe.getChemin().Calculer_cout_chemin_distance();
        graphe.getChemin().Calculer_cout_chemin_temps();

        assert true;
    }

    // testCovoit
    @Test     
    public final void testCovoit() {
        // On verifie que les 2 dijkstra de origine vers noeud rejoins et de noeud rejoins vers dest sont bons  - permet de verifier le graphe inverse !
        // PCC de la VOITURE vers TOUS
        ArrayList<Label> covoitSomme = new ArrayList<>();
        ArrayList<String> perfVoitureTous, perfPietonTous, perfDestTous;
        int noeud_rejoint = -1;
        double min = Double.POSITIVE_INFINITY;


        int origine = (int) (Math.random() * graphe.max);
        int pieton = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);

        Algo algo1, algo2, algo;
        algo = new Pcc_Dijkstra(graphe, origine, dest, 1, true, false, Double.POSITIVE_INFINITY, false, false);
        perfVoitureTous = algo.run(isTest);
        ArrayList<Label> covoitVoiture = algo.getLabels();

        // Permet de copier l'arraylist precedent sans referencer le meme
        ArrayList<Label> covoitSave = new ArrayList<>();
        for (Label aCovoitVoiture : covoitVoiture) {
            covoitSave.add(new Label(aCovoitVoiture));
        }

        // PCC du PIETON vers TOUS
        algo1 = new Pcc_Dijkstra(graphe, pieton, dest, 1, true, true, Double.POSITIVE_INFINITY, false, false);
        perfPietonTous = algo1.run(isTest);
        ArrayList<Label> covoitPieton = algo1.getLabels();

        // PCC de la DESTINATION vers TOUS
        mapdata = Openfile.open(nomCarte);
        Graphe grapheInverse = new Graphe(nomCarte, mapdata, new DessinInvisible(), true);

        algo2 = new Pcc_Dijkstra(grapheInverse, dest, pieton, 1, true, false, Double.POSITIVE_INFINITY, false, false);
        perfDestTous = algo2.run(isTest);
        ArrayList<Label> covoitDestination = algo2.getLabels();

        if (perfVoitureTous != null && perfPietonTous != null && perfDestTous != null) {

            for (int i = 0; i < covoitVoiture.size(); i++) {
                // On prend le max des deux pour avoir le temps minimum qu'il faut pour se rejoindre
                if (covoitVoiture.get(i).getCout() < covoitPieton.get(i).getCout()) {
                    covoitSomme.add(i, covoitPieton.get(i));
                } else {
                    covoitSomme.add(i, covoitVoiture.get(i));
                }

                // On ajoute le temps qu'il faut depuis ce point de ralliement vers la dest
                // c'est cette valeur qu'on veut minimiser temps complet du trajet
                covoitSomme.get(i).setCout(covoitSomme.get(i).getCout() + covoitDestination.get(i).getCout());

                // si ce temps est > au temps qu'aurait mis les deux alors ils y vont directs
                if (covoitSomme.get(i).getCout() > Math.max(covoitVoiture.get(dest).getCout(), covoitPieton.get(dest).getCout())) {
                    // Ici ca veut dire qu'ils y vont chacun pour soit !
                    if (covoitVoiture.get(dest).getCout() > covoitPieton.get(dest).getCout()) {
                        covoitSomme.set(i, covoitVoiture.get(dest));
                    } else {
                        covoitSomme.set(i, covoitPieton.get(dest));
                    }
                }

                //mise a jour du cout minimum
                if (covoitSomme.get(i).getCout() < min) {
                    min = covoitSomme.get(i).getCout();
                    noeud_rejoint = i;
                }
            }

            if (noeud_rejoint != -1) {
                // on ajoute le cout de noeud rejoins vers dest
                // on verifie ici que ce temps est le plus court jusqu'a la dest !
                algo = new Pcc_Dijkstra(graphe, noeud_rejoint, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
                algo.run(isTest);
                if (!(Math.abs(((Pcc_Dijkstra) algo).getCout() - ((Pcc_Dijkstra) algo2).chemin(dest, noeud_rejoint, graphe)) < 0.01 * (((Pcc_Dijkstra) algo).getCout()))) {
                    System.out.println("Cout dijkstra : " + ((Pcc_Dijkstra) algo).getCout() + " - cout algo covoit : " + ((Pcc_Dijkstra) algo2).chemin(dest, noeud_rejoint, graphe) + " et res = " + Math.abs(((Pcc_Dijkstra) algo).getCout() - ((Pcc_Dijkstra) algo2).chemin(dest, noeud_rejoint, graphe)));
                    assert false;
                }
            } else {
                System.out.print("Pas de rencontre car ");
                Algo algo4 = new Connexite(graphe, origine, pieton, false);
                ArrayList res = algo4.run(isTest);
                if (res.get(0).equals("non connexes")) {
                    System.out.println("le pieton et la voiture ne sont pas connexes.");
                }
            }
        }
        assert true;
    }

    // testDecoupeChemin
    @Test     
    public final void testDecoupeCheminDistance() {
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);

        Algo test = new Pcc_Dijkstra(graphe, origine, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double res = ((Pcc_Generique) test).getCout();

        int noeud_rejoins = ((Pcc_Generique) test).cheminTest();

        test = new Pcc_Dijkstra(graphe, origine, noeud_rejoins, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double res1 = ((Pcc_Generique) test).getCout();

        test = new Pcc_Dijkstra(graphe, noeud_rejoins, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double res2 = ((Pcc_Generique) test).getCout();
        if (!(Math.abs(res - (res1 + res2)) < 0.01 * res)) {
            System.out.println("Res : " + res + " - Res 1 : " + res1 + " + " + res2 + " = " + (res1 + res2));
            assert false;
        }
        assert true;
    }

    @Test     
    public final void testDecoupeCheminTemps() {
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);

        Algo test = new Pcc_Dijkstra(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double res = ((Pcc_Generique) test).getCout();

        int noeud_rejoins = ((Pcc_Generique) test).cheminTest();

        test = new Pcc_Dijkstra(graphe, origine, noeud_rejoins, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double res1 = ((Pcc_Generique) test).getCout();

        test = new Pcc_Dijkstra(graphe, noeud_rejoins, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double res2 = ((Pcc_Generique) test).getCout();
        if (Math.abs(res - res1 + res2) < 0.01 * res) {
            System.out.println("Res : " + res + " - Res 1 : " + res1 + " + " + res2 + " = " + (res1 + res2));
            assert false;
        }
        assert true;
    }

    @Test     
    public final void testDecoupeCheminPieton() {
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);

        Algo test = new Pcc_Dijkstra(graphe, origine, dest, 1, false, true, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double res = ((Pcc_Generique) test).getCout();

        int noeud_rejoins = ((Pcc_Generique) test).cheminTest();

        test = new Pcc_Dijkstra(graphe, origine, noeud_rejoins, 1, false, true, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double res1 = ((Pcc_Generique) test).getCout();

        test = new Pcc_Dijkstra(graphe, noeud_rejoins, dest, 1, false, true, Double.POSITIVE_INFINITY, false, false);
        test.run(isTest);
        double res2 = ((Pcc_Generique) test).getCout();
        if (Math.abs(res - res1 + res2) < 0.01 * res) {
            System.out.println("Res : " + res + " - Res 1 : " + res1 + " + " + res2);
            assert false;
        }
        assert true;
    }

}
