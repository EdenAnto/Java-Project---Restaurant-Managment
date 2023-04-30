import java.util.TreeMap;

public class Dish {
	private int id;
	private String name;
	private TreeMap<Product, Integer> products;
	private float sellPrice;

	// constructor 
	public Dish() {
		id=0000;
		name="test";
		products=new TreeMap<Product, Integer>();
		sellPrice=333;
	}
	
	// constructor 
	public Dish(int id,String name, TreeMap<Product, Integer> products)
	{
		this.id=id;
		this.name=name;
		this.products=products;
		setSellPrice(calculatePrice());

	}

	// simple get set methods
	public int getId() {

		return id;
	}

	public String getName() {

		return name;
	}

	public float getSellPrice() {

		return sellPrice;
	}
	
	public void setSellPrice(float sellPrice) {
		this.sellPrice = sellPrice;
	}

	public TreeMap<Product, Integer> getProducts() {
		return products;
	}
	//
	
	
	// return String that help to write system details to database file  
	public String printDetails() {
		String dishDetails="";
		for (Product i : products.keySet()) {
			dishDetails += i.getName() + "("+products.get(i)+"),";
		}
		dishDetails=dishDetails.substring(0,dishDetails.length()-1);
		return this.name+": "+getId()+","+dishDetails+"\n";
	}
	
	
	
	// update dish's products treeMap
	public void addProduct(Product p,int q) {
		
		int currentQuantity=products.getOrDefault(p,-5);
		if (currentQuantity>0)
			products.put(p,currentQuantity+q);
		else 
			this.products.put(p, q);
	}
	
	
	
	// summarize the price of the dish by products prices
	private float calculatePrice() {
		float price=0;
		for (Product p : products.keySet()) {
			price+=p.getSellPrice()*products.get(p);
		}
		return price;
	} 
	
	
	
	// override the toString method, return string of the basic fields of the dish
	@Override
	public String toString() {
		return "{"+id+","+name+","+sellPrice+"}";
	}
	

	}
