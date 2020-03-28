import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

public class parseQueries {


	

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
	public List<SearchResult> search(TreeSet<String> query,
			TreeMap<String, TreeMap<Path, TreeSet<Integer>>> wordToFileMap, TreeMap<Path, Integer> wordCountMap, CommandArguments commandArgs) {

		TreeMap<Path, SearchResult> matches = new TreeMap<Path, SearchResult>();
		List<SearchResult> listOfSearchResults = new ArrayList<SearchResult>();

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
		listOfSearchResults.addAll(matches.values());
		Collections.sort(listOfSearchResults);
		return listOfSearchResults;
	}
	
	/**
	 * Converting the single query search results into the final search results map
	 *
	 * @param queryList   unique stemmed words
	 * @param fileStemmer
	 * @param file
	 * @param index
	 * @param commandArgs
	 * @return mapOfAllQuerySearches the TreeMap of all query searches and their
	 *         search results
	 */

	public TreeMap<TreeSet<String>, List<SearchResult>> getSearchResultForEveryQuery(Path file,
			List<TreeSet<String>> queryList, TextFileStemmer fileStemmer, CommandArguments commandArgs,
			InvertedIndex index) {
		TreeMap<TreeSet<String>, List<SearchResult>> mapOfAllQuerySearches = new TreeMap<TreeSet<String>, List<SearchResult>>();

		queryList = TextFileStemmer.stemQueryFile(commandArgs.getQueryFilePath(), commandArgs.getQueryFileName());
		// returns listOfTreeSetsForEachQuery List<TreeSet<String>>
		// listOfTreeSetsForEachQuery
		for (TreeSet<String> searchQuery : queryList) {
			List<SearchResult> listOfSearchResultsForAQuery = search(searchQuery,
					index.getWordToFileMapForPath(commandArgs), index.getWordCountMap(commandArgs), commandArgs);
			mapOfAllQuerySearches.put(searchQuery, listOfSearchResultsForAQuery);

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
			TreeMap<TreeSet<String>, List<SearchResult>> mapOfAllQuerySearches) {
		try {
			if (commandArgs.getResultOutputFile() != null) {
				System.out.println("Writing search query results to output file.");
				BufferedWriter writer = new BufferedWriter(
						new FileWriter(commandArgs.getOutputFile(), StandardCharsets.UTF_8));
				JSONWriter.asNestedArray((Map) mapOfAllQuerySearches, writer, 0);

				writer.close();
			} else {
				System.out.println("No output file provided");
			}

		} catch (IOException e) {
			System.out.println("Writing to output file failure.");
		}
	}

}
