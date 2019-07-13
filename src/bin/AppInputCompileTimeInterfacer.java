package bin;

import java.util.HashMap;
import java.util.HashSet;

public abstract class AppInputCompileTimeInterfacer {
	
	/**
	 * Prodotti di cui si conoscono le allowedSize, essi possono essere già salvati nell'excel in precedenza per esempio
	 */
	public static HashSet<String> products;
	
	/**
	 * mappa (Prodotto noto, per nome) e (quantità massima)- essi possono essere già salvati nell'excel in precedenza per esempio
	 */
	public static HashMap<String, Integer> allowedSizes;
	
	/**
	 * legge i dati dall'excel riguardo i prodotti di cui si conoscono le allowedSize
	 */
	public static void extractDataFromExcelAboutAllowedSized() {
		AppExcelInterfacer.switchSheet(1);
		AppExcelInterfacer.scrollDownGetFirstEmptyRow(0);
		
		products = new HashSet<>();
		allowedSizes = new HashMap<>();
		for(int i = 1; i < AppExcelInterfacer.pointer; i++) {
			String product = AppExcelInterfacer.read(i, 0);
			int allowedSize = (int) Double.parseDouble(AppExcelInterfacer.read(i, 3));
			
			products.add(product);
			allowedSizes.put(product, allowedSize);
		}
	}
	
	/**
	 * aggiunge una nuova informazione alla mappa (Prodotto noto, per nome) e (quantità massima)
	 * Alcuni valori sono già stati letti da file
	 * @param product nome prodotto
	 * @param allowedSize quantità permessa
	 */
	public static void addNewInformation(String product, int allowedSize) {
		products.add(product);
		allowedSizes.put(product, allowedSize);
		
		AppExcelInterfacer.write(AppExcelInterfacer.pointer, 0, product);
		AppExcelInterfacer.write(AppExcelInterfacer.pointer, 3, allowedSize + "");
		
		AppExcelInterfacer.pointer++;
	}

}
