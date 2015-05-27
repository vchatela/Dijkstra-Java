package core;

/**
 * Created by valentin on 4/29/15.
 *
 */
public class Label extends Label_Generique implements Comparable<Label> {

    public Label(Node node) {
        super(node);
    }

    public Label(Label l) {
        super(l);
    }

    public String toString() {
        return "Sommet n°" + this.getNum_node() + " - Cout=" + this.getCout();
    }

    @Override
    public int compareTo(Label o) {
        if (this.getCout() < o.getCout())
            return -1;
        else {
            if (this.getCout() == o.getCout())
                return 0;
            else return 1;
        }
    }

    public boolean equals(Object o) {
        if (o instanceof Label) {
            Label other = (Label) o;
            return (this.getCout() == other.getCout() && this.isMarque() == other.isMarque() && this.getNum_node() == other.getNum_node() && this.getPere() == other.getPere());
        }
        return false;
    }
}