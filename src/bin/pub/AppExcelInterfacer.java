package bin.pub;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
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
				
				/**
				 * @since 1.1
				 */
				String value = arg.get(i)[j];
				try {
					int intValue = Integer.parseInt(value);
					/**
					 * Formato per scrivere i numeri
					 * int style
					 * https://stackoverflow.com/questions/24934975/poi-format-integer-in-excelsheet
					 * @since 1.1
					 */
					// use style wherever you need it
					CellStyle integerStyle = wb.createCellStyle();
					integerStyle.setDataFormat((short) 0);
					// sulla base di ciò...
					
					sheet.getRow(x + i).getCell(y + j).setCellValue(intValue);
					CellStyle oldStyle = sheet.getRow(x + i).getCell(y + j).getCellStyle();
					oldStyle.setDataFormat((short) 0);
					sheet.getRow(x + i).getCell(y + j).setCellStyle(oldStyle);
				} catch (Exception e) {
					sheet.getRow(x + i).getCell(y + j).setCellValue(value);
				}
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
			JOptionPane.showMessageDialog(null, "Cannot load file! Dev. recall: do not open jar from eclipse.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	@AnyTimeOk
	public static void save(File file) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);  
	        fileOut.close(); 
		} catch (Exception e) {
			/**@since 1.2*/
			JOptionPane.showMessageDialog(new JFrame(), 
					"Error: You forgot to close Excel.\nError: A fatal exception has occurred. Program will exit.",
					"Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			e.printStackTrace();
		}  
	}
	
	/**
	 * Comandi avanzati
	 */
	@AnyTimeOk
	static public class Advanced extends AppExcelInterfacer{
		
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
		
		@SuppressWarnings("unused")
		private final static int[] COLORED = {13,14,16,17,20,21,24,25,28,29,32,33,36,37,41};
		private final static int[] BLUES   = {13,14};
		private final static int[] GRAYS   =       {16,17,20,21,24,25,28,29,32,33,36,37};
		private final static int[] REDS    = 										   {41};
		@SuppressWarnings("unused") 
		private final static Color BLUE    = new Color(197, 217, 241);
		@SuppressWarnings("unused")
		private final static Color GRAY    = new Color(242, 242, 242);
		@SuppressWarnings("unused")
		private final static Color RED     = new Color(218, 150, 148);
		
		/**
		 * crea bordo superiore in prossimità della riga pointer
		 * @param pointer riga
		 */
		public static void borderUp(int pointer) {
			try {
				borderUpUnsafe(pointer);
			}catch(Exception e) {
				System.out.println("Style error");
				System.err.println(e);
			}
			
			//https://stackoverflow.com/questions/44176582/java-lang-illegalstateexception-you-can-define-up-to-4000-styles-in-a-xls-work
			//sheet.getRow(pointer).setCellStyle(r);
		}
		
		/**
		 * crea bordo superiore in prossimità della riga pointer
		 * @param pointer riga
		 */
		public static void borderUpUnsafe(int pointer) {
			CellStyle t = (HSSFCellStyle) wb.createCellStyle();
			Font boldFont = wb.createFont();
			boldFont.setBold(true);
			
			/**
			 * This is part is available @since 1.1
			 */
			for (int i = 0; i < 44 + 3 /**the +3 is @since 1.2*/; i++) {
				AppExcelInterfacer.Advanced.ensure(pointer,i);
				CellStyle oldStyle = sheet.getRow(pointer).getCell(i).getCellStyle();
				CellStyle r = wb.createCellStyle(); //B2
				// tante celle puntano a oldStyle
				r.cloneStyleFrom(oldStyle);
				// passaggi non indifferenti:
				// Cloneable ci ha aiutati, ora solo la nostra cella punta a r
				r.setBorderTop(BorderStyle.THIN);
				sheet.getRow(pointer).getCell(i).setCellStyle(r);
			}
			if (pointer < 200) // false
			/**
			 * This part is deprecated @since 1.1
			 */
		    for (int i = 0; i < 44; i++) {
		    	AppExcelInterfacer.Advanced.ensure(pointer,i);
		    	if (Arrays.binarySearch(BLUES, i) >= 0) // contains
		    	{
		    		t = (HSSFCellStyle) wb.createCellStyle();
				    t.setFont(boldFont);
				    t.setBorderTop(BorderStyle.THIN);
				    t.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
				    t.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    		sheet.getRow(pointer).getCell(i).setCellStyle(t);
		    	}
		    	else if (Arrays.binarySearch(GRAYS, i) >= 0){
		    		t = (HSSFCellStyle) wb.createCellStyle();
		    		t.setFont(boldFont);
				    t.setBorderTop(BorderStyle.THIN);
				    t.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
				    t.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				    if (i == 16) {
				    	t.setBorderLeft(BorderStyle.THIN);
				    }
		    		sheet.getRow(pointer).getCell(i).setCellStyle(t);
		    	}
		    	else if (Arrays.binarySearch(REDS, i) >= 0){
		    		t = (HSSFCellStyle) wb.createCellStyle();
		    		t.setFont(boldFont);
				    t.setBorderTop(BorderStyle.THIN);
				    t.setFillForegroundColor(IndexedColors.CORAL.getIndex());
				    t.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    		sheet.getRow(pointer).getCell(i).setCellStyle(t);
		    	}
		    	else {
		    		t = (HSSFCellStyle) wb.createCellStyle();
				    t.setBorderTop(BorderStyle.THIN);
				    t.setFillForegroundColor(IndexedColors.WHITE.getIndex());
				    //t.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    		sheet.getRow(pointer).getCell(i).setCellStyle(t);
		    	}
		    }
		}

	}

}

