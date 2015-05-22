package base;

/*
 * Ce programme propose de lancer divers algorithmes sur les graphes
 * a partir d'un menu graphique
 */


import core.*;
import core.Label;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
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
    static private final String menuDisplay[] = {"Quitter", "Connexite", "PCC Standard", "PCC A-star", "Tester les performances", "Covoiturage",
            "Charger un fichier de chemin", "Reinitialiser la carte", "Situer un sommet"};
    static private final String menuNotDisplay[] = {"Quitter", "Connexite", "PCC Standard", "PCC A-star", "Tester les performances", "Covoiturage",
            "Charger un fichier de chemin", "Reinitialiser la carte"};
    static private final String cartes[] = {"midip", "insa", "france",
            "fractal", "reunion", "newzealand", "morbihan", "mayotte", "paris", "carre-dense", "carre", "fractal-spiral"};
    static private final String chemins[] = {"chemin_insa", "chemin_insa1", "chemin_midip", "chemin_fractal", "chemin_reunion", "chemin_carre-dense", "chemin_spiral",
            "chemin_spiral2"};

    // Declaration de Variables lié à l'affichage graphique
    private JPanel		    controlPanel;               // Contient le menu de selection des choix
    private Container 	    cp;                         // Conteneur de la fenetre, on y ajoute les deux précédents éléments
    private JLabel                                      // Texte à afficher
            jLabelNames, jLabelTitle, jLabelCarte, jLabelAfficher, jLabelFichier, jLabelMenu,
            jLabelDeroulement, jLabelChoixCout, jLabelDepart, jLabelDepartVoiture, jLabelAffChemin,
            jLabelDepartPieton, jLabelArrivee, jLabelCoordsMan, jLabelCoordsClick,
            jLabelCoordClick, jLabelCoordSitues, jLabelNoeudsProches, jLabelChemin;
    private JLabel          jLabelImageGraphe;          // Image de lancement (graphe) à afficher
    private JLabel          jLabelImageINSA;            // Logo de l'INSA à afficher
    private JTextField      jTextFieldFichier;          // Zone de saisie du fichier
    private JTextField      jTextField1;                // Zone de saisie n°1
    private JTextField      jTextField2;                // Zone de saisie n°2
    private JTextField      jTextFieldOrigine;          // Zone de saisie pour l'origine (voiture si covoiturage)
    private JTextField      jTextFieldPieton;           // Zone de saisie pour l'origine du pieton
    private JTextField      jTextFieldDest;             // Zone de saisie pour la destination
    private JCheckBox       jCheckBox;                  // Un checkbox (affichage graphique ou du déroulement d'execution d'algo)
    private JRadioButton    jRadioButtonChoixTemps;     // CHoix du cout en temps
    private JRadioButton    jRadioButtonChoixDistance;  // CHoix du cout en distance
    private JComboBox	    jComboBoxMenu;              // Contient les menus
    private JComboBox	    jComboBoxCartes;            // Contient les cartes
    private JComboBox       jComboBoxChemins;          // Contient les chemins
    private JButton		    jButtonOk;                  // Button ok (lancement de l'appli, chagement de menu, attente de lecture des coord du clic)
    private JButton		    jButtonLoad;                // Button charger (lancement de l'appli)
    private Thread          thread;                     // Utilisé pour afficher la map en parallèle du menu de selection des choix

    // Declaration de Variables lié à l'execution du programme
    private String          nomCarte;                   // Nom de la carte à charger
    private String          nomChemin;                  // Nom du chemin à charger
    private String          resultat;                   // Contient les resultats des algos
    private PrintStream     sortie;                     // Fichier de sortie
    private Graphe          graphe;                     // La map
    private boolean         display;                    // Affichage graphique ou non
    private boolean         buttonClicked = false;      // Un bouton a été cliqué ou non
    private boolean         continuer = true;           // Boucle principale : le menu est accessible jusqu'a ce que l'on quitte.
    private ArrayList       clickCoord;                 // Pour avoir coordonnées d'un clic
    private int             sommetsConnus;              // L'utilisateur connait les sommets origine et dest ou non
    private int             choixCout;                  // Plus court en Distance:0 ou Temps:1
    private boolean         affichageDeroulementAlgo;   // Affichage des algorithmes ou non
    private boolean         affichageChemin;            // Affichage des chemins ou non (covoit)
    private int             origine, pieton, dest;      // Numéro des sommets origine, pieton et dest
    private Algo            algo;                       // Algorithme a executer


    /**
     * Default constructor
     */

    public Launch() {
        Dimension halfDimension = new Dimension(180, 25);
        Dimension fullDimension = new Dimension(360, 25);

        // Paramétrage des textes à afficher
        jLabelTitle         = new JLabel("<html><br>PROGRAMME DE TESTS DES ALGORITHMES DE GRAPHE<br><br></html>");
        jLabelCarte         = new JLabel("Fichier .map à utiliser : ");
        jLabelAfficher      = new JLabel("Afficher la carte : ");
        jLabelFichier       = new JLabel("Fichier de sortie : ");
        jLabelMenu          = new JLabel("Que voulez-vous faire : ");
        jLabelDeroulement   = new JLabel("Afficher le deroulement : ");
        jLabelAffChemin     = new JLabel("<html>Afficher les chemins<br>(Cela est plus long) :</html>");
        jLabelChoixCout     = new JLabel("Choix du coup : ");
        jLabelDepart        = new JLabel("Départ : ");
        jLabelDepartVoiture = new JLabel("Départ du conducteur : ");
        jLabelDepartPieton  = new JLabel("Départ du piéton : ");
        jLabelArrivee       = new JLabel("Arrivée : ");
        jLabelCoordsMan     = new JLabel("Saisir les noeuds manuellement");
        jLabelCoordClick    = new JLabel("Obtenir le noeud en cliquant");
        jLabelCoordsClick   = new JLabel("Obtenir les noeuds en cliquant");
        jLabelCoordSitues   = new JLabel("Clic aux coordonnées : ");
        jLabelNoeudsProches = new JLabel("Noeud le plus proche : ");
        jLabelNames         = new JLabel("<html>&nbsp;BUREAU D'ETUDE GRAPHE 3MIC-IR<br>" +
                                         "&nbsp;Etudiants : J. Mangel - V. Chatelard<br>" +
                                         "&nbsp;Enseignants : D. Le Botlan - M-J Huguet</html>");
        jLabelChemin        = new JLabel("Chemin .path à utiliser : ");

        jLabelTitle.setPreferredSize(fullDimension);
        jLabelCarte.setPreferredSize(halfDimension);
        jLabelAfficher.setPreferredSize(halfDimension);
        jLabelFichier.setPreferredSize(halfDimension);
        jLabelMenu.setPreferredSize(halfDimension);
        jLabelDeroulement.setPreferredSize(halfDimension);
        jLabelAffChemin.setPreferredSize(new Dimension(180, 40));
        jLabelChoixCout.setPreferredSize(halfDimension);
        jLabelDepart.setPreferredSize(halfDimension);
        jLabelDepartVoiture.setPreferredSize(halfDimension);
        jLabelDepartPieton.setPreferredSize(halfDimension);
        jLabelArrivee.setPreferredSize(halfDimension);
        jLabelCoordsMan.setPreferredSize(fullDimension);
        jLabelCoordClick.setPreferredSize(fullDimension);
        jLabelCoordsClick.setPreferredSize(fullDimension);
        jLabelCoordSitues.setPreferredSize(halfDimension);
        jLabelNoeudsProches.setPreferredSize(halfDimension);
        jLabelNames.setPreferredSize(new Dimension(275, 83));
        jLabelNames.setOpaque(true);
        jLabelNames.setBackground(Color.white);
        jLabelChemin.setPreferredSize(halfDimension);

        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelCoordsMan.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelCoordClick.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelCoordsClick.setHorizontalAlignment(SwingConstants.CENTER);

        // Paramétrage des zone de saisie
        jTextField1         = new JTextField();
        jTextField2         = new JTextField();
        jTextFieldOrigine   = new JTextField();
        jTextFieldPieton    = new JTextField();
        jTextFieldDest      = new JTextField();
        jTextFieldFichier   = new JTextField("sortie");

        jTextField1.setPreferredSize(halfDimension);
        jTextField2.setPreferredSize(halfDimension);
        jTextFieldFichier.setPreferredSize(halfDimension);
        jTextFieldOrigine.setPreferredSize(halfDimension);
        jTextFieldPieton.setPreferredSize(halfDimension);
        jTextFieldDest.setPreferredSize(halfDimension);
        jTextField1.setEditable(false);
        jTextField2.setEditable(false);
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
        jCheckBox.setPreferredSize(halfDimension);
        jCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

        // Paramétrage des radiobuttons
        jRadioButtonChoixTemps    = new JRadioButton("Temps");
        jRadioButtonChoixDistance = new JRadioButton("Distance");
        jRadioButtonChoixTemps.setPreferredSize(new Dimension(90, 25));
        jRadioButtonChoixDistance.setPreferredSize(new Dimension(90, 25));
        jRadioButtonChoixTemps.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButtonChoixTemps);
        buttonGroup.add(jRadioButtonChoixDistance);

        // Paramétrage des menus de selection (cartes et menus)
        jComboBoxMenu    = new JComboBox();
        jComboBoxCartes  = new JComboBox();
        jComboBoxChemins = new JComboBox();
        jComboBoxMenu.setPreferredSize(halfDimension);
        jComboBoxCartes.setPreferredSize(halfDimension);
        for (String carte : cartes)
            jComboBoxCartes.addItem(carte);
        jComboBoxChemins.setPreferredSize(halfDimension);
        for (String chemin : chemins)
            jComboBoxChemins.addItem(chemin);

        // Paramétrage des images : graphe et logo INSA
        ImageIcon imageGraphe = new ImageIcon("arbre.jpg");
        ImageIcon imageINSA = new ImageIcon("logoINSA.png");
        jLabelImageGraphe = new JLabel();
        jLabelImageINSA   = new JLabel();
        jLabelImageGraphe.setIcon(imageGraphe);
        jLabelImageINSA.setIcon(imageINSA);
        jLabelImageGraphe.setBorder(new EmptyBorder(0,0,25,0));

        // Paramétrage des buttons
        jButtonLoad = new JButton("CHARGER");
        jButtonLoad.setPreferredSize(new Dimension(120, 30));
        jButtonLoad.setBackground(new Color(235, 235, 235));
        jButtonLoad.addActionListener(new BoutonListener());
        jButtonOk = new JButton("OK");
        jButtonOk.setPreferredSize(new Dimension(120, 30));
        jButtonOk.setBackground(new Color(235, 235, 235));
        jButtonOk.addActionListener(new BoutonListener());

        // Paramétrage du menu de selection des choix avec ajout des composants
        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(380, 600));
        controlPanel.setBorder(new javax.swing.border.BevelBorder(BevelBorder.RAISED));
        controlPanel.add(jLabelNames);
        controlPanel.add(jLabelImageINSA);
        controlPanel.add(jLabelTitle);
        controlPanel.add(jLabelImageGraphe);
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
        this.setLocationRelativeTo(null);
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
            nomCarte = jComboBoxCartes.getSelectedItem().toString();
            DataInputStream mapdata = Openfile.open(nomCarte);

            // Afficher ou non la map -> création d'un dessin associé et ajout à la fenetre
            display = jCheckBox.isSelected();
            Dessin dessinPanel = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
            if(display)
                cp.add(dessinPanel);
            this.pack();
            this.setLocationRelativeTo(null);

            // Création du graphe en fonction de la map selectionnée
            graphe = new Graphe(nomCarte, mapdata, dessinPanel);

            // Ouverture et initialisation du fichier de sortie contenant des resultats
            sortie = fichierSortie();

            // Boucle principale : choix de la tache et execution
            while (continuer) {

                // Affichage du menu : le choix correspond au numero du menu choisi.
                int choixMenu = afficherMenu();

                // On test ce que le menu choisi
                switch (choixMenu) {

                    // Quitter l'application
                    case 0:
                        continuer = false;
                        break;

                    // Connexité
                    case 1:
                        // Initialisation et lancement de l'algorithme
                        initialiserAlgo();
                        algo = new Connexite(graphe, origine, dest, affichageDeroulementAlgo);
                        ArrayList perfConnexite = algo.run();
                        afficherEtEcrireResultats(3, perfConnexite);
                        break;

                    // PCC Standard : Dijkstra
                    case 2:
                        // Initialisation et lancement de l'algorithme
                        initialiserAlgo();
                        algo = new Pcc_Dijkstra(graphe, origine, dest, affichageDeroulementAlgo, choixCout, false, false, true);
                        ArrayList perfStandard = algo.run();
                        afficherEtEcrireResultats(1, perfStandard);
                        break;

                    // PCC A-Star : Dijkstra guidé
                    case 3:

                        //Initialisation et lancement de l'algorithme
                        initialiserAlgo();
                        algo = new Pcc_Star(graphe, origine, dest, affichageDeroulementAlgo, choixCout, false, false, true);
                        ArrayList perfAStar = algo.run();
                        afficherEtEcrireResultats(2, perfAStar);
                        break;

                    // Programme de test algo connexité et des 2 algos Dijkstra : PCC Standard + PCC A-Star
                    case 4:
                        // Initialisation des algorithmes
                        initialiserAlgo();

                        // 1i algo -> Connexité
                        algo = new Connexite(graphe, origine, dest, affichageDeroulementAlgo);
                        // Lancement des algorithmes et récupération des résultats
                        graphe.getDessin().setColor(Color.cyan);
                        ArrayList perf3 = algo.run();

                        // 2i algo -> PCC Standard : Dijkstra
                        algo = new Pcc_Dijkstra(graphe, origine, dest, affichageDeroulementAlgo, choixCout, false, false, true);
                        // Lancement des algorithmes et récupération des résultats
                        graphe.getDessin().setColor(Color.magenta);
                        ArrayList perf1 = algo.run();

                        // 3i algo -> PCC A-Star : Dijkstra guidé
                        algo = new Pcc_Star(graphe, origine, dest, affichageDeroulementAlgo, choixCout, false, false, true);
                        graphe.getDessin().setColor(Color.red);
                        ArrayList perf2 = algo.run();

                        afficherEtEcrireResultats(perf1, perf2, perf3);
                        break;

                    // Covoiturage
                    case 5:
                        // ArrayList contenant les couts màj
                        ArrayList<Label> covoitSomme = new ArrayList<>();
                        ArrayList<String> perfVoitureTous, perfPietonTous, perfDestTous, perf;
                        int noeud_rejoint = -1;
                        double min = Double.POSITIVE_INFINITY;
                        Node node = null;
                        double dureeExe; // Durée d'execution
                        double minPieton, minVoiture; // Temps min vers point de rencontre
                        ArrayList<String> durees = new ArrayList<>();
                        ArrayList<Boolean> seul = new ArrayList<>();
                        boolean seul1;
                        boolean pasRencontre = false;

                        // ICI ON A DEUX CHOIX : Soit on lance normalement (3disjktras) sans affichages
                        // Soit on en lance 6 pour les tracer

//TODO : si pieton et dest =, ca marche pas

                        // Initialisation des algorithmes : cout en TEMPS !
                        initialiserCovoit();

                        // Mesurer le temps d'execution de l'algorithme
                        dureeExe = System.currentTimeMillis();

                        // Lancement des algorithmes

                        // PCC de la VOITURE vers TOUS : récupération de l'arraylist des couts
                        algo = new Pcc_Dijkstra(graphe, origine, dest, affichageDeroulementAlgo, choixCout, true, false, false);
                        perfVoitureTous = algo.run();
                        ArrayList<Label> covoitVoiture = algo.getLabels();
                        // PCC du PIETON vers TOUS : màj de l'arraylist s'il est plus grand
                        algo = new Pcc_Dijkstra(graphe, pieton, dest, affichageDeroulementAlgo, choixCout, true, true, false);
                        perfPietonTous = algo.run();
                        ArrayList<Label> covoitPieton = algo.getLabels();

                        // PCC de la DESTINATION vers TOUS : màj de l'arraylist si max (x,y) > Pcc(dest, noeud) + Pcc( (x ou y) vers noeuds )
                        algo = new Pcc_Dijkstra(graphe, dest, pieton, affichageDeroulementAlgo, choixCout, true, false, false);
                        perfDestTous = algo.run();
                        ArrayList<Label> covoitDestination = algo.getLabels();

                        // On continue si les points saisis existent
                        if (perfVoitureTous != null && perfPietonTous != null && perfDestTous != null) {

                            // Ici on a récupérer les temps de origine vers tous - dest vers tous - et pieton vers tous
                            // Mise à jour de l'ArrayList covoitSomme :
                            // Choisir le cout (temps) le plus élevé entre :
                            // - celui de la VOITURE vers TOUS
                            // - celui du PIETONS vers TOUS
                            // -> determine le temps minimum pour se rejoindre
                            // Il y aura un cout INFINY s'il n'y a pas de noeud en commun entre les deux

                            for (int i = 0; i < covoitVoiture.size(); i++) {
                                // TODO : noter le temps d'attente final via la différence des 2 (mettre ca ds un arrayList)
                                // On prend le max des deux pour avoir le temps minimum qu'il faut pour se rejoindre
                                if (covoitVoiture.get(i).getCout() < covoitPieton.get(i).getCout())
                                    covoitSomme.add(i, covoitPieton.get(i));
                                else
                                    covoitSomme.add(i, covoitVoiture.get(i));
// C'est ici qu'on peut récupérer le temps d'attente !

                                // On ajoute le temps qu'il faut depuis ce point de ralliement vers la dest
                                covoitSomme.get(i).setCout(covoitSomme.get(i).getCout() + covoitDestination.get(i).getCout());

                                // si ce temps est > au temps qu'aurait mis les deux alors ils y vont directs
                                if (covoitSomme.get(i).getCout() > Math.max(covoitVoiture.get(dest).getCout(), covoitPieton.get(dest).getCout())) {
                                    // Ici ca veut dire qu'ils y vont chacun pour soit !
                                    if (covoitVoiture.get(dest).getCout() > covoitPieton.get(dest).getCout())
                                        covoitSomme.set(i, covoitVoiture.get(dest));
                                    else
                                        covoitSomme.set(i, covoitPieton.get(dest));
                                    seul.add(i, true);
                                }
                                else
                                    seul.add(i, false);

                                //mise à jour du cout minimum
                                if (covoitSomme.get(i).getCout() < min) {
                                    min = covoitSomme.get(i).getCout();
                                    noeud_rejoint = i;
                                }
                            }

                            if (noeud_rejoint != -1) {
                                System.out.println("On se rejoins au noeud : " + covoitSomme.get(noeud_rejoint));
                                if (affichageChemin) {
                                    // Ca signifie qu'on veut tracer les 3 chemins
                                    // TODO : on récupère et on trace les chemins sans relancer Pcc !
                                    if (seul.get(noeud_rejoint)) {
                                        // Cela signifie que chacun y va tout seul
                                        algo = new Pcc_Dijkstra(graphe, origine, dest, affichageDeroulementAlgo, choixCout, false, false, true);
                                        perf = algo.run();
                                        durees.add(perf.get(2));
                                        algo = new Pcc_Dijkstra(graphe, pieton, dest, affichageDeroulementAlgo, choixCout, false, true, true);
                                        perf = algo.run();
                                        durees.add(perf.get(2));
                                        // TODO : afficher les résultats des 2 dans la même fenêtre
                                    } else {
                                        // Ici on doit faire rejoindre les deux puis jusqu'à la fin
                                        algo = new Pcc_Star(graphe, origine, noeud_rejoint, affichageDeroulementAlgo, choixCout, false, false, true);
                                        perf = algo.run();
                                        durees.add(perf.get(2));
                                        minVoiture = algo.getCoutMinTemps();
                                        algo = new Pcc_Star(graphe, pieton, noeud_rejoint, affichageDeroulementAlgo, choixCout, false, true, true);
                                        perf = algo.run();
                                        durees.add(perf.get(2));
                                        minPieton = algo.getCoutMinTemps();
                                        if(minVoiture > minPieton) {
                                            durees.add("Pas d'attente");
                                            durees.add(algo.AffichageTempsHeureMin(minVoiture - minPieton));
                                        }
                                        else {
                                            durees.add(algo.AffichageTempsHeureMin(minPieton - minVoiture));
                                            durees.add("Pas d'attente");
                                        }
                                        algo = new Pcc_Star(graphe, noeud_rejoint, dest, affichageDeroulementAlgo, choixCout, false, false, true);
                                        perf = algo.run();
                                        durees.add(perf.get(2));
                                    }
                                }
                            } else {
                                System.out.println("Impossible de se rejoindre.");
                            }

                            // Test si le point de rencontre est trouvé
                            if (noeud_rejoint != -1 && display && !seul.get(noeud_rejoint)) {
                                // on trace le point de rencontre
                                node = graphe.getArrayList().get(noeud_rejoint);
                                graphe.getDessin().setColor(Color.magenta);
                                graphe.getDessin().drawPoint(node.getLongitude(), node.getLatitude(), 12);
                            }
                        }

                        // On enregistre le temps d'execution de l'algorithme
                        dureeExe = (System.currentTimeMillis() - dureeExe);


                        if (noeud_rejoint != -1)
                            seul1 = seul.get(noeud_rejoint);
                        else {
                            seul1 = false;
                            pasRencontre = true;
                        }

                        afficherEtEcrireResultats(perfVoitureTous, perfPietonTous, perfDestTous, node, min, dureeExe, durees, seul1, pasRencontre);

                        break;

                    // Charger un fichier de chemin
                    case 6:
                        // Paramétrer le menu de selection
                        makeControlPanel(51);

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        // On récupère le nom de la carte
                        nomChemin = jComboBoxChemins.getSelectedItem().toString();

                        // Paramétrer le menu de selection
                        makeControlPanel(52);

                        if (graphe.verifierChemin(Openfile.open(nomChemin), nomChemin) == -1)
                            continue;

                        graphe.verifierChemin(Openfile.open(nomChemin), nomChemin);
                        graphe.getChemin().tracerChemin(graphe.getDessin());
                        jTextField1.setText(graphe.getChemin().Calculer_cout_chemin_distance());
                        jTextField2.setText(graphe.getChemin().Calculer_cout_chemin_temps());

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        break;

                    // Réinitialiser la map
                    case 7:
                        // Paramétrer le menu de selection
                        makeControlPanel(6);

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        // Récupérer la carte souhaitée
                        nomCarte = jComboBoxCartes.getSelectedItem().toString();
                        mapdata = Openfile.open(nomCarte);

                        // On supprime l'ancienne carte
                        cp.remove(dessinPanel);
                        graphe = null; //Pour detruire l'objet (methode finalize())

                        // Afficher ou non la map -> création d'un dessin associé et ajout à la fenetre
                        display = jCheckBox.isSelected();
                        dessinPanel = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
                        if(display)
                            cp.add(dessinPanel);
                        this.pack();
                        this.setLocationRelativeTo(null);

                        // Création du graphe en fonction de la map selectionnée
                        graphe = new Graphe(nomCarte, mapdata, dessinPanel);

                        break;

                    // Obtenir un numéro de sommet
                    case 8:
                        // Paramétrer le menu de selection
                        makeControlPanel(8);
                        jButtonOk.setEnabled(false);

                        // On récupère les informations du click
                        clickCoord = graphe.situerClick();

                        // On vérifie que l'on a bien récupéré les informations du click
                        if(clickCoord == null) {
                            jTextField1.setText("Le clic n'a rien retourné");
                            jTextField2.setText("Le clic n'a rien retourné");
                        }
                        else {
                            // On affiche les information
                            jTextField1.setText("Lon = " + clickCoord.get(0).toString() + " - Lat = " + clickCoord.get(1).toString());
                            jTextField2.setText(clickCoord.get(2).toString());
                        }

                        // On doit cliquer sur OK pour continuer
                        jButtonOk.setEnabled(true);
                        waitButtonOk();

                        break;

                    default:
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
                clickCoord = graphe.situerClick();
                jTextFieldOrigine.setText(clickCoord.get(2).toString());
                clickCoord = graphe.situerClick();
                jTextFieldDest.setText(clickCoord.get(2).toString());
                break;
        }
        // Récupérer les valeurs des noeuds
        try {
            origine = Integer.parseInt(jTextFieldOrigine.getText());
            dest = Integer.parseInt(jTextFieldDest.getText());
        }
        catch (NumberFormatException n) {
            System.out.println(n);
            origine = -1;
            dest = -1;
        }

        // Choix du coup en temps ou distance
        if (jRadioButtonChoixDistance.isSelected())
            choixCout = 0;
        else choixCout = 1;

        // Choix de l'affichage des algo
        if (jCheckBox.isSelected())
            affichageDeroulementAlgo = true;
        else affichageDeroulementAlgo = false;
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
                clickCoord = graphe.situerClick();
                jTextFieldOrigine.setText(clickCoord.get(2).toString());
                clickCoord = graphe.situerClick();
                jTextFieldPieton.setText(clickCoord.get(2).toString());
                clickCoord = graphe.situerClick();
                jTextFieldDest.setText(clickCoord.get(2).toString());
                break;
        }
        // Récupérer les valeurs des noeuds
        try {
            origine = Integer.parseInt(jTextFieldOrigine.getText());
            pieton = Integer.parseInt(jTextFieldPieton.getText());
            dest = Integer.parseInt(jTextFieldDest.getText());
        }
        catch (NumberFormatException n) {
            System.out.println(n);
            origine = -1;
            dest = -1;
        }

        // Choix du coup en temps
        choixCout = 1;

        // Pas d'affichage des algo
        affichageDeroulementAlgo = false;

        // Choix de l'affichage des chemin
        if (jCheckBox.isSelected())
            affichageChemin = true;
        else affichageChemin = false;
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
                controlPanel.add(jLabelCoordsMan);
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
                controlPanel.add(jLabelCoordsClick);
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
                controlPanel.add(jLabelCoordsMan);
                controlPanel.add(jLabelAffChemin);
                controlPanel.add(jCheckBox);
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
                controlPanel.add(jLabelCoordsClick);
                controlPanel.add(jLabelAffChemin);
                controlPanel.add(jCheckBox);
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
            case 8:

                jButtonOk.setEnabled(true);
                controlPanel.add(jLabelCoordClick);
                controlPanel.add(jLabelCoordSitues);
                controlPanel.add(jTextField1);
                controlPanel.add(jLabelNoeudsProches);
                controlPanel.add(jTextField2);
                controlPanel.add(jButtonOk);
                break;
            default:
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

    void afficherEtEcrireResultats(int choixAlgo, ArrayList performances) {
        switch (choixAlgo) {
            case 1:
                resultat = "PCC Standard : Dijkstra non guidé\n";
                break;
            case 2:
                resultat = "PCC A-Star : Dijkstra guidé\n";
                break;
            case 3:
                resultat = "Connexité\n";
                break;
            default:
                break;
        }
        resultat += "Carte : " + nomCarte + "\n";
        resultat += "Origine : " + origine + "\n";
        resultat += "Arrivée : " + dest + "\n";
        if (performances == null)
            resultat += "Erreur - Un des sommets saisis n'appartient pas au graphe";
        else {
            resultat += "Connexité : " + performances.get(0) + "\n";
            resultat += "Temps de Calcul : " + performances.get(1) + "\n";
            if(choixAlgo != 3) {
                if (choixCout == 0) resultat += "Le cout en distance est de : " + performances.get(2) + "\n";
                else                resultat += "Le cout en temps est de : " + performances.get(2) + "\n";
                resultat += "Nb max d'element : " + performances.get(3) + "\n";
                resultat += "Nb elements explorés : " + performances.get(4) + "\n";

            }
        }
        JOptionPane.showMessageDialog(null, resultat); // On affiche le resultats en popup
        sortie.append(resultat + "\n\n\n"); // On ecrit dans le fichier
    }

    void afficherEtEcrireResultats(ArrayList perf1, ArrayList perf2, ArrayList perf3) {
        resultat = "           Programme de test des algorithmes Dijkstra :\n";
        resultat += "Carte : " + nomCarte + "\n";
        resultat += "Origine : " + origine + "\n";
        resultat += "Arrivée : " + dest + "\n\n";
        if (perf1 == null || perf2 == null)
            resultat += "Erreur - Un des sommets saisis n'appartient pas au graphe";
        else {
            resultat += " - PCC Standard vs. PCC A-Star vs. Connexité :\n";
            resultat += "Connexité : " + perf1.get(0) + " - " + perf2.get(0) + " - " + perf3.get(0) + "\n";
            resultat += "Temps de Calcul : " + perf1.get(1) + " - " + perf2.get(1) + " - " + perf3.get(1) + "\n\n";
            resultat += " - PCC Standard vs. PCC A-Star :\n";
            if (choixCout == 0) resultat += "Le cout en distance est de : " + perf1.get(2) + " - " + perf2.get(2) + "\n";
            else                resultat += "Le cout en temps est de : " + perf1.get(2) + " - " + perf2.get(2) + "\n";
            resultat += "Nb max d'element : " + perf1.get(3) + " - " + perf2.get(3) + "\n";
            resultat += "Nb elements explorés : " + perf1.get(4) + " - " + perf2.get(4) + "\n";
        }
        JOptionPane.showMessageDialog(null, resultat); // On affiche le resultats en popup
        sortie.append(resultat + "\n\n\n"); // On ecrit dans le fichier
    }

    void afficherEtEcrireResultats(ArrayList perfVoitureTous, ArrayList perfPietonTous, ArrayList perfDestTous, Node node, double min, double dureeExe, ArrayList<String> durees, boolean isSeul, boolean pasRencontre) {
        resultat = "Covoiturage\n";
        resultat += "Carte : " + nomCarte + "\n";
        resultat += "Origine Voiture : " + origine + "\n";
        resultat += "Origine Pieton : " + pieton + "\n";
        resultat += "Arrivée : " + dest + "\n\n";
        if (perfVoitureTous == null || perfPietonTous == null || perfDestTous == null)
            resultat += "Erreur - Un des sommets saisis n'appartient pas au graphe";
        else {
            if (min == Double.POSITIVE_INFINITY || pasRencontre)
                resultat += "Aucun noeud de rencontre trouvé ! \n";
            else {
                resultat += "On est bien arrivé ! \n";
                if (isSeul) {
                    resultat += "Le conducteur et le pieton se rendent chacun seuls à la destination\n\n";
                    if (affichageChemin) {
                        resultat += "Temps de la voiture vers la destination : " + durees.get(0) + "\n";
                        resultat += "Temps du pieton vers la destination : " + durees.get(1) + "\n";
                    }
                }
                else {
                    resultat += "Le conducteur et le pieton se rendent ensemble à la destination\n";
                    resultat += "Rencontre au noeud  : " + node.getNum() + "\n\n";
                    if (affichageChemin) {
                        resultat += "Temps de la voiture vers le point rencontre : " + durees.get(0) + "\n";
                        resultat += "Temps du pieton vers le point rencontre : " + durees.get(1) + "\n";
                        resultat += "Temps d'attente de la voiture : " + durees.get(2) + "\n";
                        resultat += "Temps d'attente du pieton : " + durees.get(3) + "\n";
                        resultat += "Temps du point de rencontre vers la destination : " + durees.get(4) + "\n";
                    }
                }
                // On donne au moins le temps final pour aller jusqu'au noeud
                resultat += "Temps total : " + algo.AffichageTempsHeureMin(min) + "\n";
            }
            resultat += "\nDurée exécution : " + algo.AffichageTempsCalcul(dureeExe) + "\n\n";
        }
        JOptionPane.showMessageDialog(null, resultat); // On affiche le resultats en popup
        sortie.append(resultat + "\n\n\n"); // On ecrit dans le fichier
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
                System.out.println("Error during thread sleep : " + e);
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