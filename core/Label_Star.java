package core;

/**
 * Created by valentin on 4/29/15.
 */
public class Label_Star extends Label implements Comparable<Label_Star>{

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

    @Override
    // comparaison des Label par rapport a leurs couts respectifs
    public int compareTo(Label_Star o) {
        if (this.getCout()+this.getCout_oiseau() < o.getCout()+this.getCout_oiseau())
            return -1;
        else {
            if (this.getCout() +this.getCout_oiseau() == o.getCout() +this.getCout_oiseau() )
                return 0;
            else return 1;
        }
    }

    public int hashCode() {
        return getNum_node();
    }

    public boolean equals(Object o) {
        if (o instanceof Label_Star) {
            Label_Star other = (Label_Star) o;
            return (this.getCout() == other.getCout() && this.isMarque() == other.isMarque() && this.getNum_node() == other.getNum_node() && this.getPere() == other.getPere());
        }
        return false;
    }

    //Fonctions calcul de distance vol d'oiseau


}
