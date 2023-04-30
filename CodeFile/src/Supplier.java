import java.util.TreeMap;

public class Supplier {
	private int id;
	private String name;
	private TreeMap<Product, Integer> products;

	// constructor 
	public Supplier(String name) {
		id=000;
		this.name=name;
		this.products=new TreeMap<Product,Integer>();

			// TODO: need this contructor?
	}
	
	// constructor 
	public Supplier(int id,String name,TreeMap<Product, Integer> products) {
	this.id=id;
	this.name=name;
	this.products=products;
}

	// simple get set methods
	public int getId() {

	return id;
}

	public String getName() {

	return name;
}

	public TreeMap<Product, Integer> getProducts() {
	return products;
}
	//	
	
	// return the product from map by given name
	public Product getProduct(String name) {
		for (Product p : products.keySet()) {
			if (p.getName().equals(name))
				return new Product(mmg.idManager(),p.getName(), p.getBuyPrice() , p.getSellPrice() , new myDate(true));
		}
		return new Product(0, "", 0 , 0 , new myDate(false));
	}

	@Override
	public String toString() {
		return "Supplier name: " +name + "Supplier id: " +id + "number of products: "+ products.size()+ "\n";
	}

}