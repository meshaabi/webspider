package webspider.core.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
<<<<<<< HEAD
 * A collection of internet links and operations on them
=======
 * A collection of links and operations on them
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
 * @author Zsolt Bitvai
 */
public class Links implements Iterable<URL>{
	
	/**
<<<<<<< HEAD
	 * The urls to hold
=======
	 * the urls to hold
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	 */
	private Collection<URL> urls;

	/**
<<<<<<< HEAD
	 * The path to print the urls
=======
	 * the path to print the urls
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	 */
	private String printPath;

	public Links( String printPath){
		this.printPath = printPath;
		this.urls = Collections.synchronizedSet(new LinkedHashSet<URL>());
	}
	/**
<<<<<<< HEAD
	 * Print the urls to the file specified by printPath
	 * @throws FileNotFoundException if file cannot be written
=======
	 * print the urls
	 * @throws FileNotFoundException
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	 */
	public void print() throws FileNotFoundException{
		File outfile = new File(this.printPath);
		PrintWriter urlWriter = new PrintWriter(outfile);
		synchronized (this.urls) {
			for (URL url : this.urls) {
				urlWriter.println(url);
			}
		}
		urlWriter.flush();
		urlWriter.close();
	}
	/**
<<<<<<< HEAD
	 * Add a new url to the collection this class holds
	 * @param url the url to add
=======
	 * add a new url to the collection
	 * @param url
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	 */
	public void add(URL url) {
		this.urls.add(url);			
	}

	/**
<<<<<<< HEAD
	 * Return the number of links
=======
	 * 
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	 * @return the size of the collection
	 */
	public int size(){
		return this.urls.size();
	}
	/**
<<<<<<< HEAD
	 * Checks that a url is already contained in this collection
=======
	 * 
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	 * @param checkUrl the url to check
	 * @return is the url contained by the collection?
	 */
	public boolean contains(URL checkUrl){
		return this.urls.contains(checkUrl);
	}
	/**
<<<<<<< HEAD
	 * Iterate over all urls in this collection
=======
	 * iterate over all urls in this collection
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	 */
	@Override
	public Iterator<URL> iterator() {
		return this.urls.iterator();
	}
}
