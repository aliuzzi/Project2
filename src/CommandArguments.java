
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Parses and stores command-line arguments into simple key = value pairs.
 *
 * @author CS 212 Software Development
 * @author Angelica Liuzzi University of San Francisco
 * @version Spring 2020
 */

public class CommandArguments{
	
	/**
     * Stores command-line arguments in key = value pairs.
     */
    private final Map<String, String> map;
    
    /** Input file. **/
    public String inputFilename; 
    
    /** Output file **/
    public String indexOutputFilename; 
    
    /** Input file path. **/
    public Path inputFilePath;
    
    /** Output file.**/
    public File outputFile;
    
    /** Word Count Output file **/
    public String wordCountOutputFileName;
    
    public File wordCountOutputPath;
    
    

    /** Word Count Output file **/
    public String queryFileName;
    
    public Path queryFilePath;
    
    
    
    public String exactSearch;
    
    public String resultsFileName;
    
    public File resultsFilePath;
    
    

    /**
     * Initializes this argument map and then parses the arguments into flag/value pairs where
     * possible. Some flags may not have associated values. If a flag is repeated, its value is
     * overwritten.
     *
     * @param args the command line arguments to parse
     */
    public CommandArguments(String[] args) {

        this.map = new HashMap<String, String>();
        this.parse(args);
        /** Sets input file to whatever "-path" is mapped to. **/
        this.inputFilename = map.get("-path");

        
        /** Sets word Count Output file **/
        this.queryFileName = map.get("-query");   //indicates next arg is a txt file of queries to be used for search

        this.exactSearch =  map.get("-exact");  //optional flag indicates all search operations performed should be exact searach
        
        //this.resultsFileName = map.get("-results");     //optional, indicates next arg is the path to use for search results output file, default = results.json
        /** Output file is determined by a function.**/
        this.indexOutputFilename = determineIndexOutputFilename();
        /** Sets word Count Output file **/
        this.wordCountOutputFileName = determineWordCountOutputFile();  //optional, aka word count, indicates next arg is path to output all of the locations word was found (files)  and the word count
        
        this.resultsFileName = determineResultsFile();
        
        // TODO: You probably don't want to store these. I would create the Path and File when I need it, not here.
        this.setInputFilePath(inputFilename);
        this.setQueryFilePath(queryFileName);
        this.setOutputFilePath(indexOutputFilename);
        this.setOutputFilePath(wordCountOutputFileName);  
        this.setOutputFilePath(resultsFileName); 
    }
    
    
    public String getQueryFileName() {
		return queryFileName;
	}


	public Path getQueryFilePath() {
		return queryFilePath;
	}


	public void setQueryFilePath(Path queryFilePath) {
		this.queryFilePath = queryFilePath;
	}


	/**
     * Determines whether the argument is a path. Path starts with a dash "-" character, followed by
     * at least one other non-digit character.
     *
     * @param arg the argument to test if its a flag
     * @return {true} if the argument is a flag
     */
    
    public boolean isValidFlag(String arg) {
    	
    	//"-path"
    	
    	if(arg == null) {
    		return false;
    	}
    	
    	if(arg.isBlank()) {
    		return false;
    	}
    	
    	if(arg.equals("-index") || arg.equals("-path") || arg.equals("-counts") || arg.equals("-query") || arg.equals("-results") || arg.equals("-exact")) {
    		return true;
    	}
    	
    	return false;
    }

    /**
     * Converts the String Path name into a File.
     *
     * @param filename gets the given file or creates new file.
     * @return New File path for the string
     */
    
  public File getFileOrCreate(String filename) {
	  File newFile = new File(filename);
	  try {
		  if(!newFile.exists()) {
			  newFile.createNewFile();
		  }
	  }catch (IOException e) {
		  System.out.println("404");
	  }
	  return newFile;
  }
  
  /**
   * Returns the path to which the specified arg is mapped as a {@link Path}, or {@code null} if
   * unable to retrieve this mapping (including being unable to convert the value to a {@link Path}
   * or no value exists).
   * <p>
   * This method should not throw any exceptions!
   *
   * @param file gets a path of file given.
   * @return the path of the argument 
   * this mapping
   */
  public Path getPath(String file) {
  	if(pathExists(file)) {      //value is a valid file
  		return Path.of(file);
  	}
  	return null;
  }
  
    
  /**
   * Getter for the input file.
   *
   * @return the input file name
   */
    public String getFileToInvert() {
		return inputFilename;
	}

    /**
     * Function that checks if a path exists to a single text file or directory of text files.
     *
     * @param arg the "file" to check.
     * @return True if a string is a proper file or directory, False otherwise.
     */
	public boolean pathExists(String arg) {
    	// is a path to either a single text file or a directory of text files that must be 
    	//processed and added to the inverted index
    	

    	if (arg == null || arg.isBlank() || arg.isEmpty()) {
    		return false;
    	}
    	
    	File file = new File(arg);   
    	
    	return file.exists();
    }
    
	
	/**
     * Determines and sets what file to write the output data, if any. 
     * 
     * @return Output file path, default output file path, or null.
     */
    public String determineIndexOutputFilename() {
    	if(map.containsKey("-index")) {
    		if(map.get("-index") == null) {  //sets default file
    			return "index.json";
    		}else {	
    			//if map contains -index and has a valid output file
    			return map.get("-index");
    		}
    		
    	}else {
    		return null;		
    	}	
    }
    
    /**
     * Determines and sets what file to write the word count for each file. 
     * 
     * @return Word Count Output file path, default output file path, or null.
     */
    public String determineWordCountOutputFile() {
    	if(map.containsKey("-count")) {
    		if(map.get("-count") == null) {
    			return "counts.json";
    		}else {
    			return map.get("-count");
    		}
    	}else {
    		return null;
    	}
    }
    
    public String getWordCountOutputFileName() {
		return wordCountOutputFileName;
	}

	/**
     * Determines and sets what file to write the word count for each file. 
     * 
     * @return Word Count Output file path, default output file path, or null.
     */
    public String determineResultsFile() {
    	if(map.containsKey("-results")) {
    		if(map.get("-results") == null) {
    			return "results.json";
    		}else {
    			return map.get("-results");
    		}
    	}else {
    		return null;
    	}
    }
    
    /**
     * Determines and sets what file to write the word count for each file. 
     * 
     * @return Search method to use: exact or partial.
     */
    public boolean determineSearchMethod() {
    	boolean exactSearch = false;
    	if(map.containsKey("-exact")) {
    		exactSearch = true;;
    	}
    	return exactSearch;
    	
    }
   
    
    /**
     * This function initializes the output file path by calling the getPath()
     * on our output file value if present
     * 
     * @param outputFile output file = default or given.
     */
    public void setOutputFilePath(String outputFile) {
    	if(outputFile != null) {
    		this.outputFile = getFileOrCreate(outputFile);
    	}
    }
   
    
    /**
     * Gets the output file name
     *
     * @return output file name
     */
    public String getOutputFile() {
    	return indexOutputFilename;
    }
    
    /**
     * Gets the output file name
     *
     * @return output file name
     */
    public String getResultOutputFile() {
    	return resultsFileName;
    }
    /**
     * Gets the output file Path
     *
     * @return File - Output file
     */
    public File getOutputFilePath() {
    	return outputFile;
    	
    }
    
    /**
     * Gets the input file path.
     *
     * @param File to convert
     * @return File path for the Input Files
     */
    public Path getInputFilePath() {
    	return inputFilePath;
    }
    
	
	/**
     * Sets the input file path
     *
     * @param inputFile the input file name.
     * 
     */
	public void setInputFilePath(String inputFile) {
		if(inputFile != null) {
			this.inputFilePath = getPath(inputFile);
		}
	}
	

	/**
     * Sets the input file path
     *
     * @param inputFile the input file name.
     * 
     */
	public void setQueryFilePath(String queryFile) {
		if(queryFile != null) {
			this.queryFilePath = getPath(queryFile);
		}
	}
	
	/**
     * Checks whether an exact search should be performed
     * @return boolean 
     */
	public boolean exactSearch() {
		if (map.containsKey("-exact")){
			return true;
		}
		return false;
	}
	
	  
    

    
    /**
     * Parses the arguments into flag/value pairs where possible. Some flags may not have associated
     * values. If a flag is repeated, its value is overwritten.
     *
     * @param args the command line arguments to parse
     */
   

	private void parse(String[] args) {

		if (args.length == 0) {
			System.out.println("No Arguments Found");
		}


        if ((args.length > 0) && (args != null)) {
            for (int i = 0; i < args.length; i++) {

                String key = args[i];
                String value; 
                if (i < args.length - 1) {
                	value = args[i+1];
                } else {
                	value = null;
                }



                if (isValidFlag(value)) {  
                    value = null;  
                }

                if (isValidFlag(key)) {
                    map.put(key, value);
                }


            }
        } else {
            System.out.println("No command line arguments found!"); 
        }
		
	}

	
	 /**
     * A simple main method that parses the command-line arguments provided and prints the result to
     * the console.
     *
     * @param args the command-line arguments to parse
     */
    public static void main(String[] args) {
        // Modify as needed to debug code
        CommandArguments ap = new CommandArguments(args);
        System.out.println(ap);

        
    }
    
    
    
}
