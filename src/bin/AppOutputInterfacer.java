package bin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import ext.JustConsolePrinter;

public abstract class AppOutputInterfacer {
	
	/**
	 * Dati che vogliamo scrivere nell'excel
	 */
	public static ArrayList<String[]> outputTableData;
	public static int rows;
	public static int cols;
	
	/**
	 * Spazio richiesto per ogni prodotto. mappa (nome prodotto da scrivere), (righe in cui comparirà)
	 */
	public static HashMap<String, Integer> neededSpacePerTitle;
	
	/**
	 * Prepara l'output a partire dall'input, solo lo spazio e i dati di input (nome prodotto).
	 */
	public static void prepareOutputSpace() {
		rows = AppInputRuntimeInterfacer.rows;
		cols = 43;
		outputTableData = new ArrayList<String[]>();
		
		for (int i = 0; i < rows; i++) {
			
			String[] row = new String[cols];
			
			//avoid null strings
			for (int j = 0; j < cols; j++)
				row[j] = "";
			
			for (int j = 0; j < AppInputRuntimeInterfacer.cols; j++) {
				row[j + 2] = AppInputRuntimeInterfacer.get(i,j);
			}
			
			outputTableData.add(row);
		}
		
		enlargeInternalSpace();
		JustConsolePrinter.printMatrix(outputTableData);
	}
	
	/**
	 * completa i dati dell'output dovo averlo preparato con prepareOutputSpace()
	 * Questo metodo aggiunge le parti computate (box data ecc...)
	 */
	public static void jinvoke() {
		for (Box box : Solver.boxes) {
			
			int rowPointer = internalPointerByText(box.product);
						
			String[] rowData = outputTableData.get(rowPointer);
			
			if (box.intern.size() == 1) {
				for (String key : box.intern.keySet()) { // 1 iter
					rowData[0] = "U";
					rowData[13] = key;
					rowData[15] = box.intern.get(key) + "";
					rowData[41] = box.scale + "";
					rowData[42] = (box.scale * box.intern.get(key)) + "";
				}
				
			}
			else {
				int idx = 0;
				for (String key : box.intern.keySet()) { 
					// TODO order keyset
					rowData[16 + idx] = key;
					rowData[18 + idx] = box.intern.get(key) + "";
					idx+=4;
				}
				rowData[0] = "T";
				rowData[41] = box.scale + "";
				rowData[42] = (box.scale * box.getTotalQuantity()) + "";
			}
		}
		System.out.print("\n\n\n**************\n\n\n");
		JustConsolePrinter.printStringGrid(outputTableData);
	}
	
	// dato il prodotto Camicia Griglia, da quale riga inizio a scrivere? 
	private static int internalPointerByText(String title) {
		for (int i = 0; ; i++) {
			if (outputTableData.get(i)[7].equals(title) && outputTableData.get(i)[0].equals("")) {
				return i;
			}
		}
	}
	
	/**
	 * 
	 * ---------------------------------------------------
	 * 
	 * *******************
	 * Methods below are used to enlarge the space of the output
	 * *******************
	 * 
	 * The problem:
	 * Input:
	 * CASA 34
	 * MURO 20
	 * 
	 * Output:
	 * CASA 20
	 * CASA 14 // OOOooops, this is an unexpected line...
	 * MURO 20
	 * 
	 * 
	 * ---------------------------------------------------
	 */
	
	/**
	 *  allarga lo spazio (1/3). vedi: {@link AppOutputInterfacer.neededSpacePerTitle}
	 */
	private static void enlargeInternalSpace() {
		fillNeededSpace();
		
		HashSet<String> enlarged = new HashSet<String>();
		
		for (int i = 0; i < rows; i++) {
			String title = outputTableData.get(i)[7];
			
			if (enlarged.contains(title)) 
				continue;
			
			int available = AppInputRuntimeInterfacer.availableSpacePerTitle.get(title);
			int needed = AppOutputInterfacer.neededSpacePerTitle.get(title);
			
			int difference = needed - available;
			
			while (difference > 0) {
				duplicateRow(i);
				difference--;
			}
			
			enlarged.add(title);
		}
	}
	
	/**
	 *  allarga lo spazio (2/3). vedi: {@link AppOutputInterfacer.neededSpacePerTitle}
	 */
	private static void fillNeededSpace() {
		// fill availableSpacePerTitle
		neededSpacePerTitle = new HashMap<>();
		for (String title : AppInputRuntimeInterfacer.titles) {
			
			int neededSpace = 0;
			
			for (Box box : Solver.boxes) {
				String boxTitle = box.product;
				if (boxTitle.equals(title))
					neededSpace++;
			}
			
			neededSpacePerTitle.put(title, neededSpace);
		}
	}
	
	/**
	 *  allarga lo spazio (3/3). vedi: {@link AppOutputInterfacer.neededSpacePerTitle}
	 */
	private static void duplicateRow(int index) {
		String[] rowData = outputTableData.get(index);
		String[] rowDataCopy = new String[rowData.length];
		System.arraycopy(rowData, 0, rowDataCopy, 0, rowData.length);
		rowDataCopy[11] = 0 + "";
		outputTableData.add(index, rowDataCopy);
		rows++;
	}
	

}

/**
 * -----------------------------------------------------------------------
 * Old methods
 * Absolutely @Deprecated
 * -----------------------------------------------------------------------
 */

/*
void appendRows(String[]... rows) {
	String[][] extendedBag = new String[outputTableData.length + rows.length][];
    int i = 0;
    for (String[] row : outputTableData)	{ fillRow(extendedBag, row, i++); }
    for (String[] row : rows) 				{ fillRow(extendedBag, row, i++); }
    outputTableData = extendedBag;
}

void fillRow(String[][] bag, String[] row, int i) {
    bag[i] = new String[row.length];
    System.arraycopy(row, 0, bag[i++], 0, row.length);
}
*/
