package bin;

import java.awt.Color;
import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import ext.ExcelAdapter;

/**
*  JTable con i dati dell'input form. Verranno poi scritti in {@link AppInputRuntimeInterfacer}
 */
public abstract class BeginInputForm {
	
	// dati di runtime grezzi. Lavorati: {@link AppInputRuntimeInterfacer}
	
	private static int JROWS = 500;
	private static final int JCOL = 10;
	public static JTable jtable;
	
	/**
	 * @since 1.3
	 */
	protected static boolean FLOW_MODALITY_ON = false; 
	/** @since 1.3 contains only |List<String>|=1*/
	protected volatile static List<List<String>> FLOW_REMAINING_GROUPS = new ArrayList<List<String>>(); 
	protected volatile static int FLOW_REMAINING_COUNT = 0;
	public static JTable jCache;
	
	/**
	 * show input form
	 */
	public static void show() {
		jtable = new JTable(JROWS,JCOL);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.WEST;
		
		//JTextField field0 = new JTextField("-SEversion=8 -debug=false");
	    
		jtable = new JTable(JROWS,JCOL);
		jtable.setDragEnabled(true);
		jtable.setDropMode(DropMode.INSERT_ROWS);
	    new ExcelAdapter(jtable);
	    
	    JScrollPane scrollPane = new JScrollPane(jtable);
	    jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.add(new JLabel("afti-lab-8 ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"),gbc);
	    //panel.add(new JLabel("Parametri speciali:"),gbc);
	    //panel.add(field0,gbc);
	    panel.add(new JLabel("Pronto. Attenzione: incollare l'input partendo prima cella in alto a sinistra."),gbc);
	    panel.add(scrollPane,gbc);
	    //javax.swing.JCheckBox randomDomains = new javax.swing.JCheckBox("Non applicabile");
	    //javax.swing.JCheckBox clearExecution = new javax.swing.JCheckBox("Non applicabile", true);
	    //panel.add(randomDomains,gbc);
	    //panel.add(clearExecution,gbc);
	    panel.setBackground(Color.CYAN);
	    int result = JOptionPane.showConfirmDialog(null, panel, "afti-lab-8",
	        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	    if (result == JOptionPane.OK_OPTION) {
	    	List<String> ordersIDS = askOrder();
	    	if (FLOW_MODALITY_ON == false) {
	    		AppInputRuntimeInterfacer.readMatrixInInputForm(ordersIDS);
	    		AppInputCompileTimeInterfacer.extractDataFromExcelAboutAllowedSized();
	    		showInferer();
	    	}
	    	/**
	    	 * @since 1.3
	    	 */
	    	else {
	    		if(FLOW_REMAINING_COUNT != 0) {
	    			jCache = new JTable();
	    			jCache.setModel(jtable.getModel()); // save in cache
	    			// equals to flow()
	    			FLOW_REMAINING_COUNT--;
	    			ordersIDS = FLOW_REMAINING_GROUPS.get(FLOW_REMAINING_COUNT);
	    			//System.out.println("I'm going with: " + ordersIDS.get(0));
	    			//System.out.println("I'm going with: " + FLOW_REMAINING_GROUPS.get(0).get(0));
	    			//System.out.println("I'm going with: " + FLOW_REMAINING_GROUPS.get(1).get(0));
	    			//System.out.println("I'm going with: " + FLOW_REMAINING_GROUPS.get(2).get(0));
	    			//System.out.println("I'm going with: " + FLOW_REMAINING_GROUPS.get(3).get(0));
	    			AppInputRuntimeInterfacer.readMatrixInInputForm(ordersIDS);
		    		AppInputCompileTimeInterfacer.extractDataFromExcelAboutAllowedSized();
		    		showInferer();
	    		}
	    	}
	    } else {
	    	System.exit(0);
	    }
	}
	
	/**
	 * @since 1.3
	 */
	public static void flow() {
		rAfter = null;
		JROWS = 500;
		jtable.setModel(jCache.getModel()); // restore from cache
		FLOW_REMAINING_COUNT--;
		List<String> ordersIDS = FLOW_REMAINING_GROUPS.get(FLOW_REMAINING_COUNT);
		//System.out.println("I'm going with: " + ordersIDS.get(0));
		//System.out.println("I'm going with: " + FLOW_REMAINING_GROUPS.get(0).get(0));
		//System.out.println("I'm going with: " + FLOW_REMAINING_GROUPS.get(1).get(0));
		//System.out.println("I'm going with: " + FLOW_REMAINING_GROUPS.get(2).get(0));
		//System.out.println("I'm going with: " + FLOW_REMAINING_GROUPS.get(3).get(0));
		AppInputRuntimeInterfacer.readMatrixInInputForm(ordersIDS);
		AppInputCompileTimeInterfacer.extractDataFromExcelAboutAllowedSized();
		showInferer();
		
	}
	
	/**
	 * show a form asking for allowed sizes
	 */
	public static void showInferer() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.WEST;
		
		HashSet<String> titles = new HashSet<String>();
		/**@since 1.2*/HashMap<String, String> articles = new HashMap<String, String>(); /**puo essere global per tutti :)*/
		for(int r = 0; r < usedRows(); r++) {
			/**
			 * @since 1.1
			 */
			try {
			String t = AppInputRuntimeInterfacer.get(r, 5);
			titles.add(t);
			/**@since 1.2*/articles.put(t, AppInputRuntimeInterfacer.get(r, 4));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		List<String> prodfield = new ArrayList<String>();
		List<JTextField> fields = new ArrayList<JTextField>();
		
		/** @since 1.2 */
		int new_titles = 0;
		
		for(String title : titles) {
			if (title == null) /**@since 1.1*/
				continue;
			if (title.equals(""))
				continue;
			if (AppInputCompileTimeInterfacer.products.contains(title))
				continue;
			
			/** @since 1.2 */
			new_titles++;
			
			panel.add(new JLabel(title),gbc);
			JTextField jt = new JTextField("20");
			prodfield.add(title);
			fields.add(jt);
			panel.add(jt,gbc);
			panel.add(new JLabel("----------------------------------------------------------------"),gbc);
		}
		
		/** @since 1.2 */
		if (new_titles == 0)
			return;
		
	    int result = JOptionPane.showConfirmDialog(null, panel, "afti-lab-8 \" *** Permitted sizes form *** \"",
	        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    
	    if (result == JOptionPane.OK_OPTION) {
	    	for (int i = 0; i < prodfield.size(); i++) {
	    		String prod = prodfield.get(i);
	    		AppInputCompileTimeInterfacer.addNewInformation(prod, articles.get(prod), Integer.parseInt(fields.get(i).getText()));
	    	}
	    } else {
	    	System.exit(0);
	    }
	    
	}
	
	/**
	 * Quali ordini?
	 * @return Quali ordini
	 * @since 1.1
	 */
	public static List<String> askOrder() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.WEST;
		
		HashSet<String> orders = new HashSet<String>();
		
		for(int r = 0; r < usedRows(); r++) {
			orders.add((String) jtable.getModel().getValueAt(r, 1));
		}
		
		List<String> listOrders = new ArrayList<String>(orders);
		
		if (listOrders.size() == 1)
			return listOrders;
		
		/** @since 1.2 */
		if (listOrders.size() == 0) {
			int o = JOptionPane.showConfirmDialog(new JFrame(), 
					"No order code found.\n"+
					"Do I proceed anyway?",
					"Warning", JOptionPane.WARNING_MESSAGE);
			if (o == JOptionPane.CANCEL_OPTION)
				System.exit(0);
			return listOrders;
		}
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		List<JCheckBox> cbs = new ArrayList<JCheckBox>();
		
		for(String order : listOrders) {
			//panel.add(new JLabel(order),gbc);
			JCheckBox cb = new JCheckBox(order, true);
			cbs.add(cb);
			panel.add(cb,gbc);
			panel.add(new JLabel("----------------------------------------------------------------"),gbc);
		}
		
		/**
		 * @since 1.3
		 */
		JCheckBox cblast = new JCheckBox("Do you want to keep everything and separate everything?", true);
		//cbs.add(cblast);
		panel.add(cblast,gbc);
		
	    int result = JOptionPane.showConfirmDialog(null, panel, "afti-lab-8 \" *** Orders selection form *** \"",
	        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    
	    List<String> realOrders = new ArrayList<String>();
	    
	    if (result == JOptionPane.OK_OPTION) {
	    	for (int i = 0; i < cbs.size(); i++) {
	    		if (cbs.get(i).isSelected())
	    			realOrders.add(listOrders.get(i));
	    	}
	    	
	    	/** @since 1.3 */
	    	if(cblast.isSelected()) {
	    		FLOW_MODALITY_ON = true;
	    		FLOW_REMAINING_COUNT = realOrders.size();
	    		for(String order : realOrders) {
	    			List<String> group = new ArrayList<>();
	    			group.add(order);
	    			FLOW_REMAINING_GROUPS.add(group);
	    		}
	    	}
	    	System.out.println("ORDERS: " + realOrders.size());
	    	return realOrders;
	    } else {
	    	System.exit(0);
	    }
	    // we can't be here
	    throw new InternalError();
	}
	
	public static Integer rAfter = null;
	
	/**
	 * dice quante righe sono state scritte in input
	 * @return vedi descrizione
	 */
	public static int usedRows() {
		if (rAfter != null)
			return rAfter;
	    for (int row = 0; row < JROWS; row++) {
	        if (jtable.getValueAt(row, 0) == null)
	        	if (row > 0 && jtable.getValueAt(row - 1, 0).toString().equals(""))
	        		return row - 1;
	        	else
	        		return row;
	    }
	    return JROWS;
	}
	
	/**
	 * dice quante colonne sono state scritte in input
	 * @return vedi descrizione
	 */
	public static int usedColumns() {
	    for (int col = 0; col < JCOL; col++) {
	        if (jtable.getValueAt(0, col) == null)
	            return col;
	    }
	    return JCOL;
	}

	
	
	

}
