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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class Launch {

    static private final String menu[] = {"Quitter", "PCC Standard", "PCC A-star",
            "Obtenir un numero de sommet ", "Charger un fichier de chemin"
            , "Reinitialiser la carte", "Tester les performances"};
    static private final String cartes[] = {"midip", "insa", "france",
            "fractal", "reunion", "carre-dense", "carre", "fractal-spiral"};
    /**
     * Variable declarations
     */
    private final Readarg readarg;
    private JFrame      frame;
    private JPanel		controlPanel;
    private JLabel		jLabelBienvenue;
    private JLabel		jLabelChoixCarte;
    private JLabel		jLabelChoixMenu;
    private JLabel		jLabelSortieGraphique;
    private JLabel      jLabelImage;
    private JTextField  jTextFieldOrigine;
    private JTextField  jTextFieldDestination;
    private JCheckBox   jCheckBoxSortieGraphique;
    private JComboBox	jComboBoxMenu;
    private JComboBox	jComboBoxCartes;
    private ImageIcon   image;
    private JButton		goButton;       //Button go (selection menu)
    private JButton		loadButton;     //Button charger (lancement de l'appli)
    private String      nomcarte;       //Nom de la carte à charger
    private boolean     display;        //Affichage graphique ou non
    private boolean     menuSelected;   //Choix du menu effectué ou non
    private Thread      t;              //Utilisé pour afficher la carte

    /**
     * Default constructor
     */
    public Launch(String[] args) {
        frame = new JFrame("Dijkstra");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.readarg = new Readarg(args);
        menuSelected = false;

        jLabelBienvenue = new JLabel("Bienvenue \n Version 3.0 \n de Mangel - Chatelard");
        jLabelChoixCarte = new JLabel("Nom du fichier .map a utiliser");
        jLabelChoixMenu = new JLabel("Que voulez-vous faire");
        jLabelSortieGraphique = new JLabel("Voulez-vous une sortie graphique");

        jTextFieldOrigine = new JTextField();
        jTextFieldDestination = new JTextField();

        jCheckBoxSortieGraphique = new JCheckBox();
        jCheckBoxSortieGraphique.setSelected(true);

        jComboBoxCartes = new JComboBox();
        for (String carte : cartes)
            jComboBoxCartes.addItem(carte);
        jComboBoxMenu = new JComboBox();
        for (String choix : menu)
            jComboBoxMenu.addItem(choix);

        jLabelImage = new JLabel();
        image = new ImageIcon("arbre.jpg");
        jLabelImage.setIcon(image);

        loadButton = new JButton("CHARGER");
        loadButton.setPreferredSize(new Dimension(100, 25));
        loadButton.setBackground(new Color(235, 235, 235));
        loadButton.addActionListener(new BoutonListener());

        goButton = new JButton("GO");
        goButton.setPreferredSize(new Dimension(100, 25));
        goButton.setBackground(new Color(235, 235, 235));
        goButton.addActionListener(new BoutonListener());

        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(350, 420));
        controlPanel.setLayout(new FlowLayout());

        controlPanel.add(jLabelBienvenue);
        controlPanel.add(jLabelImage);
        controlPanel.add(jLabelChoixCarte);
        controlPanel.add(jComboBoxCartes);
        controlPanel.add(jLabelSortieGraphique);
        controlPanel.add(jCheckBoxSortieGraphique);
        controlPanel.add(loadButton);

        frame.setContentPane(controlPanel);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

    } // _________  end of constructor

    public static void main(String[] args) {
        Launch launch = new Launch(args);
    }

    public void afficherMenu() {
        frame.setVisible(true);

        //On parametre le menu
        goButton.setEnabled(true);

//        int choix = -1;
//        String selection = (String) JOptionPane.showInputDialog(null, "Que voulez-vous faire ?", "Votre choix", JOptionPane.QUESTION_MESSAGE, null, menu, menu[0]);
//        if (selection != null)
//            for (int i = 0; i < menu.length; i++)
//                if (selection.equals(menu[i])) choix = i;
//
//        return choix;

    }

    public void go() {

        try {
            System.out.println("**");
            System.out.println("** Programme de test des algorithmes de graphe.");
            System.out.println("**");
            System.out.println();

            // On obtient ici le nom de la carte a utiliser.
            //String nomcarte = this.readarg.lireString ("Nom du fichier .map a utiliser ? ") ;
            DataInputStream mapdata = Openfile.open(nomcarte);

            //boolean display = (1 == this.readarg.lireInt ("Voulez-vous une sortie graphique (0 = non, 1 = oui) ? ")) ;

            Dessin dessin = (display) ? new DessinVisible(800, 600) : new DessinInvisible();

            Graphe graphe = new Graphe(nomcarte, mapdata, dessin);

            // Boucle principale : le menu est accessible
            // jusqu'a ce que l'on quitte.
            boolean continuer = true;
            int choix;

            while (continuer) {
                this.afficherMenu();

                while (menuSelected == false) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        System.out.println("Error thread sleep");
                    }
                }
                menuSelected = false;
                choix = jComboBoxMenu.getSelectedIndex();

                // Algorithme a executer
                Algo algo = null;
                Algo algo1 = null;
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
                    case 6:
                        // Programme de test des 2 algos D + D A-Star
                        int origine, dest;
                        origine = Integer.parseInt(JOptionPane.showInputDialog(null, "Numero du sommet d'origine'"));
                        dest = Integer.parseInt(JOptionPane.showInputDialog(null, "Numero du sommet d'origine'"));
                        algo1 = new Pcc_Dijkstra(graphe, this.fichierSortie(), this.readarg, true, origine, dest);
                        algo = new PccStar(graphe, this.fichierSortie(), this.readarg, true, origine, dest);

                        break;
                    default:
                        System.out.println("Choix de menu incorrect : " + choix);
                        JOptionPane.showMessageDialog(null, "Choix de menu incorrect", "Choix menu", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                }

                if (algo != null) {
                    //TODO
                    if (algo1 != null) {
                        // résultat sous la forme : cout - duree - maxTas - nb_element_tas
                        ArrayList perf = new ArrayList();
                        perf = algo1.run();
                        ArrayList perf2 = new ArrayList();
                        perf2 = algo.run();
                        // on va afficher des graphiques de performances

                    } else algo.run();
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
        String nom = JOptionPane.showInputDialog(null, "Nom du fichier de sortie ?");
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

    public class BoutonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            //Click sur le boutton Load
            if (evt.getSource() == loadButton) {
                nomcarte = jComboBoxCartes.getSelectedItem().toString();
                display = jCheckBoxSortieGraphique.isSelected();
                t = new Thread(new PlayAnimation());
                t.start();
                frame.setVisible(false);
                frame.dispose();

                //préparation du menu
                //On supprime tout le reste du précédent Panel
                controlPanel.removeAll();

                controlPanel.add(jLabelChoixMenu);
                controlPanel.add(jComboBoxMenu);
                controlPanel.add(goButton);

                frame.pack();
            } else if (evt.getSource() == goButton) {
                menuSelected = true;
                goButton.setEnabled(false);
            }
        }
    }

    class PlayAnimation implements Runnable {
        public void run() {
            go();
        }
    }

}

//TODO affichage menu sur sortie graphique (avec menu deroulant des cartes, chemins) et affichage temps, longueurs... 
