import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A utility class for finding all text files in a directory using lambda functions and streams.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2020
 */
public class TextFileFinder {

  /**
   * A lambda function that returns true if the path is a file that ends in a .txt or .text
   * extension (case-insensitive). Useful for {@link Files#walk(Path, FileVisitOption...)}.
   *
   * @see Files#isRegularFile(Path, java.nio.file.LinkOption...)
   * @see Path#getFileName()
   * @see Files#walk(Path, FileVisitOption...)
   */
  public final static Predicate<Path> isText = path -> (!Files.isDirectory(path) && Files.isRegularFile(path)
		  && (path.toString().toLowerCase().endsWith(".txt")
		  || (path.toString().toLowerCase().endsWith(".text"))));

  /**
   * A lambda function that returns true if the path is a file that ends in a .txt or .text
   * extension (case-insensitive). Useful for
   * {@link Files#find(Path, int, BiPredicate, FileVisitOption...)}.
   *
   * @see Files#find(Path, int, BiPredicate, FileVisitOption...)
   */
  // DO NOT MODIFY; THIS IS PROVIDED FOR YOU
  // (Hint: This is only useful if you decide to use Files.find(...) instead of Files.walk(...)
  public final BiPredicate<Path, BasicFileAttributes> isTextWithAttribute =
      (path, attr) -> isText.test(path);

  /**
   * Returns a stream of text files, following any symbolic links encountered.
   *
   * @param start the initial path to start with
   * @return a stream of text files
   *
   * @throws IOException if an I/O error occurs
   *
   */
  public Stream<Path> find(Path start) throws IOException {
	  
	 return Files.find(start, Integer.MAX_VALUE, isTextWithAttribute);	  
  }

  /**
   * Returns a list of text files.
   *
   * @param start the initial path to search
   * @return list of text files
   * @throws IOException if an I/O error occurs
   *
   * @see #find(Path)
   */
  public List<Path> list(Path start) throws IOException {
    // THIS METHOD IS PROVIDED FOR YOU DO NOT MODIFY
    return find(start).collect(Collectors.toList());
  }


}

