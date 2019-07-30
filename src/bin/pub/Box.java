package bin.pub;

import java.util.HashMap;
import java.util.Map;

public class Box /**cloneable @since 1.2*/ implements Cloneable {
	
	/**
	 * Nome prodotto (esempio: Camicia verde a maniche fucsia)
	 */
	public String product;
	
	/**
	 * mappa size (X, XL...) e quantità (20, 15...)
	 */
	public Map<String, Integer> intern = new HashMap<String, Integer>();

	/**
	 * Esempio: ho 40 XL ma qmax = 20, avrò 2 (quindi scale = 2) box con qtà = 20.
	 */
	public int scale = 1;
	
	/**
	 * Costruisce un box. Sarà possibile dare un valore <code>scale</code> che rappresenta
	 * quanti box sono uguali a esso.
	 * Esempio: ho 40 XL ma qmax = 20, avrò 2 (quindi scale = 2) box con qtà = 20.
	 * @param product
	 */
	public Box(String product) {
		this.product = product;
	}
	
	/**
	 * Restituisce tutte le quantità. Esempio nel box ci sono 4 S e 4 XL, ritorno 8
	 * 
	 * --------------------
	 * WARNING
	 * --------------------
	 * Se nel box ci sono 40 XL, ritorna 20 se scale = 2
	 * @return Nell'esempio, 8
	 */
	public int getTotalQuantity() {
		int tot = 0;
		for (Integer i : intern.values()) {
			tot += i;
		}
		return tot;
	}
	
	/**
	 * Box is
	 * CLONEABLE SINCE @since 1.2
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Box B = new Box(this.product);
		B.scale = this.scale;
		for (String size : this.intern.keySet()) {
			B.intern.put(size, this.intern.get(size));
		}
		return super.clone();
	}
}
