import java.util.HashMap;

public class OrderFromTable extends Order{
	private HashMap<Dish, Integer> dishes;
	private Table table;
	private String notes;
	
	/**
	* INHERITENCE CLASS
	**/
	
	// constructor 
	public OrderFromTable() {
		super();
		dishes=new HashMap<>();
	}

	// constructor 
	public OrderFromTable(int id,myDate date,HashMap<Dish, Integer> dishes,Table table, String notes) {
		super(id, date, 0);
		float price=0;
		this.table=table;
		this.dishes=dishes;
		for (Dish d : dishes.keySet()) {
			price += dishes.get(d)*d.getSellPrice();
		}
		this.setPrice(price);
		this.notes=notes;
	}
	
	
	// simple get  methods
	public HashMap<Dish, Integer> getDishes() {
		return dishes;
	}
	
	public Table getTable() {
		return table;
	}
	
	public String getNotes() {
		return notes;
	}
	//
	
	
	
	// add dish to the map
	public void addDish(Dish d) {
		if (this.dishes==null) 
			dishes=new HashMap<Dish, Integer>();
		else {
			for (Dish tmpDish : dishes.keySet()) {
				if (tmpDish.getName().equals(d.getName()))
					dishes.put(d, dishes.get(d)+1);
				break;
			}
			
		}
		dishes.put(d, 1);

		}
		
	
	
	// helper for save the data base
	public String printToFile() {
		String listOfDishes="{";
		for (Dish d : dishes.keySet()) {
			listOfDishes+=d.getName()+"("+dishes.get(d)+"),";
		}
		listOfDishes=listOfDishes.substring(0,listOfDishes.length()-1);
		listOfDishes+="}";
		return super.printToFile()+","+table+","+notes+","+listOfDishes+"\n";
		}
	
	
	
	// create a products map of the order -OVERRIDE FATHER'S METHOD-
	public HashMap<String,Integer> analyzeOrder(){
		HashMap<String, Integer> products=new HashMap<String, Integer>();
		int quantity;
		for (Dish d : dishes.keySet()) {
			for (Product p : d.getProducts().keySet()) {
				if(products.containsKey(p.getName()))
					quantity=products.get(p.getName())+(d.getProducts().get(p))*dishes.get(d);
				
				else
					quantity=d.getProducts().get(p)*dishes.get(d);
				
				products.put(p.getName(),quantity);
			}
		}
		return products;
	}


	@Override
	public String toString() {
		return super.toString()+"table id: " + table.getId() +"Number of dishes:" + dishes.size() + "\n";
	}
	
}
