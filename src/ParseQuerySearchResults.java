import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class ParseQuerySearchResults {


	

	/**
	 * Converting the Tree Set of query stems into a List so that we can put them
	 * into our map later. Custom mutable objects do not behave well when used in
	 * sets or as keys in maps.
	 *
	 * @param wordToFileMap unique stemmed words
	 * @param file 
	 * @param query
	 * @param wordCountMap
	 * @param commandArgs
	 * @return listOfSearchResults the stemmed words as an ArrayList of strings - query words
	 */
	public List<Map<String,Object>> search(TreeSet<String> query,
			TreeMap<String, TreeMap<Path, TreeSet<Integer>>> wordToFileMap, TreeMap<Path, Integer> wordCountMap, CommandArguments commandArgs) {

		TreeMap<Path, SearchResult> matches = new TreeMap<Path, SearchResult>();

		for (String queryWord : query) {
			for (String wordInIndexMap: wordToFileMap.keySet()) {
				if((commandArgs.exactSearch() && wordInIndexMap.equalsIgnoreCase(queryWord)) ||  (!commandArgs.exactSearch() && wordInIndexMap.startsWith(queryWord))) {
				
					for (Entry<Path, TreeSet<Integer>> fileAndLocations : wordToFileMap.get(wordInIndexMap).entrySet()) {
						final SearchResult searchResult;
						final int matchesForFileAndWord = fileAndLocations.getValue().size();
						// for every entry in my path to locations
		
						if (matches.containsKey(fileAndLocations.getKey())) {
							searchResult = matches.get(fileAndLocations.getKey());
							// gets search result for path
						} else {
							searchResult = new SearchResult(fileAndLocations.getKey().toString(),
									wordCountMap.get(fileAndLocations.getKey()));
							matches.put(fileAndLocations.getKey(), searchResult);
						}
		
						searchResult.addMatches(matchesForFileAndWord);
		
					}
				}
			}

		}
		

		List<SearchResult> listOfSearchResults = new ArrayList<SearchResult>();
		listOfSearchResults.addAll(matches.values());
		Collections.sort(listOfSearchResults);
		
		List<Map<String, Object>> listOfSearchResultsAsMap = new ArrayList<Map<String, Object>>();
		for(SearchResult entry: listOfSearchResults) {
			listOfSearchResultsAsMap.add(entry.getMapOutput());
		}
		return listOfSearchResultsAsMap;
	}
	
	/**
	 * Converting the single query search results into the final search results map
	 *
	 * @param queryList   unique stemmed words
	 * @param fileStemmer
	 * @param index
	 * @param commandArgs
	 * @return mapOfAllQuerySearches the TreeMap of all query searches and their
	 *         search results
	 *         
	 */

	public TreeMap<String, List<Map<String, Object>>> getSearchResultForEveryQuery(
			List<TreeSet<String>> queryList, CommandArguments commandArgs,
			InvertedIndex index) {
		TreeMap<String, List<Map<String, Object>>> mapOfAllQuerySearches = new TreeMap<String, List<Map<String, Object>>>();
		// returns listOfTreeSetsForEachQuery List<TreeSet<String>>
		// listOfTreeSetsForEachQuery
		for (TreeSet<String> searchQuery : queryList) {
			if(!searchQuery.isEmpty() ) {
			List<Map<String, Object>> listOfSearchResultsForAQuery = search(searchQuery,
					index.getWordToFileMapForPath(commandArgs), index.getWordCountMap(commandArgs), commandArgs);
			mapOfAllQuerySearches.put(String.join(" ", searchQuery), listOfSearchResultsForAQuery);

			}
		}

		return mapOfAllQuerySearches;

	}

	/**
	 * Writes the necessary output to the proper Output File
	 *
	 * @param commandArgs   Command Arguments provided by driver
	 * @param wordToFileMap the Nested TreeMap
	 */
	public void writeSearchQueryResultsToOutput(CommandArguments commandArgs,
			TreeMap<String, List<Map<String, Object>>> mapOfAllQuerySearches) {
		try {
			if (commandArgs.getResultOutputFile() != null) {
				System.out.println("Writing search query results to output file.");
				System.out.println("Result file "+ commandArgs.getResultOutputFile());
				BufferedWriter writer = new BufferedWriter(
						new FileWriter(commandArgs.getResultOutputFile(), StandardCharsets.UTF_8));
				JSONWriter.asNestedArray((Map) mapOfAllQuerySearches, writer, 0);

				writer.close();
			} else {
				System.out.println("No query results output file provided");
			}

		} catch (IOException e) {
			System.out.println("Writing search result output file failure.");
		}
	}

}
