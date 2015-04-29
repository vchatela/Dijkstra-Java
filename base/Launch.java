package base;

/*
 * Ce programme propose de lancer divers algorithmes sur les graphes
 * a partir d'un menu texte, ou a partir de la ligne de commande (ou des deux).
 *
 * A chaque question posee par le programme (par exemple, le nom de la carte), 
 * la reponse est d'abord cherchee sur la ligne de commande.
 *
 * Pour executer en ligne de commande, ecrire les donnees dans l'ordre. Par exemple
 *   "java base.Launch insa 1 1 /tmp/sortie 0"
 * ce qui signifie : charge la carte "insa", calcule les composantes connexes avec une sortie graphique,
 * ecrit le resultat dans le fichier '/tmp/sortie', puis quitte le programme.
 */

import core.Algo;
import core.Graphe;
import core.PccStar;
import core.Pcc_Dijkstra;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.PrintStream;

public class Launch {

    private final Readarg readarg;

    public Launch(String[] args) {
        this.readarg = new Readarg(args);
    }

    public static void main(String[] args) {
        Launch launch = new Launch(args);
        launch.go();
    }

    public int afficherMenu() {
        int choix = -1;
        String menu[] = {"Quitter", "Plus court chemin Standard", "Plus court chemin A-star",
                "Cliquer sur la carte pour obtenir un numero de sommet ", "Charger un fichier de chemin"
                , "Reinitialiser la carte"};
        String selection = (String) JOptionPane.showInputDialog(null, "Que voulez-vous faire ?", "Votre choix", JOptionPane.QUESTION_MESSAGE, null, menu, menu[0]);
        if (selection != null)
            for (int i = 0; i < menu.length; i++)
                if (selection.equals(menu[i])) choix = i;

        return choix;
    }

    public void go() {

        try {
            System.out.println("**");
            System.out.println("** Programme de test des algorithmes de graphe.");
            System.out.println("**");
            System.out.println();
            JOptionPane.showMessageDialog(null, "Bienvenue" +
                    "\nVersion 1.0\nde Mangel - Chatelard");

            // On obtient ici le nom de la carte a utiliser.
            String cartes[] = {"insa", "insa.0", "insa.1", "insa.2", "midip", "midip.0", "midip.1",
                    "france", "pfrance.0", "pfrance.1", "pfrance.2", "pfrance.3", "pfrance.4", "pfrance.5", "fractal",
                    "reunion", "carre-dense", "carre", "fractal-spiral"};
            String nomcarte = (String) JOptionPane.showInputDialog(null, "Nom du fichier .map a utiliser?", "Choix de la carte",
                    JOptionPane.QUESTION_MESSAGE, null, cartes, cartes[0]);
            if (nomcarte == null) {
                System.exit(1);
            }
            DataInputStream mapdata = Openfile.open(nomcarte);
            boolean display = (0 == JOptionPane.showConfirmDialog(null, "Voulez-vous une sortie graphique "
                    , "Type Affichage", JOptionPane.YES_NO_OPTION));

            Dessin dessin = (display) ? new DessinVisible(800, 600) : new DessinInvisible();

            Graphe graphe = new Graphe(nomcarte, mapdata, dessin);

            // Boucle principale : le menu est accessible
            // jusqu'a ce que l'on quitte.
            boolean continuer = true;
            int choix;

            while (continuer) {
                choix = this.afficherMenu();
                //choix = this.readarg.lireInt("Votre choix ? ");

                // Algorithme a executer
                Algo algo = null;

                // Le choix correspond au numero du menu.
                switch (choix) {
                    case 0:
                        //quitter
                        continuer = false;
                        break;

                    /*case 1:
                        algo = new Connexite(graphe, this.fichierSortie(), this.readarg);
                        break;*/

                    case 1:
                        algo = new Pcc_Dijkstra(graphe, this.fichierSortie(), this.readarg);
                        break;

                    case 2:
                        algo = new PccStar(graphe, this.fichierSortie(), this.readarg);
                        break;

                    case 3:
                        graphe.situerClick();
                        break;

                    case 4:
                        //String nom_chemin = this.readarg.lireString ("Nom du fichier .path contenant le chemin ? ") ;
                        String chemins[] = {"chemin_insa", "chemin_insa1", "chemin_midip", "chemin_fractal", "chemin_reunion", "chemin_carre-dense", "chemin_spiral",
                                "chemin_spiral2"};
                        String nom_chemin = (String) JOptionPane.showInputDialog(null, "Nom du chemin .path a utiliser?", "Choix de la carte",
                                JOptionPane.QUESTION_MESSAGE, null, chemins, chemins[0]);
                        if (nom_chemin == null) {
                            System.exit(1);
                        }

                        graphe.verifierChemin(Openfile.open(nom_chemin), nom_chemin);
                        graphe.getChemin().tracerChemin(graphe.getDessin());
                        graphe.getChemin().cout_chemin_distance();
                        graphe.getChemin().cout_chemin_temps();
                        break;
                    case 5:
/*
            for (Node noeud : graphe.getArrayList()){
				Descripteur[] descripteurs = new Descripteur[graphe.nb_descripteurs] ;
				for (int num_descr = 0 ; num_descr < graphe.nb_descripteurs ; num_descr++) {
					// Lecture du descripteur numero num_descr
					descripteurs[num_descr]= new Descripteur(mapdata);
					Couleur.set(dessin, descripteurs[num_descr].getType());
					// On affiche quelques descripteurs parmi tous. - DEBUG
					//if (0 == num_descr % (1 + nb_descripteurs / 400))
					//	System.out.println("Descripteur " + num_descr + " = " + this.listNode.get(num_descr)) ;
				}
			}*/
                        JOptionPane.showMessageDialog(null, "Fonctionnalite a venir");
                        break;
                    default:
                        System.out.println("Choix de menu incorrect : " + choix);
                        JOptionPane.showMessageDialog(null, "Choix de menu incorrect", "Choix menu", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                }

                if (algo != null) {
                    algo.run();
                }
            }

            System.out.println("Programme termine.");
            System.exit(0);


        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    // Ouvre un fichier de sortie pour ecrire les reponses
    public PrintStream fichierSortie() {
        PrintStream result = System.out;

        //String nom = this.readarg.lireString("Nom du fichier de sortie ? ");
        String nom = (String) JOptionPane.showInputDialog(null, "Nom du fichier de sortie ?");
        if ("".equals(nom)) {
            //nom = "/dev/null";
            nom = "sortie";
        }

        try {
            result = new PrintStream(nom);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur ? l'ouverture du fichier" + nom);
            //System.err.println("Erreur a l'ouverture du fichier " + nom);
            System.exit(1);
        }

        return result;
    }

}

//TODO affichage menu sur sortie graphique (avec menu deroulant des cartes, chemins) et affichage temps, longueurs... 
