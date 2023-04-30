import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

public class Restaurant {
	private int id;
	private String name;
	private TreeMap<Product,Integer> stock;
	private Vector<Dish> dishes;
	private Vector<Order> orders;

	// constructor 
	public Restaurant() {
		this.id=0000;
		this.name="Closed";
		this.stock=new TreeMap<Product,Integer>();
		this.dishes=new Vector<Dish>();
		this.orders=new Vector<Order>();

			// TODO: need?
		
	}

	// constructor 
	public Restaurant(int id,String name,TreeMap<Product,Integer> stock,Vector<Dish> dishes,Vector<Order> orders) {
		this.id=id;
		this.name=name;
		this.stock=stock;
		this.dishes=dishes;
		this.orders=orders;
	}

	//Simple get set methods
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name=name;}

	public Product getProduct(String name) {
		for (Product p : stock.keySet()) {
			if (p.getName().equals(name))
				return p;
		}
		return new Product(0, "", 0 , 0 , new myDate(false));
	}

	public TreeMap<Product, Integer> getStock() {
		return stock;
	}

	public Vector<Dish> getDishes() {
		return dishes;
	}

	public Dish getDish(String name) {
		for (Dish d : dishes) {
			if (d.getName().equals(name))
			return d;
		}
		return null; //TODO think about change null
	}
	
	public Vector<Order> getOrders() {
		return orders;
	}
	
	public Vector<String> getDishesNames() {
		Vector<String> dishesNames=new Vector<String>();

		if (dishes.size()>0) {
			for (Dish d : dishes) {
				dishesNames.add(d.getName());
			}
		}

		return dishesNames;
	}
	
	public HashSet<String> getProductsNames() {
		HashSet<String> productNames=new HashSet<>();

		if (stock.size()>0) {
			for (Product p : stock.keySet()) {
				productNames.add(p.getName());
			}
		}

		return productNames;
	}
	//
	
	
	
	// calculate and return the money balance of the restaurant
	public float getBalance() {
		float balance=0;
		for (Order o : orders) {
			if (o.getClass().equals(new OrderFromSupplier().getClass())) {
				balance-=o.getPrice();
			}
			else 
				balance+=o.getPrice();
		}
		return balance;

	}

	
	
	// delete specific dish
	public boolean deleteDish(String name) {
		Dish d=getDish(name);
		return dishes.remove(d);
	}
	
	
	
	// add order to vector called from read txt file
	public void addOrder(Order o) { 
		this.orders.add(o);
	}
	
	
	
	// add new order and update the stock if needed
	public boolean addNewOrder(Order o) { 
		
		if (o.getClass().equals(new OrderFromTable().getClass())) { // OrderFromTable
			HashMap<String, Integer> products= ((OrderFromTable)o).analyzeOrder();
			for (String name : products.keySet()) {
				if (products.get(name) > countProductsByName(name))
					return false;				
			}
			for (String name : products.keySet()) {
				reduceStock(name, products.get(name));				
			}
		}
		
		else { // OrderFromSupplier
			TreeMap<Product, Integer> products = ((OrderFromSupplier)o).getProducts();
			boolean found;
			for (Product p : products.keySet()) {
				found=false;
				for (Product pRestuarant : stock.keySet()) {
					if (p.getName().equals(pRestuarant.getName()) && p.getExpiryDate().equals(pRestuarant.getExpiryDate()))
					{//change to same date
						// TODO: need?

						stock.put(p, products.get(p)+stock.get(p));
						found=true;
						break;
					}
				}
				if (!found)
					stock.put(p, products.get(p));
			}
			 
		}
		this.orders.add(o);
		return true;
	}
	
	
	
	//helper for addNewOrder//
	public void reduceStock(String name,int quantity) {
		
		int countExist=0;
		HashSet<Product> productsToRemove = new HashSet<Product>();
		for (Product p : stock.keySet()) {
			countExist=0;
			if (quantity==0)
				break;
			if (name.equals(p.getName())) {
				countExist=stock.get(p);
				
				if (quantity>=countExist) 
				productsToRemove.add(p);
				
				else {
					stock.put(p,stock.get(p)-quantity);
					countExist=quantity; // in case there is enough on this element
				}
				
			}
			quantity -=countExist;
		}
		stock.keySet().removeAll(productsToRemove);
	}
	
	
	
	// add product to vector -OVERLOADING-
	public void addProduct(Product p) {
		int currentQuantity=stock.getOrDefault(p,-5);
		if (currentQuantity>0)
			stock.put(p,currentQuantity+1);
		else 
			stock.put(p, 1);
	}

	
	
	// add product to vector -OVERLOADING-
	public void addProduct(Product p,int q) {
			// TODO: 
		// condition for duplicate products???
			this.stock.put(p, q);
	}

	
	
	// add dish to the vector
	public void addDish(Dish d) {
		dishes.add(d);
	}
	
	
	
	// check if there are expired products and remove them from the treeMap
	public void dropExpiry() {
		HashSet<Product> productsToRemove=new HashSet<Product>();
		TreeMap<Product, Integer> productToPrintTree = new TreeMap<Product, Integer>(); 
		myDate now =new myDate(false);
		for (Product p : stock.keySet()) {
			if(!p.isValid(now)) {
				productsToRemove.add(p);
				productToPrintTree.put(p, stock.get(p));
			}
		}
		if (productsToRemove.size()==0)
			System.out.println("All products are valid");
		else {
		stock.keySet().removeAll(productsToRemove);
		System.out.println("Here is the list of the products that has been dropped");
		printProducts(true, productToPrintTree);
		}
	}
	
	
	public int getOrderIDXByID(int id) {
		for (int i=0;i<orders.size();i++) {
			if (id==orders.get(i).getId())
				return i;
		}
	return -1;	
	}
	
	
	// check if there is dishes in the restaurant
	public boolean thereIsDishes() {
		return (dishes.size()>0);
	}
	
	
	
	// by given name, return the product's quantity
	public int countProductsByName(String name) {
		int count=0;
		for (Product p : stock.keySet()) {
			if (name.equals(p.getName()))
					count+=stock.get(p);
		}
		return count;
	}
	
	
	
	// print to console a treeMap of products
	public void printProducts(boolean printDate,TreeMap<Product, Integer> stock) {
		if (stock.size()==0) {
			System.out.println("No products to print");
			return;
		}
		
		if (printDate)
			System.out.println(mmg.fill45("Name:", "Quantity:", "Expiry date:"));
		else
			System.out.println(mmg.fill30("Name:", "Quantity:"));
		
		System.out.println();
		
		if (printDate)
		for (Product p : stock.keySet()) {
			System.out.println(mmg.fill45(p.getName(),""+stock.get(p),p.getExpiryDate().toString()));
		}
		
		else {
			for (Product p : stock.keySet()) {
				System.out.println(mmg.fill30(p.getName(),""+stock.get(p)));
			}
		}
	}
	
	
	@Override
	public String toString() {
		return "Restaurant name: " +name + "restaurant id: " +id + "number of dishes: "+ dishes.size()+ "\n";
	}
	
}
