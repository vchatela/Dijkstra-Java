/**
 * 
 */
package core;

import java.util.ArrayList;

/**
 * @author valentin
 *
 */
public class Chemin {
	private int magic_number;
	private int version;
	private int id;
	private int nb_nodes;
	private int num_noeud_origine;
	private int num_noeud_dest;
	private ArrayList<Node> listNode; 
	
	/**
	 * Cree un chemin avec les infos passes en parametre ..
	 * utile pour le chargement d'un fichier .path
	 * @param magic_number
	 * @param version
	 * @param id identifiant de la carte
	 * @param nb_nodes nombre de noeuds du chemin
	 * @param node_start debut du chemin
	 * @param node_end fin du chemin
	 */
	
	public Chemin(){
		
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

	public int getNum_noeud_origine() {
		return num_noeud_origine;
	}

	public void setNum_noeud_origine(int num_noeud_origine) {
		this.num_noeud_origine = num_noeud_origine;
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
