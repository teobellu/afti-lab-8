package bin;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;

public class Solver {
	
	/**
	 * 
	 * 
	 * Il core di questa classe è @AnyTimeOk
	 * 
	 * 
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
			else
			    entry_i.qt = Integer.parseInt(AppInputRuntimeInterfacer.get(i, 9));
			// la riga è stata appena letta
			
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
			boxes.addAll(en.computeBoxes());
		}
		return boxes;
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
