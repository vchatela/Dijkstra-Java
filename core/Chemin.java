/**
 *
 */
package core;

import base.Dessin;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author valentin
 */
public class Chemin {
    private int magic_number;
    private int version;
    private int id;
    private int nb_nodes;
    private int num_noeud_origin;
    private int num_noeud_dest;
    private ArrayList<Node> listNode;

    /**
     * Cree un chemin avec les infos passes en parametre ..
     * utile pour le chargement d'un fichier .path
     *
     * @param magic_number
     * @param version
     * @param id           identifiant de la carte
     * @param nb_nodes     nombre de noeuds du chemin
     * @param node_start   debut du chemin
     * @param node_end     fin du chemin
     */

    public Chemin(int magic_number, int version, int id, int nb_nodes, int node_start, int node_end) {
        this.magic_number = magic_number;
        this.version = version;
        this.id = id;
        this.nb_nodes = nb_nodes;
        this.num_noeud_origin = node_start;
        this.num_noeud_dest = node_end;
        this.listNode = new ArrayList<Node>();
    }

    /**
     * Cree un chemin allant d'un sommet origine a un sommet destination.
     * Utile pour l'algorithme de Dijikstra
     *
     * @param node_start noeud origine
     * @param node_end   noeud destination
     */
    public Chemin(int node_start, int node_end) {
        this.num_noeud_origin = node_start;
        this.num_noeud_dest = node_end;
        this.listNode = new ArrayList<Node>();
    }

    /**
     * Ajouter un sommet au chemin
     *
     * @param node sommet a ajouter
     */
    public void addNode(Node node) {
        this.listNode.add(node);
        this.nb_nodes += 1;
    }

    /**
     * Entre 2 sommets du graphe il peut y avoir plusieurs aretes ayant des poids differents.
     * Cette fonction permet de choisir l'arete ayant le plus faible cout en distance.
     *
     * @param noeud_dep sommet 1
     * @param noeud_arr sommet 2
     * @return un arc ayant le plus faible cout en distance
     */

    public Arc renvoi_arc_distance(Node noeud_dep, Node noeud_arr) {
        Arc arc = new Arc();
        int num_dest = noeud_arr.getNum(); // on recupere le numero du noeud de dest
        int long_arete = 0;
        int nb_passage = 0;
        //pour chaque voisin du noeud noeud_dep
        for (Arc A : noeud_dep.getArrayListArc()) {
            //On examine les aretes correspondant joignant les 2 noeuds
            //et on ne retient que celle qui a la plus petite largeur
            if (A.getNum_dest() == num_dest) {
                nb_passage++;
                if (nb_passage == 1) {
                    long_arete = A.getLg_arete();
                }
                if (long_arete >= A.getLg_arete()) {
                    long_arete = A.getLg_arete();
                    arc = A;
                }
            }
        }
        if (arc.getNum_dest() != num_dest) {  // si on ne l'a pas trouve
            System.out.println("Erreur chemin");
            return null;
        } else return arc;
    }

    /**
     * Entre 2 sommets du graphe il peut y avoir plusieurs aretes ayant des poids differents.
     * Cette fonction permet de choisir l'arete ayant le plus faible cout en temps.
     *
     * @param noeud_dep sommet 1
     * @param noeud_arr sommet 2
     * @return un arc ayant le plus faible cout en temps
     */

    public Arc renvoi_arc_temps(Node noeud_dep, Node noeud_arr) {
        Arc arc = new Arc();
        int num_dest = noeud_arr.getNum(); // on recupere le numero du noeud de dest
        float temps_arete = 0;
        int nb_passage = 0;
        //pour chaque voisin du noeud noeud_dep
        for (Arc A : noeud_dep.getArrayListArc()) {
            //On examine les aretes correspondant joignant les 2 noeuds
            //et on ne retient que celle qui a la plus petite largeur
            if (A.getNum_dest() == num_dest) {
                nb_passage++;
                if (nb_passage == 1) {
                    temps_arete = ((float) A.getLg_arete()) / ((float) A.getDescripteur().getVitMax());
                }
                if (temps_arete >= ((float) A.getLg_arete()) / ((float) A.getDescripteur().getVitMax())) {
                    temps_arete = ((float) A.getLg_arete()) / ((float) A.getDescripteur().getVitMax());
                    arc = A;
                }
            }
        }
        if (arc.getNum_dest() != num_dest) {  // si on ne l'a pas trouve
            System.out.println("Erreur chemin");
            return null;
        } else return arc;
    }

    /**
     * Affiche le chemin sur la carte
     *
     * @param dessin affichage courant de la carte
     */
    public void tracerChemin(Dessin dessin) {
        float current_long, current_lat;
        //Arc arc=new Arc();
        //System.out.println(" DEBUG -- Nbre noeud du chemin = " + this.getNb_nodes());
        //Pour chaque noeud du chemin
        for (int node = 0; node < this.getListNode().size() - 1; node++) {
            current_long = this.getListNode().get(node).getLongitude();
            current_lat = this.getListNode().get(node).getLatitude();
            //Pour chaque arc du noeud numero node
            //arc = renvoi_arc_temps(this.getListNode().get(node),this.getListNode().get(node+1));
            //on regle la largeur des traits et la couleur de l'affichage
            dessin.setWidth(4);
            dessin.setColor(Color.green);
            //On joint les sommets deux a deux
            dessin.drawLine(current_long, current_lat, this.getListNode().get(node + 1).getLongitude(), this.getListNode().get(node + 1).getLatitude());
        }
    }

    /**
     * Calcule le cout en distance d'un chemin
     */
    public String cout_chemin_distance() {
        float longueur = 0;
        //float temps = 0;
        Arc arc = new Arc();
        // Pour chaque noeud du chemin
        for (int node = 0; node < this.getListNode().size() - 1; node++) {
            arc = renvoi_arc_distance(this.getListNode().get(node), this.getListNode().get(node + 1));
            longueur += ((float) arc.getLg_arete() / 1000);
        }
        return "Distance : " + longueur + " km";
    }

    /**
     * Calcule le cout en temps d'un chemin
     */
    public String cout_chemin_temps() {
        //float longueur =0;
        float temps = 0;
        Arc arc = new Arc();
        // Pour chaque noeud du chemin
        for (int node = 0; node < this.getListNode().size() - 1; node++) {
            arc = renvoi_arc_temps(this.getListNode().get(node), this.getListNode().get(node + 1));
            temps += 60 * ((float) arc.getLg_arete()) / (1000 * ((float) arc.getDescripteur().getVitMax())); // 60 * car en seconde
        }
        return "Temps : " + temps + " mn";
    }

    public int getMagic_number() {
        return magic_number;
    }

    public void setMagic_number(int magic_number) {
        this.magic_number = magic_number;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ArrayList<Node> getListNode() {
        return listNode;
    }

    public void setListNode(ArrayList<Node> listNode) {
        this.listNode = listNode;
    }

    public int getNum_noeud_dest() {
        return num_noeud_dest;
    }

    public void setNum_noeud_dest(int num_noeud_dest) {
        this.num_noeud_dest = num_noeud_dest;
    }

    public int getNum_noeud_origin() {
        return num_noeud_origin;
    }

    public void setNum_noeud_origin(int num_noeud_origin) {
        this.num_noeud_origin = num_noeud_origin;
    }

    public int getNb_nodes() {
        return nb_nodes;
    }

    public void setNb_nodes(int nb_nodes) {
        this.nb_nodes = nb_nodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
