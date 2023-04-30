public abstract class Order {
	private int id;
	private final myDate date;
	private float price;

	/**
	* ABSTRACT CLASS
	**/
	
	
	// constructor 
	public Order(){
		date=new myDate(false);
	}

	// constructor 
	public Order(int id,myDate expiryDate, float price) {
		this.id=id;
		this.date=expiryDate;
		this.price=price;
	}

	public int getId() {
		return id;
	}

	public myDate getExpiryDate() {
		return date;
	}

	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	// 
	
	
	//return the data in string to write in the database
	public String printToFile() {
		return id+","+date+","+price;
		}
	
	
	//Override toString
	@Override
	public String toString() {
	return "id: "+id+ "date: "+ date + "price: " + price+" "; 
	}
}
