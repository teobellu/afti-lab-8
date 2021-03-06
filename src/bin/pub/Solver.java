package bin.pub;

import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Solver {
	
	/**
	 * 											**** PAY ATTENTION!!! ****
	 * 
	 * 										Il core di questa classe � @AnyTimeOk
	 * 
	 */
	
	/**
	 * Will keep the solution of the solver
	 */
	public static List<Box> boxes;
	
	//** old stuff **//
	//public static Map<String, Integer> qentries = new HashMap<String, Integer>();
	//public static Map<String, Integer> qindexes = new HashMap<String, Integer>();
	//public static Map<String, Integer> qindexesMax = new HashMap<String, Integer>();
	//public static Map<String, Integer> qboxes = new HashMap<String, Integer>();
	
	public static void run() {
		/**
		 * righe lette da input
		 * @since 1.0 last update: from global to local
		 */
		EntryList entries = new EntryList();
		
		int r = BeginInputForm.usedRows();
		//int c = InputForm.usedColumns();
		
		for(int i = 0; i < r; i++) {
			// leggi una riga
			EntryRow entry_i = new EntryRow();
			entry_i.title = AppInputRuntimeInterfacer.get(i,5);
			entry_i.size = AppInputRuntimeInterfacer.get(i,7);
			
			if (AppInputRuntimeInterfacer.get(i,9).toString().equals(""))
				entry_i.qt=0;
			else {
				try {
					entry_i.qt = Integer.parseInt(AppInputRuntimeInterfacer.get(i, 9));
				}
				catch(Exception e) {
					/**
					 * Removed parseInt error on "13,0" caused by ","
					 * @since 1.1
					 */
					String a = AppInputRuntimeInterfacer.get(i, 9);
					a = a.replace(",00","");
					a = a.replace(",0","");
					a = a.replace(".","");
					
					/**
					 * @since 1.2 moved in try/catch
					 */
					try {
						entry_i.qt = Integer.parseInt(a);
					}
					catch(Exception e2) {
						JOptionPane.showMessageDialog(new JFrame(), 
								"Error: cannot convert this quantity to integer: " + a +
								"\nError: Program will exit.",
								"Error", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
				}
			}
			// la riga � stata appena letta
			
			// WARNING COMPUTAZIONE QUI:
			entries.add(entry_i);
			
			//qentries.put(entry_i.title, qentries.getOrDefault(entry_i.title, 0) + 1);
			//qindexes.put(entry_i.title, 0);
		}
		
		// WARNING COMPUTAZIONE QUI:
		boxes = entries.convert();
		
		//for (Box b : boxes) {
		//	qboxes.put(b.product, qboxes.getOrDefault(b.product, 0) + 1);
		//}
	}
}

/**
 * Rappresenta una RIGA letta dall'input form di runtime
 */
class EntryList {
	
	private List<Entry> elements;
	
	public EntryList () {
		elements = new ArrayList<Entry>();
	}
	
	public void add(EntryRow e) {
		for (Entry en : elements) {
			if (en.title.equals(e.title)) {
				if (en.qtmap.containsKey(e.size)) {
					// almost never here
					en.qtmap.put(e.size, en.qtmap.get(e.size) + e.qt);
					en.rqtmap.put(e.size, en.rqtmap.get(e.size) + e.qt);
				}
				else {
					en.qtmap.put(e.size, e.qt);
					en.rqtmap.put(e.size, e.qt);
				}
				return;
			}
		}
		Entry en = new Entry();
		en.title = e.title;
		en.div = AppInputCompileTimeInterfacer.allowedSizes.getOrDefault(e.title, 20);
		en.qtmap.put(e.size, e.qt);
		en.rqtmap.put(e.size, e.qt);
		elements.add(en);
	}
	
	public List<Box> convert(){
		List<Box> boxes = new ArrayList<Box>();
		for (Entry en : elements) {
			List<Box> boxesForTheEntry = en.computeBoxes();
			boxes.addAll(boxesForTheEntry);
			
		}
		return boxes;
	}
	
//	public List<Box> convert(){
//		List<Box> boxes = new ArrayList<Box>();
//		for (Entry en : elements) {
//			/**
//			 * after 3.1 was only boxes.addAll(en.computeBoxes())
//			 * now we want to optimize the calculus
//			 * 
//			 * try 20 as default size
//			 * then, try 21 as default size
//			 * 
//			 * if 21 leads in fewer boxes (cardinality/number) use 21, otherwise 20
//			 * 
//			 * @since 3.1
//			 */
//			List<Box> boxesForTheEntry = en.computeBoxes();
//			List<Box> boxesForTheEntryCheating = null;
//			
//			if ((int)(en.div*0.1f) < 1)
//				;
//			else {
//				en.div++;
//				for(String size : en.qtmap.keySet()) {
//					en.rqtmap.put(size, en.qtmap.get(size));
//				}
//				boxesForTheEntryCheating = en.computeBoxes();
//				en.div--;
//			}
//			
//			if (boxesForTheEntryCheating == null) {
//				boxes.addAll(boxesForTheEntry);
//			}
//			else if (calcBoxsInList(boxesForTheEntryCheating) < calcBoxsInList(boxesForTheEntry)) {
//				boxes.addAll(boxesForTheEntryCheating);
//			}
//			else {
//				boxes.addAll(boxesForTheEntry);
//			}
//			
//		}
//		return boxes;
//	}
	
	@SuppressWarnings("unused")
	private int calcBoxsInList(List<Box> boxes) {
		int t = 0;
		for (Box b : boxes) {
			t += b.scale;
		}
		return t;
	}

}

/**
 * Rappresenta una RIGA letta dall'input form di runtime
 */
class EntryRow {
	
	public String title;
	public String size;
	public int qt;
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Entry))
			return false;
		return title.equals(((Entry)obj).title);
	}
	
	@Override
	public int hashCode() {
		return title.hashCode();
	}

}
