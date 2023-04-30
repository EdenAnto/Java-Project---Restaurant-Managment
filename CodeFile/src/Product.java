public class Product implements Comparable<Product> {
	private int id;
	private String name;
	private float buyPrice;
	private float sellPrice;
	private myDate expiryDate;

	
	// constructor 
	public Product(int id,String name,float buyPrice,float sellPrice,myDate expiryDate) {
	this.id=id;
	this.name=name;
	this.buyPrice=buyPrice;
	this.sellPrice=sellPrice;
	this.expiryDate=expiryDate;
}
	
	// copy constructor for new instance
	public Product(Product p) {
		this.id=p.getId();
		this.name=new String(p.getName());
		this.buyPrice=p.getBuyPrice();
		this.sellPrice=p.getSellPrice();
		this.expiryDate=p.getExpiryDate(); // change to mydate with constructor
	}

	
	
	// simple get set methods
	public int getId() {
	return id;
	}
	
	public String getName() {
	return name;
	}
	
	public float getBuyPrice() {
	return buyPrice;
	}
	
	public float getSellPrice() {
	return sellPrice;
	}
	
	public myDate getExpiryDate() {
	return expiryDate;
	}
	//
	
	
	//return the data in string to write in the database
	public String printToFile() {
		return id+","+name+","+buyPrice+","+sellPrice;
		}
	
	
	
	// check if the product has valid date
	public boolean isValid(myDate d1) {
		if (expiryDate.compareTo(d1)!=1) // this date is further than expiryDate
			return true;
		return false;
	}

	
	
	// Override for treeMap
	@Override
	public int compareTo(Product p) {
		if (p.getName().equals(name))
			return expiryDate.compareTo(p.getExpiryDate());
		else
			return  (p.getName().compareTo(name));
	}
	
	@Override
	public String toString() {
	return "id: "+id+"name: "+name+"buy price: "+buyPrice+"sell price: "+sellPrice+ "expiry date: "+expiryDate+"\n";
	}
	

	
}