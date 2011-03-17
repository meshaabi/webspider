package webspider.core.crawler;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.net.*;
import java.io.*;

import javax.swing.text.*;
import javax.swing.text.html.*;
import webspider.actions.SpiderActions;

/**
 * That class implements a spider
 * 
 * @author BDM based on Jeff Heaton's Spider
 * @version 1.0 Implement interface
 */
public class SpiderImpl {

	// TODO change default robots.txt
	/**
	 * URL for robots.txt
	 */
	private static final String ROBOTS_TXT_URL = "http://poplar.dcs.shef.ac.uk/~u0082/intelweb2/robots.txt";

	private static final String COM4280_WEBSITE = "http://poplar.dcs.shef.ac.uk/~u0082/intelweb2/";
	/**
	 * file extension used by crawler
	 */
	private static final String EXTENSION = ".bdmc";

	/**
	 * output file path
	 */
	private static final String PATH = "./output/spider/";

	public static final String USER_AGENT_FIELD = "User-Agent";

	public static final String USER_AGENT_VALUE = "BDM_crawler_University_of_Sheffield_COM4280/1.0";

	public static final String ACCEPT_LANGUAGE_FIELD = "Accept-Language";

	public static final String ACCEPT_LANGUAGE_VALUE = "en";

	public static final String CONTENT_TYPE_FIELD = "Content-Type";

	public static final String CONTENT_TYPE_VALUE = "application/xwww-form-urlencoded";

	/**
	 * Request properties for the spider crawling the web
	 */
	private static final Map<String, String> REQUEST_PROPERTIES = new HashMap<String, String>();
	static {
		REQUEST_PROPERTIES.put(USER_AGENT_FIELD, USER_AGENT_VALUE);
		REQUEST_PROPERTIES.put(ACCEPT_LANGUAGE_FIELD, ACCEPT_LANGUAGE_VALUE);
		REQUEST_PROPERTIES.put(CONTENT_TYPE_FIELD, CONTENT_TYPE_VALUE);
	}

	/**
	 * urls disallowed by robots.txt
	 */
	private Set<URL> robotDisallowedURLs = new HashSet<URL>();

	/**
	 * base url this spider operates on
	 */
	private URL base;

	/**
	 * delay between fetching urls
	 */
	private long crawlDelay;

	/**
	 * A collection of URLs that are waiting to be processed
	 */
	private BlockingQueue<URL> activeLinkQueue = new LinkedBlockingQueue<URL>();

	/**
	 * file path to save local urls
	 */
	private String localURLsPath;

	/**
	 * file path to save external urls
	 */
	private String externalURLsPath;
	
	private String deadURLsPath;
	
	private String disallowedURLsPath;
	
	private String nonParsableURLsPath;
	
	private String robotsPath;
	/**
	 * A collection of URLs that resulted in an error
	 */
	private Collection<URL> deadLinksProcessed = new HashSet<URL>();

	/**
	 * A collection of disallowed URLs that were processed
	 */
	private Collection<URL> disallowedURLsProcessed = new HashSet<URL>();
	/**
	 * A collection of internal URLs that were processed
	 */
	private Collection<URL> internalLinksProcessed = new HashSet<URL>();

	/**
	 * A collection of external URLs that were processed
	 */
	private Collection<URL> externalLinksProcessed = new HashSet<URL>();

	/**
	 * A collection of non parsable URLS that were processed
	 */
	private Collection<URL> nonParsableLinksProcessed = new HashSet<URL>();
	/**
	 * The spider thread
	 */
	private Thread processingThread;

	/**
	 * Is the spider working at the moment?
	 */
	private volatile boolean running = false;

	private SpiderActions actions;

	/**
	 * The constructor intitalizes the base url, the file paths and read
	 * robots.txt
	 * 
	 * @param base
	 * 
	 */
	public SpiderImpl(URL base, SpiderActions actions) {
		this.actions = actions;
		this.base = base;
		this.localURLsPath = PATH + base.getHost() + "_localIWURLs" + EXTENSION;
		this.externalURLsPath = PATH + base.getHost() + "_externalIWURLs" + EXTENSION;
		this.deadURLsPath = PATH + base.getHost() + "_deadIWURLs" + EXTENSION;
		this.nonParsableURLsPath = PATH + base.getHost() + "_nonparsableIWURLs" + EXTENSION;
		this.disallowedURLsPath = PATH + base.getHost() + "_disallowedIWURLs" + EXTENSION;
		try {
			if (this.base.equals(new URL(COM4280_WEBSITE))){
				this.robotsPath = ROBOTS_TXT_URL;				
			} else {
				this.robotsPath = this.base.getHost() + "/robots.txt";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		getActiveLinkQueue().add(base);
		initDisallowedURLs();
	}

	/**
	 * set up disallowed urls from robots.txt
	 */
	private void initDisallowedURLs() {
		try {
			URL robotURL = new URL(this.robotsPath);
			URLConnection robotConn = robotURL.openConnection();
			Scanner reader = new Scanner(robotConn.getInputStream());
			boolean userAgentMatched = false;

			final String USER_AGENT_ENTRY = "user-agent:";
			final String DISALLOW_ENTRY = "disallow:";
			final String CRAWL_DELAY_ENTRY = "crawl-delay:";

			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				line = line.trim().toLowerCase();

				if (line.startsWith(USER_AGENT_ENTRY)) {

					String userAgentEntryValue = line.substring(
							USER_AGENT_ENTRY.length()).trim();
					if (userAgentEntryValue.equals(USER_AGENT_VALUE)
							|| userAgentEntryValue.equals("*")) {
						userAgentMatched = true;
					} else {
						userAgentMatched = false;
					}
					continue;
				}

				if (line.startsWith(DISALLOW_ENTRY)) {
					if (!userAgentMatched) {
						continue;
					}

					String disallowedEntryValue = line.substring(
							DISALLOW_ENTRY.length()).trim();
					URL disallowedURL = new URL(this.base, disallowedEntryValue);
					this.robotDisallowedURLs.add(disallowedURL);
				}

				if (line.startsWith(CRAWL_DELAY_ENTRY)) {

					line = line.substring(CRAWL_DELAY_ENTRY.length()).trim();
					this.crawlDelay = (long) (Double.parseDouble(line) * 1000);

				}
			}
		} catch (MalformedURLException e) {
			log("robots.txt doesn't exist");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * Get the URLs that were waiting to be processed. You should add one URL to
	 * this collection to begin the spider.
	 * 
	 * @return A collection of URLs.
	 */
	public BlockingQueue<URL> getActiveLinkQueue() {
		return this.activeLinkQueue;
	}

	/**
	 * 
	 * @return a set of disallowed urls by robots.txt
	 */
	public Collection<URL> getRobotDisallowedURLs() {
		return this.robotDisallowedURLs;
	}

	/**
	 * Get the URLs that resulted in an error.
	 * 
	 * @return A collection of URL's.
	 */
	public Collection<URL> getDeadLinksProcessed() {
		return this.deadLinksProcessed;
	}

	/**
	 * Get the URLs that were processed and disallowed by robots.txt
	 * 
	 * @return A collection of URLs
	 */
	public Collection<URL> getDisallowedURLsProcessed() {
		return this.disallowedURLsProcessed;
	}

	/**
	 * Get the URLs that were processed and cannot be parsed.
	 * 
	 * @return A collection of URLs.
	 */
	public Collection<URL> getNonParsableLinksProcessed() {
		return this.nonParsableLinksProcessed;
	}

	/**
	 * Get the URLs that were processed and are internal.
	 * 
	 * @return A collection of URLs.
	 */
	public Collection<URL> getInternalLinksProcessed() {
		return this.internalLinksProcessed;
	}

	/**
	 * Get the URLs that were processed and are external.
	 * 
	 * @return A collection of URLs.
	 */
	public Collection<URL> getExternalLinksProcessed() {
		return this.externalLinksProcessed;
	}

	/**
	 * Add a URL for processing.
	 * 
	 * @param url
	 */
	public void addURL(URL url) {
		if (getActiveLinkQueue().contains(url))
			return;
		if (getDeadLinksProcessed().contains(url))
			return;
		if (getInternalLinksProcessed().contains(url))
			return;
		if (getExternalLinksProcessed().contains(url))
			return;
		if (getDisallowedURLsProcessed().contains(url))
			return;
		if (getNonParsableLinksProcessed().contains(url))
			return;

		log("Adding to workload: " + url);
		getActiveLinkQueue().add(url);
	}

	public boolean isParseable(URLConnection connection) {
		return !((connection.getContentType() != null) && !connection
				.getContentType().toLowerCase().startsWith("text/"));
	}

	/**
	 * takes a url from the active queue and processes it, then prints to file
	 * in the end. Stops if paused.
	 */
	public synchronized void processActiveQueue() {
		do {
			// if stopped, break out of loop
			if (!this.running) {
				break;
			}
				URL currUrl = getActiveLinkQueue().poll();
				processURL(currUrl);
				try {
					Thread.sleep(this.crawlDelay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
		} while (!getActiveLinkQueue().isEmpty());
		// print to file, if ended normally
		if (this.running) {
			try {
				printToFile();
				log("Spider finished");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		this.running = false;
	}

	/**
	 * Called internally to process a URL
	 * 
	 * @param url
	 *            The URL to be processed.
	 */
	public void processURL(URL url) {
		log("Processing: " + url);
		try {

			if (!isInternal(url)) {
				log("External link - " + url);
				getExternalLinksProcessed().add(url);
				return;
			}
			if (!isRobotAllowed(url)) {
				log("Disallowed by robots.txt - " + url);
				getDisallowedURLsProcessed().add(url);
				return;
			}

			URLConnection connection = url.openConnection();
			if (!isParseable(connection)) {
				log("Not processing because content type is: "
						+ connection.getContentType());
				getNonParsableLinksProcessed().add(url);
				return;
			}

			// read the URL
			InputStream is = connection.getInputStream();
			Reader r = new InputStreamReader(is);
			// parse the URL
			HTMLEditorKit.Parser parser = new HTMLParser().getParser();
			parser.parse(r, new Parser(url), true);

			// mark URL as complete
			getInternalLinksProcessed().add(url);
			log("Complete: " + url);
		} catch (IOException e) {
			getDeadLinksProcessed().add(url);
			log("Error: " + url);
		}
	}

	public boolean isInternal(URL url) {
		return url.getHost().equalsIgnoreCase(this.base.getHost());
	}

	/**
	 * Called to start the spider
	 */
	public void start() {
		this.processingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				processActiveQueue();
			}
		});
		this.processingThread.start();
		this.running = true;
		log("webCrawler started");

	}

	// /**
	// * Stops the spider permanently.
	// */
	// public void kill() {
	// if (this.processingThread != null) {
	// this.processingThread.interrupt();
	// }
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// try {
	// printToFile();
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// }).start();
	//
	// }

	// /**
	// * Resumes processing active links
	// */
	// public void resume() {
	// processActiveQueue();
	// }

	/**
	 * Pauses the spider
	 */
	public void stop() {
		this.running = false;
		log("webCrawler stopped");
	}

	/**
	 * 
	 * @return is the parser running?
	 */
	public boolean isRunning() {
		return (this.processingThread != null) && (this.running);
	}

	/**
	 * Called internally to log information This basic method just writes the
	 * log out to the stdout.
	 * 
	 * @param entry
	 *            The information to be written to the log.
	 */
	public void log(String entry) {
		// System.out.println((new Date()) + ":" + entry);
		this.actions.log(entry);
	}

	/**
	 * Adds the Spider's headers to the connection
	 * 
	 * @param connection
	 *            the connection to set the request properties for
	 */
	public void setRequestProperties(URLConnection connection) {
		for (String key : REQUEST_PROPERTIES.keySet()) {
			connection.setRequestProperty(key, REQUEST_PROPERTIES.get(key));
		}
	}

	/**
	 * cecks that a url is allowed by robots.txt
	 * 
	 * @param checkURL
	 *            the url to check
	 * @return is the url allowed?
	 */
	public boolean isRobotAllowed(URL checkURL) {
		return !this.robotDisallowedURLs.contains(checkURL);
	}

	/**
	 * print a collection of urls to path
	 * 
	 * @param path
	 * @param urls
	 * @throws FileNotFoundException
	 */
	private void print(String path, Collection<URL> urls)
			throws FileNotFoundException {
		File outfile = new File(path);
		// if(!outfile.exists()) outfile.createNewFile();
		PrintWriter urlWriter = new PrintWriter(outfile);
		for (URL url : urls) {
			urlWriter.println(url);
		}
		urlWriter.flush();
		urlWriter.close();
	}

	/**
	 * prints internal and external urls to two files
	 * 
	 * @throws FileNotFoundException
	 */
	public void printToFile() throws FileNotFoundException {
		print(this.externalURLsPath, getExternalLinksProcessed());
		print(this.localURLsPath, getInternalLinksProcessed());
		print(this.deadURLsPath, getDeadLinksProcessed());
		print(this.nonParsableURLsPath, getNonParsableLinksProcessed());
		print(this.disallowedURLsPath, getDisallowedURLsProcessed());
	}

	/**
	 * A HTML parser callback used by this class to detect links
	 * 
	 */
	public class Parser extends HTMLEditorKit.ParserCallback {
		private URL parserBase;

		public Parser(URL base) {
			this.parserBase = base;
		}

		@Override
		public void handleSimpleTag(HTML.Tag tag,
				MutableAttributeSet attributes, int pos) {
			String href = (String) attributes.getAttribute(HTML.Attribute.HREF);

			if (href == null)
				href = (String) attributes.getAttribute(HTML.Attribute.SRC);

			if (href == null)
				return;

			int i = href.indexOf('#');
			if (i != -1)
				href = href.substring(0, i);

			if (href.toLowerCase().startsWith("mailto:")) {
				return;
			}

			handleLink(href);
		}

		@Override
		public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
			handleSimpleTag(t, a, pos); // handle the same way
		}

		protected void handleLink(String str) {
			try {
				URL url = new URL(this.parserBase, str);
				// if (Spider.this.report.spiderFoundURL(this.parserBase, url))
				addURL(url);
			} catch (MalformedURLException e) {
				log("Found malformed URL: " + str);
			}
		}
	}
}