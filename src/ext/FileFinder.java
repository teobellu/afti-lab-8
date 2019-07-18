package ext;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Classe che potrebbe essere utilizzata per localizzare l'excel qualora si trovasse in una directory differente
 * @since 1.1
 */
public class FileFinder {
	
	/**
	 * Paths
	 * @since 1.1
	 */
	public static final List<Path> paths = new ArrayList<>();
	
	/**
	 * Nei parametri è presente un esempio
	 * @param root "C:\\Users\\NomeUtente"
	 * @param maxDepth 5 
	 * @param fileName "file Dati x sovracolli.xls" oppure "afti-lab-8_1.0.jar"
	 * @since 1.1
	 */
	@Deprecated
	public static void ret(String root, int maxDepth, String fileName) {
		int[] count = {0};
		try {
			/**
			 * Reference
			 * https://stackoverflow.com/questions/41038005/find-a-file-in-a-directory-and-sub-directories-using-java-8
			 * https://stackoverflow.com/questions/44007055/avoid-java-8-files-walk-termination-cause-of-java-nio-file-accessdeniedexc
			 * @since 1.1
			 */
		    Files.walkFileTree(Paths.get(root), EnumSet.noneOf(FileVisitOption.class), maxDepth, new SimpleFileVisitor<Path>() {
		                @Override
		                public FileVisitResult visitFile(Path file , BasicFileAttributes attrs) throws IOException {
		                	if (file.endsWith(fileName)) {
		    					System.out.println(file);
		    					paths.add(file);
		    				}
		                	++count[0];
		                    return FileVisitResult.CONTINUE;
		                }

		                @Override
		                public FileVisitResult visitFileFailed(Path file , IOException e) throws IOException {
		                    System.err.printf("Visiting failed for %s\n", file);
		                    return FileVisitResult.SKIP_SUBTREE;
		                }

		                @Override
		                public FileVisitResult preVisitDirectory(Path dir , BasicFileAttributes attrs) throws IOException {
		                    System.out.printf("About to visit directory %s\n", dir);
		                    return FileVisitResult.CONTINUE;
		                }
		            });
		} 
		catch (IOException e) {
		    e.printStackTrace();
		} 
		finally {
			System.out.println(count);
			System.out.println(count[0]);
		}
	}
	
	/**
	 * Apply a function to paths {@link FileFinder.paths} (link works or n)
	 * @since 1.1
	 */
	@Deprecated
	public static void applyFunctionOnPaths() {
		for (Path p : paths) {
			String s = p.toString();
			//s.replace("\\", "\\\\");
			File file = new File(s);
			System.out.println(file);
			//file.  operation ()
		}
	}

}
