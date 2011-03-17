package webspider.core.crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 
 *
 */
public class Links implements Iterable<URL>{
	
	private Collection<URL> urls;
	private String path;

	public Links( String printPath){
		this.path = printPath;
		this.urls = Collections.synchronizedSet(new HashSet<URL>());
	}
	public void print() throws FileNotFoundException{
		File outfile = new File(this.path);
		PrintWriter urlWriter = new PrintWriter(outfile);
		synchronized (this.urls) {
			for (URL url : this.urls) {
				urlWriter.println(url);
			}
		}
		urlWriter.flush();
		urlWriter.close();
	}
	public void add(URL url) {
		this.urls.add(url);			
	}

	public int size(){
		return this.urls.size();
	}
	public boolean contains(URL url){
		return this.urls.contains(url);
	}
	@Override
	public Iterator<URL> iterator() {
		return this.urls.iterator();
	}
}
