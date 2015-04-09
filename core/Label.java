package core;

public class Label {
	private int num_node; //numéro du noeud actuel
	private boolean marque;
	private double cout;
	private int pere;//numéro du noeud père
	
	public Label() {
	}

	public int getNum_node() {
		return num_node;
	}

	public void setNum_node(int num_node) {
		this.num_node = num_node;
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
