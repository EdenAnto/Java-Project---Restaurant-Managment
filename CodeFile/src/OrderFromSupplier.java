import java.util.TreeMap;

public class OrderFromSupplier extends Order {

	private TreeMap<Product, Integer> products;
	private Supplier supplier;

	// constructor 
	public OrderFromSupplier() {
		super();
		products=new TreeMap<Product, Integer>();
	}
	// constructor 
	public OrderFromSupplier(int id,myDate expiryDate, float price,TreeMap<Product, Integer> products,Supplier supplier){
		super(id, expiryDate, price);
		this.products=products;
		this.supplier=supplier;
	}

	
	// simple get methods
	public TreeMap<Product, Integer> getProducts() {
		return products;
	}

	public Supplier getSupplier() {
		return supplier;
	}
	//
	
	
	// helper for save the data base -OVERRIDE FATHER'S METHOD-
	public String printToFile() {
		String listOfProducts="{";
		for (Product p : products.keySet()) {
			listOfProducts+=p.getName()+"("+products.get(p)+"),";
		}
		listOfProducts=listOfProducts.substring(0,listOfProducts.length()-1);
		listOfProducts+="}";
		return super.printToFile()+","+supplier.getName()+":"+listOfProducts+"\n";
		}
	
	@Override
	public String toString() {
		return super.toString()+"supplier name: " + supplier.getName() +"Number of prodcts:" + products.size() + "\n";
	}
}
