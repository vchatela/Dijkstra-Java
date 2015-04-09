package core;

public class Label {
	private int num_node; //numéro du noeud actuel
	private boolean marque;
	private double cout;
	private int pere;//numéro du noeud père
	
	public Label() {
	}
	
	public Label(Node node) {
		this.num_node = node.getNum();
		this.marque = false;//marquage à faux
		this.cout =Float.POSITIVE_INFINITY;// infini
		this.pere = -1;//pas de pere
	}
	
	public String toString(){
		return "Sommet N° "+this.num_node+" Cout: "+this.cout;
		
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
