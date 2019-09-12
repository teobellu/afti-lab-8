package bin.pub;

//import java.awt.BorderLayout;
import java.awt.Color;
//import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.DropMode;
import javax.swing.JCheckBox;
//import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
//import javax.swing.event.TableModelListener;
//import javax.swing.table.TableModel;

import ext.SecurityEnsurer;
import ext.pub.ExcelAdapter;

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
	public static boolean FLOW_MODALITY_ON = false; 
	/** @since 1.3 contains only |List<String>|=1*/
	public volatile static List<List<String>> FLOW_REMAINING_GROUPS = new ArrayList<List<String>>(); 
	public volatile static int FLOW_REMAINING_COUNT = 0;
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
	    
	    @SuppressWarnings("unused")
	    JScrollPane scrollPane = new JScrollPane(jtable);
	    jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.add(new JLabel("afti-lab-8 ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"),gbc);
	    //panel.add(new JLabel("Parametri speciali:"),gbc);
	    //panel.add(field0,gbc);
	    //panel.add(new JLabel("Try copying the data before opening this program."),gbc);
	    panel.add(scrollPane,gbc);
	    
	    /**
	     * states @since 1.5
	     */
	    //panel = new JPanel(new GridBagLayout()); removed @since 2.1
	    panel.add(new JLabel("This is version " + SecurityEnsurer.PROGRAM_VERSION + ". It is no longer necessary to paste the data."),gbc);
	    panel.add(new JLabel("Just copy the AS400 data and (later) open this program."),gbc);
	    
	    /**
	     * @since 1.5
	     */
	    JCheckBox only_u_cbx = new JCheckBox("Do you want to use only the U layer?");
	    panel.add(only_u_cbx,gbc);
	    
	    //javax.swing.JCheckBox randomDomains = new javax.swing.JCheckBox("Non applicabile");
	    //javax.swing.JCheckBox clearExecution = new javax.swing.JCheckBox("Non applicabile", true);
	    //panel.add(randomDomains,gbc);
	    //panel.add(clearExecution,gbc);
	    Color color = hslColor((float)Math.random(), (float)Math.random(), 0.75f);
	    panel.setBackground(color);
	    
	    /*
	    Runnable r = new Runnable() {
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
						Color color = Color.getHSBColor((float)Math.random(), (float)Math.random(), (float)Math.random()*66);
					    panel.setBackground(color);
					    panel.repaint();
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
		*/
	   
	    /***************************************************************************/
	    /**
	     * Read directly from getSysClipboardText()
	     * 
	     * @since 1.5
	     */
	    int startRow=0;
        int startCol=0;
    	String trstring= getSysClipboardText();
        int i = 0, j = 0;
        for(String rowstring : trstring.split("\n", -1))
        {
       	 for(String value : rowstring.split("\t", -1))
            {
               if (startRow+i< jtable.getRowCount()  &&
                   startCol+j< jtable.getColumnCount())
            	   jtable.setValueAt(value,startRow+i,startCol+j);
               j++;
           }
           i++;
           j = 0;
        }
        /***********************************************************************/
	    
	    int result = JOptionPane.showConfirmDialog(null, panel, "afti-lab-8",
	        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	    if (result == JOptionPane.OK_OPTION) {
	    	
	    	/**@since 1.5*/
	    	AppInputRuntimeInterfacer.only_u = only_u_cbx.isSelected();
	    	
	    	/**@since 1.5 ask all sizes in one time*/
	    	/***************************************************************************/
	    	jCache = new JTable();
			jCache.setModel(jtable.getModel()); // save in cache
	    	HashSet<String> orders = new HashSet<String>();
			for(int r = 0; r < usedRows(); r++) {
				orders.add((String) jtable.getModel().getValueAt(r, 1));
			}
			List<String> ordlistFull = new ArrayList<>(orders);
	    	AppInputRuntimeInterfacer.readMatrixInInputForm(ordlistFull);
	    	AppInputCompileTimeInterfacer.extractDataFromExcelAboutAllowedSized();
    		showInferer();
	    	rAfter = null;
			JROWS = 500;
			jtable.setModel(jCache.getModel()); // restore from cache
			/***************************************************************************/
	    	
			
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
	 * color
	 * @ since 1.5
	 * @param h
	 * @param s
	 * @param l
	 * @return
	 */
	static public Color hslColor(float h, float s, float l) {
	    float q, p, r, g, b;

	    if (s == 0) {
	        r = g = b = l; // achromatic
	    } else {
	        q = l < 0.5 ? (l * (1 + s)) : (l + s - l * s);
	        p = 2 * l - q;
	        r = hue2rgb(p, q, h + 1.0f / 3);
	        g = hue2rgb(p, q, h);
	        b = hue2rgb(p, q, h - 1.0f / 3);
	    }
	    return new Color(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
	}
	
	/**
	 * color
	 * @since 1.5
	 * @param p
	 * @param q
	 * @param h
	 * @return
	 */
	private static float hue2rgb(float p, float q, float h) {
	    if (h < 0) {
	        h += 1;
	    }

	    if (h > 1) {
	        h -= 1;
	    }

	    if (6 * h < 1) {
	        return p + ((q - p) * 6 * h);
	    }

	    if (2 * h < 1) {
	        return q;
	    }

	    if (3 * h < 2) {
	        return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
	    }

	    return p;
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
		List<JTextField> secondColFields = new ArrayList<JTextField>();
		
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
			
			/** @since 1.5 */
			//panel = new JPanel(new GridBagLayout());
			String article = articles.getOrDefault(title, "???");
			@SuppressWarnings("unused")
			int articleSize = article.length();
			int shift = 30 - articleSize;
			shift = Math.max(shift, 0);
			    StringBuilder r = new StringBuilder();
			   for (int i = 0; i < shift; i++) {
			        //r.append(" ");
			    }
			String printArticle = "{" + article + "} -----> " + r.toString();
			// align title
			shift = 120 - title.length();
			shift = Math.max(shift, 0);
			    r = new StringBuilder();
			   for (int i = 0; i < shift; i++) {
			        //r.append(" ");
			    }
			String printTitle = title + r.toString();
			
			panel.add(new JLabel(printArticle)); /**@since 1.5*/
			panel.add(new JLabel(printTitle));
			JTextField jt = new JTextField(""); /**@since 1.5*/
			jt.setPreferredSize( new Dimension( 50, 16 ) );
			prodfield.add(title);
			fields.add(jt);
			panel.add(jt);
			
			JTextField jt2 = new JTextField(""); /**@since 2.1*/
			jt2.setPreferredSize( new Dimension( 50, 16 ) );
			secondColFields.add(jt2);
			panel.add(jt2,gbc);
			/**@since i.5*/ //panel.add(new JLabel("----------------------------------------------------------------"),gbc);
		}
		
		/** @since 1.2 */
		if (new_titles == 0)
			return;
		
	    int result = JOptionPane.showConfirmDialog(null, panel, "afti-lab-8 \" *** Permitted sizes form *** \"",
	        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    
	    if (result == JOptionPane.OK_OPTION) {
	    	for (int i = 0; i < prodfield.size(); i++) {
	    		String prod = prodfield.get(i);
	    		/**@since 2.1*/
	    		String valCol1 = fields.get(i).getText();
	    		String valCol2 = secondColFields.get(i).getText();
	    		
	    		/**try catch @since 1.5*/
	    		int allowedSize = 0;
	    		try {
	    			allowedSize = Integer.parseInt(valCol1);
	    			if (allowedSize == 0)
	    				throw new Exception();
	    		}
	    		catch(Exception e) {
	    			JOptionPane.showMessageDialog(new JFrame(), 
	    					"Error: You forgot to put allowed sizes.\nError: A fatal exception has occurred. Program will exit.",
	    					"Error", JOptionPane.ERROR_MESSAGE);
	    			System.exit(0);
	    		}
	    		
	    		AppInputCompileTimeInterfacer.addNewInformation(prod, articles.get(prod), allowedSize, valCol2);
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
			/**@since i.5*/ //panel.add(new JLabel("----------------------------------------------------------------"),gbc); 
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

	
	/**
	 * Automatic reading @since 1.5
	 * @return
	 */
	public static String getSysClipboardText() {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipTf = sysClip.getContents(null);
        if (clipTf != null) {
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    ret = (String) clipTf
                            .getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }
	
}
