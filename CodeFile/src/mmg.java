import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionListener;


public class mmg {
	
	Restaurant myRestaurant;
	Vector<Supplier> suppliers;
	String dbPath;
	String imgPath;
	File firstBootCheck;

	static int nextId;


	//Constructor
	public mmg() {
		myRestaurant=new Restaurant();
		if(boot())
			loadData();
		}
	
	
	
	// Run the GUI platform
	public void startProgram() { 
		MainMenu window = new MainMenu(this);
		window.mainFrame.setVisible(true);
	}

	
	

	// function which help to print line in same spaces
	public static String fill30(String s1 ,String s2) { 
		String space=" ";
		String sentence=s1;
		for (int i=s1.length(); i<30;i++)
			sentence+=space;
		
		return sentence+s2;
	}

	
	
	// function which help to print line in same spaces
	public static String fill45(String s1 ,String s2,String s3) { 
		String space=" ";
		String sentence=fill30(s1, s2);
		for (int i=sentence.length(); i<45;i++)
			sentence+=space;
		
		return sentence+s3;
	}

	
	
	//Manage logically the id in the system -update largest id
	public static int idManager(int id) {
		if (id>nextId)
			nextId=id;			
		return id;
		
	}

	
	//return new id
	public static int idManager() {
	return ++nextId;
	}



	// update and initial path of the project
	private boolean boot() {
		dbPath = System.getProperty("user.dir");
		if (dbPath.contains("src"))
			dbPath=dbPath.substring(0,dbPath.length()-4);
		imgPath=dbPath+"\\images\\";
		dbPath +="\\data";
		File newDir=new File(dbPath);
		if (!newDir.exists()){
			newDir.mkdirs();
			return false;
		}
		System.out.println("DO NOT CLOSE THIS WINDOW");
		firstBootCheck=new File(dbPath+"\\firstBoot.bin");
		if (firstBootCheck.exists()) {
			firstBootMessage();
		}
		return true;
	}	

	public void firstBootMessage() {
		JPanel panel = new JPanel() {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension( 450, 100);
			}
		};

		//We can use JTextArea or JLabel to display messages
		panel.setForeground(UIManager.getColor("Button.background"));
		JTextArea textArea = new JTextArea("Hello");
		textArea.setForeground(Color.BLUE);
		textArea.setEditable(false);
		panel.setLayout(new BorderLayout());
		panel.add(new JScrollPane(textArea));
		
		JLabel lblNewLabel = new JLabel("Hello and welcome to the restaurant program!", SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(10, 0, 414, 28);
		panel.add(lblNewLabel);
		
		JLabel lblTheSystemRecognized = new JLabel("The system recognized that it might me the first time for using our program. ", SwingConstants.CENTER);
		lblTheSystemRecognized.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTheSystemRecognized.setBounds(0, 35, 450, 37);
		panel.add(lblTheSystemRecognized);
		
		JLabel lblWeJustWant = new JLabel("we just want to inform, your information about the restaurant has been setted", SwingConstants.CENTER);
		lblWeJustWant.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWeJustWant.setBounds(0, 50, 450, 37);
		panel.add(lblWeJustWant);
		
		JLabel lblWeJustWant_2 = new JLabel("and ready to be used.", SwingConstants.CENTER);
		lblWeJustWant_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWeJustWant_2.setBounds(0, 65, 450, 37);
		panel.add(lblWeJustWant_2);
		
		JLabel lblWeJustWant_1 = new JLabel("");
		lblWeJustWant_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWeJustWant_1.setBounds(0, 250, 450, 37);
		panel.add(lblWeJustWant_1);
		
		
		
		int dialogButton = JOptionPane.PLAIN_MESSAGE;
		int dialogResult =JOptionPane.showConfirmDialog(null,
				panel, //Here goes content
				"Message",
				dialogButton, // Options for JOptionPane
				JOptionPane.PLAIN_MESSAGE); // Message type
		
		if(dialogResult == JOptionPane.OK_OPTION){
			if (JOptionPane.showConfirmDialog(null, "Hide this windows in the future?", "",
			        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) 			
			firstBootCheck.delete();
			}
	}
	
	// load data from database using multithreading method
	private void loadData() {
		loadSuppliers();
		File dbRestaurant=new File(dbPath+"\\Restaurant.txt");
		if (dbRestaurant.exists())
		loadRestaurant(dbRestaurant);
	}

	
	
	// set up the restaurant from database
	private void loadRestaurant(File file) {
		String data;
		int idx;
		try {
			Scanner input = new Scanner(file);
			int id;
			input.nextLine(); //Withdraw #Restaurant#
			input.nextLine();
			input.nextLine(); //Withdraw --Details--

			data=input.nextLine();

			idx=data.indexOf(",");
			id=idManager(Integer.parseInt(data.substring(0,idx)));
			myRestaurant.setId(id);
			myRestaurant.setName(data.substring(++idx,data.length()));

			input.nextLine(); //Withdraw --Products--
			input.nextLine();


			data=input.nextLine();
			while (!data.equals("--Dishes--")){
				if(data.equals("")) {
					data=input.nextLine();
					continue;
				}
				Product p1=stringToProduct(data,true);
				idx=data.indexOf(",");
				int quantity=Integer.parseInt(data.substring(1,idx-1));
			
				myRestaurant.addProduct(p1,quantity);
				data=input.nextLine();
			}

			while (!data.equals("--Orders--")){
				data=input.nextLine();
				while (data.equals("")) {
					data=input.nextLine();
				}
				if (data.equals("--Orders--"))
					continue;
				this.myRestaurant.addDish(stringToDish(data));
			}

			while (!data.equals("Suppliers:")){
				data=input.nextLine();
				if (data.equals("Tables:"))
					data=input.nextLine();

				if(data.equals("")) {
					data=input.nextLine();
					continue;
				}
				this.myRestaurant.addOrder(stringToOrderFromTable(data));	
				
			}
			while (input.hasNextLine()){
				data=input.nextLine();
				if(data.equals("")) {
					data=input.nextLine();
					continue;
				}
				this.myRestaurant.addOrder(stringToOrderFromSupplier(data));	

			}

			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	
	// ****multiThreading method**** parallel reading suppliers details and stock
	/**
	* You gave us permission to perform the operation on this method
	**/
	private void loadSuppliers() {
		ExecutorService ex = Executors.newCachedThreadPool();
		this.suppliers = new Vector<Supplier>();
		File dir = new File(dbPath);
		for (File f : dir.listFiles()) {
			if (f.getName().toUpperCase().contains("SUPPLIER") && f.getName().toUpperCase().contains(".TXT")) {
				ex.execute(new SupplierLoader(f, this));
			}
		}
		ex.shutdown();
		while (!ex.isTerminated()){}
	}

	
	
	// reading from txt file row method helper
	 public Product stringToProduct(String data,boolean readDate){
		int id;
		String name="";
		Float buyPrice;
		Float sellPrice;
		myDate expiryDate;

		int idx=data.indexOf(",");
		data=data.substring(++idx,data.length());

		idx=data.indexOf(",");
		id=idManager(Integer.parseInt(data.substring(0,idx)));
		data=data.substring(++idx,data.length());

		idx=data.indexOf(",");
		name=data.substring(0,idx);
		data=data.substring(++idx,data.length());

		idx=data.indexOf(",");
		buyPrice=Float.parseFloat(data.substring(0,idx));
		data=data.substring(++idx,data.length());

		if(readDate) {
			idx=data.indexOf(",");
			sellPrice=Float.parseFloat(data.substring(0,idx));
			data=data.substring(++idx,data.length());
		}
		else
			sellPrice=Float.parseFloat(data);
		
		if(readDate) 
			expiryDate=stringToMyDate(data);
		
		else {
			expiryDate=new myDate(false);// set expiry for month because gregorian is further in month
		}
		return new Product(id, name, buyPrice, sellPrice, expiryDate);

	}

		
		
	// reading from txt file row method helper
	private Dish stringToDish(String data) {
		int idx=data.indexOf(":");
		String dishName="";
		TreeMap<Product,Integer> productsMap= new TreeMap<Product,Integer>();
		dishName =data.substring(0,idx);
		data=data.substring(idx+2);
		idx=data.indexOf(",");
		int id = mmg.idManager(Integer.parseInt(data.substring(0,idx)));
		data=data.substring(idx+1);

				
		if (this.myRestaurant.getDish(dishName)==null);//TODO

		productsMap=stringToProductsMap(data);

		return new Dish(id, dishName, productsMap);

	}

	
	
	// reading from txt file row method helper
	private Order stringToOrderFromTable(String data) {
		int idx=data.indexOf(",");
		int id=idManager(Integer.parseInt(data.substring(0, idx))); 
		data=data.substring(idx+1,data.length());

		idx=data.indexOf(",");
		myDate date=stringToMyDate(data.substring(0, idx));
		data=data.substring(idx+1,data.length());

		idx=data.indexOf(",");
		int newLimit=data.indexOf("}");
		String tableData= data.substring(idx+2,newLimit);
		Table table=stringToTable(tableData);
		data= data.substring(newLimit+2);
		idx=data.indexOf(",");
		String notes=data.substring(0,idx);
		data=data.substring(idx+2,data.length()-1);
		HashMap<Dish, Integer> dishesMap=stringToDishesMap(data);

		return new OrderFromTable(id, date, dishesMap, table, notes);
	}

	
	
	// reading from txt file row method helper
	private Order stringToOrderFromSupplier(String data) {
		Supplier s;
		TreeMap<Product, Integer> productsMap;
		String supplierName="";
		int idx=data.indexOf(",");
		int id=idManager(Integer.parseInt(data.substring(0, idx))); 
		data=data.substring(idx+1,data.length());

		idx=data.indexOf(",");
		myDate date=stringToMyDate(data.substring(0, idx));
		data=data.substring(idx+1,data.length());

		idx=data.indexOf(",");
		float totalPrice=Float.parseFloat(data.substring(0, idx)); 

		idx=data.indexOf(",");
		data=data.substring(idx+1,data.length());

		idx=data.indexOf(":");
		supplierName=data.substring(0,idx);
		data=data.substring(idx+2,data.length()-1);

		productsMap=stringToProductsMap(data);

		s=getSupplier(supplierName);

		return new OrderFromSupplier(id, date, totalPrice, productsMap, s);

	}

	
	
	// write system details to database file  
	public void saveData() throws IOException {
		FileWriter fw = new FileWriter(new File(dbPath+"\\Restaurant.txt"));
		fw.write("##Restaurant##\n\n--Details--\n");
		fw.write(myRestaurant.getId()+","+myRestaurant.getName()+"\n\n--Products--\n");
		saveProductsHelper(fw,myRestaurant.getStock(),true);

		fw.write("\n--Dishes--\n"); 		///print Dishes
		for (Dish d : myRestaurant.getDishes()) {
			fw.write(d.printDetails());
		}
		fw.write("\n");
		fw.write("\n--Orders--\nTables:\n");
		for (Order o : myRestaurant.getOrders()) { ///print tables order
			if (o.getClass().equals(new OrderFromTable().getClass()))
				fw.write(o.printToFile());
		}	

		fw.write("\n");

		fw.write("Suppliers:\n");
		for (Order o : myRestaurant.getOrders()) { ///print suppliers order
			if (o.getClass().equals(new OrderFromSupplier().getClass()))
				fw.write(o.printToFile());
		}
		fw.close();

	}

	

	// writing to txt file method helper
	public void saveProductsHelper(FileWriter fw,TreeMap<Product,Integer> stock, boolean printDate) throws IOException {
		String dataLine;

		Set<Product> products= stock.keySet();
		if (printDate) {
			stock=myRestaurant.getStock();
			for (Product p : products) {
				dataLine="@"+stock.get(p)+"@,"+p.printToFile()+","+p.getExpiryDate()+"\n";
				fw.write(dataLine);
			}
		}
		else 
			for (Product p : products) {
				dataLine="@999@,"+p.printToFile()+"\n";
				fw.write(dataLine);
			}
	}

	
	
	// reading from txt file method helper
	private myDate stringToMyDate(String data) {
		int year=Integer.parseInt(data.substring(data.length()-4,data.length()));
		int month=Integer.parseInt(data.substring(3,5));
		int day=Integer.parseInt(data.substring(0,2));
		return new myDate(day,month,year);
	}

	
	
	// reading from txt file method helper
	private Table stringToTable(String data) {
		int idxL=data.indexOf(":");
		int idxR=data.indexOf(",");
		int tableNum=Integer.parseInt(data.substring(idxL+1,idxR));
		data=data.substring(idxR+1,data.length());

		idxL=data.indexOf(":");
		int numOfDiners=Integer.parseInt(data.substring(idxL+1,data.length()));
		return new Table(tableNum, numOfDiners);
	}

	
	
	// reading from txt file method helper
	private HashMap<String, Integer> stringToMapHelper(String data) {
		HashMap<String, Integer> map=new HashMap<String,Integer>();
		int idxL,idxR,quantity=0;
		String name="";
		while(!data.equals("")) {
			idxL=data.indexOf("(");
			idxR=data.indexOf(")");
			name=data.substring(0,idxL);
			quantity=Integer.parseInt(data.substring(idxL+1,idxR));
			map.put(name, quantity);
			data=data.substring(idxR);
			if (data.length()<=1)
				break;
			data=data.substring(2);
		}
		return map;
	}

	
	
	// reading from txt file method helper
	private HashMap<Dish, Integer> stringToDishesMap(String data) {
		HashMap<Dish, Integer> map=new HashMap<Dish,Integer>();
		HashMap<String, Integer> tmpMap=stringToMapHelper(data);
		for (String s : tmpMap.keySet()) {
			map.put(this.myRestaurant.getDish(s),tmpMap.get(s));
		}
		return map;
	}

	
	
	// reading from txt file method helper
	private TreeMap<Product, Integer> stringToProductsMap(String data) {
		TreeMap<Product, Integer> map=new TreeMap<Product,Integer>();
		HashMap<String, Integer> tmpMap=stringToMapHelper(data);
		for (String s : tmpMap.keySet()) {
			map.put(this.myRestaurant.getProduct(s),tmpMap.get(s));
		}
		return map;
	}

	
	
	//by given name return the supplier from suppliers vector
	public Supplier getSupplier(String name) {
		for (Supplier s : suppliers) {
			if (name.equals(s.getName()))
				return s;
		}
		return new Supplier(name);
	}

	
	
	//return a set of suppliers name
	public HashSet<String> getSuppliersNames() {
		HashSet<String> names=new HashSet<String>();
		if (suppliers.size()==0)
			names.add("");
		for (Supplier s : suppliers) {
			names.add(s.getName());
		}
		return names;
	}

	
	
	//return suppliers product's
	public HashSet<String> getSuppliersProducts(Supplier s) {
		HashSet<String> products=new HashSet<>();
		if (suppliers.size()==0)
			products.add("");
		for (Product p : s.getProducts().keySet()) {
			products.add(p.getName());
		}
		return products;
	}

	
	
	//Console menu 
	public boolean consoleMenu() throws Exception {
		Scanner inputScanner = new Scanner(System.in);
		int decision = 1;
		while (decision != 0) {
			decision = consoleMenuHelper(inputScanner);
			switch (decision) {
				case 1:
					System.out.println();
					slowPrint(". . . . . . . . \n");
					printProductsPlatform();
					System.out.println();
					break;
				case 2:
					slowPrint(". . . . . . . . \n");
					printOrderFromSupplierPlatform(inputScanner);
					System.out.println();
					break;
				case 3:
					slowPrint(". . . . . . . . \n");
					printDishDetailsPlatform(inputScanner);
					System.out.println();
					break;
				case 4:
					slowPrint(". . . . . . . . \n");
					setDiscoutPlatform(inputScanner);
					System.out.println();
					break;
				case 5:
					slowPrint(". . . . . . . . \n");
					printCommonDishesPlatform();
					System.out.println();
					break;
				case 6:
					slowPrint(". . . . . . . . \n");
					myRestaurant.dropExpiry();
					System.out.println();
					break;
				case 7:
					JOptionPane.showMessageDialog(null, "Main menu opened, check maybe he is minimized");
					return true;
				case 8:
					decision = 0;
			}
		}
		return false;
	}

	
	
	//helper for console menu 
	public int consoleMenuHelper(Scanner s) {
		String input="";
		slowPrint(". . . . . . . . \n");
		System.out.println("Hello! please write the number of your selection:");
		System.out.println("1. Print all products on restaurant");
		System.out.println("2. Print order from supplier");
		System.out.println("3. Print dish details");
		System.out.println("4. Set discount for a table");
		System.out.println("5. Print common dish list");
		System.out.println("6. Drop expiry items");
		System.out.println("7. Go to main screen");
		System.out.println("8. Save&Exit\n");
		input =s.nextLine();
		int choice;
		try {
			choice=Integer.parseInt(input);
			if(choice >= 1 && choice <= 8 )
				return choice;
			else {
				throw new Exception();
			}
		} catch (Exception e) {
			consoleMenuHelper(s);
			}
		return 10;

	}

	
	
	// print string slow (for design needs)
	public static void slowPrint(String output) {
	    for (int i = 0; i<output.length(); i++) {
	      char c = output.charAt(i);
	      System.out.print(c);
	      try {
	        TimeUnit.MILLISECONDS.sleep(50);
	      }
	      catch (Exception e) {
	      }
	    }
	}

	
	
	// print dish to console
	public boolean PrintDish()
	{
		if (!myRestaurant.thereIsDishes()) {
			System.out.println("There is no dishes at the restaurant");
			return false;
		}
		Vector<String> dishes= myRestaurant.getDishesNames();
		int i=1;
		System.out.println("There is the list of the dishes in the restaurant:");
		for (String dish : dishes) {
			System.out.println(i+". "+ dish);
			i++;
		}
		return true;
	}

	
	
	//Console menu: set a special discount for table from order
		public void setDiscoutPlatform(Scanner s) {
			System.out.println("Please enter table id , to exit insert 'exit'");
			String input="";
			int choice;
			do {
				input = s.nextLine();
				if (input.equalsIgnoreCase("EXIT"))
					return;	
				try {
					choice=Integer.parseInt(input);
					if (choice>0 && choice< 16)// 15 tables at the restaurant
					break;
					throw new Exception();

				} catch (Exception e) {
					System.out.println("Wrong input, please insert again , to exit insert 'exit'");
					}
			}
				while (true);	
			
			System.out.println(mmg.fill45("Id:","Price:","Date:"));
			for (Order o : myRestaurant.getOrders()) {
				if (o.getClass().equals(new OrderFromTable().getClass())  &&  ((OrderFromTable)o).getTable().getId()==choice   ) {
					System.out.println(mmg.fill45(""+o.getId(),""+o.getPrice(),""+o.getExpiryDate().toString()));
				}
			}
			int orderIDX;
			Order tmpOrder;
			do {
				input = s.nextLine();
				if (input.equalsIgnoreCase("EXIT"))
					return;	
				try {
					choice=Integer.parseInt(input);
					orderIDX=myRestaurant.getOrderIDXByID(choice);
					if (orderIDX!= -1) {
					tmpOrder= myRestaurant.getOrders().get(orderIDX);
					break;
					}
					throw new Exception();

				} catch (Exception e) {
					System.out.println("Wrong input, please insert again , to exit insert 'exit'");
					}
			}
				while (true);	
			
			System.out.println("How much discount you want to apply?");
			float discount;
			do {
				input = s.nextLine();
				if (input.equalsIgnoreCase("EXIT"))
					return;	
				try {
					discount=Float.parseFloat(input);
					if (discount >= 0 && discount <= 100)
					break;
					throw new Exception();

				} catch (Exception e) {
					System.out.println("Wrong input, please insert again , to exit insert 'exit'");
					}
			}
				while (true);	
			final DecimalFormat df = new DecimalFormat("0.00");
			Float newPrice=  Float.parseFloat((df.format(tmpOrder.getPrice()*((100-discount)/100))));

			tmpOrder.setPrice(newPrice);
			
			System.out.println("Discount has been set, order number "+choice+" new price: "+newPrice);
			
		}

	
	
	//Console menu: prints the dishes list sorted by the number of times the dish was ordered
	public void printCommonDishesPlatform() {
		if (!myRestaurant.thereIsDishes()) {
			System.out.println("There is no dishes at the restuarant");
			return;
		}
		HashMap<Dish, Integer> CommonDishes= new HashMap<Dish,Integer>();
		
		for (Dish d : myRestaurant.getDishes()) {
			CommonDishes.put(d, 0);
		}
		
		for (Order o : myRestaurant.getOrders()) {
			if (o.getClass().equals(new OrderFromTable().getClass())) {
				HashMap<Dish, Integer> tmpMap=  ((OrderFromTable)o).getDishes();
				for (Dish d : tmpMap.keySet()) {
					CommonDishes.put(d, CommonDishes.get(d)+tmpMap.get(d));
				}
			}	
		}		

		 List<Map.Entry<Dish, Integer> > list = 
				 new LinkedList<Map.Entry<Dish, Integer> >(CommonDishes.entrySet());
		 
		 Collections.sort(
		            list,
		            (i1,i2) -> i2.getValue().compareTo(i1.getValue()));
		
		System.out.println(mmg.fill30("Name:", "Quantity:"));
		for (int i=0;i<list.size();i++)
		{
			System.out.println(fill30(((i+1)+". "+list.get(i).getKey().getName()),""+list.get(i).getValue()));
		}
			
	}

	
	
	//Console menu: print specific order from supplier
	public void printOrderFromSupplierPlatform(Scanner s) throws Exception {
		boolean flag =false;
		for (Order o : myRestaurant.getOrders()) {
			if (o.getClass().equals(new OrderFromSupplier().getClass())) {
				flag=true;
				break;
			}
		}
		
		if (!flag) {
			System.out.println("There is no orders to print");
			return;
		}
		HashMap<Integer,OrderFromSupplier> orderFromSu =new HashMap<Integer,OrderFromSupplier>();
		System.out.println("Please insert order's id to print, if you want to exit insert 'Exit'");
		for (Order o : myRestaurant.getOrders()) {
			if (o.getClass().equals(new OrderFromSupplier().getClass())) {
				orderFromSu.put(o.getId(),(OrderFromSupplier)o);
				System.out.println(mmg.fill45(""+o.getId(), ((OrderFromSupplier)o).getSupplier().getName(), o.getExpiryDate().toString()));

			}
		}	
		
		int choice;
		do {
			String input = s.nextLine();
			if (input.toUpperCase().equals("EXIT"))
				return;	
			try {
				choice=Integer.parseInt(input);
				if (orderFromSu.keySet().contains(choice))
				break;
				throw new Exception();

			} catch (Exception e) {
				System.out.println("Wrong input, please insert again");
				}
		} while (true);
		OrderFromSupplier order =orderFromSu.get(choice);
		
		System.out.println("\nOrder total price: "+order.getPrice() + "\nHere is what you ordered:\n");
		myRestaurant.printProducts(false,order.getProducts());

	}

	
	
	//Console menu: prints all the products that are in the restaurant
	public void printProductsPlatform() {
		System.out.println("Here is a list of the products on restaurant:");
		myRestaurant.printProducts(true,myRestaurant.getStock());
	
	}

	
	
	//Console menu: print specific dish from dishes vector
	private void printDishDetailsPlatform(Scanner input)
	{
		if (!PrintDish())
			return;
		System.out.println("Please write the name of the dish that you want to see the details");
		String name;
		name = input.nextLine();
		Dish d=myRestaurant.getDish(name);

		while(d==null) {
			if (name.toUpperCase().equals("EXIT"))
				return;
			System.out.println("your input is wrong , please try again");
			name = input.nextLine();
			d= myRestaurant.getDish(name);
		}
		int i=1;
		System.out.println(mmg.fill45("Name:", "Quantity:","Sell price:"));
		for (Product p:d.getProducts().keySet()) {
			System.out.println(mmg.fill45(". "+p.getName(), ""+d.getProducts().get(p),""+d.getSellPrice()));
			i++;
		}


	}
	
}


// Runnable class for ****multiThreading method**** 


class SupplierLoader implements Runnable {
	File file;
	mmg m;

	public SupplierLoader(File f, mmg m) {
		this.file = f;
		this.m = m;
	}


	@Override
	public void run() {
		try {
			Scanner input = new Scanner(file);
			TreeMap<Product, Integer> products = new TreeMap<Product, Integer>();
			int id;
			String name = "";
			String data = "";

			input.nextLine(); //Withdraw #Supplier#
			data = input.nextLine();

			int idx = data.indexOf(",");
			id = mmg.idManager(Integer.parseInt(data.substring(0, idx)));
			name = data.substring(++idx, data.length());


			while (input.hasNextLine()) {
				data = input.nextLine();
				products.put(m.stringToProduct(data, false), 999);
			}
			m.suppliers.add(new Supplier(id, name, products));
			input.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}


	}
	
	
}






