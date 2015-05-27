package core;

/**
 * Created by valentin on 4/29/15.
 *
 */
public class Label_Star extends Label_Generique implements Comparable<Label_Star> {

    private double cout_oiseau;

    public Label_Star(Node node, Node node_dest) {
        super(node);
        this.cout_oiseau = distance(node, node_dest); // calcul du cout a vol d'oiseau
    }

    //Fonctions calcul de distance vol d'oiseau
    public static double distance(Node node, Node node_dest) {
        double rayon_terre = 6378137.0;
        double sinLat = Math.sin(Math.toRadians(node.getLatitude())) * Math.sin(Math.toRadians(node_dest.getLatitude()));
        double cosLat = Math.cos(Math.toRadians(node.getLatitude())) * Math.cos(Math.toRadians(node_dest.getLatitude()));
        double cosLong = Math.cos(Math.toRadians(node.getLongitude() - node_dest.getLongitude()));
        return rayon_terre * Math.acos(sinLat + cosLat * cosLong);
    }

    public String toString() {
        return "Sommet N" + this.getNum_node() + " Cout: " + this.getCout() + " Cout oiseau : " + this.cout_oiseau;
    }

    public double getCout_oiseau() {
        return cout_oiseau;
    }

    public void setCout_oiseau(double cout_oiseau) {
        this.cout_oiseau = cout_oiseau;
    }

    @Override
    public int compareTo(Label_Star lab) {

        int res = (int) (1000.0 * (this.getCout() + this.getCout_oiseau()) - 1000.0 * (lab.getCout() + lab.getCout_oiseau()));
// si egalite, alors plus petite estimation
        if (res == 0) {
            res = (int) (1000.0 * this.getCout_oiseau() - 1000.0 * lab.getCout_oiseau());
        }
        return res;
    }

    public boolean equals(Object o) {
        if (o instanceof Label_Star) {
            Label_Star other = (Label_Star) o;
            return (this.getCout() == other.getCout() && this.isMarque() == other.isMarque() && this.getNum_node() == other.getNum_node() && this.getPere() == other.getPere());
        }
        return false;
    }
}