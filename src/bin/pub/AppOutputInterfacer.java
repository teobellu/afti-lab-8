package bin.pub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import ext.pub.JustConsolePrinter;

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
	 * Index column T
	 * @since 1.5
	 */
	private static final int INDEX_FIRST_CODE_ORDER_CLIENT = 19;;
	
	/**
	 * Prepara l'output a partire dall'input, solo lo spazio e i dati di input (nome prodotto).
	 */
	public static void prepareOutputSpace() {
		rows = AppInputRuntimeInterfacer.rows;
		cols = 48;
		outputTableData = new ArrayList<String[]>();
		
		for (int i = 0; i < rows; i++) {
			
			String[] row = new String[cols];
			
			//avoid null strings
			for (int j = 0; j < cols; j++)
				row[j] = "";
			
			/**
			 * @since 1.1
			 */
			row[1] = "st";
			
			for (int j = 0; j < AppInputRuntimeInterfacer.cols; j++) {
				row[j + 2] = AppInputRuntimeInterfacer.get(i,j);
				if (j == 9) {
					/**
					 * Removed parseInt error on "13,0" caused by ","
					 * @since 1.1
					 */
					String a = AppInputRuntimeInterfacer.get(i,j);
					a = a.replace(",00","");
					a = a.replace(",0","");
					a = a.replace(".00","");
					a = a.replace(".0","");
					row[j + 2] = a;
				}
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
		
		/**@since September 2019*/
		int sumElementsInTheRedCol = 0;
		
		for (Box box : Solver.boxes) {
			
			String product = box.product;
			// size is key? asked in 1.5
			int rowPointer = internalPointerByText(product);
						
			String[] rowData = outputTableData.get(rowPointer);
			
			/**@since September 2019*/
			sumElementsInTheRedCol += box.scale;
			
			if (box.intern.size() == 1) {
				for (String key : box.intern.keySet()) { // 1 iter
					rowData[0] = "U";
					rowData[13] = key;
					rowData[15] = box.intern.get(key) + "";
					rowData[41] = box.scale + "";
					rowData[42] = (box.scale * box.intern.get(key)) + "";
					
					/**
					 * @since 1.2
					 */
					rowData[45] = AppInputCompileTimeInterfacer.boxAdditionInfoes.getOrDefault(product, "");
				}
				
			}
			else {
				int idx = 0;
				int countSizes = 0;
				for (String key : box.intern.keySet()) { 
					rowData[16 + idx] = key;
					rowData[INDEX_FIRST_CODE_ORDER_CLIENT + idx] 
							= AppInputRuntimeInterfacer
							.consumerCodeBySize
							.get(product)
							.get(key);
					System.out.print(rowData[15 + idx]);
					rowData[18 + idx] = box.intern.get(key) + "";
					idx+=4;
					countSizes++;
				}
				rowData[0] = "T";
				rowData[41] = box.scale + "";
				rowData[42] = (box.scale * box.getTotalQuantity()) + "";
				
				/**
				 * @since 1.2
				 */
				rowData[45] = AppInputCompileTimeInterfacer.boxAdditionInfoes.getOrDefault(product, "");
				// riordina
				outputTableData.remove(rowPointer);
				outputTableData.add(rowPointer, ord(rowData, countSizes));
				
			}
		}
		
		for (int i = 0; i < rows; i++) {
			outputTableData.get(i)[47] = sumElementsInTheRedCol + "";
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
	
	/**
	 * 
	 * ---------------------------------------------------
	 * 
	 * *******************
	 * Methods below are used to order S < M < L < XL...
	 * *******************
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * ---------------------------------------------------
	 */
	
	private static final String[] ORDERED = {"XXS", "XS", "S", "S/M", "M", 
											 "M/L", "L", "L/XL", "XL", "XL/2XL", 
											 "XXL", "2XL", "XXXL", "3XL", "4XL", 
											 "30", "31", "32", "33", "34", "35", 
											 "36", "37", "38", "39",
											 "40", "41", "42", "43", "44", "45", 
											 "46", "47", "48", "49",
											 "50", "51", "52", "53", "54", "55", 
											 "56", "57", "58", "59",
											 "60", "61", "62", "63", "64", "65", 
											 "66", "67", "68", "69", "70"};
	
	private static final int ELEMENTS_IN_ORDERED = ORDERED.length;
			
	// riordina riga rowData
	private static String[] ord(String[] rowData, int countSizes) {
		
		String[] newData = Arrays.copyOf(rowData, rowData.length);
		
		int c = 0;
		
		// per ogni size fs esistente...
		for (int fs = 0; fs < ELEMENTS_IN_ORDERED; fs++) {
			
			String fm = ORDERED[fs]; //o meglio, fm è la size, fs è ORDERED.indexof(fm)
			
			// per ogni colonna utile i in rowData...
			for (int i = 0; i < countSizes*4; i+=4) { 
			
				String size = rowData[16 + i];
				String orderclientcodeprod = rowData[INDEX_FIRST_CODE_ORDER_CLIENT + i]; /**@since 1.5*/
				String q    = rowData[18 + i];
				
				// se la size è quella
				/**@since 1.2 deprecated contains, substitute with equals*/
				if (size.equals(fm)){
					
					newData[16 + c] = size;
					newData[INDEX_FIRST_CODE_ORDER_CLIENT + c] = orderclientcodeprod; /**@since 1.5*/
					newData[18 + c] = q;
					c+=4;
					
					System.out.println("oooo");
					
					if (c == countSizes*4) {
						return newData;
					}
					
				}
				
			}
			
		}
		
		// error
		return rowData;
		
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
