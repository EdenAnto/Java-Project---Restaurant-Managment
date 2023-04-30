public class Table {
	private int id;
	private int numOfDiners;

	// constructor 
	public Table() {
		id=0;
		numOfDiners=0;
	}

	// constructor 
	public Table(int id,int numOfDiners) {
		this.id=id;
		this.numOfDiners=numOfDiners;
	}

	
	
	// simple get set methods
	public int getId() {
		return id;
	}

	public int getNumOfDiners() {
		return numOfDiners;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNumOfDiners(int numOfDiners) {
		this.numOfDiners = numOfDiners;
	}
	//
	
	//Override toString
	@Override
	public String toString() {
		return "{Table:"+id+",Diners:"+numOfDiners+"}";
	}
}
