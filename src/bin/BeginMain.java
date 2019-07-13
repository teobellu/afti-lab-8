package bin;

import java.io.File;

import ext.ExternalExcelEnsurer;

/**
 * contains Main
 *
 */
public abstract class BeginMain {
	
	public static File file = new File("./file Dati x sovracolli.xls");
	
	public static void main(String[] args) throws Exception{
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
		
	}

}
