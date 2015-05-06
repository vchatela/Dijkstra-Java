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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

public class Launch extends JFrame {

    static private final String menu[] = {"Quitter", "PCC Standard", "PCC A-star",
            "Obtenir un numero de sommet ", "Charger un fichier de chemin"
            , "Reinitialiser la carte", "Tester les performances"};
    static private final String cartes[] = {"midip", "insa", "france",
            "fractal", "reunion", "carre-dense", "carre", "fractal-spiral"};
    /**
     * Variable declarations
     */
    private final Readarg readarg;
    private Graphe      graphe;
    private JPanel		controlPanel;
    private Container 	cp;
    private JLabel		jSpace;
    private JLabel		jLabel1;
    private JLabel		jLabel2;
    private JLabel		jLabel3;
    private JLabel		jLabel4;
    private JLabel      jLabelImage;
    private JLabel      jLabelFichier;
    private JTextField  jTextFieldFichier;
    private JTextField  jTextField1;
    private JTextField  jTextField2;
    private JCheckBox   jCheckBoxSortieGraphique;
    private JComboBox	jComboBoxMenu;
    private JComboBox	jComboBoxCartes;
    private ImageIcon   image;
    private JButton		loadButton;     //Button ok (lancement de l'appli, chagement de menu, attente de lecture des coord du clic)
    private JButton		okButton;     //Button charger (lancement de l'appli)
    private String      nomcarte;       //Nom de la carte à charger
    private boolean     display;        //Affichage graphique ou non
    private boolean     buttonHasBeenClicked;   //Choix du menu effectué ou non
    private Thread      t;              //Utilisé pour afficher la carte
    private PrintStream sortie;

    /**
     * Default constructor
     */
    public Launch(String[] args) {
        this.setTitle("Dijkstra");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.readarg = new Readarg(args);
        buttonHasBeenClicked = false;

        jSpace = new JLabel();
        jSpace.setPreferredSize(new Dimension(350, 25));
        jLabel1 = new JLabel("Bienvenue (Version 3.0 de Mangel - Chatelard)");
        jLabel2 = new JLabel("Programme de test des algorithmes de graphe");
        jLabel3 = new JLabel("Nom du fichier .map a utiliser");
        jLabel4 = new JLabel("Voulez-vous une sortie graphique");
        jLabelFichier = new JLabel("Fichier de sortie :");

        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jTextFieldFichier = new JTextField();
        jTextField1.setPreferredSize(new Dimension(300, 25));
        jTextField2.setPreferredSize(new Dimension(300, 25));
        jTextFieldFichier.setPreferredSize(new Dimension(100, 25));

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

        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(100, 25));
        okButton.setBackground(new Color(235, 235, 235));
        okButton.addActionListener(new BoutonListener());

        controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(350, 645));
        controlPanel.setLayout(new FlowLayout());

        controlPanel.add(jLabel1);
        controlPanel.add(jLabel2);
        controlPanel.add(jLabelImage);
        controlPanel.add(jLabel3);
        controlPanel.add(jComboBoxCartes);
        controlPanel.add(jLabel4);
        controlPanel.add(jCheckBoxSortieGraphique);
        controlPanel.add(jLabelFichier);
        controlPanel.add(jTextFieldFichier);
        controlPanel.add(loadButton);

        cp = getContentPane();
        cp.setLayout(new FlowLayout());
        cp.add(controlPanel);

        this.pack();
        this.setResizable(false);
        this.setVisible(true);

    } // _________  end of constructor

    public static void main(String[] args) {
        Launch launch = new Launch(args);
    }

    public void afficherMenu() {
        makeControlPanel(1);
    }

    public void makeControlPanel(int choice) {
        cp.remove(controlPanel);
        controlPanel.removeAll();
        controlPanel.revalidate();

        // Afficher le menu
        if (choice == 1) {
            jLabel1.setText("Que voulez-vous faire");
            okButton.setEnabled(true);

            controlPanel.add(Box.createHorizontalStrut(300));
            controlPanel.add(jLabel1);
            controlPanel.add(jComboBoxMenu);
            controlPanel.add(okButton);
        }

        // Situer clic
        else if (choice == 3) {
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
        }

        //Reinitialisation de la carte
        else if (choice == 5) {
            jLabel1 = new JLabel("Nom du fichier .map a utiliser");

            controlPanel.add(jLabel1);
            controlPanel.add(jComboBoxCartes);
            controlPanel.add(okButton);
        }

        //Perf
        else if(choice == 6) {
            //Label2.setText("Clic aux coordonnées : ");
            jLabel3.setText("Noeud le plus proche : ");
            //controlPanel.add(jLabel2);
            //controlPanel.add(jTextField1);
            controlPanel.add(jLabel3);
            controlPanel.add(jTextField2);
        }

        cp.add(controlPanel, 0);
        this.repaint();
        this.pack();
    }

    public void go() {

        try {

            DataInputStream mapdata = Openfile.open(nomcarte);

            ArrayList clickCoord = null; //Pour avoir coordonnées d'un clic

            Dessin dessin = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
            cp.add(dessin);
            this.pack();

            graphe = new Graphe(nomcarte, mapdata, dessin);

            sortie = this.fichierSortie();

            // Boucle principale : le menu est accessible jusqu'a ce que l'on quitte.
            boolean continuer = true;
            int choix;

            while (continuer) {
                this.afficherMenu();
                okButton.setEnabled(true);

                //On attend d'avoir cliqué sur OK
                waitButtonOk();

                okButton.setEnabled(false);
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
                        algo = new Pcc_Dijkstra(graphe, sortie, this.readarg);
                        break;

                    case 2:
                        algo = new PccStar(graphe, sortie, this.readarg);
                        break;

                    case 3:
                        makeControlPanel(3);

                        clickCoord=graphe.situerClick();
                        if(clickCoord == null) {
                            System.out.println();
                        }
                        else {
                            jTextField1.setText(clickCoord.get(0).toString());
                            jTextField2.setText(clickCoord.get(1).toString());
                        }

                        okButton.setEnabled(true);
                        waitButtonOk();

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
                        makeControlPanel(5);

                        //On attend d'avoir cliqué sur OK
                        waitButtonOk();

                        nomcarte = jComboBoxCartes.getSelectedItem().toString();

                        cp.remove(dessin);
                        dessin = (display) ? new DessinVisible(800, 600) : new DessinInvisible();
                        cp.add(dessin);
                        dessin.revalidate();
                        mapdata = Openfile.open(nomcarte);
                        graphe = null; //Pour detruire l'objet (methode finalize())
                        graphe = new Graphe(nomcarte, mapdata, dessin);
                        this.pack();

                        break;
                    case 6:
                        // Programme de test des 2 algos D + D A-Star
                        int origine, dest;
                        // On demande à l'utilisateur s'il connait les numéros ou veut cliquer
                        int click;
                        click = JOptionPane.showConfirmDialog(null, "Connaissez vous le numéro des sommets", "", JOptionPane.OK_OPTION);
                        switch (click) {
                            case JOptionPane.OK_OPTION:
                                origine = Integer.parseInt(JOptionPane.showInputDialog(null, "Numero du sommet d'origine'"));
                                dest = Integer.parseInt(JOptionPane.showInputDialog(null, "Numero du sommet d'origine'"));
                                break;
                            default:
                                makeControlPanel(6);
                                this.pack();

                                try {

                                    clickCoord = graphe.situerClick();
                                    jTextField2.setText(clickCoord.get(1).toString());
                                    origine = Integer.parseInt(jTextField2.getText());
                                    clickCoord = graphe.situerClick();
                                    jTextField2.setText(clickCoord.get(1).toString());
                                    dest = Integer.parseInt(jTextField2.getText());
                                    break;
                                } catch (NumberFormatException n) {
                                    System.out.println(n);
                                    origine = -1;
                                    dest = -1;
                                }
                        }

                        algo1 = new Pcc_Dijkstra(graphe, sortie, this.readarg, true, origine, dest);
                        // TODO : PCCStar non assign?
                        algo = new PccStar(graphe, sortie, this.readarg, true, origine, dest);

                        break;

                    default:
                        System.out.println("Choix de menu incorrect : " + choix);
                        JOptionPane.showMessageDialog(null, "Choix de menu incorrect", "Choix menu", JOptionPane.ERROR_MESSAGE);
                        System.exit(1);
                }

                if (algo != null) {
                    //TODO
                    if (algo1 != null) {
                        // on est dans la partie des performances
                        ArrayList perf1 = null;
                        perf1 = algo1.run();
                        if (perf1 == null) {
                            // on revient au debut du menu
                            continue;
                        }
                        ArrayList perf2 = null;
                        perf2 = algo.run();
                        if (perf2 == null) {
                            continue;
                        }
                        // on affiche les performances
                        String resultat = new String("Performance des algos Dijkstra VS Dijkstra A-Star \n");
                        resultat += "Le cout est de : " + perf1.get(0) + " km - " + perf2.get(0) + " km \n";
                        resultat += "Durée exécution : " + perf1.get(1) + " ms - " + perf2.get(1) + " ms \n";
                        resultat += "Nbr max éléments dans le tas : " + perf1.get(2) + " - " + perf2.get(2) + "\n";
                        resultat += "Nombre d'éléments parcourut : " + perf1.get(3) + " - " + perf2.get(3) + "\n";
                        JOptionPane.showMessageDialog(null, resultat);
                    } else {
                        algo.run();
                    }
                }
            }

            //On detruit le jFrame
            this.setVisible(false);
            this.dispose();

            sortie.close();

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

    public class BoutonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            //Click sur le boutton Load
            if (evt.getSource() == loadButton) {
                nomcarte = jComboBoxCartes.getSelectedItem().toString();
                display = jCheckBoxSortieGraphique.isSelected();
                loadButton.setEnabled(false);
                t = new Thread(new PlayAnimation());
                t.start();
            } else if (evt.getSource() == okButton) {
                buttonHasBeenClicked = true;
            }
        }
    }

    public void waitButtonOk() {
        while (buttonHasBeenClicked == false) {
            try {
                t.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Error thread sleep");
            }
        }
        buttonHasBeenClicked = false;
    }

    class PlayAnimation implements Runnable {
        public void run() {
            go();
        }
    }

}