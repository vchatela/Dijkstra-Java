package core;

public class Label_Dijkstra extends Label implements Comparable<Label_Dijkstra> {
    public Label_Dijkstra(Node node) {
        super(node);
    }

    @Override
    // comparaison des Label par rapport a leurs couts respectifs
    public int compareTo(Label_Dijkstra o) {
        if (this.getCout() < o.getCout())
            return -1;
        else {
            if (this.getCout() == o.getCout())
                return 0;
            else return 1;
        }
    }

    public int hashCode() {
        return getNum_node();
    }

    public boolean equals(Object o) {
        if (o instanceof Label_Dijkstra) {
            Label_Dijkstra other = (Label_Dijkstra) o;
            return (this.getCout() == other.getCout() && this.isMarque() == other.isMarque() && this.getNum_node() == other.getNum_node() && this.getPere() == other.getPere());
        }
        return false;
    }


}
