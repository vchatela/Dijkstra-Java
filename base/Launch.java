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

public class Launch extends JFrame{

    /**
     * Variable declarations
     */
    private final Readarg readarg;

    private JButton		loadButton;
    private JPanel		controlPanel;
    private JLabel		jLabelBienvenue;
    private JLabel		jLabelChoixCarte;
    private JCheckBox   jCheckBoxSortieGraphique;
    private JLabel		jLabelSortieGraphique;
    private JLabel      jLabelImage;
    private JComboBox	jComboBoxCartes;
    private ImageIcon   image;
    private Container 	cp;

    static private final String menu[] = {"Quitter", "Plus court chemin Standard", "Plus court chemin A-star",
            "Cliquer sur la carte pour obtenir un numero de sommet ", "Charger un fichier de chemin"
            , "Reinitialiser la carte", "Tester les performances"};


    /**
     * Default constructor
     */
    public Launch(String[] args) {
        this.setTitle("Dijkstra");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.readarg = new Readarg(args);
        this.loadButton = new JButton("CHARGER");
        this.jLabelBienvenue = new JLabel("Bienvenue\nVersion 3.0\nde Mangel - Chatelard");
        this.jLabelChoixCarte = new JLabel("Nom du fichier .map a utiliser");
        this.jLabelSortieGraphique = new JLabel("Voulez-vous une sortie graphique");
        this.jCheckBoxSortieGraphique = new JCheckBox();
        this.jComboBoxCartes = new JComboBox();
        this.jComboBoxCartes.addItem("insa");
        this.jComboBoxCartes.addItem("midip");
        this.jComboBoxCartes.addItem("france");
        this.jComboBoxCartes.addItem("fractal");
        this.jComboBoxCartes.addItem("reunion");
        this.jComboBoxCartes.addItem("carre-dense");
        this.jComboBoxCartes.addItem("carre");
        this.jComboBoxCartes.addItem("fractal-spiral");

        this.jLabelImage = new JLabel();
        this.image = new ImageIcon("arbre.jpg");
        this.jLabelImage.setIcon(image);

        this.jCheckBoxSortieGraphique.setSelected(true);

        // Make the panel of buttons
        this.controlPanel = new JPanel();
        this.controlPanel.setPreferredSize(new Dimension(350, 420));
        this.controlPanel.setLayout(new FlowLayout());
        //this.controlPanel.setBackground(new Color(225, 225, 123));

        // Set up loadButton
        this.loadButton.setPreferredSize(new Dimension(100, 25));
        this.loadButton.setBackground(new Color(235, 235, 235));
        this.loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                go(jComboBoxCartes.getSelectedItem().toString(), jCheckBoxSortieGraphique.isSelected());
            }
        });

        // Set the Layout
        this.cp = getContentPane();
        this.cp.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Add the components to the control panels
        //On positionne la case de départ du composant
        gbc.gridx = 0;
        gbc.gridy = 0;
        //La taille en hauteur et en largeur
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        //Cette instruction informe le layout que c'est une fin de ligne
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.controlPanel.add(jLabelBienvenue, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.controlPanel.add(jLabelImage, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        this.controlPanel.add(jLabelChoixCarte, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.controlPanel.add(jComboBoxCartes, gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        this.controlPanel.add(jLabelSortieGraphique, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.controlPanel.add(jCheckBoxSortieGraphique, gbc);
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.controlPanel.add(loadButton, gbc);

        // Add the panels to the Launch
        this.cp.add(controlPanel);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    } // _________  end of constructor

    public static void main(String[] args) {
        Launch launch = new Launch(args);
        //launch.go();
    }

    public int afficherMenu() {
        int choix = -1;
        String selection = (String) JOptionPane.showInputDialog(null, "Que voulez-vous faire ?", "Votre choix", JOptionPane.QUESTION_MESSAGE, null, menu, menu[0]);
        if (selection != null)
            for (int i = 0; i < menu.length; i++)
                if (selection.equals(menu[i])) choix = i;

        return choix;
    }

    public void go(String carte, boolean sortieGraphique) {

        try {
            System.out.println("**");
            System.out.println("** Programme de test des algorithmes de graphe.");
            System.out.println("**");
            System.out.println();

            // On obtient ici le nom de la carte a utiliser.
            String nomcarte = carte;
            if (nomcarte == null) {
                System.exit(1);
            }
            DataInputStream mapdata = Openfile.open(nomcarte);
            boolean display = sortieGraphique;

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
                        origine = 12;
                        origine = Integer.parseInt(JOptionPane.showInputDialog(null, "Numero du sommet d'origine'"));
                        dest = Integer.parseInt(JOptionPane.showInputDialog(null, "Numero du sommet d'origine'"));
                        algo1 = new Pcc_Dijkstra(graphe, this.fichierSortie(), this.readarg, true, origine, dest);
                        // TODO : PCCStar non assign?
                        algo = new PccStar(graphe, this.fichierSortie(), this.readarg, true, origine, dest);

                        break;
                    default:
                        System.out.println("Choix de menu incorrect : " + choix);
                        JOptionPane.showMessageDialog(null, "Choix de menu incorrect", "Choix menu", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                }

                if (algo != null) {
                    //TODO
                    if (algo1 != null)
                        algo1.run();
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
