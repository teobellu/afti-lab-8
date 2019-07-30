package bin;

//import java.awt.Toolkit;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import bin.pub.AppExcelInterfacer;
import bin.pub.AppOutputInterfacer;
import bin.pub.BeginInputForm;
import bin.pub.Solver;
import ext.SecurityEnsurer;
import ext.pub.ExternalExcelEnsurer;

/**
 * contains Main
 *
 */
public abstract class BeginMain {
	
	public static File file = new File("./file Dati x sovracolli.xls");
	
	public static void main(String[] args) {
		try {
		SecurityEnsurer.checkSecurity();
		AppExcelInterfacer.open(file);
		BeginInputForm.show();
		
		 //set Workbook
		AppExcelInterfacer.switchSheet(0); //open first sheet
	    AppExcelInterfacer.scrollDownGetFirstEmptyRow(2); //set pointer
	    //Utilizer.jwrite();
	    //Utilizer.jinvoke();
	    Solver.run();
	    AppExcelInterfacer.Advanced.borderUp(AppExcelInterfacer.pointer);
		AppOutputInterfacer.prepareOutputSpace();
		AppOutputInterfacer.jinvoke();
		AppExcelInterfacer.write(AppExcelInterfacer.pointer, 0, AppOutputInterfacer.outputTableData);
		
		//optional external controls for excel
		ExternalExcelEnsurer.check();
	    
	    AppExcelInterfacer.save(file);
	    
	    if (BeginInputForm.FLOW_MODALITY_ON && BeginInputForm.FLOW_REMAINING_COUNT != 0) {
	    	BeginInputForm.flow();
	    	flowmain(args);
	    }
		}
		/**
		 * @since 1.5
		 */
		catch(Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			System.out.println(sStackTrace);
			SecurityEnsurer.securityFault(sStackTrace);
		}
	}
	
	/**
	 * RESTART @since 1.4
	 * @param args
	 * @throws Exception
	 */
	public static void flowmain(String[] args) throws Exception{
		 //set Workbook
		AppExcelInterfacer.switchSheet(0); //open first sheet
	    AppExcelInterfacer.scrollDownGetFirstEmptyRow(2); //set pointer
	    //Utilizer.jwrite();
	    //Utilizer.jinvoke();
	    Solver.run();
	    AppExcelInterfacer.Advanced.borderUp(AppExcelInterfacer.pointer);
		AppOutputInterfacer.prepareOutputSpace();
		AppOutputInterfacer.jinvoke();
		AppExcelInterfacer.write(AppExcelInterfacer.pointer, 0, AppOutputInterfacer.outputTableData);
		
		//optional external controls for excel
		ExternalExcelEnsurer.check();
	    
	    AppExcelInterfacer.save(file);
	    
	    if (BeginInputForm.FLOW_MODALITY_ON && BeginInputForm.FLOW_REMAINING_COUNT != 0) {
	    	BeginInputForm.flow();
	    	flowmain(args);
	    }
	}
	
	
}
