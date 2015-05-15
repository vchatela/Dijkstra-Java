package base;

/*
 * Ce programme propose de lancer divers algorithmes sur les graphes
 * a partir d'un menu graphique
 */


import core.*;
import core.Label;

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
    static private final String menuDisplay[] = {"Quitter", "PCC Standard", "PCC A-star", "Tester les performances", "Covoiturage",
            "Charger un fichier de chemin", "Reinitialiser la carte", "Obtenir un numero de sommet "};
    static private final String menuNotDisplay[] = {"Quitter", "PCC Standard", "PCC A-star", "Tester les performances", "Covoiturage",
            "Charger un fichier de chemin", "Reinitialiser la carte"};
    static private final String cartes[] = {"midip", "insa", "france",
            "fractal", "reunion", "newzealand", "morbihan", "mayotte", "paris", "carre-dense", "carre", "fractal-spiral"};
    static private final String chemins[] = {"chemin_insa", "chemin_insa1", "chemin_midip", "chemin_fractal", "chemin_reunion", "chemin_carre-dense", "chemin_spiral",
            "chemin_spiral2"};

    // Declaration de Variables lié à l'affichage graphique
    private JPanel		    controlPanel;               // Contient le menu de selection des choix
    private Dessin          dessinPanel;                     // Contient la map
    private Container 	    cp;                         // Conteneur de la fenetre, on y ajoute les deux précédents éléments
    private JLabel		    jSpace;                     // Espace vertical dans le menu de selection des choix
    private JLabel                                      // Texte à afficher
            jLabelNames, jLabelTitle, jLabelCarte,
            jLabelAfficher, jLabelFichier, jLabelMenu,
            jLabelDeroulement, jLabelChoixCout,
            jLabelDepart, jLabelDepartVoiture, jLabelDepartPieton, jLabelArrivee,
            jLabelCoordMan, jLabelCoordClick, jLabelCoordSitues,
            jLabelNoeudsProches, jLabelChemin;
    private JLabel          jLabelImageGraphe;          // Image de lancement (graphe) à afficher
    private JLabel          jLabelImageINSA;            // Logo de l'INSA à afficher
    private ImageIcon       imageGraphe;                // Image de lancement (graphe) en dur
    private ImageIcon       imageINSA;                  // Logo de l'INSA en dur
    private JTextField      jTextFieldFichier;          // Zone de saisie du fichier
    private JTextField      jTextField1;                // Zone de saisie n°1
    private JTextField      jTextField2;                // Zone de saisie n°2
    private JTextField      jTextFieldOrigine;          // Zone de saisie pour l'origine (voiture si covoiturage)
    private JTextField      jTextFieldPieton;           // Zone de saisie pour l'origine du pieton
    private JTextField      jTextFieldDest;             // Zone de saisie pour la destination
    private JCheckBox       jCheckBox;                  // Un checkbox (affichage graphique ou du déroulement d'execution d'algo)
    private JRadioButton    jRadioButtonChoixTemps;     // CHoix du cout en temps
    private JRadioButton    jRadioButtonChoixDistance;  // CHoix du cout en distance
    private ButtonGroup     buttonGroup;                // Grouper les deux précédents boutons
    private JComboBox	    jComboBoxMenu;              // Contient les menus
    private JComboBox	    jComboBoxCartes;            // Contient les cartes
    private JComboBox	    jComboBoxChemins;          // Contient les chemins
    private JButton		    jButtonOk;                  // Button ok (lancement de l'appli, chagement de menu, attente de lecture des coord du clic)
    private JButton		    jButtonLoad;                // Button charger (lancement de l'appli)
    private Thread          thread;                     // Utilisé pour afficher la map en parallèle du menu de selection des choix

    // Declaration de Variables lié à l'execution du programme
    private String          nomcarte;                   // Nom de la carte à charger
    private String          nomchemin;                  // Nom du chemin à charger
    private String          resultat;                   // Contient les resultats des algos
    private String          cout;                       // Contient le cout
    private PrintStream     sortie;                     // Fichier de sortie
    private Graphe          graphe;                     // La map
    private boolean         display;                    // Affichage graphique ou non
    private boolean         buttonClicked = false;      // Un bouton a été cliqué ou non
    private boolean         continuer = true;           // Boucle principale : le menu est accessible jusqu'a ce que l'on quitte.
    private ArrayList       clickCoord;                 // Pour avoir coordonnées d'un clic
    private int             sommetsConnus;              // L'utilisateur connait les sommets origine et dest ou non
    private int             choixMenu;                  // Choix de la tache à effectuer
    private int             choixCout;                  // Plus court en Distance:0 ou Temps:1
    private int             affichageDeroulementAlgo;   // Affichage des algorithmes ou non
    private int             origine, pieton, dest;      // Numéro des sommets origine, pieton et dest
    private Algo            algo;                       // Algorithme a executer
    private Algo            algo1;                      // Algorithme n°2 a executer si on lance le test de performance


    /**
     * Default constructor
     */

    public Launch() {

        // Paramétrage des textes à afficher
        jLabelTitle         = new JLabel("<html><br>PROGRAMME DE TESTS DES ALGORITHMES DE GRAPHE<br><br></html>");
        jLabelCarte         = new JLabel("Fichier .map à utiliser : ");
        jLabelAfficher      = new JLabel("Afficher la carte : ");
        jLabelFichier       = new JLabel("Fichier de sortie : ");
        jLabelMenu          = new JLabel("Que voulez-vous faire : ");
        jLabelDeroulement   = new JLabel("Afficher le deroulement : ");
        jLabelChoixCout     = new JLabel("Choix du coup : ");
        jLabelDepart        = new JLabel("Départ : ");
        jLabelDepartVoiture = new JLabel("Départ du conducteur : ");
        jLabelDepartPieton  = new JLabel("Départ du piéton : ");
        jLabelArrivee       = new JLabel("Arrivée : ");
        jLabelCoordMan      = new JLabel("Saisir les noeuds manuellement");
        jLabelCoordClick    = new JLabel("Obtenir les noeuds en cliquant");
        jLabelCoordSitues   = new JLabel("Clic aux coordonnées : ");
        jLabelNoeudsProches = new JLabel("Noeud le plus proche : ");
        jLabelNames         = new JLabel("<html>BUREAU D'ETUDE GRAPHE 3MIC-IR<br>" +
                                         "Etudiants : J. Mangel - V. Chatelard<br>" +
                                         "Enseignants : D. Le Botlan - M-J Huguet</html>");
        jLabelChemin        = new JLabel("Chemin .path à utiliser : ");
        jLabelCarte.setPreferredSize(new Dimension(170, 25));
        jLabelAfficher.setPreferredSize(new Dimension(170, 25));
        jLabelFichier.setPreferredSize(new Dimension(170, 25));
        jLabelMenu.setPreferredSize(new Dimension(170, 25));
        jLabelDeroulement.setPreferredSize(new Dimension(170, 25));
        jLabelChoixCout.setPreferredSize(new Dimension(170, 25));
        jLabelDepart.setPreferredSize(new Dimension(170, 25));
        jLabelDepartVoiture.setPreferredSize(new Dimension(170, 25));
        jLabelDepartPieton.setPreferredSize(new Dimension(170, 25));
        jLabelArrivee.setPreferredSize(new Dimension(170, 25));
        jLabelCoordSitues.setPreferredSize(new Dimension(170, 25));
        jLabelNoeudsProches.setPreferredSize(new Dimension(170, 25));
        jLabelNames.setPreferredSize(new Dimension(255, 83));
        jLabelNames.setOpaque(true);
        jLabelNames.setBackground(Color.white);
        jLabelChemin.setPreferredSize(new Dimension(170, 25));

        // Paramétrage de l'espace vertical
        jSpace = new JLabel();
        jSpace.setPreferredSize(new Dimension(350, 25));

        // Paramétrage des zone de saisie
        jTextField1         = new JTextField();
        jTextField2         = new JTextField();
        jTextFieldOrigine   = new JTextField();
        jTextFieldPieton   = new JTextField();
        jTextFieldDest      = new JTextField();
        jTextFieldFichier   = new JTextField("sortie");

        jTextField1.setPreferredSize(new Dimension(170, 25));
        jTextField2.setPreferredSize(new Dimension(170, 25));
        jTextFieldFichier.setPreferredSize(new Dimension(170, 25));
        jTextFieldOrigine.setPreferredSize(new Dimension(170, 25));
        jTextFieldPieton.setPreferredSize(new Dimension(170, 25));
        jTextFieldDest.setPreferredSize(new Dimension(170, 25));
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
        jTextFieldPieton.getDocument().addDocumentListener(new DocumentListener() {
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
        jCheckBox.setPreferredSize(new Dimension(170, 25));

        // Paramétrage des radiobuttons
        jRadioButtonChoixTemps    = new JRadioButton("Temps");
        jRadioButtonChoixDistance = new JRadioButton("Distance");
        jRadioButtonChoixTemps.setSelected(true);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButtonChoixTemps);
        buttonGroup.add(jRadioButtonChoixDistance);

        // Paramétrage des menus de selection (cartes et menus)
        jComboBoxMenu    = new JComboBox();
        jComboBoxCartes  = new JComboBox();
        jComboBoxChemins = new JComboBox();
        jComboBoxMenu.setPreferredSize(new Dimension(170, 25));
        jComboBoxCartes.setPreferredSize(new Dimension(170, 25));
        for (String carte : cartes)
            jComboBoxCartes.addItem(carte);
        jComboBoxChemins.setPreferredSize(new Dimension(170, 25));
        for (String chemin : chemins)
            jComboBoxChemins.addItem(chemin);

        // Paramétrage des images : graphe et logo INSA
        imageGraphe       = new ImageIcon("arbre.jpg");
        imageINSA         = new ImageIcon("logoINSA.png");
        jLabelImageGraphe = new JLabel();
        jLabelImageINSA   = new JLabel();
        jLabelImageGraphe.setIcon(imageGraphe);
        jLabelImageINSA.setIcon(imageINSA);

        // Paramétrage des buttons
        jButtonLoad = new JButton("CHARGER");
        jButtonLoad.setPreferredSize(new Dimension(100, 25));
        jButtonLoad.setBackground(new Color(235, 235, 235));
        jButtonLoad.addActionListener(new BoutonListener());
        jButtonOk = new JButton("OK");
        jButtonOk.setPreferredSize(new Dimension(100, 25));
        jButtonOk.setBackground(new Color(235, 235, 235));
        jButtonOk.addActionListener(new BoutonListener());

        // Paramétrage du menu de selection des choix avec ajout des composants
        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(350, 600));
        controlPanel.add(jLabelNames);
        controlPanel.add(jLabelImageINSA);
        controlPanel.add(jLabelTitle);
        controlPanel.add(jLabelImageGraphe);
        controlPanel.add(jSpace);
        controlPanel.add(jLabelCarte);
        controlPanel.add(jComboBoxCartes);
        controlPanel.add(jLabelAfficher);
        controlPanel.add(jCheckBox);
        controlPanel.add(jLabelFichier);
        controlPanel.add(jTextFieldFichier);
        controlPanel.add(jButtonLoad);

        // Ajout du menu de selection des choix dans le conteneur de la fenêtre
        cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(controlPanel);

        // Paramétrage de la fenêtre et mise en place des éléments graphiques
        this.setTitle("INSA Toulouse - BE Graphes 3-MIC IR");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);

    } // _________  end of constructor


    /**
     * Main function
     */

    public static void main(String[] args) {
        new Launch();
    }


    /**
     * Lancement de l'application générale (dans un thread)
     */

    public void go() {

        try {
            // On ne peux plus cliquer sur CHARGER (le temps que la carte charge, sinon probleme, on en charge d'autres)
            jButtonLoad.setEnabled(false);

            // Récupérer la carte souhaitée
            nomcarte = jComboBoxCartes.getSelectedItem().toString();
            DataInputStream mapdata = Openfile.open(nomcarte);

            // Afficher ou non la map -> création d'un dessin associé et ajout à la fenetre
            display = jCheckBox.isSelected();
            dessinPanel = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
            if(display)
                cp.add(dessinPanel);
            this.pack();

            // Création du graphe en fonction de la map selectionnée
            graphe = new Graphe(nomcarte, mapdata, dessinPanel);

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
                        // Initialisation et lancement de l'algorithme
                        initialiserAlgo();
                        algo = new Pcc_Dijkstra(graphe, choixCout, affichageDeroulementAlgo, origine, dest, false);
                        ArrayList perfStandard = algo.run();
                        afficherEtEcrireResultats(1, perfStandard);
                        break;

                    // PCC A-Star : Dijkstra guidé
                    case 2:

                        //Initialisation et lancement de l'algorithme
                        initialiserAlgo();
                        algo = new PccStar(graphe, choixCout, affichageDeroulementAlgo, origine, dest, false);
                        ArrayList perfAStar = algo.run();
                        afficherEtEcrireResultats(2, perfAStar);
                        break;

                    // Programme de test des 2 algos Dijkstra : PCC Standard + PCC A-Star
                    case 3:
                        // Initialisation des algorithmes
                        initialiserAlgo();

                        // 1i algo -> PCC Standard : Dijkstra, 2i algo -> PCC A-Star : Dijkstra guidé
                        algo1 = new Pcc_Dijkstra(graphe, choixCout, affichageDeroulementAlgo, origine, dest, false);
                        algo = new PccStar(graphe, choixCout, affichageDeroulementAlgo, origine, dest, false);

                        // Lancement des algorithmes et récupération des résultats
                        graphe.getDessin().setColor(Color.magenta);
                        ArrayList perf1 = algo1.run();
                        graphe.getDessin().setColor(Color.red);
                        ArrayList perf2 = algo.run();

                        afficherEtEcrireResultats(perf1, perf2);
                        break;

                    // Covoiturage
                    case 4:
                        // ArrayList contenant les couts màj
                        ArrayList<Label_Dijkstra> covoitSomme = new ArrayList<Label_Dijkstra>();
                        ArrayList perfVoitureTous, perfPietonTous, perfDestTous;
                        int noeud_rejoint = -1;
                        double min = Double.POSITIVE_INFINITY;
                        Node node = null;

                        // Initialisation des algorithmes : cout en TEMPS !
                        initialiserCovoit();

                        // Lancement des algorithmes

                        // PCC de la VOITURE vers TOUS : récupération de l'arraylist des couts
                        algo = new Pcc_Dijkstra(graphe, choixCout, affichageDeroulementAlgo, origine, dest, true);
                        perfVoitureTous = algo.run();
                        ArrayList<Label_Dijkstra> covoitVoiture = algo.getLabels();

                        // PCC du PIETON vers TOUS : màj de l'arraylist s'il est plus grand
                        algo = new Pcc_Dijkstra(graphe, choixCout, affichageDeroulementAlgo, pieton, dest, true);
                        perfPietonTous = algo.run();
                        ArrayList<Label_Dijkstra> covoitPieton = algo.getLabels();

                        // PCC de la DESTINATION vers TOUS : màj de l'arraylist si max (x,y) < Pcc(dest, noeud) + Pcc( (x ou y) vers noeuds )
                        algo = new Pcc_Dijkstra(graphe, choixCout, affichageDeroulementAlgo, dest, dest, true);
                        perfDestTous = algo.run();
                        ArrayList<Label_Dijkstra> covoitDestination = algo.getLabels();

                        for (int i=0; i<covoitPieton.size()||i<covoitVoiture.size(); i++) {
                            // Mise à jour de l'ArrayList covoitSomme :
                            // Choisir le cout (temps) le plus élevé entre :
                            // - celui de la VOITURE vers TOUS
                            // - celui du PIETONS vers TOUS
                            // -> determine le temps minimum
                            // Il y aura un cout INFINY s'il n'y a pas de noeud en commun entre les deux
                            if (covoitVoiture.get(i).getCout() < covoitPieton.get(i).getCout())
                                covoitSomme.add(i, covoitPieton.get(i));
                            else
                                covoitSomme.add(i, covoitVoiture.get(i));

                            // Mise à jour entre : le max des 2 couts entre PIETON et VOITURE plus celui de la DESTINATION :
                            // Si ce nouveau cout est inférieur au maximum PRECEDENT, on
                            //TODO : t'es sur pour ceci ? car on utilise jamais covoitDestination...
                            covoitSomme.get(i).setCout(covoitSomme.get(i).getCout() + covoitDestination.get(i).getCout());
                            if (covoitSomme.get(i).getCout() < Math.max(covoitVoiture.get(i).getCout(), covoitPieton.get(i).getCout()))
                                covoitDestination.set(i, covoitSomme.get(i));

                            // Ici covoitSomme nous donne le coup du noeud i (PIETON inter VOITURE) vers DESTINATION
                            // On garde le minimum
                            if (covoitSomme.get(i).getCout() < min) {
                                min = covoitSomme.get(i).getCout();
                                noeud_rejoint = i;
                            }
                        }

                        // Test si le point de rencontre est trouvé
                        if (noeud_rejoint != -1) {
                            // on trace le point de rencontre
                            node = this.graphe.getArrayList().get(noeud_rejoint);
                            this.graphe.getDessin().drawPoint(node.getLongitude(), node.getLatitude(), 12);
                        }

                        afficherEtEcrireResultats(perfVoitureTous, perfPietonTous, perfDestTous, node, min);

                        break;

                    // Charger un fichier de chemin
                    case 5:
                        // Paramétrer le menu de selection
                        makeControlPanel(51);

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        // On récupère le nom de la carte
                        nomchemin = jComboBoxChemins.getSelectedItem().toString();

                        // Paramétrer le menu de selection
                        makeControlPanel(52);

                        if (graphe.verifierChemin(Openfile.open(nomchemin), nomchemin) == -1)
                            continue;

                        graphe.verifierChemin(Openfile.open(nomchemin), nomchemin);
                        graphe.getChemin().tracerChemin(graphe.getDessin());
                        jTextField1.setText(graphe.getChemin().cout_chemin_distance());
                        jTextField2.setText(graphe.getChemin().cout_chemin_temps());

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        break;

                    // Réinitialiser la map
                    case 6:
                        // Paramétrer le menu de selection
                        makeControlPanel(6);

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        // Récupérer la carte souhaitée
                        nomcarte = jComboBoxCartes.getSelectedItem().toString();
                        mapdata = Openfile.open(nomcarte);

                        // On supprime l'ancienne carte
                        cp.remove(dessinPanel);
                        graphe = null; //Pour detruire l'objet (methode finalize())

                        // Afficher ou non la map -> création d'un dessin associé et ajout à la fenetre
                        dessinPanel = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
                        display = jCheckBox.isSelected();
                        if(display)
                            cp.add(dessinPanel);
                        this.pack();

                        // Création du graphe en fonction de la map selectionnée
                        graphe = new Graphe(nomcarte, mapdata, dessinPanel);

                        break;

                    // Obtenir un numéro de sommet
                    case 7:
                        // Paramétrer le menu de selection
                        makeControlPanel(7);
                        jButtonOk.setEnabled(false);

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
                        jButtonOk.setEnabled(true);
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
     * Initialisation des algorithmes Dijkstra :
     * Pcc Standard, Pcc A-Star et le Programme de test des 2 algos Disjkstra
     * - Connait-on les sommets ORIGINE et ARRIVE
     *  - Oui : on les saisit
     *  - Non : on clique pour les obtenir
     * - Choix du cout : temps ou distance
     * - Choix de l'affichage du déroulement des algorithmes
     */

    public void initialiserAlgo() {

        // On demande à l'utilisateur s'il connait les numéros ou veut cliquer
        if(display)
            sommetsConnus = JOptionPane.showConfirmDialog(null, "Connaissez vous le numéro des sommets", "", JOptionPane.OK_OPTION);
        else
            sommetsConnus = 0;

        switch (sommetsConnus) {
            case JOptionPane.OK_OPTION:
                //Paramétrer le menu de selection
                makeControlPanel(11);
                // Les coordonnées vont automatiquements se mettrent à jours dans les zones de texte après clic

                // On doit cliquer sur OK pour continuer
                waitButtonOk();
                break;
            default:
                // Paramétrer le menu de selection
                makeControlPanel(12);
                try {
                    clickCoord = graphe.situerClick();
                    jTextFieldOrigine.setText(clickCoord.get(1).toString());
                    clickCoord = graphe.situerClick();
                    jTextFieldDest.setText(clickCoord.get(1).toString());
                }
                catch (NumberFormatException n) {
                    System.out.println(n);
                    origine = -1;
                    dest = -1;
                }
                break;
        }
        // Récupérer les valeurs des noeuds
        origine = Integer.parseInt(jTextFieldOrigine.getText());
        dest = Integer.parseInt(jTextFieldDest.getText());

        // Choix du coup en temps ou distance
        if (jRadioButtonChoixDistance.isSelected())
            this.choixCout = 0;
        else this.choixCout = 1;

        // Choix de l'affichage des algo
        if (jCheckBox.isSelected())
            this.affichageDeroulementAlgo = 1;
        else this.affichageDeroulementAlgo = 0;
    }


    /**
     * Initialisation du covoiturage :
     * Pcc Standard, Pcc A-Star et le Programme de test des 2 algos Disjkstra
     * - Connait-on les sommets ORIGINE (conducteur et pieton) et ARRIVE
     *  - Oui : on les saisit
     *  - Non : on clique pour les obtenir
     */

    public void initialiserCovoit() {

        // On demande à l'utilisateur s'il connait les numéros ou veut cliquer
        if(display)
            sommetsConnus = JOptionPane.showConfirmDialog(null, "Connaissez vous le numéro des sommets", "", JOptionPane.OK_OPTION);
        else
            sommetsConnus = 0;

        switch (sommetsConnus) {
            case JOptionPane.OK_OPTION:
                //Paramétrer le menu de selection
                makeControlPanel(21);
                // Les coordonnées vont automatiquements se mettrent à jours dans les zones de texte après clic

                // On doit cliquer sur OK pour continuer
                waitButtonOk();
                break;
            default:
                // Paramétrer le menu de selection
                makeControlPanel(22);
                try {
                    clickCoord = graphe.situerClick();
                    jTextFieldOrigine.setText(clickCoord.get(1).toString());
                    clickCoord = graphe.situerClick();
                    jTextFieldPieton.setText(clickCoord.get(1).toString());
                    clickCoord = graphe.situerClick();
                    jTextFieldDest.setText(clickCoord.get(1).toString());
                }
                catch (NumberFormatException n) {
                    System.out.println(n);
                    origine = -1;
                    dest = -1;
                }
                break;
        }
        // Récupérer les valeurs des noeuds
        origine = Integer.parseInt(jTextFieldOrigine.getText());
        pieton = Integer.parseInt(jTextFieldPieton.getText());
        dest = Integer.parseInt(jTextFieldDest.getText());

        // Choix du coup en temps
        choixCout = 1;

        // Choix de ne pas affichager le déroulement des algo
        affichageDeroulementAlgo = 0;
    }


    /**
     * Affichage du menu de choix :
     * L'utilisateur choisit parmis un menu de selection
     * et clique sur ok pour effectuer l'action associée
     */

    public int afficherMenu() {
        // Paramétrer le menu de selection
        makeControlPanel(0);

        // On attend d'avoir cliqué sur OK
        jButtonOk.setEnabled(true);
        waitButtonOk();
        jButtonOk.setEnabled(false);

        // On retourne le numéro du menu séléctionné
        return jComboBoxMenu.getSelectedIndex();
    }


    /**
     * re-Paramétrage du menu de selection des choix
     */

    public void makeControlPanel(int choice) {
        // On supprimme tous les éléments précédemment placé et mise en place des éléments graphiques
        cp.remove(controlPanel);
        controlPanel.removeAll();
        controlPanel.add(jLabelNames);
        controlPanel.add(jLabelImageINSA);
        controlPanel.add(jLabelTitle);
        controlPanel.revalidate();
        jTextField1.setText("");
        jTextField2.setText("");
        jTextFieldOrigine.setText("");
        jTextFieldDest.setText("");

        // En fonction du paramètre donné
        switch (choice) {
            // Menu de lancement de l'application
            case 0:
                jComboBoxMenu.removeAllItems();
                if(display)
                    for (String menu : menuDisplay)
                        jComboBoxMenu.addItem(menu);
                else
                    for (String menu : menuNotDisplay)
                        jComboBoxMenu.addItem(menu);
                controlPanel.add(Box.createHorizontalStrut(300));
                controlPanel.add(jLabelMenu);
                controlPanel.add(jComboBoxMenu);
                controlPanel.add(jButtonOk);
                break;

            // Utilisé par Pcc Standard, Pcc A-Star et le Programme de test des 2 algos Disjkstra...
            // ... si on ne connait pas les points
            case 11:
                jTextFieldOrigine.setEditable(true);
                jTextFieldDest.setEditable(true);
                jButtonOk.setEnabled(false);
                jTextFieldOrigine.setText("");
                jTextFieldPieton.setText("0"); // Obligé pour afficher le bouton OK
                jTextFieldDest.setText("");
                controlPanel.add(jLabelCoordMan);
                if(display) {
                    controlPanel.add(jLabelDeroulement);
                    controlPanel.add(jCheckBox);
                }
                controlPanel.add(jLabelChoixCout);
                controlPanel.add(jRadioButtonChoixTemps);
                controlPanel.add(jRadioButtonChoixDistance);
                controlPanel.add(jLabelDepart);
                controlPanel.add(jTextFieldOrigine);
                controlPanel.add(jLabelArrivee);
                controlPanel.add(jTextFieldDest);
                controlPanel.add(jButtonOk);
                break;

            // ... si on connait les points
            case 12:
                jTextFieldOrigine.setEditable(false);
                jTextFieldDest.setEditable(false);
                jTextFieldOrigine.setText("");
                jTextFieldPieton.setText("0"); // Obligé pour afficher le bouton OK
                jTextFieldDest.setText("");
                controlPanel.add(jLabelCoordClick);
                if(display) {
                    controlPanel.add(jLabelDeroulement);
                    controlPanel.add(jCheckBox);
                }
                controlPanel.add(jLabelChoixCout);
                controlPanel.add(jRadioButtonChoixTemps);
                controlPanel.add(jRadioButtonChoixDistance);
                controlPanel.add(jLabelDepart);
                controlPanel.add(jTextFieldOrigine);
                controlPanel.add(jLabelArrivee);
                controlPanel.add(jTextFieldDest);
                break;

            // Utilisé par le covoiturage...
            // ... si on ne connait pas les points
            case 21:
                jTextFieldOrigine.setEditable(true);
                jTextFieldPieton.setEditable(true);
                jTextFieldDest.setEditable(true);
                jButtonOk.setEnabled(false);
                jTextFieldOrigine.setText("");
                jTextFieldPieton.setText("");
                jTextFieldDest.setText("");
                controlPanel.add(jLabelCoordMan);
                controlPanel.add(jLabelDepartVoiture);
                controlPanel.add(jTextFieldOrigine);
                controlPanel.add(jLabelDepartPieton);
                controlPanel.add(jTextFieldPieton);
                controlPanel.add(jLabelArrivee);
                controlPanel.add(jTextFieldDest);
                controlPanel.add(jButtonOk);
                break;

            // ... si on connait les points
            case 22:
                jTextFieldOrigine.setEditable(false);
                jTextFieldPieton.setEditable(false);
                jTextFieldDest.setEditable(false);
                jTextFieldOrigine.setText("");
                jTextFieldPieton.setText("");
                jTextFieldDest.setText("");
                controlPanel.add(jLabelCoordClick);
                controlPanel.add(jLabelDepartVoiture);
                controlPanel.add(jTextFieldOrigine);
                controlPanel.add(jLabelDepartPieton);
                controlPanel.add(jTextFieldPieton);
                controlPanel.add(jLabelArrivee);
                controlPanel.add(jTextFieldDest);
                break;

            // Fichier de chemin (avant selection du fichier .path)
            case 51:
                jButtonOk.setEnabled(true);
                jComboBoxChemins.setEnabled(true);
                controlPanel.add(jLabelChemin);
                controlPanel.add(jComboBoxChemins);
                controlPanel.add(jButtonOk);
                break;

            // Fichier de chemin (pour l'affichage des résultats)
            case 52:
                jButtonOk.setEnabled(true);
                jComboBoxChemins.setEnabled(false);
                controlPanel.add(jLabelChemin);
                controlPanel.add(jComboBoxChemins);
                controlPanel.add(jTextField1);
                controlPanel.add(jTextField2);
                controlPanel.add(jButtonOk);
                break;

            // Reinitialiser la carte
            case 6:
                jButtonOk.setEnabled(true);
                controlPanel.add(jLabelFichier);
                controlPanel.add(jComboBoxCartes);
                controlPanel.add(jLabelAfficher);
                controlPanel.add(jCheckBox);
                controlPanel.add(jButtonOk);
                break;

            // Obtenir un numéro de sommet
            case 7:
                jButtonOk.setEnabled(true);
                controlPanel.add(jLabelCoordSitues);
                controlPanel.add(jTextField1);
                controlPanel.add(jLabelNoeudsProches);
                controlPanel.add(jTextField2);
                controlPanel.add(jButtonOk);
                break;
        }
        cp.add(controlPanel, 0);
        this.repaint();
        this.pack();
    }


    /**
     * Ouvre un fichier de sortie pour ecrire les résultats des algorithmes
     */

    public PrintStream fichierSortie() {
        PrintStream result = System.out;

        String nom = jTextFieldFichier.getText();
        if ("".equals(nom))
            nom = "sortie";

        try {
            result = new PrintStream(nom);
            result.println("Fichier créé le : " + new Date() + "\n");
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur ? l'ouverture du fichier" + nom);
            System.exit(1);
        }

        return result;
    }


    /**
     * Ecrire les résultats des algorithmes dans le fichier de sortie
     * Afficher les résultats dans un pop-up
     */

    void afficherEtEcrireResultats(int choix, ArrayList performances) {
        if (choix == 1)
            resultat = "PCC Standard : Dijkstra non guidé\n";
        else
            resultat = "PCC A-Star : Dijkstra guidé\n";
        resultat += "Carte : " + nomcarte + "\n";
        resultat += "Origine : " + origine + "\n";
        resultat += "Arrivée : " + dest + "\n";
        if (performances == null)
            resultat += "Erreur)";
        else {
            cout = (choixCout == 0) ? "distance" : "temps";
            if (choixCout == 0)
                resultat += "Le cout en " + cout + " est de : " + performances.get(0) + "km\n";
            else
                resultat += "Le cout en " + cout + " est de : " + performances.get(0) + "min\n";
            resultat += "Temps de Calcul : " + performances.get(1) + " ms\n";
            resultat += "Nb max d'element : " + performances.get(2) + "\n";
            resultat += "Nb elements explores : " + performances.get(3);
            // Affichage résultats
            JOptionPane.showMessageDialog(null, resultat);
        }
        resultat += "\n\n";
        // On ecrit dans le fichier
        sortie.append(resultat);
    }
    void afficherEtEcrireResultats(ArrayList perf1, ArrayList perf2) {
        resultat = "Programme de test des algorithmes Dijkstra : PCC Standard vs. PCC A-Star\n";
        resultat += "Carte : " + nomcarte + "\n";
        resultat += "Origine : " + origine + "\n";
        resultat += "Arrivée : " + dest + "\n";
        if (perf1 == null || perf2 == null)
            resultat += "Erreur)";
        else {
            cout = (choixCout == 0) ? "distance" : "temps";
            if (choixCout == 0)
                resultat += "Le cout en " + cout + " est de : " + perf1.get(0) + " km - " + perf2.get(0) + " km \n";
            else
                resultat += "Le cout en " + cout + " est de : " + perf1.get(0) + " min - " + perf2.get(0) + " min \n";
            resultat += "Temps de Calcul : " + perf1.get(1) + " ms - " + perf2.get(1) + " ms \n";
            resultat += "Nbr max éléments dans le tas : " + perf1.get(2) + " - " + perf2.get(2) + "\n";
            resultat += "Nombre d'éléments parcourut : " + perf1.get(3) + " - " + perf2.get(3) + "\n\n";
            // Affichage résultats
            JOptionPane.showMessageDialog(null, resultat);
        }
        resultat += "\n\n";
        // On ecrit dans le fichier
        sortie.append(resultat);
    }
    void afficherEtEcrireResultats(ArrayList perfVoitureTous, ArrayList perfPietonTous, ArrayList perfDestTous, Node node, double min) {
        cout = (choixCout == 0) ? "distance" : "temps";
        resultat = "Covoiturage\n";
        resultat += "Carte : " + nomcarte + "\n";
        resultat += "Origine Voiture : " + origine + "\n";
        resultat += "Origine Pieton : " + pieton + "\n";
        resultat += "Arrivée : " + dest + "\n";
        if (perfVoitureTous == null || perfPietonTous == null || perfDestTous == null)
            resultat += "Erreur)";
        else {
            resultat += "On est bien arrivé ! \n" +
                    "Rencontre au noeud  : " + node + "\n" +
                    "Avec pour temps : " + min + " min\n";
            resultat += "Durée exécution : " + perfVoitureTous.get(1) + "+" + perfPietonTous.get(1) + "+" + perfDestTous.get(1) + " ms \n\n";
            // Affichage résultats
            JOptionPane.showMessageDialog(null, resultat);
        }
        resultat += "\n\n";
        // On ecrit dans le fichier
        sortie.append(resultat);
    }


    /**
     * Methods that is called when one of the following textfields is modified
     * We won't be able to clic on the OK button if one of them if empty
     */

    private void textFieldCoordChanged() {
        if(jTextFieldOrigine.getText().equals("") || jTextFieldPieton.getText().equals("") || jTextFieldDest.getText().equals("")) {
            jButtonOk.setEnabled(false);
        }
        else {
            jButtonOk.setEnabled(true);
        }
    }


    /**
     * Oblige l'utilisateur de cliquer sur le button OK pour continuer
     * Le thread est mis en pause est vérifit périodiquement si on a cliqué
     * (permet de ne pas utiliser le processeur continuellement)
     */

    public void waitButtonOk() {
        while (!buttonClicked) {
            try {
                thread.sleep(200);
            }
            catch (InterruptedException e) {
                System.out.println("Error thread sleep");
            }
        }
        buttonClicked = false;
    }


    /**
     * Gestion des évenement des bouttons
     * Si on clique sur :
     * - CHARGER (au lancement) : création d'un thread permettant d'afficher la carte et lancement de l'application
     * - OK : on signal simplement que le boutton a été cliqué (voir fonction waitButtonOk())
     */

    public class BoutonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            //Click sur le boutton Load
            if (evt.getSource() == jButtonLoad) {
                thread = new Thread(new Play());
                thread.start();
            }
            else if (evt.getSource() == jButtonOk) {
                buttonClicked = true;
            }

        }
    }


    /**
     * Au lancement de l'application, après avoir cliqué sur CHARGER :
     * On lance le thread qui gèrera l'affichage de la carte
     */

    class Play implements Runnable {
        public void run() {
            go();
        }
    }

}