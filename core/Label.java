package core;

public class Label implements Comparable<Label>{
	private int num_node; //numero du noeud actuel
	private boolean marque;
	private double cout;
	private int pere;//numero du noeud pere
	
	public Label(Node node) {
		this.num_node = node.getNum();
		this.marque = false;//marquage a faux
		this.cout =Float.POSITIVE_INFINITY;// infini
		this.pere = -1;//pas de pere
	}
	
	public String toString(){
		return "Sommet N"+this.num_node+" Cout: "+this.cout;
		
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


	@Override
	// comparaison des Label par rapport a leurs couts respectifs
	public int compareTo(Label o) {
		if (this.cout < o.cout)
			return -1;
		else {
			if (this.cout == o.cout)
				return 0;
				else return 1;
		}
	}
	
	public int hashCode() {
	    return num_node;
	}
	public boolean equals(Object o) {
	    if (o instanceof Label) {
	    	Label other = (Label) o;
	      return (this.cout == other.cout && this.marque == other.marque && this.num_node == other.num_node && this.pere == other.pere);
	    }
	    return false;
	}
	
	
}
