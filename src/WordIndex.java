import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A special type of {@link Index} that indexes the locations words were found.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2020
 */

public class WordIndex implements Index<String>{
	
	
	TreeMap<String, Integer> treeMap  = new TreeMap<String, Integer>();

	  // TODO Modify class declaration to implement the Index interface for String elements
	  // TODO Modify anything within this class as necessary
	
	  /**
	   * Adds the element and position.
	   *
	   * @param element the element found
	   * @param position the position the element was found
	   * @return {@code true} if the index changed as a result of the call
	   */
	@Override
	public boolean add(String element, int position) {
		
		
		
		if(treeMap.containsKey(element) && treeMap.get(element) == position) {
			
			return false;
			
		}else if(treeMap.containsKey(element) && treeMap.get(element)!= position) {
			
			treeMap.compute(element, (key,val) -> (val == null) ? position : position);
			return true;
			
		}else if (!treeMap.containsKey(element)){
		
			treeMap.put(element, position);
			return true;
		
		}
			return false;
		
	}

	  /**
	   * Returns the number of positions stored for the given element.
	   *
	   * @param element the element to lookup
	   * @return 0 if the element is not in the index or has no positions, otherwise the number of
	   *         positions stored for that element
	   */
	
	@Override
	public int numPositions(String element) {
		//TODO how to get the number of values stores
		
		if(treeMap.containsKey(element)) {
			int numValues = treeMap.values().size();
			return numValues;
		}
		return 0;
	}
	
	 /**
	   * Returns the number of element stored in the index.
	   *
	   * @return 0 if the index is empty, otherwise the number of element in the index
	   */
	@Override
	public int numElements() {
		if(treeMap.isEmpty()) {
			return 0;
		}
		return treeMap.size();
	}

	 /**
	   * Determines whether the element is stored in the index.
	   *
	   * @param element the element to lookup
	   * @return {@true} if the element is stored in the index
	   */
	@Override
	public boolean contains(String element) {
		if(treeMap.containsKey(element)) {
			return true;
		}
		return false;
	}

	/**
	   * Determines whether the element is stored in the index and the position is stored for that
	   * element.
	   *
	   * @param element the element to lookup
	   * @param position the position of that element to lookup
	   * @return {@true} if the element and position is stored in the index
	   */
	@Override
	public boolean contains(String element, int position) {
		if(treeMap.containsKey(element) && treeMap.get(element) == position) {
			return true;
		}
		return false;
	}

	/**
	   * Returns an unmodifiable view of the elements stored in the index.
	   *
	   * @return an unmodifiable view of the elements stored in the index
	   * @see Collections#unmodifiableCollection(Collection)
	   */
	@Override
	public Collection getElements() {
		//TODO type mismatch bc of treemap vs trying to create a new unmod map.
		
		Set<String> keys = treeMap.keySet();
		
		Set<String> unmodifiedElements = Collections.unmodifiableSet(keys);
		
		return unmodifiedElements;
		
	}
	

	 /**
	   * Returns an unmodifiable view of the positions stored in the index for the provided element, or
	   * an empty collection if the element is not in the index.
	   *
	   * @param element the element to lookup
	   * @return an unmodifiable view of the positions stored for the element
	   */
	
	@Override
	public Collection getPositions(String element) {
		
		SortedMap<String,Integer> unmodsortmap = Collections.unmodifiableSortedMap(treeMap);

		return unmodsortmap.values();
	}

	@Override
	public boolean addAll(String[] elements) {
		// TODO Auto-generated method stub
		boolean changed = false;
		for(String word: elements) {
			if(treeMap.containsKey(word)) {
				return false;
			}else {
				treeMap.put(word, 1);
			}
		}
		return false;
		
	}

	@Override
	public boolean addAll(String[] elements, int start) {
		// TODO Auto-generated method stub
		boolean changed = false;
		
		for(int i = 0; i < elements.length; i++) {
			
			if(elements.length == 1) {
				return false;
			}
			
			if(contains(elements[i])){
				changed = true;
			}else {
			treeMap.putIfAbsent(elements[i], start);
			}
			
		}
		
		return changed;
	}
}