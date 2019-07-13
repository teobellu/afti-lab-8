package bin;

import java.awt.Color;
import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.DropMode;
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
	
	private static final int JROWS = 500;
	private static final int JCOL = 100;
	public static JTable jtable = new JTable(JROWS,JCOL);
	
	/**
	 * show input form
	 */
	public static void show() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.WEST;
		
		//JTextField field0 = new JTextField("-SEversion=8 -debug=false");
	    
		jtable = new JTable(500,100);
		jtable.setDragEnabled(true);
		jtable.setDropMode(DropMode.INSERT_ROWS);
	    new ExcelAdapter(jtable);
	    
	    JScrollPane scrollPane = new JScrollPane(jtable);
	    jtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.add(new JLabel("afti-lab-8 --------------------------------------------------------------------------------------------------------------------------------------------------------------------------"),gbc);
	    //panel.add(new JLabel("Parametri speciali:"),gbc);
	    //panel.add(field0,gbc);
	    panel.add(new JLabel("Pronto"),gbc);
	    panel.add(scrollPane,gbc);
	    //javax.swing.JCheckBox randomDomains = new javax.swing.JCheckBox("Non applicabile");
	    //javax.swing.JCheckBox clearExecution = new javax.swing.JCheckBox("Non applicabile", true);
	    //panel.add(randomDomains,gbc);
	    //panel.add(clearExecution,gbc);
	    panel.setBackground(Color.GREEN);
	    int result = JOptionPane.showConfirmDialog(null, panel, "afti-lab-8",
	        JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	    if (result == JOptionPane.OK_OPTION) {
	    	AppInputRuntimeInterfacer.readMatrixInInputForm();
	    	AppInputCompileTimeInterfacer.extractDataFromExcelAboutAllowedSized();
	    	showInferer();
	    } else {
	    	System.exit(0);
	    }
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
		for(int r = 0; r < usedRows(); r++) {
			titles.add(AppInputRuntimeInterfacer.get(r, 5));
		}
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		
		List<String> prodfield = new ArrayList<String>();
		List<JTextField> fields = new ArrayList<JTextField>();
		
		for(String title : titles) {
			if (title.equals(""))
				continue;
			if (AppInputCompileTimeInterfacer.products.contains(title))
				continue;
			panel.add(new JLabel(title),gbc);
			JTextField jt = new JTextField("20");
			prodfield.add(title);
			fields.add(jt);
			panel.add(jt,gbc);
			panel.add(new JLabel("----------------------------------------------------------------"),gbc);
		}
		
	    int result = JOptionPane.showConfirmDialog(null, panel, "afti-lab-8",
	        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	    
	    
	    if (result == JOptionPane.OK_OPTION) {
	    	for (int i = 0; i < prodfield.size(); i++) {
	    		AppInputCompileTimeInterfacer.addNewInformation(prodfield.get(i), Integer.parseInt(fields.get(i).getText()));
	    	}
	    } else {
	    	System.exit(0);
	    }
	    
	}
	
	/**
	 * dice quante righe sono state scritte in input
	 * @return vedi descrizione
	 */
	public static int usedRows() {
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
