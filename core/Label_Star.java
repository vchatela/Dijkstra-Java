package core;

/**
 * Created by valentin on 4/29/15.
 */
public class Label_Star extends Label {

    private double cout_oiseau;

    public Label_Star(Node node) {
        super(node);
        this.cout_oiseau = Float.POSITIVE_INFINITY;
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


}
