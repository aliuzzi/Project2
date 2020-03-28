import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Outputs several simple data structures in "pretty" JSON format where newlines are used to
 * separate elements and nested elements are indented.
 *
 * Warning: This class is not thread-safe. If multiple threads access this class concurrently,
 * access must be synchronized externally.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2020
 */
public class JSONWriter {

  /**
   * Writes the elements as a pretty JSON array.
   *
   * @param elements the elements to write
   * @param writer the writer to use
   * @param level the initial indent level
   * @throws IOException if an IO error occurs
   */
  public static void asArray(Collection<Object> elements, Writer writer, int level) throws IOException { 
    

    if(writer == null) {
    	throw new IOException("No writer found.");
    }
    if(elements == null) {
    	throw new IOException("No elements found.");
    }
    if (level < 0) {
    	throw new IOException("Levels not found");
    }
    
    try {
      Iterator<Object> it = elements.iterator();
      writer.append("[\n");
      
      
      while(it.hasNext()) {
        indent(writer, level+1);
         
        Object value = it.next();
        if (value instanceof Map) {
        	asNestedArray((Map) value, writer, level+1);
        } else if (value instanceof Collection) {
            asArray((Collection) value, writer, level+1);
        } else {
        	writer.append(asValue(value));
        }       
        
        
        if(it.hasNext()){
        	writer.append(",\n");
        }
      }
        

      if(!elements.isEmpty()) {
        writer.append("\n");
      }
      
      indent("]", writer, level);
    
    } catch (IOException E){
      System.out.println("404");
    }
  }

  /**
   * Returns the elements as a pretty JSON array.
   *
   * @param elements the elements to use
   * @return a {@link String} containing the elements in pretty JSON format
   *
   * @see #asArray(Collection, Writer, int)
   */
  public static String asArray(Collection<Object> elements) {
    // THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
    try {
      StringWriter writer = new StringWriter();
      asArray(elements, writer, 0);
      return writer.toString();
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Writes the elements as a pretty JSON object.
   *
   * @param elements the elements to write
   * @param writer the writer to use
   * @param level the initial indent level
   * @throws IOException if an IO error occurs
   */
  public static void asObject(Map<Object, Integer> elements, Writer writer, int level) throws IOException{
    
	if(writer == null) {
	   throw new IOException("No writer found.");
	}
	if(elements == null) {
	    throw new IOException("No elements found.");
	}
	if (level < 0) {
		throw new IOException("Levels not found");
	}
    
    try {
      writer.append("{\n");  
      Iterator<Map.Entry<Object, Integer>> entries = elements.entrySet().iterator();  
      
      
      if(entries.hasNext()) {
        Map.Entry<Object, Integer> entry = entries.next();
        indent(writer, level+1);
        writer.append("\"" + entry.getKey().toString() + "\": " + entry.getValue().toString());  //wraps key in quotes
      } 
      
      while(entries.hasNext()) {
        Map.Entry<Object, Integer> entry = entries.next();
        writer.append(",\n");
        indent(writer, level+1);
        writer.append("\"" + entry.getKey() + "\": " + entry.getValue().toString()); //wrapping key in quotes 
      }
      
      if(!elements.isEmpty()) {
    	  indent("\n}", writer, level);
      }else{
    	  indent("}", writer, level);
      }
     
      
      
    } catch (IOException E){
      System.out.println("404");
    }
    
  }

  /**
   * Writes the elements as a pretty JSON object to file.
   *
   * @param elements the elements to write
   * @param path the file path to use
   * @throws IOException if an IO error occurs
   *
   * @see #asObject(Map, Writer, int)
   */
  public static void asObject(Map<Object, Integer> elements, Path path) throws IOException {
    // THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
    try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
      asObject(elements, writer, 0);
    }
  }

  /**
   * Returns the elements as a pretty JSON object.
   *
   * @param elements the elements to use
   * @return a {@link String} containing the elements in pretty JSON format
   *
   * @see #asObject(Map, Writer, int)
   */
  public static String asObject(Map<Object, Integer> elements) {
    // THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
    try {
      StringWriter writer = new StringWriter();
      asObject(elements, writer, 0);
      return writer.toString();
    } catch (IOException e) {
      return null;
    }
  }
  
  public static String asValue(Object value) {
	if(value instanceof String) {
		return "\"" + value +"\"";
		
	}else if( value instanceof Integer) {
		return value.toString();
		
	}else if(value instanceof Double) {
		String formatted = String.format("%.8f", value);
		return formatted;
	}else {
		System.out.println("Error");
		return "error";
	}
	
  }
  

 

  /**
   * Recursively writes the elements as a pretty JSON object with a nested array. The generic notation used
   * allows this method to be used for any type of map with any type of nested collection of integer
   * objects.
   *
   * @param elements the elements to write
   * @param writer the writer to use
   * @param level the initial indent level
   * @throws IOException if an IO error occurs
   */
  
  public static void asNestedArray(Map<String, ? extends Collection<Integer>> elements,
      Writer writer, int level) throws IOException {
 
	  if(writer == null) {
	    throw new IOException("No writer found.");
	  }
	  if(elements == null) {
	    throw new IOException("No elements found.");
	  }
	  if (level < 0) {
	    throw new IOException("Levels not found");
	  }

	  try {
      Iterator<String> it = elements.keySet().iterator();

      writer.append("{\n");  //done 
      
      while(it.hasNext()) {
        
        Object key = it.next();
        quote(key.toString(), writer, level+1);
        
        writer.append(": ");

        Object value = elements.get(key);
        if (value instanceof Map) {
        	asNestedArray((Map) value, writer, level+1);
        } else if (value instanceof Collection) {
            asArray((Collection) value, writer, level+1);
        } else {
        	writer.append(asValue(value));
        }
        

        if(it.hasNext()) {
            writer.append(",\n");
        }

      }
      if(!elements.isEmpty()) {

          writer.append("\n");
      }
      indent("}", writer, level);
      
      }catch(IOException E){
      
      System.out.println("404");
      
    }
  }


 
  /**
   * Indents using 2 spaces by the number of times specified.
   *
   * @param writer the writer to use
   * @param times the number of times to write a tab symbol
   * @throws IOException if an IO error occurs
   */
  public static void indent(Writer writer, int times) throws IOException {
    // THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
    for (int i = 0; i < times; i++) {
      writer.write(' ');
      writer.write(' ');
    }
  }

  /**
   * Indents and then writes the element.
   *
   * @param element the element to write
   * @param writer the writer to use
   * @param times the number of times to indent
   * @throws IOException if an IO error occurs
   *
   * @see #indent(String, Writer, int)
   * @see #indent(Writer, int)
   */
  public static void indent(Integer element, Writer writer, int times) throws IOException {
    // THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
    indent(element.toString(), writer, times);
  }

  /**
   * Indents and then writes the element.
   *
   * @param element the element to write
   * @param writer the writer to use
   * @param times the number of times to indent
   * @throws IOException if an IO error occurs
   *
   * @see #indent(Writer, int)
   */
  public static void indent(String element, Writer writer, int times) throws IOException {
    // THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
    indent(writer, times);
    writer.write(element);
  }

  /**
   * Writes the element surrounded by {@code " "} quotation marks.
   *
   * @param element the element to write
   * @param writer the writer to use
   * @throws IOException if an IO error occurs
   */
  public static void quote(String element, Writer writer) throws IOException {
    // THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
    writer.write('"');
    writer.write(element);
    writer.write('"');
  }

  /**
   * Indents and then writes the element surrounded by {@code " "} quotation marks.
   *
   * @param element the element to write
   * @param writer the writer to use
   * @param times the number of times to indent
   * @throws IOException if an IO error occurs
   *
   * @see #indent(Writer, int)
   * @see #quote(String, Writer)
   */
  public static void quote(String element, Writer writer, int times) throws IOException {
    // THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
    indent(writer, times);
    quote(element, writer);
  }


}
