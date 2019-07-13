package bin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class AppExcelInterfacer {
	
	/**
	 * foglio corrente
	 */
	@AnyTimeOk
	public static Sheet sheet;
	
	/**
	 * pointer corrente
	 */
	@AnyTimeOk
	public static int pointer;

	// cartella di lavoro, privata: è un riferimento al file excel
	@AnyTimeOk
	private static Workbook wb;
	
	@AnyTimeOk
	public static String read(int x, int y) {
		AppExcelInterfacer.Advanced.ensure(x,y);
		return sheet.getRow(x).getCell(y).toString();
	}
	
	@AnyTimeOk
	public static void write(int x, int y, String arg) {
		AppExcelInterfacer.Advanced.ensure(x,y);
		sheet.getRow(x).getCell(y).setCellValue(arg);
	}
	
	@AnyTimeOk
	public static void write(int x, int y, ArrayList<String[]> arg) {
		for (int i = 0; i < arg.size(); i++)
			for (int j = 0; j < arg.get(0).length; j++) {
				AppExcelInterfacer.Advanced.ensure(x + i, y + j);
				sheet.getRow(x + i).getCell(y + j).setCellValue(arg.get(i)[j]);
			}
	}
	
	@AnyTimeOk
	public static void switchSheet(int sheetindex) {
		sheet = wb.getSheetAt(sheetindex);
	}
	
	/**
	 * <p>Given column <code>indexColonna</code>.</p>
	 * <p>Goto to this column and scroll down.</p>
	 * <p>Get the position of the first empty cell.</p>
	 * <p>Now: <code>Main.pointer == position</code></p>
	 * @param index column index indexColonna
	 */
	public static void scrollDownGetFirstEmptyRow(int indexColonna) {
		for(int i = 0; ; i++)
	        if (sheet.getRow(i) == null 
	        	|| sheet.getRow(i).getCell(indexColonna) == null 
	        	|| sheet.getRow(i).getCell(indexColonna).toString().isEmpty() 
	        	// -----------------
	        	// Warning
	        	// -----------------
	            // Method isBlank() is not defined in JAVA SE 8
	        	// Method isBlank() is defined only since JAVA SE 11
	        	//
	        	//|| sheet.getRow(i).getCell(indexColonna).toString().isBlank()
	            //
	        	) {
	        	pointer = i;
	        	return;
	        }
	}
	
	@AnyTimeOk
	public static void open(File file) {
		try {
			FileInputStream fstream = new FileInputStream(file);
			wb = new HSSFWorkbook(fstream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AnyTimeOk
	public static void save(File file) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);  
	        fileOut.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	/**
	 * Comandi avanzati
	 */
	@AnyTimeOk
	static class Advanced extends AppExcelInterfacer{
		
		/**
		 * è un controllo che si fa prima di scrivere/leggere sulla cella
		 */
		@AnyTimeOk
		public static void ensure(int x, int y) {
			if(sheet.getRow(x) == null)
				sheet.createRow(x);
			if(sheet.getRow(x).getCell(y) == null)
				sheet.getRow(x).createCell(y);
		}
		
		/**
		 * crea bordo superiore in prossimità della riga pointer
		 * @param pointer riga
		 */
		@AnyTimeOk
		public static void borderUp(int pointer) {
			HSSFCellStyle t = (HSSFCellStyle) wb.createCellStyle();
		    t.setBorderTop(BorderStyle.THIN);
		    sheet.getRow(pointer).setRowStyle(t);
		    for (int i = 0; i < 30; i++) {
		    	AppExcelInterfacer.Advanced.ensure(pointer,i);
		        sheet.getRow(pointer).getCell(i).setCellStyle(t);
		    }
		}

	}

}

