public class Arc{
	private Descripteur descripteur;
	private Node dest;
	// TODO private Segment[] seg;
	
	public Arc(){};
	
	public Arc(Descripteur descripteur){
		this.descripteur = descripteur;
	}
	public void setDescripteur(Descripteur descripteur){
		this.descripteur = descripteur;
	}
}
