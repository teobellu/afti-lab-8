package bin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entry {
	
    /**
     * Questa classe contiene tutte le info per un prodotto.
     * Tutte le info sono salvare in qtmap (= rqtmap teoricamente)
     */
	
	// prodotto
	public String title;
	
	// mappa (size, quantità)
	public Map<String, Integer> qtmap = new HashMap<String, Integer>();
	
	// parte piena e finisce vuota
	public Map<String, Integer> rqtmap = new HashMap<String, Integer>();
	
	// max q allowed
	int div = 20; //TODO
	
	public List<Box> computeBoxes() {
		//List<Box> boxes = new ArrayList<Box>();
		//int resto = tot % div;
		
		
		
		return computeBoxesNoRest();
	}
	
	private List<Box> computeBoxesNoRest() {
		List<Box> boxes = new ArrayList<Box>();
		
		List<String> clearkey = new ArrayList<String>();
		
		//from 0 goto clear
		for (String size : rqtmap.keySet()) {
			int qsize = rqtmap.get(size);
			if (qsize == 0) {
				clearkey.add(size);
			}
		}
		
		clearkey.forEach(k -> rqtmap.remove(k));
		clearkey.clear();
		
		//from 40 goto 20 20
		//from 20 goto 20
		for (String size : rqtmap.keySet()) {
			int qsize = rqtmap.get(size);
			if (qsize % div == 0 && qsize > 0) {
				Box box = new Box(title);
				box.intern.put(size, div);
				box.scale = qsize/div;
				boxes.add(box);
				clearkey.add(size);
			}
		}
		
		clearkey.forEach(k -> rqtmap.remove(k));
		clearkey.clear();
		
		//from 113 goto 20 20 20 20 20 + keep 13
		for (String size : rqtmap.keySet()) {
			int qsize = rqtmap.get(size);
			if (qsize > div) {
				Box box = new Box(title);
				box.intern.put(size, div); //XL 20
				box.scale = (int)qsize/div; //5
				boxes.add(box);
				rqtmap.put(size, qsize - (box.scale * div));
			}
		}
		
		clearkey.forEach(k -> rqtmap.remove(k));
		clearkey.clear();
		
		for(int divi = div; divi >= 0; divi--) {
			if (rqtmap.isEmpty()) 
				return boxes;
			else
				perfectMatch(boxes, divi);
		}
		
		//the rest
		Box currentbox = new Box(title);
		for (String size : rqtmap.keySet()) {
			System.out.println("In the rest");
			int qsize = rqtmap.get(size);
			
			if (qsize <= 0) {
				continue;
			}
			
			//all inside is possible?
			int available = div - currentbox.getTotalQuantity();
			if (available > qsize) {
				currentbox.intern.put(size, qsize);
				continue;
			}
			
			//all inside is not possible?
			else {
				currentbox.intern.put(size, available);
				boxes.add(currentbox);
				currentbox = new Box(title);
				if (qsize - available > 0)
					currentbox.intern.put(size, qsize - available);
				continue;
			}
		}
		if (currentbox.getTotalQuantity() > 0)
			boxes.add(currentbox);
		return boxes;
	}
	
	/**
	 * with side effect on boxes
	 */
	public void perfectMatch(List<Box> boxes, int localdiv) { //e.g. local_div = 20
		List<String> clearkey = new ArrayList<String>();
		
		//from 20 goto 20
		for (String size : rqtmap.keySet()) {
			int qsize = rqtmap.get(size);
			if (qsize == localdiv) {
				Box box = new Box(title);
				box.intern.put(size, localdiv);
				box.scale = 1;
				boxes.add(box);
				clearkey.add(size);
			}
		}
		
		clearkey.forEach(k -> rqtmap.remove(k));
		clearkey.clear();
		
		//from 15 + 5 goto 20
		List<String> sizes = new ArrayList<String>(rqtmap.keySet());
		if (sizes.size() >= 2)
		for (int i = 0; i < sizes.size() - 1; i++) {
			for (int j = i + 1; j < sizes.size(); j++) {
				String sizei = sizes.get(i);
				String sizej = sizes.get(j);
				int qsizei = rqtmap.get(sizei);
				int qsizej = rqtmap.get(sizej);
				if (qsizei == 0 || qsizej == 0)
					continue;
				if (qsizei + qsizej == localdiv) {
					Box box = new Box(title);
					box.intern.put(sizei, qsizei); //M 15
					box.intern.put(sizej, qsizej); //XL 5
					box.scale = 1;
					boxes.add(box);
					rqtmap.put(sizei, 0); //useless
					rqtmap.put(sizej, 0); //useful
					clearkey.add(sizei);
					clearkey.add(sizej);
				}
			}
		}
		
		clearkey.forEach(k -> rqtmap.remove(k));
		clearkey.clear();
		
		//from 7 + 8 + 5 goto 20
		sizes = new ArrayList<String>(rqtmap.keySet());
		if (sizes.size() >= 3)
		for (int i = 0; i < sizes.size() - 2; i++) {
			for (int j = i + 1; j < sizes.size() - 1; j++) {
				for (int k = j + 1; k < sizes.size(); k++) {
					String sizei = sizes.get(i);
					String sizej = sizes.get(j);
					String sizek = sizes.get(k);
					int qsizei = rqtmap.get(sizei);
					int qsizej = rqtmap.get(sizej);
					int qsizek = rqtmap.get(sizek);
					if (qsizei == 0 || qsizej == 0 || qsizek == 0)
						continue;
					if (qsizei + qsizej + qsizek == localdiv) {
						Box box = new Box(title);
						box.intern.put(sizei, qsizei); //M 7
						box.intern.put(sizej, qsizej); //L 8
						box.intern.put(sizek, qsizek); //XL 5
						box.scale = 1;
						boxes.add(box);
						rqtmap.put(sizei, 0); //useless
						rqtmap.put(sizej, 0); //useful?
						rqtmap.put(sizek, 0); //useful
						clearkey.add(sizei);
						clearkey.add(sizej);
						clearkey.add(sizek);
					}
				}
				
			}
		}
		
		clearkey.forEach(k -> rqtmap.remove(k));
		clearkey.clear();
		
		//from 7 + 8 + 3 + 2 goto 20
		sizes = new ArrayList<String>(rqtmap.keySet());
		if (sizes.size() >= 4)
		for (int i = 0; i < sizes.size() - 3; i++) {
			for (int j = i + 1; j < sizes.size() - 2; j++) {
				for (int k = j + 1; k < sizes.size() - 1; k++) {
					for (int h = k + 1; h < sizes.size(); h++) {
						String sizei = sizes.get(i);
						String sizej = sizes.get(j);
						String sizek = sizes.get(k);
						String sizeh = sizes.get(h);
						int qsizei = rqtmap.get(sizei);
						int qsizej = rqtmap.get(sizej);
						int qsizek = rqtmap.get(sizek);
						int qsizeh = rqtmap.get(sizeh);
						if (qsizei == 0 || qsizej == 0 || qsizek == 0 || qsizeh == 0)
							continue;
						if (qsizei + qsizej + qsizek + qsizeh == localdiv) {
							Box box = new Box(title);
							box.intern.put(sizei, qsizei); //M 7
							box.intern.put(sizej, qsizej); //L 8
							box.intern.put(sizek, qsizek); //XL 3
							box.intern.put(sizeh, qsizeh); //XXL 2
							box.scale = 1;
							boxes.add(box);
							rqtmap.put(sizei, 0); //useless
							rqtmap.put(sizej, 0); //useful?
							rqtmap.put(sizek, 0); //useful?
							rqtmap.put(sizeh, 0); //useful
							clearkey.add(sizei);
							clearkey.add(sizej);
							clearkey.add(sizek);
							clearkey.add(sizeh);
						}
					}
					
				}
				
			}
		}
		
		clearkey.forEach(k -> rqtmap.remove(k));
		clearkey.clear();
		
		//from 7 + 8 + 3 + 1 + 1 goto 20
		sizes = new ArrayList<String>(rqtmap.keySet());
		if (sizes.size() >= 5)
		for (int i = 0; i < sizes.size() - 4; i++) {
			for (int j = i + 1; j < sizes.size() - 3; j++) {
				for (int k = j + 1; k < sizes.size() - 2; k++) {
					for (int h = k + 1; h < sizes.size() - 1; h++) {
						for (int v = h + 1; v < sizes.size(); v++) {
							String sizei = sizes.get(i);
							String sizej = sizes.get(j);
							String sizek = sizes.get(k);
							String sizeh = sizes.get(h);
							String sizev = sizes.get(v);
							int qsizei = rqtmap.get(sizei);
							int qsizej = rqtmap.get(sizej);
							int qsizek = rqtmap.get(sizek);
							int qsizeh = rqtmap.get(sizeh);
							int qsizev = rqtmap.get(sizev);
							if (qsizei == 0 || qsizej == 0 || qsizek == 0 || qsizeh == 0 || qsizev == 0)
								continue;
							if (qsizei + qsizej + qsizek + qsizeh + qsizev == localdiv) {
								Box box = new Box(title);
								box.intern.put(sizei, qsizei); 
								box.intern.put(sizej, qsizej); 
								box.intern.put(sizek, qsizek); 
								box.intern.put(sizeh, qsizeh); 
								box.intern.put(sizev, qsizev); 
								box.scale = 1;
								boxes.add(box);
								rqtmap.put(sizei, 0); 
								rqtmap.put(sizej, 0); 
								rqtmap.put(sizek, 0);
								rqtmap.put(sizeh, 0); 
								rqtmap.put(sizev, 0); 
								clearkey.add(sizei);
								clearkey.add(sizej);
								clearkey.add(sizek);
								clearkey.add(sizeh);
								clearkey.add(sizev);
							}
						}
					}
				}
			}
		}
		
		clearkey.forEach(k -> rqtmap.remove(k));
		clearkey.clear();
		
		//from 7 + 8 + 2 + 1 + 1 + 1 goto 20
		sizes = new ArrayList<String>(rqtmap.keySet());
		if (sizes.size() >= 6)
		for (int i = 0; i < sizes.size() - 5; i++) {
			for (int j = i + 1; j < sizes.size() - 4; j++) {
				for (int k = j + 1; k < sizes.size() - 3; k++) {
					for (int h = k + 1; h < sizes.size() - 2; h++) {
						for (int v = h + 1; v < sizes.size() - 1; v++) {
							for (int w = v + 1; w < sizes.size(); w++) {
								String sizei = sizes.get(i);
								String sizej = sizes.get(j);
								String sizek = sizes.get(k);
								String sizeh = sizes.get(h);
								String sizev = sizes.get(v);
								String sizew = sizes.get(w);
								int qsizei = rqtmap.get(sizei);
								int qsizej = rqtmap.get(sizej);
								int qsizek = rqtmap.get(sizek);
								int qsizeh = rqtmap.get(sizeh);
								int qsizev = rqtmap.get(sizev);
								int qsizew = rqtmap.get(sizew);
								if (qsizei == 0 || qsizej == 0 || qsizek == 0 || qsizeh == 0 || qsizev == 0 || qsizew == 0)
									continue;
								if (qsizei + qsizej + qsizek + qsizeh + qsizev + qsizew == localdiv) {
									Box box = new Box(title);
									box.intern.put(sizei, qsizei); 
									box.intern.put(sizej, qsizej); 
									box.intern.put(sizek, qsizek); 
									box.intern.put(sizeh, qsizeh); 
									box.intern.put(sizev, qsizev); 
									box.intern.put(sizew, qsizew); 
									box.scale = 1;
									boxes.add(box);
									rqtmap.put(sizei, 0); 
									rqtmap.put(sizej, 0); 
									rqtmap.put(sizek, 0);
									rqtmap.put(sizeh, 0); 
									rqtmap.put(sizev, 0); 
									rqtmap.put(sizew, 0); 
									clearkey.add(sizei);
									clearkey.add(sizej);
									clearkey.add(sizek);
									clearkey.add(sizeh);
									clearkey.add(sizev);
									clearkey.add(sizew);
								}
							}
						}
					}
				}
			}
		}
		
		clearkey.forEach(k -> rqtmap.remove(k));
		clearkey.clear();
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Entry))
			return false;
		return title.equals(((Entry)obj).title);
	}
	
	@Override
	public int hashCode() {
		return title.hashCode();
	}

}
