
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Utility class for parsing and stemming text and text files into collections of stemmed words.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2020
 *
 * @see TextParser
 */
public class TextFileStemmer {

  /** The default stemmer algorithm used by this class. */
	
  public final static SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;


  /**
   * Returns a list of cleaned and stemmed words parsed from the provided line.
   *
   * @param line the line of words to clean, split, and stem
   * @param stemmer the stemmer to use
   * @return a list of cleaned and stemmed words
   *
   * @see Stemmer#stem(CharSequence)
   * @see TextParser#parse(String)
   */
  public ArrayList<String> listStems(String line, Stemmer stemmer) {
    
	ArrayList<String> parsedList = new ArrayList<String>();
	if(!line.equals(null)) {
		
		for(String word: TextParser.parse(line)) {
			
			parsedList.add(stemmer.stem(word).toString());
		}
	}
	return parsedList;
  }

  /**
   * Returns a list of cleaned and stemmed words parsed from the provided line.
   *
   * @param line the line of words to clean, split, and stem
   * @return a list of cleaned and stemmed words
   *
   * @see SnowballStemmer
   * @see #DEFAULT
   * @see #listStems(String, Stemmer)
   */
  public ArrayList<String> listStems(String line) {
    // THIS IS PROVIDED FOR YOU; NO NEED TO MODIFY
    return listStems(line, new SnowballStemmer(DEFAULT));
  }

  /**
   * Returns a set of unique (no duplicates) cleaned and stemmed words parsed from the provided
   * line.
   *
   * @param line the line of words to clean, split, and stem
   * @param stemmer the stemmer to use
   * @return a sorted set of unique cleaned and stemmed words
   *
   * @see Stemmer#stem(CharSequence)
   * @see TextParser#parse(String)
   */
  public TreeSet<String> uniqueStems(String line, Stemmer stemmer) {
	  TreeSet<String> nonDuplicates = new TreeSet<String>();
	  for(String word: listStems(line, stemmer)){
		  nonDuplicates.add(word);
	  }
	  return nonDuplicates;
	  
	 }

  /**
   * Returns a set of unique (no duplicates) cleaned and stemmed words parsed from the provided
   * line.
   *
   * @param line the line of words to clean, split, and stem
   * @return a sorted set of unique cleaned and stemmed words
   *
   * @see SnowballStemmer
   * @see #DEFAULT
   * @see #uniqueStems(String, Stemmer)
   */
  public TreeSet<String> uniqueStems(String line) {
    // THIS IS PROVIDED FOR YOU; NO NEED TO MODIFY
    return uniqueStems(line, new SnowballStemmer(DEFAULT));
  }

  /**
   * Reads a file line by line, parses each line into cleaned and stemmed words, and then adds those
   * words to a set.
   *
   * @param inputFile the input file to parse
   * @return a sorted set of stems from file
   * @throws IOException if unable to read or parse file
   *
   * @see #uniqueStems(String)
   * @see TextParser#parse(String)
   */
  public TreeSet<String> uniqueStems(Path inputFile) throws IOException {
	  TreeSet<String> tSet = new TreeSet<String>();
	  Stemmer stemmer = new SnowballStemmer(DEFAULT);
	  Files.lines(inputFile)
	       .map(it -> uniqueStems(it, stemmer))
	       .forEach(it -> tSet.addAll(it));
	  
	  return tSet;  
  }
  
  /**
   * Stems all of the words in file
   *
   * @param file The current file to be stemmed
   * @param commandArgs
   * @param fileStemmer
   * @return TreeSet fileStemme r.listStems(file) the stemmed file as an ArrayList of words
   */

public static List<TreeSet<String>> stemQueryFile(Path file, String queryFileName) {
	// TreeSet<String> tSet = new TreeSet<String>();
	try {
		if (queryFileName != null) {
			return uniqueStemsOfEachLine(file);
			// uniqueStems Reads a file line by line, parses each line into cleaned and
			// stemmed words, and then adds those words to a set.
		} else {
			System.out.println("No Query File Found");
		}
	} catch (Exception e) {
		System.out.println("File not found.");

	}
	return new ArrayList<TreeSet<String>>();
}
  
  /**
   * Reads a file line by line, parses each line into cleaned and stemmed words, and then adds those
   * words to a set.
   *
   * @param queryFile the input file to parse
   * @return a sorted set of stems from file
   * @throws IOException if unable to read or parse file
   *
   * @see #uniqueStems(String)
   * @see TextParser#parse(String)
   */
  private static List<TreeSet<String>> uniqueStemsOfEachLine(Path queryFile) throws IOException {
	  List<TreeSet<String>> listOfTreeSetsForEachQuery = new ArrayList<TreeSet<String>>(); //list of stemmed word sets
	  List<String> allLines = Files.readAllLines(queryFile);
	  Stemmer stemmer = new SnowballStemmer(DEFAULT);
	  for(String line: allLines) {
		  TreeSet<String> tSet = new TreeSet<String>();
		  for(String word: TextParser.parse(line)) {
			  tSet.add(stemmer.stem(word).toString());
		  }
		  listOfTreeSetsForEachQuery.add(tSet);
	  }
	  
	  return listOfTreeSetsForEachQuery;  
	  
  }

  /**
   * Reads a file line by line, parses each line into cleaned and stemmed words, and then adds those
   * words to a set.
   *
   * @param inputFile the input file to parse
   * @return a sorted set of stems from file
   * @throws IOException if unable to read or parse file
   *
   * @see #uniqueStems(String)
   * @see TextParser#parse(String)
   */
  public ArrayList<String> listStems(Path inputFile) throws IOException {

    ArrayList<String> parsedList = new ArrayList<String>();
    Stemmer stemmer = new SnowballStemmer(DEFAULT);
    Files.lines(inputFile)
         .map(it -> listStems(it, stemmer))
         .forEach(it -> parsedList.addAll(it));
    return parsedList;
  }


}
