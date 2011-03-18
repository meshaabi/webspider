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
 * A collection of links and operations on them
 * @author Zsolt Bitvai
 */
public class Links implements Iterable<URL>{
	
	/**
	 * the urls to hold
	 */
	private Collection<URL> urls;

	/**
	 * the path to print the urls
	 */
	private String printPath;

	public Links( String printPath){
		this.printPath = printPath;
		this.urls = Collections.synchronizedSet(new LinkedHashSet<URL>());
	}
	/**
	 * print the urls
	 * @throws FileNotFoundException
	 */
	public void print() throws FileNotFoundException{
		File outfile = new File(this.printPath);
        outfile.mkdirs();     
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
	 * add a new url to the collection
	 * @param url
	 */
	public void add(URL url) {
		this.urls.add(url);			
	}

	/**
	 * 
	 * @return the size of the collection
	 */
	public int size(){
		return this.urls.size();
	}
	/**
	 * 
	 * @param checkUrl the url to check
	 * @return is the url contained by the collection?
	 */
	public boolean contains(URL checkUrl){
		return this.urls.contains(checkUrl);
	}
	/**
	 * iterate over all urls in this collection
	 */
	@Override
	public Iterator<URL> iterator() {
		return this.urls.iterator();
	}
	
	/**
	 * returns the links
	 * @return the links
	 */
	public Collection<URL> getLinks(){
		return this.urls;
	}
	
}
