package bin.pub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import ext.pub.JustConsolePrinter;

public abstract class AppInputRuntimeInterfacer {
	
	/**
	 * Dati letti dal form in input (inseriti dall'utente a runtime)
	 */
	public static String[][] jTableData;
	public static int rows;
	public static int cols;
	
	/**
	 * hashset dei nomi dei prodotti letti dal form in input (inseriti dall'utente a runtime)
	 */
	public static HashSet<String> titles;
	
	/**
	 * mappa (nome prodotto), (righe disponibili nell'input form (runtime))
	 * 
	 * </p><b>Esempio</b>
	 * </p>CASA ... 10
	 * </p>CASA ... 
	 * </p>PIZZA ... 45
	 * </p>
	 * Mappa: (CASA, 2) (PIZZA, 1)
	 */
	public static HashMap<String, Integer> availableSpacePerTitle;
	
	/**
	 * mappa (Prodotto noto, per nome) e (codice prodotto del cliente, per me � x023, per lui � y023)
	 * @since 1.5
	 */
	public static HashMap<String, HashMap<String, String>> consumerCodeBySize;
	
	/**
	 * solo etichetta U?
	 * @since 1.5
	 */
	public static boolean only_u;
	
	/**
	 * legge i dati nell'input form (inseriti dall'utente a runtime)
	 * @param list 
	 */
	@SuppressWarnings("deprecation")
	public static void readMatrixInInputForm(List<String> list) {
		rows = BeginInputForm.usedRows();
		cols = BeginInputForm.usedColumns();
		jTableData = new String[rows][cols];
		
		System.out.println(rows);
		System.out.println(cols);
		
		// fill table 
		int retro_i = 0;
		for (int i = 0; i < rows; i++) {
			/**
			 * @since 1.1
			 */
			if (!list.contains(BeginInputForm.jtable.getModel().getValueAt(i, 1))) {
				retro_i++;
				continue;
			}
			/**
			 * @since 1.0 but modified i in i - retro_i since @since 1.1
			 */
			for (int j = 0; j < cols; j++) {
				Object o = BeginInputForm.jtable.getModel().getValueAt(i, j);
				if (o == null)
					jTableData[i - retro_i][j] = "";
				else
					jTableData[i - retro_i][j] = o.toString();
			}
		}
		
		/**
		 * @since 1.1 avoid possible nullPointerException
		 */
		String[][] ls = new String[rows - retro_i][cols];
		for (int i = 0; i < rows - retro_i; i++) {
			for (int j = 0; j < cols; j++) {
				ls[i][j] = jTableData[i][j];
			}
		}
		jTableData = ls;
		JustConsolePrinter.printMatrix(jTableData);
		rows = rows - retro_i;
		BeginInputForm.rAfter = rows;
		
		//---------------
		
		// fill titles
		titles = new HashSet<>();
		for (int i = 0; i < rows; i++) {
			String rowTitle = jTableData[i][5];
			
			titles.add(rowTitle);
			
		}
		
		// fill availableSpacePerTitle
		availableSpacePerTitle = new HashMap<>();
		for (String title : titles) {
			
			int availableSpace = 0;
			
			for (int i = 0; i < rows; i++) {
				String rowTitle = jTableData[i][5];
				if (rowTitle.equals(title))
					availableSpace++;
			}
			
			availableSpacePerTitle.put(title, availableSpace);
		}
		
		// fill consumerCodeBySize 
		/**@since 1.5*/
		consumerCodeBySize = new HashMap<>();
		for (String title : titles) {
			System.out.println("->" + title);
			consumerCodeBySize.put(title, new HashMap<>());	
		}
		for (int i = 0; i < rows; i++) {
			String prod = jTableData[i][7 - 2];
			String codeConsumerProd = jTableData[i][8 - 2];
			String size = jTableData[i][9 - 2];
			// FIXME skip if some data is empty or blank
			//if (prod.isEmpty() || 
			//		codeConsumerProd.isEmpty()  || 
			//		size.isEmpty())
			//	continue;
			HashMap<String,String> ree = consumerCodeBySize.get(prod);
			System.out.println(prod);
			System.out.println(codeConsumerProd);
			System.out.println(size);
			System.out.println(ree);
			ree.put(size, codeConsumerProd)	;
		}
		
	}
	
	/**
	 * dice cosa ha scritto l'utente nella cella (x,y) dell'input form
	 * @param x x
	 * @param y y
	 * @return cosa ha scritto
	 */
	public static String get(int x, int y) {
		return jTableData[x][y];
	}
}
