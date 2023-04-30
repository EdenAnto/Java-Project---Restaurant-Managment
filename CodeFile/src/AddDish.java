import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class AddDish {

	public JFrame addDishframe;
	private mmg manager;
	String[] zeroToFifteen = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11", "12", "13", "14", "15" };
	JComboBox<Object> comboBoxQuantity;
	public int tableSize;
	private JTable DishTable;
	private JScrollPane scrollPaneDish;
	private JTextField textFieldName;
	private JButton btnBack;
	private Dish newDish;


	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public AddDish(mmg manager) {
		this.manager=manager;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		addDishframe = new JFrame();
		addDishframe.setTitle("Add dish");
		addDishframe.setBounds(100, 150, 550, 383);
		addDishframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addDishframe.getContentPane().setLayout(null);
		
		JLabel lblPrice = new JLabel("");
		lblPrice.setBounds(382, 186, 113, 36);
		addDishframe.getContentPane().add(lblPrice);
		
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addDishframe.setVisible(false);
				//mainFrame.setVisible(true);
			}
		});
		btnBack.setBounds(435, 310, 89, 23);
		addDishframe.getContentPane().add(btnBack);
		
		JComboBox<Object> comboBoxQuantity = new JComboBox<Object>(zeroToFifteen);
		 Object[] columns= {"Product","Quantity"};
		 
		 DishTable=new JTable(productToMatrix(false,null), columns);
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
			 JOptionPane.showMessageDialog(null, "Dish added succesfully!");	
			 addDishframe.setVisible(false);
		 		}
		 });
		 btnSave.setBounds(336, 310, 89, 23);
		 addDishframe.getContentPane().add(btnSave);
		 
		 JLabel lblName = new JLabel("Name:");
		 lblName.setBounds(351, 29, 46, 14);
		 addDishframe.getContentPane().add(lblName);
		 
		 textFieldName = new JTextField();
		 textFieldName.setBounds(394, 26, 130, 20);
		 addDishframe.getContentPane().add(textFieldName);
		 textFieldName.setColumns(10);
		 
		 JButton btnCalculatePrice = new JButton("Calculate");
		 btnCalculatePrice.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		newDish=convertTableToDish();
		 		if (newDish==null)
		 			btnBack.doClick();
		 		lblPrice.setText(""+newDish.getSellPrice());
		 		lblPrice.setEnabled(true);
		 		btnCalculatePrice.setEnabled(false);
		 		btnSave.setEnabled(true);		 		
		 	}
		 });
			btnCalculatePrice.setBounds(391, 151, 89, 23);
			addDishframe.getContentPane().add(btnCalculatePrice);
	}
	
	private Object[][] productToMatrix(boolean productsFromSupplier,String supplierName ){
		HashSet<String> productsNames;
		if (productsFromSupplier)
			productsNames =manager.getSuppliersProducts(manager.getSupplier(supplierName));
		else 
			productsNames=manager.myRestaurant.getProductsNames();
		
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
	
	private Dish convertTableToDish() {
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
		return new Dish(id,dishName,products);
		
	}
	
	
}
