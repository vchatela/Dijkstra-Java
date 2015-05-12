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


//FRANCE
//Origine :1494811
//Arrivée : 311006

import core.Algo;
import core.Graphe;
import core.PccStar;
import core.Pcc_Dijkstra;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

public class Launch extends JFrame {

    /**
     * Variable declarations
     */

    // Declaration et initialisation des tableaux d'informations
    static private final String menuDisplay[] = {"Quitter", "PCC Standard", "PCC A-star","Charger un fichier de chemin"
            , "Reinitialiser la carte", "Tester les performances", "Covoiturage", "Obtenir un numero de sommet "};
    static private final String menuNotDisplay[] = {"Quitter", "PCC Standard", "PCC A-star","Charger un fichier de chemin"
            , "Reinitialiser la carte", "Tester les performances", "Covoiturage"};
    static private final String cartes[] = {"midip", "insa", "france",
            "fractal", "reunion", "newzealand", "morbihan", "mayotte", "paris", "carre-dense", "carre", "fractal-spiral"};
    static private final String chemins[] = {"chemin_insa", "chemin_insa1", "chemin_midip", "chemin_fractal", "chemin_reunion", "chemin_carre-dense", "chemin_spiral",
            "chemin_spiral2"};
    private final Readarg readarg;                    // Contient les arguments au lancement de l'appli
    // Declaration de Variables lié à l'affichage graphique
    private JPanel		    controlPanel;               // Contient le menu de selection des choix
    private Dessin          dessin;                     // Contient la map
    private Container 	    cp;                         // Conteneur de la fenetre, on y ajoute les deux précédents éléments
    private JLabel		    jSpace;                     // Espace vertical dans le menu de selection des choix
    private JLabel		    jLabel1;                    // Texte à afficher dans le menu de selection des choix
    private JLabel		    jLabel2;
    private JLabel		    jLabel3;
    private JLabel		    jLabel4;
    private JLabel          jLabel5;
    private JLabel          jLabelImage;                // Image de lancement à afficher
    private ImageIcon       image;                      // Image de lancement en dur
    private JTextField      jTextFieldFichier;          // Zone de saisie du fichier
    private JTextField      jTextField1;                // Zone de saisie n°1
    private JTextField      jTextField2;                // Zone de saisie n°2
    private JTextField      jTextFieldOrigine;          // Zone de saisie pour l'origine
    private JTextField      jTextFieldDest;             // Zone de saisie pour la destination
    private JCheckBox       jCheckBox;                  // Un checkbox (affichage graphique ou du déroulement d'execution d'algo)
    private JRadioButton    jRadioButtonChoixTemps;     // CHoix du cout en temps
    private JRadioButton    jRadioButtonChoixDistance;  // CHoix du cout en distance
    private ButtonGroup     buttonGroup;                // Grouper les deux précédents boutons
    private JComboBox	    jComboBoxMenu;              // Contient les menus
    private JComboBox	    jComboBoxCartes;            // Contient les cartes
    private JButton		    okButton;                   // Button ok (lancement de l'appli, chagement de menu, attente de lecture des coord du clic)
    private JButton		    loadButton;                 // Button charger (lancement de l'appli)
    private Thread          t;                          // Utilisé pour afficher la map en parallèle du menu de selection des choix

    // Declaration de Variables lié à l'execution du programme
    private String          nomcarte;                   // Nom de la carte à charger
    private PrintStream     sortie;                     // Fichier de sortie
    private Graphe          graphe;                     // La map
    private boolean         display;                    // Affichage graphique ou non
    private boolean         buttonClicked = false;      // Un bouton a été cliqué ou non
    private boolean         textFieldsSet = false;      // Tous les textFields (1&2) sont remplis)
    private boolean         continuer = true;           // Boucle principale : le menu est accessible jusqu'a ce que l'on quitte.
    private ArrayList       clickCoord;                 // Pour avoir coordonnées d'un clic
    private int             sommetsConnus;              // L'utilisateur connait les sommets origine et dest ou non
    private int             choixMenu;                  // Choix de la tache à effectuer
    private int             choixCout;                  // Plus court en Distance:0 ou Temps:1
    private int             affichageDeroulementAlgo;   // Affichage des algorithmes ou non
    private int             origine, dest;              // Numéro des sommets origine et dest
    private Algo            algo;                       // Algorithme a executer
    private Algo            algo1;                      // Algorithme n°2 a executer si on lance le test de performance

    /**
     * Default constructor
     */

    public Launch(String[] args) {

        // Lecture des paramètres de lancement de l'application
        this.readarg = new Readarg(args);

        // Paramétrage de l'espace vertical
        jSpace = new JLabel();
        jSpace.setPreferredSize(new Dimension(350, 25));

        // Paramétrage des textes à afficher
        jLabel1 = new JLabel("Bienvenue (Version 4.0 de Mangel - Chatelard)");
        jLabel2 = new JLabel("Programme de test des algorithmes de graphe");
        jLabel3 = new JLabel("Nom du fichier .map a utiliser");
        jLabel4 = new JLabel("Voulez-vous une sortie graphique");
        jLabel5 = new JLabel("Fichier de sortie :");

        // Paramétrage des zone de saisie
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jTextFieldOrigine = new JTextField();
        jTextFieldDest = new JTextField();
        jTextFieldFichier = new JTextField();
        jTextField1.setPreferredSize(new Dimension(300, 25));
        jTextField2.setPreferredSize(new Dimension(300, 25));
        jTextFieldOrigine.setPreferredSize(new Dimension(100, 25));
        jTextFieldDest.setPreferredSize(new Dimension(100, 25));
        jTextFieldFichier.setPreferredSize(new Dimension(100, 25));
        jTextFieldOrigine.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                textFieldCoordChanged();
            }

            public void removeUpdate(DocumentEvent e) {
                textFieldCoordChanged();
            }

            public void insertUpdate(DocumentEvent e) {
                textFieldCoordChanged();
            }
        });
        jTextFieldDest.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                textFieldCoordChanged();
            }

            public void removeUpdate(DocumentEvent e) {
                textFieldCoordChanged();
            }

            public void insertUpdate(DocumentEvent e) {
                textFieldCoordChanged();
            }
        });

        // Paramétrage du checkbox
        jCheckBox = new JCheckBox();
        jCheckBox.setSelected(true);

        // Paramétrage des radiobuttons
        jRadioButtonChoixTemps = new JRadioButton("En temps");
        jRadioButtonChoixDistance = new JRadioButton("En distance");
        jRadioButtonChoixTemps.setSelected(true);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButtonChoixTemps);
        buttonGroup.add(jRadioButtonChoixDistance);

        // Paramétrage des menus de selection (cartes et menus)
        jComboBoxCartes = new JComboBox();
        for (String carte : cartes)
            jComboBoxCartes.addItem(carte);
        jComboBoxMenu = new JComboBox();

        // Paramétrage de l'image
        jLabelImage = new JLabel();
        image = new ImageIcon("arbre.jpg");
        jLabelImage.setIcon(image);

        // Paramétrage des buttons
        loadButton = new JButton("CHARGER");
        loadButton.setPreferredSize(new Dimension(100, 25));
        loadButton.setBackground(new Color(235, 235, 235));
        loadButton.addActionListener(new BoutonListener());
        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(100, 25));
        okButton.setBackground(new Color(235, 235, 235));
        okButton.addActionListener(new BoutonListener());

        // Paramétrage du menu de selection des choix avec ajout des composants
        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(350, 645));
        controlPanel.setLayout(new FlowLayout());
        controlPanel.add(jLabel1);
        controlPanel.add(jLabel2);
        controlPanel.add(jSpace);
        controlPanel.add(jLabelImage);
        controlPanel.add(jLabel3);
        controlPanel.add(jComboBoxCartes);
        controlPanel.add(jLabel4);
        controlPanel.add(jCheckBox);
        controlPanel.add(jLabel5);
        controlPanel.add(jTextFieldFichier);
        controlPanel.add(loadButton);

        // Ajout du menu de selection des choix dans le conteneur de la fenêtre
        cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(controlPanel);

        // Paramétrage de la fenêtre et mise en place des éléments graphiques
        this.setTitle("Dijkstra");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);

    } // _________  end of constructor

    /**
     * Main function
     */

    public static void main(String[] args) {
        Launch launch = new Launch(args);
    }

    /**
     * Methods
     */

    /**
     * Affichage du menu au lancement de l'application
     */
    public int afficherMenu() {
        // Paramétrer le menu de selection
        makeControlPanel(0);

        // On attend d'avoir cliqué sur OK
        okButton.setEnabled(true);
        waitButtonOk();
        okButton.setEnabled(false);

        // On retourne le numéro du menu séléctionné
        return jComboBoxMenu.getSelectedIndex();
    }

    /**
     * Lancement de l'application générale (dans un thread)
     */
    public void go() {

        try {
            // Récupérer la carte souhaitée
            DataInputStream mapdata = Openfile.open(nomcarte);

            // Afficher ou non la map -> création d'un dessin associé et ajout à la fenetre
            dessin = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
            cp.add(dessin);
            this.pack();

            // Création du graphe en fonction de la map selectionnée
            graphe = new Graphe(nomcarte, mapdata, dessin);

            // Ouverture et initialisation du fichier de sortie contenant des resultats
            sortie = this.fichierSortie();

            // Boucle principale : choix de la tache et execution
            while (continuer) {

                // Affichage du menu : le choix correspond au numero du menu choisi.
                choixMenu = this.afficherMenu();

                // On test ce que le menu choisi
                switch (choixMenu) {

                    // Quitter l'application
                    case 0:
                        continuer = false;
                        break;

                    // PCC Standard : Dijkstra
                    case 1:
                        //Initialisation et lancement de l'algorithme
                        initialiserAlgo();
                        algo = new Pcc_Dijkstra(graphe, sortie, this.readarg, this.choixCout, this.affichageDeroulementAlgo, true, origine, dest);
                        algo.run();

                        break;

                    // PCC A-Star : Dijkstra guidé
                    case 2:

                        //Initialisation et lancement de l'algorithme
                        initialiserAlgo();
                        algo = new PccStar(graphe, sortie, this.readarg, this.choixCout, this.affichageDeroulementAlgo, true, origine, dest);
                        algo.run();

                        break;

                    // Charger un fichier de chemin
                    case 3:
                        // TODO affichage graphique

                        // Paramétrer le menu de selection
                        makeControlPanel(3);

                        String nom_chemin = (String) JOptionPane.showInputDialog(null, "Nom du chemin .path a utiliser?", "Choix de la carte",
                                JOptionPane.QUESTION_MESSAGE, null, chemins, chemins[0]);
                        if (nom_chemin == null) {
                            System.exit(1);
                        }

                        int ok = graphe.verifierChemin(Openfile.open(nom_chemin), nom_chemin);
                        if (ok == -1)
                            continue;
                        graphe.getChemin().tracerChemin(graphe.getDessin());
                        graphe.getChemin().cout_chemin_distance();
                        graphe.getChemin().cout_chemin_temps();

                        break;

                    // Réinitialiser la map
                    case 4:
                        // Paramétrer le menu de selection
                        makeControlPanel(5);

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        // On récupère le nom de la carte
                        nomcarte = jComboBoxCartes.getSelectedItem().toString();

                        // Choix de l'affichage de la carte ou non
                        display = jCheckBox.isSelected();

                        // On met à jour la carte et on la réaffiche si on a souhaité avoir l'affichage graphique au lancement
                        cp.remove(dessin);
                        dessin = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
                        cp.add(dessin);
                        dessin.revalidate();
                        mapdata = Openfile.open(nomcarte);
                        graphe = null; //Pour detruire l'objet (methode finalize())
                        graphe = new Graphe(nomcarte, mapdata, dessin);
                        this.pack();

                        break;

                    // Programme de test des 2 algos D + D A-Star
                    case 5:
                        // Initialisation des algorithmes
                        initialiserAlgo();
                        algo1 = new Pcc_Dijkstra(graphe, sortie, this.readarg, this.choixCout, this.affichageDeroulementAlgo, true, origine, dest);

                        // On paramètre la couleur d'execution du 1i algorithme
                        graphe.getDessin().setColor(Color.magenta);

                        // Lancement du 1i algorithmes et récupération des résultats
                        ArrayList perf1 = null;
                        perf1 = algo1.run();

                        // Si l'algorithme n°1 n'a rien retourné, on revient au choix du menu
                        if (perf1 == null)
                            continue;

                        algo = new PccStar(graphe, sortie, this.readarg, this.choixCout, this.affichageDeroulementAlgo, true, origine, dest);

                        // On paramètre la couleur d'execution du 2i algorithme
                        graphe.getDessin().setColor(Color.red);

                        //Lancement du 2i algorithmes et récupération des résultats
                        ArrayList perf2 = null;
                        perf2 = algo.run();

                        // Si l'algorithme n°2 n'a rien retourné, on revient au choix du menu
                        if (perf1 == null)
                            continue;

                        // On affiche les performances
                        String resultat = new String("Performance des algos Dijkstra VS Dijkstra A-Star \n");
                        if(choixCout == 0)
                            resultat += "Le cout est de : " + perf1.get(0) + " km - " + perf2.get(0) + " km \n";
                        else
                            resultat += "Le cout est de : " + perf1.get(0) + " min - " + perf2.get(0) + " min \n";
                        resultat += "Durée exécution : " + perf1.get(1) + " ms - " + perf2.get(1) + " ms \n";
                        resultat += "Nbr max éléments dans le tas : " + perf1.get(2) + " - " + perf2.get(2) + "\n";
                        resultat += "Nombre d'éléments parcourut : " + perf1.get(3) + " - " + perf2.get(3) + "\n";
                        JOptionPane.showMessageDialog(null, resultat);

                        break;

                    // Covoiturage
                    case 6:
                        break;

                    // Obtenir un numéro de sommet
                    case 7:
                        // Paramétrer le menu de selection
                        makeControlPanel(3);

                        // On récupère les informations du click
                        clickCoord = graphe.situerClick();

                        // On vérifie que l'on a bien récupéré les informations du click
                        if(clickCoord == null)
                            System.out.println("Le clic n'a rien retourné");
                        else {
                            // On affiche les information
                            jTextField1.setText(clickCoord.get(0).toString());
                            jTextField2.setText(clickCoord.get(1).toString());
                        }

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        break;

                    default:
                        System.out.println("Choix de menu incorrect : " + choixMenu);
                        JOptionPane.showMessageDialog(null, "Choix de menu incorrect", "Choix menu", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                }
            }

            // On a décidé de quitter l'application

            // On detruit le jFrame
            this.setVisible(false);
            this.dispose();

            // On ferme le fichier
            sortie.close();

            // On quitte
            System.out.println("Programme termine.");
            System.exit(0);


        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * re-Paramétrage du menu de selection des choix
     */

    public void makeControlPanel(int choice) {
        // On supprimme tous les éléments précédemment placé et mise en place des éléments graphiques
        cp.remove(controlPanel);
        controlPanel.removeAll();
        controlPanel.revalidate();
        jTextField1.setText("");
        jTextField2.setText("");
        jTextFieldOrigine.setText("");
        jTextFieldDest.setText("");

        // En fonction du paramètre donné
        switch (choice) {
            // Menu de lancement
            case 0:
                jLabel1.setText("Que voulez-vous faire");
                if(display)
                    for (String choix : menuDisplay)
                        jComboBoxMenu.addItem(choix);
                else
                    for (String choix : menuNotDisplay)
                        jComboBoxMenu.addItem(choix);
                controlPanel.add(Box.createHorizontalStrut(300));
                controlPanel.add(jLabel1);
                controlPanel.add(jComboBoxMenu);
                controlPanel.add(okButton);
                break;
            // Utilisé par Pcc Standard, Pcc A-Star et le Programme de test des 2 algos D + D A-Star
            case 1:
                jTextField1.setEditable(true);
                jTextField2.setEditable(true);
                okButton.setEnabled(false);
                jLabel1.setText("Afficher le deroulement des algorithmes ?");
                jLabel2.setText("Plus court en :");
                jLabel3.setText("Saisir le noeud de départ : ");
                jLabel4.setText("Saisir le noeud d'arrivée : ");
                jLabel5.setText("Saisir les coordonnées manuellement");
                jTextFieldOrigine.setText("");
                jTextFieldDest.setText("");
                if(display) {
                    controlPanel.add(jLabel1);
                    controlPanel.add(jCheckBox);
                }
                controlPanel.add(jLabel2);
                controlPanel.add(jRadioButtonChoixTemps);
                controlPanel.add(jRadioButtonChoixDistance);
                controlPanel.add(jSpace);
                controlPanel.add(jLabel5);
                controlPanel.add(jLabel3);
                controlPanel.add(jTextFieldOrigine);
                controlPanel.add(jLabel4);
                controlPanel.add(jTextFieldDest);
                controlPanel.add(okButton);
                break;
            case 2:
                jTextField1.setEditable(false);
                jTextField2.setEditable(false);
                jLabel1.setText("Afficher le deroulement des algorithmes ?");
                jLabel2.setText("Plus court en :");
                jLabel3.setText("Cliquez une 1i fois pour le noeud de départ : ");
                jLabel4.setText("Cliquez une 2i fois pour le noeud d'arrivée : ");
                jLabel5.setText("Obtenir les coordonnées en cliquant");
                jTextFieldOrigine.setText("");
                jTextFieldDest.setText("");
                if(display) {
                    controlPanel.add(jLabel1);
                    controlPanel.add(jCheckBox);
                }
                controlPanel.add(jLabel2);
                controlPanel.add(jRadioButtonChoixTemps);
                controlPanel.add(jRadioButtonChoixDistance);
                controlPanel.add(jSpace);
                controlPanel.add(jLabel5);
                controlPanel.add(jLabel3);
                controlPanel.add(jTextFieldOrigine);
                controlPanel.add(jLabel4);
                controlPanel.add(jTextFieldDest);

                break;
            case 3:
                okButton.setEnabled(true);
                jLabel2.setText("Clic aux coordonnées : ");
                jLabel3.setText("Noeud le plus proche : ");
                jLabel4.setText("Cliquez sur OK une fois terminé");
                controlPanel.add(jSpace);
                controlPanel.add(jLabel2);
                controlPanel.add(jTextField1);
                controlPanel.add(jLabel3);
                controlPanel.add(jTextField2);
                controlPanel.add(jLabel4);
                controlPanel.add(okButton);
                break;
            case 4:
                break;
            case 5:
                okButton.setEnabled(true);
                jLabel1.setText("Nom du fichier .map a utiliser");
                jLabel2.setText("Voulez-vous une sortie graphique");
                controlPanel.add(jLabel1);
                controlPanel.add(jComboBoxCartes);
                controlPanel.add(jLabel2);
                controlPanel.add(jCheckBox);
                controlPanel.add(okButton);
                break;
        }
        cp.add(controlPanel, 0);
        this.repaint();
        this.pack();
    }

    public void initialiserAlgo() {

        // On demande à l'utilisateur s'il connait les numéros ou veut cliquer
        if(display)
            sommetsConnus = JOptionPane.showConfirmDialog(null, "Connaissez vous le numéro des sommets", "", JOptionPane.OK_OPTION);
        else
            sommetsConnus = 0;

        switch (sommetsConnus) {
            case JOptionPane.OK_OPTION:
                //Paramétrer le menu de selection
                makeControlPanel(1);
                // Les coordonnées vont automatiquements se mettrent à jours

                // On doit cliquer sur OK pour continuer
                waitButtonOk();
                break;
            default:
                //Paramétrer le menu de selection
                makeControlPanel(2);
                try {
                    clickCoord = graphe.situerClick();
                    jTextFieldOrigine.setText(clickCoord.get(1).toString());
                    clickCoord = graphe.situerClick();
                    jTextFieldDest.setText(clickCoord.get(1).toString());
                    origine = Integer.parseInt(jTextFieldOrigine.getText());
                    dest = Integer.parseInt(jTextFieldDest.getText());
                } catch (NumberFormatException n) {
                    System.out.println(n);
                    origine = -1;
                    dest = -1;
                }
                break;
        }

        //Choix du coup en temps ou distance
        if(jRadioButtonChoixDistance.isSelected())
            this.choixCout = 0;
        else this.choixCout = 1;

        //Choix de l'affichage des algo
        if(jCheckBox.isSelected())
            this.affichageDeroulementAlgo = 1;
        else this.affichageDeroulementAlgo = 0;
    }

    // Ouvre un fichier de sortie pour ecrire les reponses
    public PrintStream fichierSortie() {
        PrintStream result = System.out;

        //String nom = this.readarg.lireString("Nom du fichier de sortie ? ");
        //String nom = JOptionPane.showInputDialog(null, "Nom du fichier de sortie ?");
        String nom = jTextFieldFichier.getText();
        if ("".equals(nom)) {
            //nom = "/dev/null";
            nom = "sortie";
        }

        try {
            result = new PrintStream(nom);
            result.println("Fichier créé le : " + new Date() + "\n");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur ? l'ouverture du fichier" + nom);
            //System.err.println("Erreur a l'ouverture du fichier " + nom);
            System.exit(1);
        }

        return result;
    }

    public void waitButtonOk() {
        while (buttonClicked == false) {
            try {
                t.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Error thread sleep");
            }
        }
        buttonClicked = false;
    }

    /**
     * Method that is called when one of the following textfields is modified
     * We won't be able to clic on the DRAW button if one of them if empty
     */
    private void textFieldCoordChanged() {
        if(jTextFieldOrigine.getText().equals("") || jTextFieldDest.getText().equals("")) {
            okButton.setEnabled(false);
        }
        else {
            origine = Integer.parseInt(jTextFieldOrigine.getText());
            dest = Integer.parseInt(jTextFieldDest.getText());
            okButton.setEnabled(true);
        }
    }

    public class BoutonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            //Click sur le boutton Load
            if (evt.getSource() == loadButton) {
                nomcarte = jComboBoxCartes.getSelectedItem().toString();
                display = jCheckBox.isSelected();
                loadButton.setEnabled(false);
                t = new Thread(new PlayAnimation());
                t.start();
            }
            else if (evt.getSource() == okButton) {
                buttonClicked = true;
            }

        }
    }

    class PlayAnimation implements Runnable {
        public void run() {
            go();
        }
    }

}
