import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.io.BufferedWriter;

import java.io.FileWriter;
/**
 * Class responsible for running this project based on the provided command-line arguments. See the
 * README for details.
 *
 * @author CS 212 Software Development
 * @author Angelica Liuzzi University of San Francisco
 * @version Spring 2020
 */
public class Driver {
	

  /**
   * Initializes the classes necessary based on the provided command-line arguments. This includes
   * (but is not limited to) how to build or search an inverted index.
   *
   * @param args flag/value pairs used to start this program
   */
	
	
  public static void main(String[] args) {
    // store initial start time
    Instant start = Instant.now();
    InvertedIndex invertedIndex = new InvertedIndex();
    CommandArguments commandArgs = new CommandArguments(args);
    ParseQuerySearchResults querySearch = new ParseQuerySearchResults();
    
    TreeMap<String, TreeMap<Path, TreeSet<Integer>>> wordToFileMap = invertedIndex.getWordToFileMapForPath(commandArgs);
    TreeMap<Path, Integer> wordCountMap = invertedIndex.getWordCountMap(commandArgs);
    TreeMap<String, List<Map<String, Object>>> mapOfAllQuerySearches = querySearch.getSearchResultForEveryQuery(TextFileStemmer.stemQueryFile(commandArgs.getQueryFilePath(), commandArgs.getQueryFileName()), commandArgs, invertedIndex);
    
    invertedIndex.writeToOutput(commandArgs, wordToFileMap);
    invertedIndex.writeWordCountOutput(commandArgs, wordCountMap);
    querySearch.writeSearchQueryResultsToOutput(commandArgs, mapOfAllQuerySearches);

    // calculate time elapsed and output
    Duration elapsed = Duration.between(start, Instant.now());
    double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
    System.out.printf("Elapsed: %f seconds%n", seconds);
  

  }
}
