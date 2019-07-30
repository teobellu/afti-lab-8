package ext.pub;

import java.util.ArrayList;

import bin.pub.AnyTimeOk;

public abstract class JustConsolePrinter {
	
	@AnyTimeOk
	public static void printStringGrid(ArrayList<String[]> array){
	    System.out.print("    |");
	    for (int i = 0; i < array.get(0).length; i++){
	        System.out.print("    ");
	        System.out.print((char)('A' + i));
	        System.out.print("    |");
	    }
	    System.out.println();
	    for (int i = 0; i < array.size(); i++){
	        System.out.print("----+");
	        for (int j = 0; j < array.get(0).length; j++){
	            System.out.print("---------+");
	        }
	        System.out.println();
	        if (i + 1 < 10)
	        System.out.print("  " + (i + 1) + " |");
	        else
	        System.out.print(" " + (i + 1) + " |");
	        for (int j = 0; j < array.get(0).length; j++){
	            if (array.get(i)[j].length() < 10){
	                int spaces = (9 - array.get(i)[j].length()) / 2;
	                for (int k = 0; k < spaces; k++){
	                    System.out.print(" ");
	                }
	                System.out.print(array.get(i)[j]);
	                for (int k = 0; k < (9 - array.get(i)[j].length()) - spaces; k++){
	                    System.out.print(" ");
	                }
	            }
	            else{
	                System.out.print(array.get(i)[j].substring(0, 9));
	            }
	            System.out.print("|");
	        }
	        System.out.println();
	    }
	}
	
	@AnyTimeOk
	public static void printMatrix(ArrayList<String[]> m){
	    try{
	        int rows = m.size();
	        int columns = m.get(0).length;
	        String str = "|\t";

	        for(int i=0;i<rows;i++){
	            for(int j=0;j<columns;j++){
	                str += m.get(i)[j] + "\t";
	            }

	            System.out.println(str + "|");
	            str = "|\t";
	        }

	    }catch(Exception e){System.out.println("Matrix is empty!!");}
	}
	
	/**
	 * 
	 * 
	 * 
	 *  Below are Deprecated
	 * 
	 * 
	 * 
	 * 
	 */
	
	@Deprecated
	static void printStringGrid(String[][] array){
	    System.out.print("    |");
	    for (int i = 0; i < array[0].length; i++){
	        System.out.print("    ");
	        System.out.print((char)('A' + i));
	        System.out.print("    |");
	    }
	    System.out.println();
	    for (int i = 0; i < array.length; i++){
	        System.out.print("----+");
	        for (int j = 0; j < array[0].length; j++){
	            System.out.print("---------+");
	        }
	        System.out.println();
	        System.out.print("  " + (i + 1) + " |");
	        for (int j = 0; j < array[0].length; j++){
	            if (array[i][j].length() < 10){
	                int spaces = (9 - array[i][j].length()) / 2;
	                for (int k = 0; k < spaces; k++){
	                    System.out.print(" ");
	                }
	                System.out.print(array[i][j]);
	                for (int k = 0; k < (9 - array[i][j].length()) - spaces; k++){
	                    System.out.print(" ");
	                }
	            }
	            else{
	                System.out.print(array[i][j].substring(0, 9));
	            }
	            System.out.print("|");
	        }
	        System.out.println();
	    }
	}
	
	@Deprecated
	static void printTable(ArrayList<String[]> table) {
		  // Find out what the maximum number of columns is in any row
		  int maxColumns = 0;
		  for (int i = 0; i < table.size(); i++) {
		    maxColumns = Math.max(table.get(i).length, maxColumns);
		  }

		  // Find the maximum length of a string in each column
		  int[] lengths = new int[maxColumns];
		  for (int i = 0; i < table.size(); i++) {
		    for (int j = 0; j < table.get(i).length; j++) {
		      lengths[j] = Math.max(table.get(i)[j].length(), lengths[j]);
		    }
		  }

		  // Generate a format string for each column
		  String[] formats = new String[lengths.length];
		  for (int i = 0; i < lengths.length; i++) {
		   formats[i] = "%1$" + lengths[i] + "s" 
		       + (i + 1 == lengths.length ? "\n" : " ");
		 }

		  // Print 'em out
		  for (int i = 0; i < table.size(); i++) {
		    for (int j = 0; j < table.get(i).length; j++) {
		      System.out.printf(formats[j], table.get(i)[j]);
		    }
		  }
		}
	
	@Deprecated
	static void printTable(String[][] table) {
		  // Find out what the maximum number of columns is in any row
		  int maxColumns = 0;
		  for (int i = 0; i < table.length; i++) {
		    maxColumns = Math.max(table[i].length, maxColumns);
		  }

		  // Find the maximum length of a string in each column
		  int[] lengths = new int[maxColumns];
		  for (int i = 0; i < table.length; i++) {
		    for (int j = 0; j < table[i].length; j++) {
		      lengths[j] = Math.max(table[i][j].length(), lengths[j]);
		    }
		  }

		  // Generate a format string for each column
		  String[] formats = new String[lengths.length];
		  for (int i = 0; i < lengths.length; i++) {
		   formats[i] = "%1$" + lengths[i] + "s" 
		       + (i + 1 == lengths.length ? "\n" : " ");
		 }

		  // Print 'em out
		  for (int i = 0; i < table.length; i++) {
		    for (int j = 0; j < table[i].length; j++) {
		      System.out.printf(formats[j], table[i][j]);
		    }
		  }
		}
	
	
	@Deprecated
	public
	static void printMatrix(String[][] m){
	    try{
	        int rows = m.length;
	        int columns = m[0].length;
	        String str = "|\t";

	        for(int i=0;i<rows;i++){
	            for(int j=0;j<columns;j++){
	                str += m[i][j] + "\t";
	            }

	            System.out.println(str + "|");
	            str = "|\t";
	        }

	    }catch(Exception e){System.out.println("Matrix is empty!!");}
	}

}
