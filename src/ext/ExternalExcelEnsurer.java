package ext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import bin.AnyTimeOk;
import bin.AppExcelInterfacer;

public abstract class ExternalExcelEnsurer {
	
	@AnyTimeOk
	public static void check() {
		Row row;
	    Cell cell;
	    
		int rows; // No of rows
	    rows = AppExcelInterfacer.sheet.getPhysicalNumberOfRows();
	    
		int cols = 0; // No of columns
	    int tmp = 0;

	    // This trick ensures that we get the data properly even if it doesn't start from first few rows
	    for(int i = 0; i < 10 || i < rows; i++) {
	        row = AppExcelInterfacer.sheet.getRow(i);
	        if(row != null) {
	            tmp = AppExcelInterfacer.sheet.getRow(i).getPhysicalNumberOfCells();
	            if(tmp > cols) cols = tmp;
	        }
	    }

	    out: for(int r = 0; r < rows; r++) {
	        row = AppExcelInterfacer.sheet.getRow(r);
	        if(row != null) {
	            for(int c = 0; c < cols; c++) {
	                cell = row.getCell((short)c);
	                if(cell != null) {
	                    System.out.print(cell + " ");
	                    break out;
	                }
	            }
	        }
	    }
	}

}
