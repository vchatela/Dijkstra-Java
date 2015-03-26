public class Node {
	private float longitude;
	private float latitude;
	private int number_Successeur;
	private ArrayList<Arc> listArcSuccesseur;
	
	public Node(){
		this.listSuccesseur = new ArrayList<Node>();
	}
	
	public Node(float longitude, float latitude){
		this.longitude=longitude;
		this.latitude=latitude;
		this.number_Successeur = 0;
		this.listSuccesseur = new ArrayList<Node>();
	}
	public ArrayList<Arc> getListArc(){
		return listArcSuccesseur;
	}
	
	public void setLongitude(float f){
		this.longitude = f;
	}
	public void setLatitude(float f){
		this.latitude = f;
	}
	public float getLongitude(){
		return this.longitude;
	}
	public float getLatitude(){
		return this.latitude;
	}
	public void setNumberSuccesseur(int i){
		this.number_Successeur=i;
	}
	public int getNumberSuccesseur(){
		return this.number_successeur;
	}
}
