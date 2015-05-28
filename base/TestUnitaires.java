package base;

import core.*;

import java.io.DataInputStream;
import java.util.ArrayList;

public class TestUnitaires {
    private String nomCarte;                   // Nom de la carte à charger
    private DataInputStream mapdata;
    private Dessin dessinPanel;
    private Graphe graphe;                     // La map

    public TestUnitaires() {
    }

    public static void main(String[] args) {
        TestUnitaires t = new TestUnitaires();
        t.go();
    }

    public void go() {
        boolean error = false;
        // TODO : tableau de boolean pour montrer quels sont les tests qui n'ont pas marché !
        if (!testCarte("midip")) {
            System.out.println("Error : testCarte");
            error = true;
        }
        if (!testDessin()) {
            System.out.println("Error : testDessin");
            error = true;
        }
        if (!testGraphe()) {
            System.out.println("Error : testGraphe");
            error = true;
        }
        if (!testDijkstra()) {
            System.out.println("Error : testDijkstra");
            error = true;
        }
        if (!testStar()) {
            System.out.println("Error : testStar");
            error = true;
        }
        if (!testChemin("chemin_midip")) {
            System.out.println("Error : testChemin");
            error = true;
        }
        if (!testDecoupeCheminDistance()) {
            System.out.println("Error : testDecoupeCheminDistance");
            error = true;
        }
        if (!testDecoupeCheminTemps()) {
            System.out.println("Error : testDecoupeCheminTemps");
            error = true;
        }
        if (!testDecoupeCheminPieton()) {
            System.out.println("Error : testDecoupeCheminPieton");
            error = true;
        }
        if (!testConnexite()) {
            System.out.println("Error : testConnexite");
            error = true;
        }
        if (!testCovoit()) {
            System.out.println("Error : testCovoit");
            error = true;
        }
        if (!error)
            System.out.println("Campagne de test : Validée");

    }


    public boolean testConnexite() {
        int origine;
        int dest;
        if (nomCarte.equals("midip")) {
            origine = 127072;
            dest = 588;
            Algo al = new Connexite(graphe, origine, dest, false);
            if (al.run().equals("non connexes"))
                return false;
            al = new Connexite(graphe, dest, origine, false);
            return !al.run().equals("connexes");
        } else {
            // pour le moment on ne test que sur midip
            return true;
        }


    }

    public boolean testCarte(String s) {
        nomCarte = s;
        mapdata = Openfile.open(nomCarte);
        return mapdata != null;
    }

    public boolean testDessin() {
        Dessin dessinInvisible = new DessinInvisible();
        dessinPanel = new DessinVisible(800, 600);
        return !(dessinPanel == null || dessinInvisible == null);
    }

    public boolean testGraphe() {
        graphe = new Graphe(nomCarte, mapdata, dessinPanel, false);
        return graphe != null;
    }

    public boolean testDijkstra() {
        Algo test;
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);
        // test noeud inconnu
        test = new Pcc_Dijkstra(graphe, -1, -1, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() != null)
            return false;

        // test en distance
        test = new Pcc_Dijkstra(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;

        // test noeud inconnu
        test = new Pcc_Dijkstra(graphe, -1, -1, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() != null)
            return false;

        // test en temps
        test = new Pcc_Dijkstra(graphe, origine, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;


        origine = (int) (Math.random() * graphe.max);
        dest = (int) (Math.random() * graphe.max);

        // test cout limite en distance
        test = new Pcc_Dijkstra(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;

        //test pieton
        test = new Pcc_Dijkstra(graphe, origine, dest, 0, false, true, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;

        // test TOUS
        test = new Pcc_Dijkstra(graphe, origine, dest, 0, true, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;

        // test midip environ = a 200 minutes
        test = new Pcc_Dijkstra(graphe, 119963, 96676, 1, true, false, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double cout = ((Pcc_Generique) test).getCout();
        // on vérifie les résultats
        if (!(cout > 200 && cout < 201))
            return false;
        ((Pcc_Generique) test).chemin();

        return true;
    }


    public boolean testStar() {
        Algo test;
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);
        // test noeud inconnu
        test = new Pcc_Star(graphe, -1, -1, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() != null)
            return false;

        // test en distance
        test = new Pcc_Star(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;

        // test noeud inconnu
        test = new Pcc_Star(graphe, -1, -1, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() != null)
            return false;

        // test en temps
        test = new Pcc_Star(graphe, origine, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;


        origine = (int) (Math.random() * graphe.max);
        dest = (int) (Math.random() * graphe.max);

        // test cout limite en distance
        test = new Pcc_Star(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;

        //test pieton
        test = new Pcc_Star(graphe, origine, dest, 0, false, true, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;

        // test TOUS
        test = new Pcc_Star(graphe, origine, dest, 0, true, false, Double.POSITIVE_INFINITY, false, false);
        if (test.run() == null || ((Pcc_Generique) test).getCout() == Double.POSITIVE_INFINITY)
            return false;

        // test midip environ = a 200 minutes
        test = new Pcc_Star(graphe, 119963, 96676, 1, true, false, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double cout = ((Pcc_Generique) test).getCout();
        // on vérifie les résultats
        if (!(cout > 200 && cout < 201))
            return false;
        ((Pcc_Generique) test).chemin();

        return true;
    }

    public boolean testChemin(String s) {
        // On récupère le nom de la carte

        if (graphe.verifierChemin(Openfile.open(s), s) == -1)
            return false;

        graphe.verifierChemin(Openfile.open(s), s);
        graphe.getChemin().tracerChemin(graphe.getDessin());
        graphe.getChemin().Calculer_cout_chemin_distance();
        graphe.getChemin().Calculer_cout_chemin_temps();

        return true;
    }

    // testCovoit
    public boolean testCovoit() {
        // On vérifie que les 2 dijkstra de origine vers noeud rejoins et de noeud rejoins vers dest sont bons  - permet de vérifier le graphe inverse !
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
        perfVoitureTous = algo.run();
        ArrayList<Label> covoitVoiture = algo.getLabels();

        // Permet de copier l'arraylist précédent sans référencer le même
        ArrayList<Label> covoitSave = new ArrayList<>();
        for (Label aCovoitVoiture : covoitVoiture) {
            covoitSave.add(new Label(aCovoitVoiture));
        }

        // PCC du PIETON vers TOUS
        algo1 = new Pcc_Dijkstra(graphe, pieton, dest, 1, true, true, Double.POSITIVE_INFINITY, false, false);
        perfPietonTous = algo1.run();
        ArrayList<Label> covoitPieton = algo1.getLabels();

        // PCC de la DESTINATION vers TOUS
        mapdata = Openfile.open(nomCarte);
        Graphe grapheInverse = new Graphe(nomCarte, mapdata, new DessinInvisible(), true);

        algo2 = new Pcc_Dijkstra(grapheInverse, dest, pieton, 1, true, false, Double.POSITIVE_INFINITY, false, false);
        perfDestTous = algo2.run();
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

                //mise à jour du cout minimum
                if (covoitSomme.get(i).getCout() < min) {
                    min = covoitSomme.get(i).getCout();
                    noeud_rejoint = i;
                }
            }

            if (noeud_rejoint != -1) {
                // on ajoute le cout de noeud rejoins vers dest
                // on vérifie ici que ce temps est le plus court jusqu'à la dest !
                algo = new Pcc_Dijkstra(graphe, noeud_rejoint, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
                algo.run();
                if (!(Math.abs(((Pcc_Dijkstra) algo).getCout() - ((Pcc_Dijkstra) algo2).chemin(dest, noeud_rejoint, graphe)) < 0.01 * (((Pcc_Dijkstra) algo).getCout()))) {
                    System.out.println("Cout dijkstra : " + ((Pcc_Dijkstra) algo).getCout() + " - cout algo covoit : " + ((Pcc_Dijkstra) algo2).chemin(dest, noeud_rejoint, graphe) + " et res = " + Math.abs(((Pcc_Dijkstra) algo).getCout() - ((Pcc_Dijkstra) algo2).chemin(dest, noeud_rejoint, graphe)));
                    return false;
                }
            } else {
                System.out.print("Pas de rencontre car ");
                Algo algo4 = new Connexite(graphe, origine, pieton, false);
                ArrayList res = algo4.run();
                if (res.get(0).equals("non connexes")) {
                    System.out.println("le pieton et la voiture ne sont pas connexes.");
                }
            }
        }
        return true;
    }

    // testDécoupeChemin
    public boolean testDecoupeCheminDistance() {
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);

        Algo test = new Pcc_Dijkstra(graphe, origine, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double res = ((Pcc_Generique) test).getCout();

        int noeud_rejoins = ((Pcc_Generique) test).cheminTest();

        test = new Pcc_Dijkstra(graphe, origine, noeud_rejoins, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double res1 = ((Pcc_Generique) test).getCout();

        test = new Pcc_Dijkstra(graphe, noeud_rejoins, dest, 1, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double res2 = ((Pcc_Generique) test).getCout();
        if (!(Math.abs(res - (res1 + res2)) < 0.01 * res)) {
            System.out.println("Res : " + res + " - Res 1 : " + res1 + " + " + res2 + " = " + (res1 + res2));
            return false;
        }
        return true;
    }

    public boolean testDecoupeCheminTemps() {
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);

        Algo test = new Pcc_Dijkstra(graphe, origine, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double res = ((Pcc_Generique) test).getCout();

        int noeud_rejoins = ((Pcc_Generique) test).cheminTest();

        test = new Pcc_Dijkstra(graphe, origine, noeud_rejoins, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double res1 = ((Pcc_Generique) test).getCout();

        test = new Pcc_Dijkstra(graphe, noeud_rejoins, dest, 0, false, false, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double res2 = ((Pcc_Generique) test).getCout();
        if (Math.abs(res - res1 + res2) < 0.01 * res) {
            System.out.println("Res : " + res + " - Res 1 : " + res1 + " + " + res2 + " = " + (res1 + res2));
            return false;
        }
        return true;
    }

    public boolean testDecoupeCheminPieton() {
        int origine = (int) (Math.random() * graphe.max);
        int dest = (int) (Math.random() * graphe.max);

        Algo test = new Pcc_Dijkstra(graphe, origine, dest, 1, false, true, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double res = ((Pcc_Generique) test).getCout();

        int noeud_rejoins = ((Pcc_Generique) test).cheminTest();

        test = new Pcc_Dijkstra(graphe, origine, noeud_rejoins, 1, false, true, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double res1 = ((Pcc_Generique) test).getCout();

        test = new Pcc_Dijkstra(graphe, noeud_rejoins, dest, 1, false, true, Double.POSITIVE_INFINITY, false, false);
        test.run();
        double res2 = ((Pcc_Generique) test).getCout();
        if (Math.abs(res - res1 + res2) < 0.01 * res) {
            System.out.println("Res : " + res + " - Res 1 : " + res1 + " + " + res2);
            return false;
        }
        return true;
    }

}
