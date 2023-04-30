import java.awt.*;
import javax.swing.*;

import javax.swing.table.TableColumn;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;
import java.awt.event.ActionEvent;

/**
* The GUI platform
* this class contains 4 frames of the system.
**/

public class MainMenu {
	private mmg manager;
	public JFrame mainFrame;
	public JFrame NewOrderFromSupplierFrame,addDishframe,NewOrderFromTableFrame;
	private JTable orderFromTableDishTable, orderFromSupplierTable, DishTable;
	private JScrollPane scrollPaneTable,scrollPaneSupplier, scrollPaneDish;
	private TextField textFieldNotes;
	private JComboBox<Object> comboBoxTableDiners,comboBoxID,comboBoxQuantity;
	public int tableSize;
	private JLabel lblReport,lblLoading,lblSupplier;
	private Timer timerPL,timerTable,timerSupplier;
	private JButton btnPLReport;
	private Dish newDish;
	private Image icon;

	// for the comboBox   
	private	String[] zeroToFifteen = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11", "12", "13", "14", "15" };
	private String[] oneToFifteen = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11", "12", "13", "14", "15" };

	// constructor
	public MainMenu(mmg m1) {
		manager=m1;
		icon = Toolkit.getDefaultToolkit().getImage(manager.imgPath+"Main_Icon.jpeg");

		initializeMainFrame();
	}

	// Main frame 
	private void initializeMainFrame() {

		mainFrame = new JFrame("Menu");
		mainFrame.setBounds(450, 200, 450, 356);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setIconImage(icon);

		mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(mainFrame,
						"     Exit without saving?", "Warning",
						JOptionPane.YES_NO_OPTION,JOptionPane.NO_OPTION) == JOptionPane.YES_OPTION){
					System.exit(0);
				}
				else
				{
					mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});

		timerPL = new Timer(2500,new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e){
				lblReport.setVisible(false);
				btnPLReport.setEnabled(true);
				
			}
		});
		timerPL.setRepeats(false);

		JLabel lblRestaurantName = new JLabel("",SwingConstants.CENTER);
		lblRestaurantName.setFont(new Font("Arial", Font.PLAIN, 18));
		lblRestaurantName.setBounds(10, 11, 414, 41);
		mainFrame.getContentPane().add(lblRestaurantName);
		lblRestaurantName.setText("Restaurant - "+ manager.myRestaurant.getName());
		
		lblReport = new JLabel("");
		lblReport.setBounds(257, 237, 167, 14);
		mainFrame.getContentPane().add(lblReport);
		lblReport.setVisible(false);
		
		JButton btnOrderFromSupplier = new JButton("Order From Supplier");
		btnOrderFromSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblReport.setVisible(false);
				mainFrame.setVisible(false);
				initializeNewOrderFromSupplierFrame();
				NewOrderFromSupplierFrame.setVisible(true);
			}
		});
		btnOrderFromSupplier.setBounds(10, 63, 169, 52);
		mainFrame.getContentPane().add(btnOrderFromSupplier);
		
		JButton btnOrderFromTable = new JButton("Order From Table");
		btnOrderFromTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblReport.setVisible(false);
				mainFrame.setVisible(false);
				initializeNewOrderFromTableFrame();
				NewOrderFromTableFrame.setVisible(true);
				}
		});
		btnOrderFromTable.setBounds(255, 63, 169, 52);
		mainFrame.getContentPane().add(btnOrderFromTable);
		
		JButton btnMoreOption = new JButton("More Options");
		btnMoreOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblReport.setVisible(false);
				mainFrame.setVisible(false);
				try {
					if (!manager.consoleMenu())
						try {
							manager.saveData();
							JOptionPane.showMessageDialog(null, "Data saved succesfully! thank you for using our program!");
							System.exit(0);

						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					else
						mainFrame.setVisible(true);
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnMoreOption.setBounds(255, 143, 169, 52);
		mainFrame.getContentPane().add(btnMoreOption);

		
		btnPLReport = new JButton("P&L Report");
		btnPLReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPLReport.setEnabled(false);
				float balance= manager.myRestaurant.getBalance();
				final DecimalFormat df = new DecimalFormat("0.00");
				lblReport.setText("Balance is: " +df.format(balance));
				lblReport.setVisible(true);
				if (balance >= 0)
					lblReport.setForeground(Color.green);
				else
					lblReport.setForeground(Color.red);
				lblReport.setVisible(true);

				timerPL.start();

			}
		});
		btnPLReport.setBounds(10, 220, 169, 52);
		mainFrame.getContentPane().add(btnPLReport);
		
		JButton btnSaveExit = new JButton("Save&Exit");
		btnSaveExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblReport.setVisible(false);
				mainFrame.setVisible(false);
				try {
					manager.saveData();
					JOptionPane.showMessageDialog(null, "Data saved succesfully! thank you for using our program!");
				} catch (IOException e1) {
					//// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
		btnSaveExit.setBounds(167, 283, 100, 23);
		mainFrame.getContentPane().add(btnSaveExit);
		
		JButton btnAddDish = new JButton("Add Dish");
		btnAddDish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.setVisible(false);
				initializeAddDishFrame();
				addDishframe.setVisible(true);
			}
		});
		btnAddDish.setBounds(10, 143, 169, 52);
		mainFrame.getContentPane().add(btnAddDish);
		
	
		if(manager.myRestaurant.getId()==0) { // for empty database
			btnAddDish.setEnabled(false);
			btnMoreOption.setEnabled(false);
			btnSaveExit.setEnabled(false);
			btnOrderFromSupplier.setEnabled(false);
			btnOrderFromTable.setEnabled(false);
			btnPLReport.setEnabled(false);
			mainFrame.setVisible(true);
			JOptionPane.showMessageDialog(null, "Missing folder data or restaurant file");

		}		

		
	}
	
	
	
	// Order from table frame 
	private void initializeNewOrderFromTableFrame() {
		NewOrderFromTableFrame = new JFrame();
		NewOrderFromTableFrame.setTitle("New Order");
		NewOrderFromTableFrame.setBounds(450, 200, 550, 383);
		NewOrderFromTableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		NewOrderFromTableFrame.getContentPane().setLayout(null);
		NewOrderFromTableFrame.setIconImage(icon);

		
		NewOrderFromTableFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(NewOrderFromTableFrame,
						"     Exit without saving?", "Warning",
						JOptionPane.YES_NO_OPTION,JOptionPane.NO_OPTION) == JOptionPane.YES_OPTION){
					System.exit(0);
				}
				else
				{
					NewOrderFromTableFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});


		JLabel lblDishEmpty = new JLabel("Dishes list are empty",SwingConstants.CENTER);
		 lblDishEmpty.setVisible(false);
		 lblDishEmpty.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		 lblDishEmpty.setForeground(Color.RED);
		 lblDishEmpty.setBounds(0, 124, 310, 80);
		 NewOrderFromTableFrame.getContentPane().add(lblDishEmpty);
		 
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewOrderFromTableFrame.setVisible(false);
				mainFrame.setVisible(true);
			}
		});
		btnBack.setBounds(435, 310, 89, 23);
		NewOrderFromTableFrame.getContentPane().add(btnBack);
		
	
		
		JLabel lblTableID = new JLabel("Table ID:");
		lblTableID.setBounds(358, 11, 89, 14);
		NewOrderFromTableFrame.getContentPane().add(lblTableID);
		

		comboBoxID = new JComboBox<Object>(oneToFifteen);
		comboBoxID.setBounds(478, 7, 46, 23);
		NewOrderFromTableFrame.getContentPane().add(comboBoxID);
		
		comboBoxTableDiners = new JComboBox<Object>(oneToFifteen);
		comboBoxTableDiners.setBounds(478, 46, 46, 23);
		NewOrderFromTableFrame.getContentPane().add(comboBoxTableDiners);
		
		
		JLabel lblTableDiners = new JLabel("Num of diners:");
		lblTableDiners.setBounds(358, 50, 89, 14);
		NewOrderFromTableFrame.getContentPane().add(lblTableDiners);

	    
		
		comboBoxQuantity = new JComboBox<Object>(zeroToFifteen);
		comboBoxQuantity.setBounds(478, 7, 46, 23);
		
		

		 Object[] columns= {"Dish","Quantity"};
		 orderFromTableDishTable = new JTable(dishOrderHelper(manager.myRestaurant.getDishesNames()), columns)
		 {
			 @Override
			 public boolean isCellEditable(int row, int column){
				 return column==1;
			 }
		 };
		 if (tableSize==0)
			 lblDishEmpty.setVisible(true);
		 TableColumn quantityColumn = orderFromTableDishTable.getColumnModel().getColumn(1);
		

		 quantityColumn.setCellEditor(new DefaultCellEditor(comboBoxQuantity));
		 
		 //JScrollPane
		 scrollPaneTable = new JScrollPane(orderFromTableDishTable);//
		 scrollPaneTable.setBounds(0, 0, 310, 346);
		 scrollPaneTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		 orderFromTableDishTable.setFillsViewportHeight(true);
		 NewOrderFromTableFrame.getContentPane().add(scrollPaneTable);

		 
		 lblLoading = new JLabel(new ImageIcon(manager.imgPath+"Loading_Snail.gif"));
		 lblLoading.setBounds(372, 219, 126, 80);
		 NewOrderFromTableFrame.getContentPane().add(lblLoading);
		 lblLoading.setVisible(false);
		 
		 timerTable = new Timer(2000,new ActionListener() {
				@Override
				public void actionPerformed (ActionEvent e){
					lblLoading.setVisible(false);
					JOptionPane.showMessageDialog(null, "Order saved successfully!");
					btnBack.setEnabled(true);
					btnBack.doClick();
				}
			});
		 timerTable.setRepeats(false);
			
		 
		 JButton btnSave = new JButton("Save");
		 btnSave.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 Order o=convertTableToOrder();
				 if(o!=null) {
					 if(manager.myRestaurant.addNewOrder(o)) {
						lblLoading.setVisible(true);
						btnBack.setEnabled(false);
						btnSave.setEnabled(false);
						comboBoxTableDiners.setEnabled(false);
						comboBoxID.setEnabled(false);
						textFieldNotes.setEnabled(false);
						 orderFromTableDishTable.setEnabled(false);

						timerTable.start();
					 }
					 
					 else {
					 JOptionPane.showMessageDialog(null, "Error - not enough products to prepare the order");
					 btnBack.doClick();
					 }
				 }
				 else {
					 JOptionPane.showMessageDialog(null, "Ilegal form - try again!");
					 btnBack.doClick();
				 }

			 }
		 });
		 btnSave.setBounds(336, 310, 89, 23);
		 NewOrderFromTableFrame.getContentPane().add(btnSave);
		 
		 JLabel lblGeneralNotes = new JLabel("General notes:");
		 lblGeneralNotes.setBounds(358, 89, 89, 14);
		 NewOrderFromTableFrame.getContentPane().add(lblGeneralNotes);
		 
		 textFieldNotes = new TextField();
		 textFieldNotes.setBounds(358, 109, 166, 68);
		 NewOrderFromTableFrame.getContentPane().add(textFieldNotes);
		 
		
		 
		}
	
	
	
	// Creating new dish frame
	private void initializeAddDishFrame() {
		addDishframe = new JFrame();
		addDishframe.setTitle("Add dish");
		addDishframe.setBounds(450, 200, 550, 383);
		addDishframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addDishframe.getContentPane().setLayout(null);
		addDishframe.setIconImage(icon);
		addDishframe.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(addDishframe,
						"     Exit without saving?", "Warning",
						JOptionPane.YES_NO_OPTION,JOptionPane.NO_OPTION) == JOptionPane.YES_OPTION){
					System.exit(0);
				}
				else
				{
					addDishframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
		JLabel lblPrice = new JLabel("");
		lblPrice.setBounds(382, 186, 113, 36);
		addDishframe.getContentPane().add(lblPrice);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addDishframe.setVisible(false);
				mainFrame.setVisible(true);
			}
		});
		btnBack.setBounds(435, 310, 89, 23);
		addDishframe.getContentPane().add(btnBack);
		
		JComboBox<Object> comboBoxQuantity = new JComboBox<Object>(zeroToFifteen);
		 Object[] columns= {"Product","Quantity"};
		 
			DishTable=new JTable(productToMatrix(false,null), columns)
			 {
				 @Override
				 public boolean isCellEditable(int row, int column){
					 return column==1;
				 }
			 };
				 
		 scrollPaneDish = new JScrollPane(DishTable);//
		 scrollPaneDish.setBounds(0, 0, 310, 346);
		 scrollPaneDish.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		 TableColumn quantityColumn = DishTable.getColumnModel().getColumn(1);
		 quantityColumn.setCellEditor(new DefaultCellEditor(comboBoxQuantity));	
		 DishTable.setFillsViewportHeight(true);
		 addDishframe.getContentPane().add(scrollPaneDish);/////////////

		
		 JButton btnSave = new JButton("Save");
		 btnSave.setEnabled(false);
		 btnSave.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 manager.myRestaurant.addDish(newDish);
				 JOptionPane.showMessageDialog(null, "Dish added successfully!");
				 addDishframe.setVisible(false);
				 mainFrame.setVisible(true);
			 }
		 });
		 btnSave.setBounds(336, 310, 89, 23);
		 addDishframe.getContentPane().add(btnSave);
		 
		 JLabel lblName = new JLabel("Name:");
		 lblName.setBounds(351, 29, 46, 14);
		 addDishframe.getContentPane().add(lblName);
		 
		 JTextField textFieldName = new JTextField();
		 textFieldName.setBounds(394, 26, 130, 20);
		 addDishframe.getContentPane().add(textFieldName);
		 textFieldName.setColumns(10);
		 
		 JButton btnCalculatePrice = new JButton("Calculate");
		 btnCalculatePrice.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		newDish=convertTableToDish(textFieldName);
		 		if (newDish==null) {
					btnBack.doClick();
					return;
				}
		 		lblPrice.setText(""+newDish.getSellPrice());
		 		lblPrice.setEnabled(true);
		 		btnCalculatePrice.setEnabled(false);
		 		btnSave.setEnabled(true);
		 		DishTable.setEnabled(false);
		 	}
		 });
			btnCalculatePrice.setBounds(391, 151, 89, 23);
			addDishframe.getContentPane().add(btnCalculatePrice);
	}
	
	
	
	// Order from supplier frame 
	private void initializeNewOrderFromSupplierFrame() {
		NewOrderFromSupplierFrame = new JFrame();
		NewOrderFromSupplierFrame.setTitle("Order from supplier");
		NewOrderFromSupplierFrame.setBounds(450, 200, 550, 383);
		NewOrderFromSupplierFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		NewOrderFromSupplierFrame.getContentPane().setLayout(null);

		NewOrderFromSupplierFrame.setIconImage(icon);

		NewOrderFromSupplierFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				if (JOptionPane.showConfirmDialog(NewOrderFromSupplierFrame,
						"     Exit without saving?", "Warning",
						JOptionPane.YES_NO_OPTION,JOptionPane.NO_OPTION) == JOptionPane.YES_OPTION){
					System.exit(0);
				}
				else
				{
					NewOrderFromSupplierFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
		 Object[] columns= {"Product","Quantity"};

		 JButton btnRst = new JButton("Reset");
		 btnRst.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent e) {
				 NewOrderFromSupplierFrame.setVisible(false);
				 initializeNewOrderFromSupplierFrame();

				 NewOrderFromSupplierFrame.setVisible(true);
			 }
		 });
		btnRst.setBounds(423, 72, 101, 23);
		btnRst.setEnabled(false);
		NewOrderFromSupplierFrame.getContentPane().add(btnRst);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewOrderFromSupplierFrame.setVisible(false);
				mainFrame.setVisible(true);
			}
		});
		btnBack.setBounds(435, 310, 89, 23);
		NewOrderFromSupplierFrame.getContentPane().add(btnBack);
		
		 
		JLabel lblSupplierName = new JLabel("Supplier:");
		lblSupplierName.setBounds(360, 14, 68, 14);
		lblSupplierName.setFont(new Font("Arial", Font.PLAIN, 14));
		NewOrderFromSupplierFrame.getContentPane().add(lblSupplierName);
		 
		JComboBox<String> comboBoxSuppliersNames = new JComboBox<String>();
		SetToComboBox(comboBoxSuppliersNames,manager.getSuppliersNames());
		comboBoxSuppliersNames.setBounds(423, 11, 101, 23);
		NewOrderFromSupplierFrame.getContentPane().add(comboBoxSuppliersNames);
		
		JComboBox<Object> comboBoxQuantity = new JComboBox<Object>(zeroToFifteen);
		
		
		JButton btnSave = new JButton("Save");
		 btnSave.setEnabled(false);
		 btnSave.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		Order o=convertTableToOrderFromSupplier(manager.getSupplier((String)comboBoxSuppliersNames.getSelectedItem()));
				 if (o!=null) {
					 manager.myRestaurant.addNewOrder(o);
					 lblSupplier.setVisible(true);
					 btnBack.setEnabled(false);
					 btnSave.setEnabled(false);
					 btnRst.setEnabled(false);
					 comboBoxSuppliersNames.setEnabled(false);
					 orderFromSupplierTable.setEnabled(false);
					 timerSupplier.start();
				 }
				 else {
					 JOptionPane.showMessageDialog(null, "Error - You must choose at least one product");
				 }
	 		}
		 });
		 btnSave.setBounds(336, 310, 89, 23);
		 NewOrderFromSupplierFrame.getContentPane().add(btnSave);
		
		
		
		JButton btnStartOrder = new JButton("Start order");
		btnStartOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSave.setEnabled(true);
				btnRst.setEnabled(true);
				btnStartOrder.setEnabled(false);
				comboBoxSuppliersNames.setEnabled(false);
				orderFromSupplierTable=new JTable(productToMatrix(true,(String)comboBoxSuppliersNames.getSelectedItem()), columns)
				 {
					 @Override
					 public boolean isCellEditable(int row, int column){
						 return column == 1;
					 }
				 };
				scrollPaneSupplier = new JScrollPane(orderFromSupplierTable);//
				scrollPaneSupplier.setBounds(0, 0, 310, 346);
				scrollPaneSupplier.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

				TableColumn quantityColumn = orderFromSupplierTable.getColumnModel().getColumn(1);
				quantityColumn.setCellEditor(new DefaultCellEditor(comboBoxQuantity));	
				orderFromSupplierTable.setFillsViewportHeight(true);
				NewOrderFromSupplierFrame.getContentPane().add(scrollPaneSupplier);/////////////
			}
		});
		btnStartOrder.setBounds(423, 45, 101, 23);
		NewOrderFromSupplierFrame.getContentPane().add(btnStartOrder);
			
		
		lblSupplier = new JLabel(new ImageIcon(manager.imgPath+"Truck.gif"));
		lblSupplier.setBounds(372, 219, 133, 100);
		NewOrderFromSupplierFrame.getContentPane().add(lblSupplier);
		lblSupplier.setVisible(false);
		timerSupplier = new Timer(2000,new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e){
				lblSupplier.setVisible(false);
				JOptionPane.showMessageDialog(null, "Order saved successfully!");
				btnBack.setEnabled(true);
				btnBack.doClick();
			}
		});
		timerSupplier.setRepeats(false);
		

		
	}

	
	
	// helper to create scrollPane for Order from table frame 
	private Object[][] dishOrderHelper(Vector<String> vector){
			tableSize=vector.size();
			 Object[][] data= new Object[tableSize][2];
				
			  for (int i=0 ; i<tableSize ; i++) {
				  data[i][0]=vector.get(i);
				  data[i][1]="";
			  }
			  return data;
		}
		
	
	
	// helper to create scrollPane for create dish frame & order from supplier 
	private Object[][] productToMatrix(boolean productsFromSupplier,String supplierName ){
		HashSet<String> productsNames;
		if (productsFromSupplier)
			productsNames =manager.getSuppliersProducts(manager.getSupplier(supplierName));
		else
			productsNames = manager.myRestaurant.getProductsNames();
		
		tableSize=productsNames.size();

		Object[][] data= new Object[tableSize][2];
		int i=0;
		for (String s: productsNames) {
			data[i][0]=s;
			data[i][1]="";
			i++;
		}
		return data;
	}

	
	
	// put set's elements into comboBox platform
	private void SetToComboBox(JComboBox<String> combo,HashSet<String> hash) {
			for (String s : hash) {
				combo.addItem(s);				
			}
		}
		
		
		
	// reading the GUI's scroll pane and convert to Order 
	private Order convertTableToOrder() {
			HashMap<Dish, Integer> dishes=new HashMap<Dish, Integer>();
			for (int i=0;i<tableSize;i++) {
				String dishName=(String)orderFromTableDishTable.getValueAt(i, 0);
				String quantityInString =(String)orderFromTableDishTable.getValueAt(i, 1);
				if (quantityInString.equals(""))
					continue;
				int quantity=Integer.parseInt(quantityInString);
				if (dishName.equals("")||quantity==0)
					continue;
				Dish d= manager.myRestaurant.getDish(dishName);
				dishes.put(d, quantity);
			}
			Table table=new Table(Integer.parseInt((String)comboBoxID.getSelectedItem()), Integer.parseInt((String)comboBoxTableDiners.getSelectedItem()));

			if(dishes.size()==0)
				return null;
			
			return new OrderFromTable(mmg.idManager(), new myDate(false), dishes, table, textFieldNotes.getText());
		}
		
		
	// reading the GUI's scroll pane and convert to Order 
	private Order convertTableToOrderFromSupplier(Supplier supplier) {
			TreeMap<Product, Integer> products=new TreeMap<Product, Integer>();
			float price=0;
			String productName,quantityInString;
			int quantity;
			for (int i=0;i<tableSize;i++) {
				productName=(String)orderFromSupplierTable.getValueAt(i, 0);
				quantityInString=(String)orderFromSupplierTable.getValueAt(i, 1);
				if(quantityInString.equals(""))
					continue;
				quantity=Integer.parseInt(quantityInString);
				if (productName.equals("")|| quantity==0 )//TODO
					continue;
				Product p=new Product(supplier.getProduct(productName));
				if (!p.getName().equals("")) {
					products.put(p, quantity);
					price+=p.getBuyPrice()*quantity;
				}
			}
			if (products.size()==0)
				return null;

			return new OrderFromSupplier(mmg.idManager(), new myDate(false), price, products, supplier);
		}
		
		
	// reading the GUI's scroll pane and convert to Dish 
	//
	private Dish convertTableToDish(JTextField textFieldName ) {
			TreeMap<Product, Integer> products=new TreeMap<Product, Integer>();
			Product tmpProduct;
			String quantityInString,dishName;
			int quantity;
			int id=mmg.idManager();
			dishName=textFieldName.getText();
			if (manager.myRestaurant.getDish(dishName) !=null) { 
				 JOptionPane.showMessageDialog(null, "The system found that the dish name already exist! , you will be transfereed to the main menu");
				 return null;
			}
			if (dishName.equals("")) {
				 JOptionPane.showMessageDialog(null, "Dish name can not be empty, you will be transferred to the main menu");
				 return null;
			}


				for (int i=0;i<tableSize;i++) {
					quantityInString=(String)DishTable.getValueAt(i, 1);
					if(quantityInString.equals(""))
						continue;
					tmpProduct=manager.myRestaurant.getProduct((String)DishTable.getValueAt(i, 0));
					quantity=Integer.parseInt(quantityInString);
					if (tmpProduct.getName().equals("")|| quantity==0 )//TODO
						continue;
					products.put(tmpProduct, quantity);
				}
				if (products.size()==0) {
					JOptionPane.showMessageDialog(null, "Dish has to include products, you will be transferred to the main menu");
					 return null;
				}
					
			return new Dish(id,dishName,products);
			
		}

}
