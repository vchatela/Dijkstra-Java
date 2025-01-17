package src.main.java.core;

/**
 * Classe representant un graphe.
 * A vous de completer selon vos choix de conception.
 */

import src.main.java.base.Couleur;
import src.main.java.base.Descripteur;
import src.main.java.base.Dessin;
import src.main.java.base.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Graphe {

    // Version du format MAP utilise'.
    private static final int version_map = 4;
    private static final int magic_number_map = 0xbacaff;
    // Version du format PATH.
    private static final int version_path = 1;
    private static final int magic_number_path = 0xdecafe;
    protected final boolean inverse;
    // Nom de la carte utilisee pour construire ce graphe
    private final String nomCarte;
    // Fenetre graphique
    private final Dessin dessin;
    public int max;


    /*
     * Ces attributs constituent une structure ad-hoc pour stocker les informations du graphe.
     * Vous devez modifier et ameliorer ce choix de conception simpliste.
     */
    // Identifiant de la carte
    private int idcarte;
    // Numero de zone de la carte
    private int numzone;
    private ArrayList<Node> listNode;
    private Chemin chemin; // afin de charger le fichier path du chemin

    // Le constructeur cree le graphe en lisant les donnees depuis le DataInputStream
    public Graphe(String nomCarte, DataInputStream dis, Dessin dessin, boolean inverse) {
        this.inverse = inverse;
        this.listNode = new ArrayList<>();
        this.nomCarte = nomCarte;
        this.dessin = dessin;
        Utils.calibrer(nomCarte, dessin);

        // Petit outil pour verifier le chargement des cartes qui sont parfois longues
        JProgressBar pb = new JProgressBar();
        pb.setStringPainted(true);
        JFrame frame = new JFrame("Affichage en cours");
        frame.setUndecorated(true);
        frame.setPreferredSize(new Dimension(150, 30));
        frame.setContentPane(pb);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Lecture du fichier MAP.
        // Voir le fichier "FORMAT" pour le detail du format binaire.
        try {

            // Nombre d'aretes
            int edges = 0;

            // Verification du magic number et de la version du format du fichier .map
            int magic = dis.readInt();
            int version = dis.readInt();
            Utils.checkVersion(magic, magic_number_map, version, version_map, nomCarte, ".map");

            // Lecture de l'identifiant de carte et du numero de zone,
            this.idcarte = dis.readInt();
            this.numzone = dis.readInt();

            // Lecture du nombre de descripteurs et du nombre de noeuds.
            int nb_descripteurs = dis.readInt();
            int nb_Nodes = dis.readInt();
            max = nb_Nodes;

            // Reglage de la barre de progression
            pb.setMinimum(0);
            pb.setMaximum(nb_Nodes);

            if (inverse) {
                // on ne veut pas afficher
                frame.setVisible(false);
                frame.dispose();
            }

            // stocke la latitude et longitude d'un noeud
            float latitude, longitude;
            // nombre de successeurs d'un noeud
            int nb_successeur;

            // Lecture des noeuds
            for (int num_Node = 0; num_Node < nb_Nodes; num_Node++) {
                // initialisation avec les bonnes valeurs
                longitude = ((float) dis.readInt()) / 1E6f;
                latitude = ((float) dis.readInt()) / 1E6f;
                nb_successeur = dis.readUnsignedByte();
                this.listNode.add(new Node(num_Node, longitude, latitude, nb_successeur));
            }

            // Verification de la lecture des noeuds ! cf format fichier .map
            Utils.checkByte(255, dis);

            // Les noeuds sont charges, passons aux successeurs

            // Lecture des descripteurs
            Descripteur[] descripteurs = new Descripteur[nb_descripteurs];
            for (int num_descr = 0; num_descr < nb_descripteurs; num_descr++) {
                // Lecture du descripteur numero num_descr
                descripteurs[num_descr] = new Descripteur(dis);
            }

            // On verifie que la lecture des descripteurs est bonne (cf format .map)
            Utils.checkByte(254, dis);

            // Pour le noeud num_node,  des successeurs
            for (int num_Node = 0; num_Node < nb_Nodes; num_Node++) {

                //Mise a jour de la barre de progression
                pb.setValue(num_Node);

                //Initialisation de tous ces arcs (successeurs)
                for (int num_succ = 0; num_succ < this.listNode.get(num_Node).getNumberArc(); num_succ++) {

                    // Lecture de tous les successeurs du noeud num_Node
                    int succ_zone = dis.readUnsignedByte(); // zone du successeur
                    int dest_Node = Utils.read24bits(dis);  // numero de noeud du successeur
                    int descr_num = Utils.read24bits(dis);  // descripteur de l'arete
                    int longueur = dis.readUnsignedShort(); // longueur de l'arete en metres
                    int nb_segm = dis.readUnsignedShort();  // Nombre de segments constituant l'arete

                    // Creation d'un arc initialise avec toutes les lectures precedentes et ajout dans la liste des arcs du noeud num_node
                    Arc arc;
                    if (!inverse) {
                        arc = new Arc(succ_zone, dest_Node, descr_num, longueur, nb_segm, descripteurs[descr_num], this.listNode.get(num_Node));
                        this.listNode.get(num_Node).getArrayListArc().add(arc);
                    } else {
                        // ici on doit construire le graphe inverse, donc inverser tous les arcs
                        arc = new Arc(succ_zone, num_Node, descr_num, longueur, nb_segm, descripteurs[descr_num], this.listNode.get(dest_Node));
                        // on doit maintenant l'ajouter au bon endroit (ds les arcs de celui qui aurait du être le successeur)
                        this.listNode.get(dest_Node).getArrayListArc().add(arc);
                    }


                    //Incrementation du nombre d'arretes
                    edges++;

                    // Si le sens n'est pas unique on doit ajouter une arete dans l'autre sens (noeud destinataire ->noeud actuel)
                    if (!descripteurs[descr_num].isSensUnique() && (succ_zone == numzone)) {
                        if (!inverse) {
                            Arc arc_dest = new Arc(succ_zone, num_Node, descr_num, longueur, nb_segm, descripteurs[descr_num], this.listNode.get(dest_Node));
                            this.listNode.get(dest_Node).getArrayListArc().add(arc_dest);
                        } else {
                            arc = new Arc(succ_zone, dest_Node, descr_num, longueur, nb_segm, descripteurs[descr_num], this.listNode.get(num_Node));
                            this.listNode.get(num_Node).getArrayListArc().add(arc);
                        }
                    }
                    Couleur.set(dessin, descripteurs[descr_num].getType());

                    float current_long = this.listNode.get(num_Node).getLongitude();
                    float current_lat = this.listNode.get(num_Node).getLatitude();

                    // Chaque segment est dessine
                    for (int i = 0; i < nb_segm; i++) {
                        float delta_lon = (dis.readShort()) / 2.0E5f;
                        float delta_lat = (dis.readShort()) / 2.0E5f;

                        //Ajout du segment i au successeur actuel
                        if (!inverse)
                            this.listNode.get(num_Node).getArrayListArc().get(num_succ).addSegment(new Segment(delta_lon, delta_lat));
                        // Sinon on s'en fout car on ne tracera jamais le graphe inverse

                        //Dessin des segments
                        dessin.drawLine(current_long, current_lat, (current_long + delta_lon), (current_lat + delta_lat));

                        current_long += delta_lon;
                        current_lat += delta_lat;
                    }

                    // Le dernier trait rejoint le Node destination.
                    // On le dessine si le noeud destination est dans la zone du graphe courant.
                    if (succ_zone == numzone) {
                        dessin.drawLine(current_long, current_lat, this.listNode.get(dest_Node).getLongitude(), this.listNode.get(dest_Node).getLatitude());
                    }
                }
            }

            //Fin du chargement des donnees de notre graphe
            //On fait disparaitre la barre de progression
            frame.setVisible(false);
            frame.dispose();

            Utils.checkByte(253, dis);

            System.out.println("Fichier lu : " + nb_Nodes + " Nodes, " + edges + " aretes, " + nb_descripteurs + " descripteurs.");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public Dessin getDessin() {
        return dessin;
    }

    public int getZone() {
        return numzone;
    }

    public ArrayList<Node> getArrayList() {
        return this.listNode;
    }

    /**
     * Attend un clic sur la carte et affiche le numero de Node le plus proche du clic.
     * A n'utiliser que pour faire du debug ou des tests ponctuels.
     * Ne pas utiliser automatiquement a chaque invocation des algorithmes.
     */
    public ArrayList<Number> situerClick() {

        if (dessin.waitClick()) {

            float lon = dessin.getClickLon();
            float lat = dessin.getClickLat();
            ArrayList<Number> resultat;
            resultat = new ArrayList<>();

            // On cherche le noeud le plus proche. O(n)
            float minDist = Float.MAX_VALUE;
            int noeud = 0;

            for (int num_Node = 0; num_Node < this.listNode.size(); num_Node++) {
                float londiff = (this.listNode.get(num_Node).getLongitude() - lon);
                float latdiff = (this.listNode.get(num_Node).getLatitude() - lat);
                float dist = londiff * londiff + latdiff * latdiff;
                if (dist < minDist) {
                    noeud = num_Node;
                    minDist = dist;
                }
            }

            dessin.setColor(java.awt.Color.red);
            dessin.drawPoint(this.listNode.get(noeud).getLongitude(), this.listNode.get(noeud).getLatitude(), 5);

            resultat.add(lon);
            resultat.add(lat);
            resultat.add(noeud);
            return resultat;
        }

        return null;
    }

    /**
     * Charge un chemin depuis un fichier .path (voir le fichier FORMAT_PATH qui decrit le format)
     * Verifie que le chemin est empruntable et calcule le temps de trajet.
     */
    public int verifierChemin(DataInputStream dis, String nom_chemin) {

        try {

            // Verification du magic number et de la version du format du fichier .path
            int magic = dis.readInt();
            int version = dis.readInt();
            Utils.checkVersion(magic, magic_number_path, version, version_path, nom_chemin, ".path");

            // Lecture de l'identifiant de carte
            /**
             * 1)0x100 INSA -> chemin_0x100_2_139
             * 				-> chemin_0x110_268_418
             * 2)0x300 Reunion	-> chemin_0x300_0_500
             * 3)0x400 midip	-> chemin_0x400_119963_96676
             * 4)0x800 carte fractale	-> chemin_0x800_0_999999
             * 5)0x801 carte spirale	-> chemin_0x801_0_999998
             * 6)0X801 carte spirale	-> chemin_0x801_0_999999
             * 7)0x851 carre dense		-> chemin_0x851_53328_161696
             */
            int path_carte = dis.readInt();
            System.out.println("ID chemin= " + path_carte);

            if (path_carte != this.idcarte) {
                JOptionPane.showMessageDialog(null, "Le chemin du fichier " + nom_chemin + " n'appartient pas a la carte actuellement chargee.");
                return -1;
            }

            int nb_noeuds = dis.readInt();

            // Origine du chemin
            int first_zone = dis.readUnsignedByte();
            int first_Node = Utils.read24bits(dis);

            // Destination du chemin
            int last_zone = dis.readUnsignedByte();
            int last_Node = Utils.read24bits(dis);

            System.out.println();
            System.out.println("Chemin de " + first_zone + ":" + first_Node + " vers " + last_zone + ":" + last_Node);

            int current_zone = 0;
            int current_Node = 0;
            Chemin chemin1 = new Chemin();

            // Tous les noeuds du chemin
            for (int i = 0; i < nb_noeuds; i++) {
                current_zone = dis.readUnsignedByte();
                current_Node = Utils.read24bits(dis);

                chemin1.addNode(this.listNode.get(current_Node));
                this.setChemin(chemin1);
                //System.out.println(" --> " + current_zone + ":" + current_Node);
            }

            if ((current_zone != last_zone) || (current_Node != last_Node)) {
                System.out.println("Le chemin " + nom_chemin + " ne termine pas sur le bon noeud.");
                System.exit(1);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return 0;
    }

    public Chemin getChemin() {
        return chemin;
    }

    public void setChemin(Chemin chemin) {
        this.chemin = chemin;
    }

}
