import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Outputs files provided from command line to necessary locations.
 *
 * @author Angelica Liuzzi CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2020
 */

public class OutputInitializer {
	  /**
	   * Processes all word files into a Nested TreeMap.
	   *
	   * @param commandArgs The command line arguments to be provided in Driver
	   * @return wordToFileMap the Nested Tree Map of the Words and their File + locations
	   */
   public TreeMap<String, TreeMap<Path, TreeSet<Integer>>> getWordToFileMapForPath(CommandArguments commandArgs){
	   TreeMap<String, TreeMap<Path, TreeSet<Integer>>> wordToFileMap = new TreeMap<>(); 
	   List<Path> pathsList = getPathsList(commandArgs);
	   for(Path file: pathsList) {
		   List<String> fileWords = stemFile(file);
		   processFileStems(fileWords, file, wordToFileMap); 
	   }
	   
	   return wordToFileMap;
	   
   }
   
   public TreeMap<Path, Integer> getWordCountMap(CommandArguments ca){
	   List<Path> pathsList = getPathsList(ca);
	   TreeMap<Path, Integer> wordCountMap = new TreeMap<Path, Integer>();
	   for(Path file: pathsList) {
		   List<String> fileWords = stemFile(file);
		   wordCountMap.put(file, fileWords.size());
	   }
	   return wordCountMap;
   }
   
      /**
	   * Processes the input file path argument
	   *
	   * @param commandArgs The command line arguments to be provided in Driver
	   * @return pathsList a List of every file to read or an empty ArrayList
	   */

    public List<Path> getPathsList(CommandArguments commandArgs){
    	try {
    		if(commandArgs.getInputFilePath() != null) {
    			TextFileFinder fileFinder = new TextFileFinder();
    			List<Path> pathsList = fileFinder.list(commandArgs.getInputFilePath()); 
    			return pathsList;//creates a List of every file to read
    		}else {
    			System.out.println("No Input File Path Found.");
    		}
    	}catch (IOException e) {
    		System.out.println("IO Excpetion");
    	}
    	return new ArrayList<>();
    }
    
      /**
	   * Stems all of the words in file
	   *
	   * @param file The current file to be stemmed
	   * @return fileStemmer.listStems(file) the stemmed file as an ArrayList of words
	   */

    public List<String> stemFile(Path file){
    	TextFileStemmer fileStemmer = new TextFileStemmer();
    	try {
			return fileStemmer.listStems(file);
		} catch (Exception e) {
			System.out.println("File not found.");
			return new ArrayList<>();
		} 
    	
    }
    
      /**
	   * Processes the stemmed words and puts them into a TreeMap while also accounting for all of the positions the word was found
	   *
	   * @param fileWords List of words to add to TreeMap
	   * @param file The file path to search through
	   * @param wordToFileMap the Nested Tree Map of the Words and their File + locations
	   */

    public void processFileStems(List<String> fileWords, Path file, TreeMap<String, TreeMap<Path, TreeSet<Integer>>> wordToFileMap) {
    	for(int index = 0; index < fileWords.size(); index++) {
    		int position = index+1;
    		TreeMap<Path, TreeSet<Integer>> fileNameToPositionsMap = getOrCreateFileNameToPositionsMap(fileWords, index, wordToFileMap);
    		TreeSet<Integer> positionSet = getOrCreatePositionSet(fileNameToPositionsMap, file);
    		positionSet.add(position);
    	}
    	
    }
    
      /**
	   * Creates Nested TreeMap 
	   *
	   * @param fileWords List of words in file 
	   * @param index the iterator index of our outer for loop located in processFileStems
	   * @param wordToFileMap our final Nested Tree Map that we are adding to
	   * @return fileNameToPositionsMap returns the Map with file name and the positions word was found
	   */
    public TreeMap<Path, TreeSet<Integer>> getOrCreateFileNameToPositionsMap(List<String> fileWords, int index, TreeMap<String, TreeMap<Path, TreeSet<Integer>>> wordToFileMap){
    	final TreeMap<Path, TreeSet<Integer>> fileNameToPositionsMap;
		if(wordToFileMap.containsKey(fileWords.get(index))) {
			fileNameToPositionsMap = wordToFileMap.get(fileWords.get(index));
		}else {
			fileNameToPositionsMap = new TreeMap<Path, TreeSet<Integer>>();
			wordToFileMap.put(fileWords.get(index), fileNameToPositionsMap);
		}
		return fileNameToPositionsMap;
		
    }
    
      /**
	   * Creating the inner TreeSet of positions in which words were found
	   *
	   * @param file The current file to be stemmed
	   * @param fileNameToPositionsMap the TreeMap of files and positions
	   * @return fileStemmer.listStems(file) the stemmed file as an ArrayList of words
	   */
    public TreeSet<Integer> getOrCreatePositionSet(TreeMap<Path, TreeSet<Integer>> fileNameToPositionsMap, Path file){
    	final TreeSet<Integer> positionSet;
		if(fileNameToPositionsMap.containsKey(file)) {
			positionSet = fileNameToPositionsMap.get(file);

		}else {
			positionSet = new TreeSet<Integer>();
			fileNameToPositionsMap.put(file, positionSet);
			
		}

		return positionSet;
	
    }
    
      /**
	   * Writes the necessary output to the proper Output File
	   *
	   * @param commandArgs Command Arguments provided by driver
	   * @param wordToFileMap the Nested TreeMap 
	   */
    public void writeToOutput(CommandArguments commandArgs, TreeMap<String, TreeMap<Path, TreeSet<Integer>>> wordToFileMap) {
		try {
	    	if(commandArgs.getOutputFile() != null) {
	    		System.out.println("Writing input to output file.");
	    		BufferedWriter writer = new BufferedWriter(new FileWriter(commandArgs.getOutputFile(), StandardCharsets.UTF_8));  
	    		JSONWriter.asNestedArray((Map)wordToFileMap, writer, 0);
	    		
	    		writer.close();
	    	}else {
	    		System.out.println("No output file provided");
	    	}
	  
	    }catch(IOException e) {
	    	System.out.println("Writing to output file failure.");
	    }
    }
    
    /**
	   * Writes the necessary output to the proper Output File
	   *
	   * @param commandArgs Command Arguments provided by driver
	   * @param wordToFileMap the Nested TreeMap 
	   */
  public void writeWordCountOutput(CommandArguments commandArgs, TreeMap<Path,Integer> wordCountMap) {
		try {
	    	if(commandArgs.getWordCountOutputFileName() != null) {
	    		System.out.println("Writing word count to output file.");
	    		BufferedWriter writer = new BufferedWriter(new FileWriter(commandArgs.getWordCountOutputFileName(), StandardCharsets.UTF_8));  
	    		JSONWriter.asObject((Map)wordCountMap, writer, 0);
	    		writer.close();
	    	}else {
	    		System.out.println("No output file provided");
	    	}
	  
	    }catch(IOException e) {
	    	System.out.println("Writing to output file failure.");
	    }
  }
}


