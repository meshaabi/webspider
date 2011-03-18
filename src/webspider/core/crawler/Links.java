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
 * A collection of internet links and operations on them
 * @author Zsolt Bitvai
 */
public class Links implements Iterable<URL>{
	
	/**
	 * The urls to hold
	 */
	private Collection<URL> urls;

	/**
	 * The path to print the urls
	 */
	private String printPath;

	public Links( String printPath){
		this.printPath = printPath;
		this.urls = Collections.synchronizedSet(new LinkedHashSet<URL>());
	}
	/**
	 * Print the urls to the file specified by printPath
	 * @throws FileNotFoundException if file cannot be written
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
	 * Add a new url to the collection this class holds
	 * @param url the url to add
	 */
	public void add(URL url) {
		this.urls.add(url);			
	}

	/**
	 * Return the number of links
	 * @return the size of the collection
	 */
	public int size(){
		return this.urls.size();
	}
	/**
	 * Checks that a url is already contained in this collection
	 * @param checkUrl the url to check
	 * @return is the url contained by the collection?
	 */
	public boolean contains(URL checkUrl){
		return this.urls.contains(checkUrl);
	}
	/**
	 * Iterate over all urls in this collection
	 */
	@Override
	public Iterator<URL> iterator() {
		return this.urls.iterator();
	}
}
