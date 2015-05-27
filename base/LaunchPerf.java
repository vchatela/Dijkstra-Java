package base;

import core.Algo;
import core.Graphe;

import java.io.DataInputStream;
import java.util.ArrayList;

/**
 * Created by Valentin on 18/05/2015.
 *
 */
public class LaunchPerf {
    // Declaration de Variables li� � l'execution du programme
    private String nomCarte;                   // Nom de la carte � charger
    private Graphe graphe;                     // La map
    private int origine, dest;      // Num�ro des sommets origine, pieton et dest
    private Algo algo;                       // Algorithme a executer

    public LaunchPerf() {
    }

    public static void main(String[] args) {
        LaunchPerf l = new LaunchPerf();
        l.go();
    }

    public void go() {
        nomCarte = "reunion";
        DataInputStream mapdata = Openfile.open(nomCarte);
        Dessin dessinPanel = new DessinInvisible();
        graphe = new Graphe(nomCarte, mapdata, dessinPanel, false);
        int tabPcc[] = {0, 0, 0, 0};
        int tabStar[] = {0, 0, 0, 0};
        int Perf[] = {0, 0};
        int dureeP, dureeA;
        ArrayList test;
        // on veut lancer 100 algos sur la carte avec des sommets diff�rents al�atoires
        for (int i = 0; i < 100; i++) {
            origine = (int) (Math.random() * graphe.max);
            dest = (int) (Math.random() * graphe.max);

            // algo -> PCC Standard : Dijkstra
            //algo = new Pcc_Dijkstra(graphe, origine, dest, false, 0, false, false, true);
            test = algo.run();
            dureeP = algo.getNb_elements_tas();
            System.out.println(test.get(4));
            try {
                java.lang.Thread.sleep(500);
            } catch (Exception ignored) {
            }

            // algo -> PCC A-Star : Dijkstra guid�
            //algo = new Pcc_Star(graphe, origine, dest, false, 0, false, false, true);
            test = algo.run();
            System.out.println(test.get(4));

            dureeA = algo.getNb_elements_tas();
           /*if (dureeP < 15)
                tabPcc[0]++;
            else if (dureeP < 30)
                tabPcc[1]++;
            else if(dureeP < 50)
                tabPcc[2]++;
            else tabPcc[3]++;

            if (dureeA < 15)
                tabStar[0]++;
            else if (dureeA < 30)
                tabStar[1]++;
            else if(dureeA < 50)
                tabStar[2]++;
            else tabStar[3]++;*/

            if (dureeA < dureeP)
                Perf[1]++;
            else Perf[0]++;
        }
        System.out.println("Comparaison :");
        for (int i = 0; i < 4; i++) {
            System.out.println(tabPcc[i] + " " + tabStar[i]);
        }
        System.out.println("Perf :");
        for (int i = 0; i < 2; i++) {
            System.out.println(Perf[i]);
        }
    }

}
