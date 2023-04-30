import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class OrdSupTable {

	public static final int     TABLE_SIZE = 50;

	private JFrame NewOrderFromSupplierFrame;
	mmg manager=new mmg();
	JTable orderFromTableSupplierTable;
	JScrollPane scrollPaneSupplier;
	String[] zeroToFifteen = { "0","1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11", "12", "13", "14", "15" };
	int tableSize=0;
	private JLabel lblReport,lblLoading,lblSupplier;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrdSupTable window = new OrdSupTable();
					window.NewOrderFromSupplierFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OrdSupTable() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		NewOrderFromSupplierFrame = new JFrame();
		NewOrderFromSupplierFrame.setTitle("Order from supplier");
		NewOrderFromSupplierFrame.setBounds(100, 150, 550, 383);
		NewOrderFromSupplierFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		NewOrderFromSupplierFrame.getContentPane().setLayout(null);
		
		lblSupplier = new JLabel(new ImageIcon(manager.imgPath+"Truck3.gif"));
		lblSupplier.setBounds(372, 219, 133, 100);
		NewOrderFromSupplierFrame.getContentPane().add(lblSupplier);
		lblSupplier.setVisible(true);
	

		
		 Object[] columns= {"Product","Quantity"};

		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewOrderFromSupplierFrame.setVisible(false);
			//	mainFrame.setVisible(true);
			}
		});
		btnBack.setBounds(435, 310, 89, 23);
		NewOrderFromSupplierFrame.getContentPane().add(btnBack);
		
		 
		JLabel lblSupplierName = new JLabel("Supplier:");
		lblSupplierName.setBounds(360, 14, 68, 14);
		lblSupplierName.setFont(new Font("Arial", Font.PLAIN, 14));
		NewOrderFromSupplierFrame.getContentPane().add(lblSupplierName);
		 
		JComboBox<String> comboBoxSuppliersNames = new JComboBox<String>();
		vectorToComboBox(comboBoxSuppliersNames,manager.getSuppliersNames());
		comboBoxSuppliersNames.setBounds(423, 11, 101, 23);
		NewOrderFromSupplierFrame.getContentPane().add(comboBoxSuppliersNames);
		
		JComboBox<Object> comboBoxQuantity = new JComboBox<Object>(zeroToFifteen);
		
		
		
		
		JButton btnStartOrder = new JButton("Start order");
		btnStartOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnStartOrder.setEnabled(false);
				orderFromTableSupplierTable=new JTable(supplierOrderHelper((String)comboBoxSuppliersNames.getSelectedItem()), columns);
				scrollPaneSupplier = new JScrollPane(orderFromTableSupplierTable);//
				scrollPaneSupplier.setBounds(0, 0, 310, 346);
				scrollPaneSupplier.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

				TableColumn quantityColumn = orderFromTableSupplierTable.getColumnModel().getColumn(1);
				quantityColumn.setCellEditor(new DefaultCellEditor(comboBoxQuantity));	
				orderFromTableSupplierTable.setFillsViewportHeight(true);
				NewOrderFromSupplierFrame.getContentPane().add(scrollPaneSupplier);/////////////
			}
		});
		btnStartOrder.setBounds(423, 45, 101, 23);
		NewOrderFromSupplierFrame.getContentPane().add(btnStartOrder);
			
		
		

		JButton btnSave = new JButton("Save");
		 btnSave.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		Order o=convertTableToOrderFromSupplier(manager.getSupplier((String)comboBoxSuppliersNames.getSelectedItem()));
		 		manager.myRestaurant.addOrder(o);
		 		JOptionPane.showMessageDialog(null, "Order saved successfully");
		 		btnBack.doClick();
		 		}
		 });
		 btnSave.setBounds(336, 310, 89, 23);
		 NewOrderFromSupplierFrame.getContentPane().add(btnSave);
	}
	


	private Object[][] supplierOrderHelper(String supplierName ){

		HashSet<String> productsNames =manager.getSuppliersProducts(manager.getSupplier(supplierName));
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
	
	
	private Order convertTableToOrderFromSupplier(Supplier suplier) {
		TreeMap<Product, Integer> products=new TreeMap<Product, Integer>();
		float price=0;
		String productName,quantityInString;
		int quantity;
		for (int i=0;i<tableSize;i++) {
			productName=(String)orderFromTableSupplierTable.getValueAt(i, 0);
			quantityInString=(String)orderFromTableSupplierTable.getValueAt(i, 1);
			if(quantityInString.equals(""))
				continue;
			quantity=Integer.parseInt(quantityInString);
			if (productName.equals("")|| quantity==0 )//TODO
				continue;
			Product p= manager.myRestaurant.getProduct(productName);
			if (!p.getName().equals("")) {
				products.put(p, quantity);
				price+=p.getBuyPrice()*quantity;
			}
		}

		return new OrderFromSupplier(mmg.idManager(), new myDate(false), price, products, suplier);
	}
	//________
	
	private void vectorToComboBox(JComboBox<String> combo, HashSet<String> hash) {
		for (String s : hash) {
			combo.addItem(s);				
		}
	}
}
