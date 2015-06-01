package base;

/*
 * Ce programme propose de lancer divers algorithmes sur les graphes
 * a partir d'un menu graphique
 */

//TODO : thread qui plante en fin de prog : AWT - EvenQueue-0

import core.*;
import core.Label;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
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
    private JPanel controlPanel;               // Contient le menu de selection des choix
    private Container cp;                         // Conteneur de la fenetre, on y ajoute les deux précédents éléments
    private JLabel space;
    private JLabel jLabelNames;
    private JLabel jLabelTitle;
    private JLabel jLabelCarte;
    private JLabel jLabelAfficher;
    private JLabel jLabelMenu;
    private JLabel jLabelDeroulement;
    private JLabel jLabelChoixCout;
    private JLabel jLabelDepart;
    private JLabel jLabelDepartVoiture;
    private JLabel jLabelDepartPieton;
    private JLabel jLabelArrivee;
    private JLabel jLabelCoordsMan;
    private JLabel jLabelCoordsClick;
    private JLabel jLabelTempsMax;
    private JLabel jLabelCoordClick;
    private JLabel jLabelCoordSitues;
    private JLabel jLabelNoeudsProches;
    private JLabel jLabelChemin;
    private JLabel jLabelImageINSA;            // Logo de l'INSA à afficher
    private JTextField jTextFieldFichier;          // Zone de saisie du fichier
    private Log log;                // Pour le log
    private JTextField jTextField1;                // Zone de saisie n°1
    private JTextField jTextField2;                // Zone de saisie n°2
    private JTextField jTextFieldOrigine;          // Zone de saisie pour l'origine (voiture si covoiturage)
    private JTextField jTextFieldPieton;           // Zone de saisie pour l'origine du pieton
    private JTextField jTextFieldDest;             // Zone de saisie pour la destination
    private JCheckBox jCheckBox;                  // Un checkbox (affichage graphique ou du déroulement d'execution d'algo)
    private JRadioButton jRadioButtonChoixTemps;     // CHoix du cout en temps
    private JRadioButton jRadioButtonChoixDistance;  // CHoix du cout en distance
    private JComboBox<String> jComboBoxMenu;              // Contient les menus
    private JComboBox<String> jComboBoxCartes;            // Contient les cartes
    private JComboBox<String> jComboBoxChemins;          // Contient les chemins
    private JSpinner jSpinnerTempsMax;
    private JButton jButtonOk;                  // Button ok (lancement de l'appli, chagement de menu, attente de lecture des coord du clic)
    private JButton jButtonLoad;                // Button charger (lancement de l'appli)

    // Declaration de Variables lié à l'execution du programme
    private String nomCarte;                   // Nom de la carte à charger
    private String resultat;                   // Contient les resultats des algos
    private PrintStream sortie;                     // Fichier de sortie
    private Graphe graphe;                     // La map
    private boolean display;                    // Affichage graphique ou non
    private boolean buttonClicked = false;      // Un bouton a été cliqué ou non
    private boolean continuer = true;           // Boucle principale : le menu est accessible jusqu'a ce que l'on quitte.
    private ArrayList clickCoord;                 // Pour avoir coordonnées d'un clic
    private int sommetsConnus;              // L'utilisateur connait les sommets origine et dest ou non
    private int choixCout;                  // Plus court en Distance:0 ou Temps:1
    private double tempsAttenteMaxPieton;      // Temps maximum d'attente du piéton pour covoiturage
    private boolean affichageDeroulementAlgo;   // Affichage des algorithmes ou non
    private boolean affichageChemin;            // Affichage des chemins ou non (covoit)
    private int origine, pieton, dest;      // Numéro des sommets origine, pieton et dest

    /**
     * Default constructor
     */
    public Launch() {
        Dimension halfDimension = new Dimension(190, 25);
        Dimension fullDimension = new Dimension(380, 25);

        // Paramétrage des textes à afficher
        space = new JLabel();
        jLabelTitle = new JLabel("<html><br>PROGRAMME DE TESTS DES ALGORITHMES DE GRAPHE<br><br></html>");
        jLabelCarte = new JLabel("Fichier .map à utiliser : ");
        jLabelAfficher = new JLabel("Afficher la carte : ");
        JLabel jLabelFichier = new JLabel("Fichier de sortie : ");
        jLabelMenu = new JLabel("Que voulez-vous faire : ");
        jLabelDeroulement = new JLabel("Afficher le deroulement : ");
        jLabelTempsMax = new JLabel("<html>Temps min d'attente<br>du piéton (max 2h) :</html>");
        jLabelChoixCout = new JLabel("Choix du coup : ");
        jLabelDepart = new JLabel("Départ : ");
        jLabelDepartVoiture = new JLabel("Départ du conducteur : ");
        jLabelDepartPieton = new JLabel("Départ du piéton : ");
        jLabelArrivee = new JLabel("Arrivée : ");
        jLabelCoordsMan = new JLabel("Saisir les noeuds manuellement");
        jLabelCoordClick = new JLabel("Obtenir le noeud en cliquant");
        jLabelCoordsClick = new JLabel("Obtenir les noeuds en cliquant");
        jLabelCoordSitues = new JLabel("Clic aux coordonnées : ");
        jLabelNoeudsProches = new JLabel("Noeud le plus proche : ");
        jLabelNames = new JLabel("<html>&nbsp;BUREAU D'ETUDE GRAPHE 3MIC-IR<br>" +
                "&nbsp;Etudiants : J. Mangel - V. Chatelard<br>" +
                "&nbsp;Enseignants : D. Le Botlan - M-J Huguet</html>");
        jLabelChemin = new JLabel("Chemin .path à utiliser : ");

        space.setPreferredSize(fullDimension);
        jLabelTitle.setPreferredSize(fullDimension);
        jLabelCarte.setPreferredSize(halfDimension);
        jLabelAfficher.setPreferredSize(halfDimension);
        jLabelFichier.setPreferredSize(halfDimension);
        jLabelMenu.setPreferredSize(halfDimension);
        jLabelDeroulement.setPreferredSize(halfDimension);
        jLabelTempsMax.setPreferredSize(new Dimension(190, 40));
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
        jLabelNames.setPreferredSize(new Dimension(305, 83));
        jLabelNames.setOpaque(true);
        jLabelNames.setBackground(Color.white);
        jLabelChemin.setPreferredSize(halfDimension);

        jLabelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelCoordsMan.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelCoordClick.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelCoordsClick.setHorizontalAlignment(SwingConstants.CENTER);

        // Paramétrage des zone de saisie
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jTextFieldOrigine = new JTextField();
        jTextFieldPieton = new JTextField();
        jTextFieldDest = new JTextField();
        jTextFieldFichier = new JTextField("sortie");

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
        jRadioButtonChoixTemps = new JRadioButton("Temps");
        jRadioButtonChoixDistance = new JRadioButton("Distance");
        jRadioButtonChoixTemps.setPreferredSize(new Dimension(90, 25));
        jRadioButtonChoixDistance.setPreferredSize(new Dimension(90, 25));
        jRadioButtonChoixTemps.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(jRadioButtonChoixTemps);
        buttonGroup.add(jRadioButtonChoixDistance);

        // Paramétrage des menus de selection (cartes et menus)
        jComboBoxMenu = new JComboBox<>();
        jComboBoxCartes = new JComboBox<>();
        jComboBoxChemins = new JComboBox<>();
        jComboBoxMenu.setPreferredSize(halfDimension);
        jComboBoxCartes.setPreferredSize(halfDimension);
        for (String carte : cartes) {
            jComboBoxCartes.addItem(carte);
        }
        jComboBoxChemins.setPreferredSize(halfDimension);
        for (String chemin : chemins) {
            jComboBoxChemins.addItem(chemin);
        }

        // Paramétrage des images : graphe et logo INSA
        ImageIcon imageGraphe = new ImageIcon("arbre.jpg");
        ImageIcon imageINSA = new ImageIcon("logoINSA.png");
        JLabel jLabelImageGraphe = new JLabel();
        jLabelImageINSA = new JLabel();
        jLabelImageGraphe.setIcon(imageGraphe);
        jLabelImageINSA.setIcon(imageINSA);
        jLabelImageGraphe.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Paramétrage des buttons
        jButtonLoad = new JButton("CHARGER");
        jButtonLoad.setPreferredSize(new Dimension(120, 30));
        jButtonLoad.setBackground(new Color(235, 235, 235));
        jButtonLoad.addActionListener(new BoutonListener());
        jButtonOk = new JButton("OK");
        jButtonOk.setPreferredSize(new Dimension(120, 30));
        jButtonOk.setBackground(new Color(235, 235, 235));
        jButtonOk.addActionListener(new BoutonListener());

        // Paramétrage de la selection du temps max d'attente du piéton
        SpinnerModel spinnerModel = new SpinnerNumberModel(10, 0, 120, 1.0);
        jSpinnerTempsMax = new JSpinner(spinnerModel);
        jSpinnerTempsMax.setPreferredSize(halfDimension);
        ChangeListener listener = e -> tempsAttenteMaxPieton = (Double) jSpinnerTempsMax.getModel().getValue();
        jSpinnerTempsMax.addChangeListener(listener);
        ((JSpinner.DefaultEditor) jSpinnerTempsMax.getEditor()).getTextField().setEditable(false);

        //Paramétrage zone de log
//        log = new JTextArea();
//        log.setPreferredSize(new Dimension(380, 100));
//        log.setEditable(false);
//        log.append("Lancement de l'application");
        log = new Log();
        log.appendToLog("Lancement de l'application");

        // Paramétrage du menu de selection des choix avec ajout des composants
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setPreferredSize(new Dimension(400, 600));
        controlPanel.add(jLabelNames);
        controlPanel.add(jLabelImageINSA);
        controlPanel.add(space);
        controlPanel.add(jLabelTitle);
        controlPanel.add(jLabelImageGraphe);
        controlPanel.add(jLabelCarte);
        controlPanel.add(jComboBoxCartes);
        controlPanel.add(jLabelAfficher);
        controlPanel.add(jCheckBox);
        controlPanel.add(jLabelFichier);
        controlPanel.add(jTextFieldFichier);
        controlPanel.add(jButtonLoad);
        controlPanel.add(space);
        controlPanel.add(log);

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
            jComboBoxCartes.setEnabled(false);
            jCheckBox.setEnabled(false);
            jTextFieldFichier.setEnabled(false);
            jCheckBox.setEnabled(false);

            // Récupérer la carte souhaitée
            nomCarte = jComboBoxCartes.getSelectedItem().toString();
            DataInputStream mapdata = Openfile.open(nomCarte);

            // Afficher ou non la map -> création d'un dessin associé et ajout à la fenetre
            display = jCheckBox.isSelected();
            Dessin dessinPanel = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
            if (display)
                cp.add(dessinPanel);
            this.pack();
            this.setLocationRelativeTo(null);
            log.appendToLog("Instanciation de la carte ");
            // Création du graphe en fonction de la map selectionnée
            graphe = new Graphe(nomCarte, mapdata, dessinPanel, false);

            // Variable pour les algorithmes
            Algo algo;
            int noeud_rejoint;
            Node node;

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
                        log.setText("");
                        log.appendToLog("Quitter l'application");
                        continuer = false;
                        break;

                    // Connexité
                    case 1:
                        log.setText("");
                        // Initialisation et lancement de l'algorithme
                        initialiserAlgo(true);
                        log.appendToLog("Algorithme de CONNEXITE : " + origine + " vers " + dest);
                        algo = new Connexite(graphe, origine, dest, affichageDeroulementAlgo);
                        ArrayList perfConnexite = algo.run();
                        afficherEtEcrireResultats(3, perfConnexite);
                        break;

                    // PCC Standard : Dijkstra
                    case 2:
                        log.setText("");
                        // Initialisation et lancement de l'algorithme
                        initialiserAlgo(false);
                        log.appendToLog("Algorithme PCC Standard : DIJKSTRA : \nDe " + origine + " vers " + dest);
                        algo = new Pcc_Dijkstra(graphe, origine, dest, choixCout, false, false, Double.POSITIVE_INFINITY, affichageDeroulementAlgo, true);
                        ArrayList perfStandard = algo.run();
                        afficherEtEcrireResultats(1, perfStandard);

                        // Recupération d'un noeud au hasard du chemin tracé si on y est arrivé
                        if (perfStandard != null) {
                            noeud_rejoint = ((Pcc_Generique) algo).cheminTest();
                            log.appendToLog("Un noeud du chemin : " + noeud_rejoint);
                            node = graphe.getArrayList().get(noeud_rejoint);
                            graphe.getDessin().setColor(Color.black);
                            graphe.getDessin().drawPoint(node.getLongitude(), node.getLatitude(), 8);
                        }
                        break;

                    // PCC A-Star : Dijkstra guidé
                    case 3:
                        log.setText("");
                        //Initialisation et lancement de l'algorithme
                        initialiserAlgo(false);
                        log.appendToLog("Algorithme PCC guidé : DIJKSTRA A-STAR : " + origine + " vers " + dest);
                        algo = new Pcc_Star(graphe, origine, dest, choixCout, false, false, Double.POSITIVE_INFINITY, affichageDeroulementAlgo, true);
                        ArrayList perfAStar = algo.run();
                        afficherEtEcrireResultats(1, perfAStar);

                        // Recupération d'un noeud au hasard du chemin tracé si on y est arrivé
                        if (perfAStar != null) {
                            noeud_rejoint = ((Pcc_Generique) algo).cheminTest();
                            log.appendToLog("Un noeud du chemin : " + noeud_rejoint);
                            node = graphe.getArrayList().get(noeud_rejoint);
                            graphe.getDessin().setColor(Color.black);
                            graphe.getDessin().drawPoint(node.getLongitude(), node.getLatitude(), 8);
                        }
                        break;

                    // Programme de test algo connexité et des 2 algos Dijkstra : PCC Standard + PCC A-Star
                    case 4:
                        log.setText("");
                        // Initialisation des algorithmes
                        initialiserAlgo(false);
                        log.appendToLog("Programme de TEST des Algorithmes : " + origine + " vers " + dest);

                        // 1i algo -> Connexité
                        algo = new Connexite(graphe, origine, dest, affichageDeroulementAlgo);
                        // Lancement des algorithmes et récupération des résultats
                        ArrayList perf3 = algo.run();

                        // 2i algo -> PCC Standard : Dijkstra
                        algo = new Pcc_Dijkstra(graphe, origine, dest, choixCout, false, false, Double.POSITIVE_INFINITY, affichageDeroulementAlgo, true);
                        // Lancement des algorithmes et récupération des résultats
                        ArrayList perf1 = algo.run();

                        // 3i algo -> PCC A-Star : Dijkstra guidé
                        algo = new Pcc_Star(graphe, origine, dest, choixCout, false, false, Double.POSITIVE_INFINITY, affichageDeroulementAlgo, true);
                        ArrayList perf2 = algo.run();

                        afficherEtEcrireResultats(perf1, perf2, perf3);
                        break;

                    // Covoiturage
                    case 5:
                        log.setText("");
                        log.appendToLog("Covoiturage :");
                        // ArrayList contenant les couts màj
                        Algo algoVoitureDest, algoPietonDest, algoDestInverse;
                        ArrayList<Label> covoitSomme = new ArrayList<>();
                        ArrayList perfVoitureTous;
                        ArrayList perfPietonTous;
                        ArrayList perfDestTous;
                        ArrayList<String> durees = new ArrayList<>();
                        ArrayList<Boolean> seuls = new ArrayList<>();
                        noeud_rejoint = -1;
                        double tempsTotalmin = Double.POSITIVE_INFINITY;
                        double dureeExe; // Durée d'execution
                        double minPieton, minVoiture; // Temps min vers point de rencontre
                        boolean pasRencontres = false;
                        boolean connexes = true;
                        boolean pointsExistent;
                        boolean separement = false;

                        // Initialisation des algorithmes : cout en TEMPS !
                        initialiserCovoit();

                        // Mesurer le temps d'execution de l'algorithme
                        dureeExe = System.currentTimeMillis();

                        // Lancement des algorithmes
                        log.setText("");
                        log.appendToLog("Algorithme Dijsktra Voiture de " + origine + " vers " + dest);
                        // PCC de la VOITURE vers TOUS
                        algoVoitureDest = new Pcc_Dijkstra(graphe, origine, dest, choixCout, true, false, Double.POSITIVE_INFINITY, affichageDeroulementAlgo, false);
                        perfVoitureTous = algoVoitureDest.run();
                        ArrayList<Label> covoitVoiture = algoVoitureDest.getLabels();

                        // Permet de copier l'arraylist précédent sans le déréférencer
                        ArrayList<Label> covoitSave = new ArrayList<>();
                        for (Label aCovoitVoiture : covoitVoiture) covoitSave.add(new Label(aCovoitVoiture));
                        log.appendToLog("Algorithme Dijsktra Pieton de " + pieton + " vers " + dest);
                        // PCC du PIETON vers TOUS
                        algoPietonDest = new Pcc_Dijkstra(graphe, pieton, dest, choixCout, true, true, tempsAttenteMaxPieton, affichageDeroulementAlgo, false);
                        perfPietonTous = algoPietonDest.run();
                        ArrayList<Label> covoitPieton = algoPietonDest.getLabels();

                        log.appendToLog("Construction du Graphe inverse");

                        // PCC de la DESTINATION vers TOUS utilisant un graphe INVERSE
                        nomCarte = jComboBoxCartes.getSelectedItem().toString();
                        mapdata = Openfile.open(nomCarte);
                        Graphe grapheInverse = new Graphe(nomCarte, mapdata, new DessinInvisible(), true);
                        algoDestInverse = new Pcc_Dijkstra(grapheInverse, dest, pieton, choixCout, true, false, Double.POSITIVE_INFINITY, affichageDeroulementAlgo, false);
                        log.appendToLog("Algorithme Dijsktra sur Graphe Inverse de " + dest + " vers " + origine);
                        perfDestTous = algoDestInverse.run();
                        ArrayList<Label> covoitDestination = algoDestInverse.getLabels();

                        // On continue si les points saisis existent
                        if (pointsExistent = (perfVoitureTous != null && perfPietonTous != null && perfDestTous != null)) {

                            // Ici on a récupéré les temps de origine vers tous - pieton vers tous - et dest vers tous (graphe inverse)
                            // Mise à jour de l'ArrayList covoitSomme :
                            // Choisir le cout (temps) le plus élevé entre :
                            // - celui de la VOITURE vers TOUS
                            // - celui du PIETONS vers TOUS
                            // -> determine le temps minimum pour se rejoindre
                            // Il y aura un cout INFINY s'il n'y a pas de noeud en commun entre les deux

                            // Pour tous les points de la carte :
                            for (int i = 0; i < covoitVoiture.size(); i++) {
                                // On prend le temps MAX des deux (voiture et pieton) pour avoir le temps MIN qu'il faut pour se rejoindre
                                if (covoitVoiture.get(i).getCout() < covoitPieton.get(i).getCout())
                                    covoitSomme.add(i, covoitPieton.get(i));
                                else covoitSomme.add(i, covoitVoiture.get(i));

                                // On ajoute le temps qu'il faut depuis le point choisi précédemment de ralliement vers la dest
                                // C'est cette valeur TOTALE qu'on veut minimiser temps complet du trajet
                                covoitSomme.get(i).setCout(covoitSomme.get(i).getCout() + covoitDestination.get(i).getCout());

                                // Si ce temps est > au temps qu'aurait mis le MAX des deux (voiture et pieton)
                                if (covoitSomme.get(i).getCout() > Math.max(covoitVoiture.get(dest).getCout(), covoitPieton.get(dest).getCout())) {
                                    // Alors ils y vont séparements

                                    // On prend ainsi le MAX des deux temps (voiture et pieton) pour y aller, ce qui correspond au MIN du temps total
                                    if (covoitVoiture.get(dest).getCout() > covoitPieton.get(dest).getCout())
                                        covoitSomme.set(i, covoitVoiture.get(dest));
                                    else covoitSomme.set(i, covoitPieton.get(dest));
                                    seuls.add(i, true);
                                }
                                else {
                                    // Sinon ils y vont ensembles
                                    seuls.add(i, false);
                                }

                                // Mise à jour du cout total minimum et du NOEUD REJOINT
                                if (covoitSomme.get(i).getCout() < tempsTotalmin) {
                                    tempsTotalmin = covoitSomme.get(i).getCout();
                                    noeud_rejoint = i;
                                }
                            }
                            log.setText("");
                            // On test si un NOEUD REJOINT existe, ie. les
                            if (noeud_rejoint != -1) {
                                log.appendToLog("On se rejoint au noeud : " + covoitSomme.get(noeud_rejoint).getNum_node());

                                // Tester pour savoir si le pieton et la voiture se rendent seuls à la destination
                                if (separement = seuls.get(noeud_rejoint)) {
                                    // Chacun y va tout seul

                                    // On ajoute le cout de origine vers dest
                                    durees.add(AffichageTempsHeureMin(((Pcc_Dijkstra) algoVoitureDest).chemin(origine, dest, graphe)));
                                    durees.add(AffichageTempsHeureMin(((Pcc_Dijkstra) algoPietonDest).chemin(pieton, dest, graphe)));

                                } else {
                                    // La voiture et le pieton y vont ensemble

                                    // On ajoute le cout de l'algo voiture vers noeud rejoint
                                    minVoiture = covoitSave.get(noeud_rejoint).getCout();
                                    ((Pcc_Dijkstra) algoVoitureDest).chemin(origine, noeud_rejoint, graphe);
                                    durees.add(AffichageTempsHeureMin(minVoiture));

                                    // On ajoute le cout de l'algo pieton vers noeud rejoint
                                    minPieton = ((Pcc_Dijkstra) algoPietonDest).chemin(pieton, noeud_rejoint, graphe);
                                    durees.add(AffichageTempsHeureMin(minPieton));

                                    // On ajoute les couts d'attente après comparaison
                                    if (minVoiture > minPieton) {
                                        durees.add("Pas d'attente");
                                        durees.add(AffichageTempsHeureMin(minVoiture - minPieton));
                                    } else {
                                        durees.add(AffichageTempsHeureMin(minPieton - minVoiture));
                                        durees.add("Pas d'attente");
                                    }

                                    // On ajoute le cout de noeud rejoins vers dest
                                    durees.add(AffichageTempsHeureMin(((Pcc_Dijkstra) algoDestInverse).chemin(dest, noeud_rejoint, graphe)));

                                    // Afficher le point de rencontre si souhaité
                                    if (display) {
                                        // on trace le point de rencontre
                                        node = graphe.getArrayList().get(noeud_rejoint);
                                        graphe.getDessin().setColor(Color.black);
                                        graphe.getDessin().drawPoint(node.getLongitude(), node.getLatitude(), 12);
                                    }
                                }
                            } else {
                                pasRencontres = true;
                                // Pas de rencontre car ...
                                algo = new Connexite(graphe, origine, pieton, false);
                                ArrayList res = algo.run();
                                // ... le piéton attend trop et doit marcher plus de tempsAttenteMaxPieton minutes
                                log.appendToLog("Connexité :" + res.get(2));
                                if (res.get(2).equals("non connexes")) {
                                    connexes = false;
                                }
                                // ... OU que les points ne sont pas connexes
                                else durees.add(AffichageTempsHeureMin(tempsAttenteMaxPieton));
                            }
                        }

                        // On enregistre le temps d'execution de l'algorithme
                        dureeExe = (System.currentTimeMillis() - dureeExe);

                        // Afficher les résultats
                        afficherEtEcrireResultats(pointsExistent, pasRencontres, connexes, separement, noeud_rejoint, durees, tempsTotalmin, dureeExe);

                        //Si le pieton doit attendre plus que son temps d'attente max,
                        //On demande à l'utilisateur s'il veut lancer un PCC jusqu'à la dest
                        if (!connexes && noeud_rejoint == -1) {
                            algo = new Connexite(graphe, pieton, dest, false);
                            ArrayList res = algo.run();
                            if (res.get(2).equals("connexes")) {
                                switch (JOptionPane.showConfirmDialog(null, "Souhaitez-vous simuler la durée prévue si le piéton se rend en voiture à la destination ?", "", JOptionPane.OK_OPTION)) {
                                    case JOptionPane.OK_OPTION:
                                        algo = new Pcc_Star(graphe, pieton, dest, choixCout, false, false, Double.POSITIVE_INFINITY, affichageDeroulementAlgo, true);
                                        ArrayList perfPietonDest = algo.run();
                                        afficherEtEcrireResultats(2, perfPietonDest);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }

                        // on le détruit sinon la ram n'aime pas trop trop ...
                        grapheInverse = null;
                        break;

                    // Charger un fichier de chemin
                    case 6:
                        log.setText("");
                        log.appendToLog("Charger un fichier de chemin : ");
                        // Paramétrer le menu de selection
                        makeControlPanel(51);

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        // On récupère le nom de la carte
                        String nomChemin = jComboBoxChemins.getSelectedItem().toString();

                        log.appendToLog(nomChemin);

                        // Paramétrer le menu de selection
                        makeControlPanel(52);

                        if (graphe.verifierChemin(Openfile.open(nomChemin), nomChemin) == -1)
                            continue;

                        graphe.verifierChemin(Openfile.open(nomChemin), nomChemin);
                        graphe.getChemin().tracerChemin(graphe.getDessin());
                        jTextField1.setText(graphe.getChemin().Calculer_cout_chemin_distance());
                        jTextField2.setText(graphe.getChemin().Calculer_cout_chemin_temps());

                        log.appendToLog("Distance : " + graphe.getChemin().Calculer_cout_chemin_distance());
                        log.appendToLog("Temps : " + graphe.getChemin().Calculer_cout_chemin_temps());


                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        break;

                    // Réinitialiser la map
                    case 7:
                        log.setText("");
                        log.appendToLog("Réinitialiser la carte : ");
                        // Paramétrer le menu de selection
                        makeControlPanel(6);

                        // On doit cliquer sur OK pour continuer
                        waitButtonOk();

                        // Récupérer la carte souhaitée
                        nomCarte = jComboBoxCartes.getSelectedItem().toString();
                        mapdata = Openfile.open(nomCarte);
                        log.appendToLog(nomCarte);

                        // On supprime l'ancienne carte
                        cp.remove(dessinPanel);
                        graphe = null; //Pour detruire l'objet (methode finalize())

                        // Afficher ou non la map -> création d'un dessin associé et ajout à la fenetre
                        display = jCheckBox.isSelected();
                        dessinPanel = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
                        if (display)
                            cp.add(dessinPanel);
                        this.pack();
                        this.setLocationRelativeTo(null);

                        log.appendToLog("Instanciation de la carte...");

                        // Création du graphe en fonction de la map selectionnée
                        graphe = new Graphe(nomCarte, mapdata, dessinPanel, false);

                        break;

                    // Obtenir un numéro de sommet
                    case 8:
                        log.setText("");
                        log.appendToLog("Obtenir un numéro de sommet :");
                        // Paramétrer le menu de selection
                        makeControlPanel(8);
                        jButtonOk.setEnabled(false);

                        // On récupère les informations du click
                        clickCoord = graphe.situerClick();

                        // On vérifie que l'on a bien récupéré les informations du click
                        if (clickCoord == null) {
                            jTextField1.setText("Le clic n'a rien retourné");
                            jTextField2.setText("Le clic n'a rien retourné");
                        } else {
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
            log.appendToLog("Programme terminé.");
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
     * - Oui : on les saisit
     * - Non : on clique pour les obtenir
     * - Choix du cout : temps ou distance
     * - Choix de l'affichage du déroulement des algorithmes
     */
    public void initialiserAlgo(boolean connexite) {

        // On demande à l'utilisateur s'il connait les numéros ou veut cliquer
        if (display)
            sommetsConnus = JOptionPane.showConfirmDialog(null, "Connaissez vous le numéro des sommets", "", JOptionPane.OK_OPTION);
        else
            sommetsConnus = 0;

        switch (sommetsConnus) {
            case JOptionPane.OK_OPTION:
                //Paramétrer le menu de selection
                if (connexite) makeControlPanel(11);
                else makeControlPanel(21);
                // Les coordonnées vont automatiquements se mettrent à jours dans les zones de texte après clic

                // On doit cliquer sur OK pour continuer
                waitButtonOk();
                break;
            default:
                // Paramétrer le menu de selection
                if (connexite) makeControlPanel(12);
                else makeControlPanel(22);
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
        } catch (NumberFormatException n) {
            origine = -1;
            dest = -1;
        }

        // Choix du coup en temps ou distance
        if (jRadioButtonChoixDistance.isSelected())
            choixCout = 0;
        else choixCout = 1;

        // Choix de l'affichage des algo
        affichageDeroulementAlgo = jCheckBox.isSelected();
    }

    /**
     * Initialisation du covoiturage :
     * Pcc Standard, Pcc A-Star et le Programme de test des 2 algos Disjkstra
     * - Connait-on les sommets ORIGINE (conducteur et pieton) et ARRIVE
     * - Oui : on les saisit
     * - Non : on clique pour les obtenir
     */
    public void initialiserCovoit() {

        // On demande à l'utilisateur s'il connait les numéros ou veut cliquer
        if (display)
            sommetsConnus = JOptionPane.showConfirmDialog(null, "Connaissez vous le numéro des sommets", "", JOptionPane.OK_OPTION);
        else
            sommetsConnus = 0;

        switch (sommetsConnus) {
            case JOptionPane.OK_OPTION:
                //Paramétrer le menu de selection
                makeControlPanel(31);
                // Les coordonnées vont automatiquements se mettrent à jours dans les zones de texte après clic

                // On doit cliquer sur OK pour continuer
                waitButtonOk();
                break;
            default:
                // Paramétrer le menu de selection
                makeControlPanel(32);
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
        } catch (NumberFormatException n) {
            origine = -1;
            dest = -1;
        }

        // Choix du coup en temps
        choixCout = 1;

        // Pas d'affichage des algo
        affichageDeroulementAlgo = false;

        // Choix de l'affichage des chemin
        affichageChemin = jCheckBox.isSelected();

        // Récupération du temps max d'attente du pieton
        tempsAttenteMaxPieton = (Double) jSpinnerTempsMax.getModel().getValue();
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
        jComboBoxCartes.setEnabled(true);
        jCheckBox.setEnabled(true);

        // En fonction du paramètre donné
        switch (choice) {
            // Menu de lancement de l'application
            case 0:
                jComboBoxMenu.removeAllItems();
                if (display)
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
                if (display) {
                    controlPanel.add(jLabelDeroulement);
                    controlPanel.add(jCheckBox);
                }
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
                if (display) {
                    controlPanel.add(jLabelDeroulement);
                    controlPanel.add(jCheckBox);
                }
                controlPanel.add(jLabelDepart);
                controlPanel.add(jTextFieldOrigine);
                controlPanel.add(jLabelArrivee);
                controlPanel.add(jTextFieldDest);
                break;

            // Utilisé par Pcc Standard, Pcc A-Star et le Programme de test des 2 algos Disjkstra...
            // ... si on ne connait pas les points
            case 21:
                jTextFieldOrigine.setEditable(true);
                jTextFieldDest.setEditable(true);
                jButtonOk.setEnabled(false);
                jTextFieldOrigine.setText("");
                jTextFieldPieton.setText("0"); // Obligé pour afficher le bouton OK
                jTextFieldDest.setText("");
                controlPanel.add(jLabelCoordsMan);
                if (display) {
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
            case 22:
                jTextFieldOrigine.setEditable(false);
                jTextFieldDest.setEditable(false);
                jTextFieldOrigine.setText("");
                jTextFieldPieton.setText("0"); // Obligé pour afficher le bouton OK
                jTextFieldDest.setText("");
                controlPanel.add(jLabelCoordsClick);
                if (display) {
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
            case 31:
                jTextFieldOrigine.setEditable(true);
                jTextFieldPieton.setEditable(true);
                jTextFieldDest.setEditable(true);
                jButtonOk.setEnabled(false);
                jTextFieldOrigine.setText("");
                jTextFieldPieton.setText("");
                jTextFieldDest.setText("");
                controlPanel.add(jLabelCoordsMan);
                controlPanel.add(jLabelTempsMax);
                controlPanel.add(jSpinnerTempsMax);
                controlPanel.add(jLabelDepartVoiture);
                controlPanel.add(jTextFieldOrigine);
                controlPanel.add(jLabelDepartPieton);
                controlPanel.add(jTextFieldPieton);
                controlPanel.add(jLabelArrivee);
                controlPanel.add(jTextFieldDest);
                controlPanel.add(jButtonOk);
                break;

            // ... si on connait les points
            case 32:
                jTextFieldOrigine.setEditable(false);
                jTextFieldPieton.setEditable(false);
                jTextFieldDest.setEditable(false);
                jTextFieldOrigine.setText("");
                jTextFieldPieton.setText("");
                jTextFieldDest.setText("");
                controlPanel.add(jLabelCoordsClick);
                controlPanel.add(jLabelTempsMax);
                controlPanel.add(jSpinnerTempsMax);
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
                controlPanel.add(jLabelCarte);
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
        controlPanel.add(space);
        controlPanel.add(log);
        cp.add(controlPanel, 0);
        controlPanel.repaint();
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
        } catch (Exception e) {
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
        if (performances == null)
            resultat += "Erreur - Un des sommets saisis n'appartient pas au graphe";
        else {
            resultat += "Origine : " + performances.get(0) + "\n";
            resultat += "Arrivée : " + performances.get(1) + "\n";
            resultat += "Connexité : " + performances.get(2) + "\n";
            resultat += "Temps de Calcul : " + performances.get(3) + "\n";
            if (choixAlgo != 3) {
                if (choixCout == 0) resultat += "Le cout en distance est de : " + performances.get(4) + "\n";
                else resultat += "Le cout en temps est de : " + performances.get(4) + "\n";
                resultat += "Nb max d'element : " + performances.get(5) + "\n";
                resultat += "Nb elements explorés : " + performances.get(6) + "\n";

            }
        }
        JOptionPane.showMessageDialog(null, resultat); // On affiche le resultats en popup
        sortie.append(resultat).append("\n\n\n"); // On ecrit dans le fichier
    }

    void afficherEtEcrireResultats(ArrayList perf1, ArrayList perf2, ArrayList perf3) {
        resultat = "           Programme de test des algorithmes Dijkstra :\n";
        resultat += "Carte : " + nomCarte + "\n";
        if (perf1 == null || perf2 == null)
            resultat += "Erreur - Un des sommets saisis n'appartient pas au graphe";
        else {
            resultat += "Origine : " + perf1.get(0) + "\n";
            resultat += "Arrivée : " + perf2.get(1) + "\n\n";
            resultat += " - PCC Standard vs. PCC A-Star vs. Connexité :\n";
            resultat += "Connexité : " + perf1.get(2) + " - " + perf2.get(2) + " - " + perf3.get(2) + "\n";
            resultat += "Temps de Calcul : " + perf1.get(3) + " - " + perf2.get(3) + " - " + perf3.get(3) + "\n\n";
            resultat += " - PCC Standard vs. PCC A-Star :\n";
            if (choixCout == 0)
                resultat += "Le cout en distance est de : " + perf1.get(4) + " - " + perf2.get(4) + "\n";
            else resultat += "Le cout en temps est de : " + perf1.get(4) + " - " + perf2.get(4) + "\n";
            resultat += "Nb max d'element : " + perf1.get(5) + " - " + perf2.get(5) + "\n";
            resultat += "Nb elements explorés : " + perf1.get(6) + " - " + perf2.get(6) + "\n";
        }
        JOptionPane.showMessageDialog(null, resultat); // On affiche le resultats en popup
        sortie.append(resultat).append("\n\n\n"); // On ecrit dans le fichier
    }

    void afficherEtEcrireResultats(boolean pointsExistent, boolean pasRencontres, boolean connexes, boolean separement, int noeud_rejoint, ArrayList<String> durees, double tempsTotal, double dureeExe) {
        resultat = "Covoiturage\n";
        resultat += "Carte : " + nomCarte + "\n";
        resultat += "Origine Voiture : " + origine + "\n";
        resultat += "Origine Pieton : " + pieton + "\n";
        resultat += "Arrivée : " + dest + "\n\n";
        if (!pointsExistent)
            resultat += "Erreur - Un des sommets saisis n'appartient pas au graphe";
        else {
            if (pasRencontres) {
                resultat += "Aucun noeud de rencontre trouvé !\n";
                if (!connexes) resultat += "Le piéton et la voiture ne sont pas connexes.\n";
                else resultat += "La destination n'est pas atteignable.\n";
            } else {
                resultat += "On est bien arrivé ! \n";
                if (separement) {
                    resultat += "Le conducteur et le pieton se rendent chacun seuls à la destination\n\n";
                    if (affichageChemin) {
                        resultat += "Temps de la voiture vers la destination : " + durees.get(0) + "\n";
                        resultat += "Temps du pieton vers la destination : " + durees.get(1) + "\n";
                    }
                } else {
                    resultat += "Le conducteur et le pieton se rendent ensemble à la destination\n";
                    resultat += "Rencontre au noeud  : " + noeud_rejoint + "\n\n";
                    if (affichageChemin) {
                        resultat += "Temps de la voiture vers le point rencontre : " + durees.get(0) + "\n";
                        resultat += "Temps du pieton vers le point rencontre : " + durees.get(1) + "\n";
                        resultat += "Temps d'attente de la voiture : " + durees.get(2) + "\n";
                        resultat += "Temps d'attente du pieton : " + durees.get(3) + "\n";
                        resultat += "Temps du point de rencontre vers la destination : " + durees.get(4) + "\n";
                    }
                }
                // On donne au moins le temps final pour aller jusqu'au noeud
                resultat += "Temps total : " + AffichageTempsHeureMin(tempsTotal) + "\n";
            }
            resultat += "\nDurée exécution : " + AffichageTempsCalcul(dureeExe) + "\n\n";
        }
        JOptionPane.showMessageDialog(null, resultat); // On affiche le resultats en popup
        sortie.append(resultat).append("\n\n\n"); // On ecrit dans le fichier
    }


    /**
     * Methods that is called when one of the following textfields is modified
     * We won't be able to clic on the OK button if one of them if empty
     */
    private void textFieldCoordChanged() {
        if (jTextFieldOrigine.getText().equals("") || jTextFieldPieton.getText().equals("") || jTextFieldDest.getText().equals("")) {
            jButtonOk.setEnabled(false);
        } else {
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
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.appendToLog("Error during thread sleep : " + e);
            }
        }
        buttonClicked = false;
    }

    public String AffichageTempsHeureMin(double min) {
        int heures, minutes, sec;
        double totalSecondes2 = min*60;
        int totalSecondes = (int) Math.round(totalSecondes2);

        minutes = (totalSecondes / 60) % 60;
        heures = (totalSecondes / (60 * 60));
        sec = (totalSecondes - 60*minutes - 3600*heures);

        System.out.println("Cout : " + min);

        if (min >= 60)
            return (heures + " heure(s), " + minutes + " minute(s) et " + sec + " sec");
        else if (minutes > 0)
            return  (minutes + " minute(s) et " + sec + " sec");
        else return (sec + " sec");
    }

    public String AffichageTempsCalcul(double ms) {
        int sec;
        double milli;

        if (ms >= 1000) {
            sec = (int) ms / 1000;
            milli = ms % 1000;
            return (sec + " seconde(s) " + milli);
        }
        return (ms + "ms");
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
                Thread thread = new Thread(new Play());
                thread.start();
            } else if (evt.getSource() == jButtonOk) {
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