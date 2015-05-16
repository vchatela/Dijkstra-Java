package core;

/**
 * Created by Joris on 16/05/15.
 */
public class Label_Generique {

    private int num_node; //numero du noeud actuel
    private boolean marque;
    private double cout;
    private int pere;//numero du noeud pere

    public Label_Generique(Node node) {
        this.num_node = node.getNum();
        this.marque = false;//marquage a faux
        this.cout = Float.POSITIVE_INFINITY;// infini
        this.pere = -1;//pas de pere
    }

    public int hashCode() {
        return getNum_node();
    }

    public int getNum_node() {
        return num_node;
    }

    public boolean isMarque() {
        return marque;
    }

    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public double getCout() {
        return cout;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public int getPere() {
        return pere;
    }

    public void setPere(int pere) {
        this.pere = pere;
    }
}
