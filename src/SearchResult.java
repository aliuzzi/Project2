import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchResult implements Comparable<SearchResult> {

	public final String path;
	public int totalMatches = 0;
	private final int totalWords;

	public SearchResult(String path, int totalWords) {
		this.path = path;
		this.totalWords = totalWords;
	}

	public double getScore() {
		return totalMatches / (double) totalWords;
		
	}

	public int addMatches(int newMatchCount) {
		totalMatches += newMatchCount;
		return totalMatches;
	}
	public Map<String, Object> getMapOutput(){
		LinkedHashMap<String, Object> mapForSearchResults = new LinkedHashMap<String, Object>();
		mapForSearchResults.put("where", path);
		mapForSearchResults.put("count", Integer.valueOf(totalMatches));
		mapForSearchResults.put("score", Double.valueOf(getScore()));
		return mapForSearchResults;
	}

	@Override
	public int compareTo(SearchResult o) {
		int compareScoreValue = Double.compare(this.getScore(), o.getScore());
		if (compareScoreValue != 0) {
			return -compareScoreValue;
			// TODO possibly invert this
		}

		int rawCountComparable = Integer.compare(totalWords, o.totalWords);
		if (rawCountComparable != 0) {
			return -rawCountComparable;
			// TODO possibly invert this
		}

		return path.compareToIgnoreCase(o.path);
	}

}
