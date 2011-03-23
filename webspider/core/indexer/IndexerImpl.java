package webspider.core.indexer;

// Imports all the necessary packages
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import webspider.Settings;
import webspider.actions.SpiderActions;
import webspider.core.crawler.Crawler;

/**
 * This class is used to create an inverted vertex of a list of pages. A text
 * file is given as input to the class and it reads the list of URLs from the
 * text file. It then processes the web pages that each URL is associated with
 * and obtains a list of keywords for the page using the text that is present on
 * the page. An inverted index is created from these keywords and saved to an
 * output file.
 * 
 * @author Kushal Lyndon D'Souza
 * 
 */
public class IndexerImpl extends HTMLEditorKit.ParserCallback {

	// The variables used in the class are declared.

	/**
	 * Input file from which the list of URLs are read.
	 */
	String inputFileName;

	/**
	 * Output file to which the index is saved.
	 */
	String outputFileName;

	/**
	 * StringBuffer to which the lines from the url file are saved to.
	 */
	private StringBuffer s;

	/**
	 * URLs that have yet to be indexed.
	 */
	private Collection<URL> fileUrlsToProcess = new HashSet<URL>();

	/**
	 * URLs that have already been indexed.
	 */
	private Collection<URL> fileUrlsProcessed = new HashSet<URL>();

	/**
	 * Index mapping the keywords to their repecive URLs in which they are
	 * contained.
	 */
	private Map<String, Set<URL>> index = new HashMap<String, Set<URL>>();

	/**
	 * Stop words which are removed from the list of words retrieved from the
	 * web pages.
	 */
	private Set<String> stopwords = new HashSet<String>();

	/**
	 * File containing the list of stop words.
	 */
	private String stopFileName = Settings.STOPFILE_NAME;

	/**
	 * Indexer Thread
	 */
	private Thread processingThread;

	/**
	 * Status of the indexer Thread.
	 */
	private boolean indexerRunning = false;

	/**
	 * Instance of the SpiderActions class.
	 */
	private SpiderActions actions;

	/**
	 * List of URLs returned by the search results.
	 */
	private Set<URL> searchResults;

	/**
	 * Status showing whether the indexer is currently reading from the input
	 * file.
	 */
	private boolean readingFromFile = false;

	/**
	 * Status showing whether the indexer is currently processing the list of
	 * URLs.
	 */
	private boolean processingPages = false;

	/**
	 * BufferedReader instance used to read lines from the input file.
	 */
	private BufferedReader indexerBufferReader;

	/**
	 * Status that is sent to the GUI.
	 */
	private String status = "";

	/**
	 * Number of URLs read so far.
	 */
	private int urlCount = 0;

	/**
	 * Current URL being processed.
	 */
	private String currentUrl;

	/**
	 * Number of keywords obtained so far.
	 */
	private int indexCount = 0;

	/**
	 * Delay before sending next connection request.
	 */
	private Long crawlDelay;

	/**
	 * Constructor for IndexerImpl class.
	 * 
	 * @param inputFileName
	 *            file from which the URLs are read.
	 * @param outputFileName
	 *            file to which the index is written.
	 * @param actions
	 *            Instance of SpiderActions class.
	 */
	public IndexerImpl(String inputFileName, String outputFileName,
			SpiderActions actions) {
		this.actions = actions;
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
	}

	/**
	 * Secondary Constructor.
	 * 
	 * @param actions
	 *            Instance of the SpiderActions class.
	 */
	public IndexerImpl(SpiderActions actions) {
		this.actions = actions;
	}

	/**
	 * Reads a list of stop words from a file and saves these into a HashSet.
	 */
	public void addStopWords() {
		try {
			// Opens file to read stopwords from.
			FileInputStream fsStream = new FileInputStream(this.stopFileName);
			DataInputStream in = new DataInputStream(fsStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Update GUI status.
			this.status = "Loading stopwords";
			this.actions.getIndexerActions().updateStats();
			// Reads stopwords line by line from the file.
			while ((strLine = br.readLine()) != null) {
				// Add stopwords to HashSet.
				this.stopwords.add(strLine);
			}
			// Close FileInputStream, DataInputStream and BufferedReader.
			br.close();
			in.close();
			fsStream.close();

		} catch (Exception e) {
			this.actions.log(e.getMessage());
		}
	}

	/**
	 * Loads a list of URLs from the input file and saves the inverted indices
	 * onto the output file
	 * 
	 * @param inFileName
	 *            The input file from where the list of URLs are read.
	 * 
	 * @param outFileName
	 *            The output file to which the inverted indices are stored.
	 * 
	 */
	public void IndexCrawledPages(String inFileName, String outFileName) {
		// Add stop words into memory when the indexer is run.
		addStopWords();
		// Update reading from file status. Used for pause/resume.
		this.readingFromFile = true;
		// Update the log and GUI status messages.
		this.actions.log("Reading in URLs from file");
		this.status = "Reading in URLs from file";
		this.actions.getIndexerActions().updateStats();
		try {
			// Open file to read the URLs
			FileInputStream fsStream = new FileInputStream(inFileName);
			DataInputStream in = new DataInputStream(fsStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			this.indexerBufferReader = br;
			// Call function to read data from each line.
			readInputLine(br);
			// Close the FileInputStream, DataInputStream and BufferedReader.
			br.close();
			in.close();
			fsStream.close();
		} catch (Exception e) {
			// Print stack trace in the event that an exception is thrown.
			System.out.println(e.getCause());
			e.printStackTrace();
		}
		// Update reading from file and processing pages status. Used for pause
		// resume.
		this.readingFromFile = false;
		this.processingPages = true;
		try {
			// Update log message.
			this.actions.log("Starting to process pages");
			// Run the processPages function.
			processPages();
			// Update processing pages status.
			this.processingPages = false;
		} catch (IOException ex) {
			// Log exception in case it is thrown.
			Logger.getLogger(IndexerImpl.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		try {
			// Update log message.
			this.actions.log("Writing index to outputfile " + outFileName);
			// Call function to write index to file.
			writeIndexToFile(outFileName);
		} catch (IOException ex) {
			// Log exception in case it is thrown.
			Logger.getLogger(IndexerImpl.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 * Goes through the list of URLs and then process each web page associated
	 * with the URL so as to obtain the keywords from the page.
	 * 
	 * @throws IOException
	 *             Input Output Exception might be thrown.
	 */
	private void processPages() throws IOException {

		// Iterate through the list of URLs to be proceesed.
		Iterator<URL> toProcessIterator = this.fileUrlsToProcess.iterator();
		while (toProcessIterator.hasNext() && this.indexerRunning) {

			URL url = toProcessIterator.next();

			// Parse page content using the parser function
			String[] pageContent = parser(url).split(" ");

			// Add each word along with the URL to a Map with the keyword as
			// the key and the set of URLs as the index.
			for (String word : pageContent) {
				// Check if the word is a stop word. If not, then add to index.
				if (!this.stopwords.contains(word)) {
					if (this.index.get(word) == null) {
						Set<URL> set = new HashSet<URL>();
						set.add(url);
						this.index.put(word, set);
					} else {
						this.index.get(word).add(url);
					}
				}
			}
			// Remove from URLs to be processed.
			toProcessIterator.remove();
			// Add to URLs procccesed.
			this.fileUrlsProcessed.add(url);
			this.currentUrl = url.toString();
			// Update GUI status message.
			this.actions.getIndexerActions().updateStats();
			// Update log message.
			this.actions.log("Index for " + url.toString() + " has been created.");
			// wait for crawl delay
			try {
				Thread.sleep(getProcessingDelay(url));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Update processingPages status.
		this.processingPages = false;

	}

	/**
	 * Gets the delay between processing pages
	 * 
	 * @return
	 */
	@SuppressWarnings("boxing")
	private long getProcessingDelay(URL url) {
		try {
			if (this.crawlDelay == null) {
				Crawler crawler = null;
				URL defaultUrl = new URL(Settings.DEFAULT_URL);
				if(url.getHost().equals(defaultUrl.getHost())){
					crawler = new Crawler(this.actions);
				} else {
					crawler = new Crawler(this.actions, new URL(url.getProtocol()
						+ "://" + url.getHost()));
				}
				this.crawlDelay = crawler.getCrawlDelay();
			}
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		return this.crawlDelay;
	}

	/**
	 * Parses a webpage by removing all the javascript code as well as css
	 * styles and HTML/XML tags. It then returns only the text contained on the
	 * page.
	 * 
	 * @param url
	 *            URL of the page.
	 * @return String containing the text on the page.
	 * @throws FileNotFoundException
	 *             Throws exception if file is not found.
	 * @throws IOException
	 *             Throws IOException.
	 */
	public String parser(URL url) throws FileNotFoundException, IOException {
		// Open page to read content
		InputStream is = url.openStream(); // throws an IOException
		BufferedReader d = new BufferedReader(new InputStreamReader(is));
		// Call parse function to parse content.
		parse(d);
		// Close the InputStream and the BufferedReader.
		d.close();
		is.close();
		// Call deHtml to check for any remaining tags, special charecters,
		// white spaces or links in the page.
		return deHtml(getText());
	}

	/**
	 * Fuction is used by the parser function. This function actually does
	 * parsing of the webpage.It then removes the tags from the page returning
	 * only the text contained on the page.
	 * 
	 * @param in
	 *            Is an instance of the Reader class.
	 * @throws IOException
	 *             Throws IOException.
	 */
	public void parse(Reader in) throws IOException {
		// Instance of StringBuffere is created to store consecutive lines.
		this.s = new StringBuffer();
		// ParseDelegator is instantiated to parse the content.
		ParserDelegator delegator = new ParserDelegator();
		// the third parameter is TRUE to ignore charset directive
		delegator.parse(in, this, true);
	}

	/**
	 * Removes all the special charecters present in the string. This function
	 * also checks for HTML tags and removes them. However, it will not remove
	 * any javascript or css code from the page.
	 * 
	 * @param string
	 *            string containing text from a page.
	 * 
	 * @return String which has had all the special charecters and any remaining
	 *         HTML tages removed.
	 */
	private String deHtml(String string) {
		// User regular expression to remove links.
		String noLinks = string
				.replaceAll(
						"(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?",
						"");
		// Use regular expression to remove HTML tags.
		String noHtml = noLinks.replaceAll("\\<.*?>", "");
		// Use regular expression to remove special charecters.
		String html = noHtml.replaceAll("[^A-Z|^a-z|^0-9|^\\s]+", "");
		// Replaces the "|" charecter and multiple white space with single
		// space, trims the ends.
		String finalHtml = html.replaceAll("\\|", "").replaceAll("\\s+", " ")
				.trim();
		return finalHtml;
	}

	/**
	 * Appends text to a given StringBuffer.
	 * 
	 * @param text
	 *            text to append to the StringBuffer
	 * @param pos
	 *            position at which text must be appended.
	 */
	@Override
	public void handleText(char[] text, int pos) {
		this.s.append(text).append(" ");
	}

	/**
	 * Converts the StringBuffer to a string.
	 * 
	 * @return String returns the string.
	 */
	public String getText() {
		return this.s.toString();
	}

	/**
	 * Loads an index table from a file into the memory
	 * 
	 * @param fileName
	 *            Name of file from which the index table is loaded
	 * 
	 * @return returns the index table
	 */
	public Map<String, Set<URL>> loadIndexTable(String fileName) {
		// Clear index contents
		this.index.clear();
		try {
			// Open file to read the index
			FileInputStream fsStream;
			fsStream = new FileInputStream(fileName);
			DataInputStream in = new DataInputStream(fsStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			try {
				// Read from file line by line.
				while ((strLine = br.readLine()) != null) {
					// Read each line and create index.
					// Split the lines at the whitespace charecter.
					String[] parts = strLine.split(" ");
					// Keyword is the first element in the array.
					String currentKeyword = parts[0];
					Set<URL> urls = new HashSet<URL>();
					// Add keyword and associated URLs to the index.
					this.index.put(currentKeyword, urls);
					for (int x = 1; x < parts.length; x++) {
						URL url = new URL(parts[x]);
						this.index.get(currentKeyword).add(url);
					}

				}
				// Close FileInputStream, DataInputStream and BufferedReader.
				br.close();
				in.close();
				fsStream.close();
			} catch (IOException ex) {
				// Log exception if thrown.
				Logger.getLogger(IndexerImpl.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		} catch (FileNotFoundException ex) {
			// Log exception if thrown.
			Logger.getLogger(IndexerImpl.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return this.index;
	}

	/**
	 * Returns the list of pages containing a specific keyword
	 * 
	 * @param keyword
	 *            the keyword for which the list of pages must be returned
	 * @return A list of pages containing the keyword
	 */
	public Set<URL> search(String keyword) {
		// Search for keyword in index and return it if found, else return null.
		if (this.index.containsKey(keyword)) {
			Set<URL> urls = this.index.get(keyword);
			return urls;
		}
		return null;
	}

	/**
	 * Writes the index that is present in the memory to a file.
	 * 
	 * @param fileName
	 *            file name to write to.
	 * @throws IOException
	 *             IOException can be thrown.
	 * 
	 */
	public void writeIndexToFile(String fileName) throws IOException {
		// Open FileWriter instance to write to a file.
		FileWriter outputFile = new FileWriter(fileName);
		PrintWriter out = new PrintWriter(outputFile);
		// Iterate through index and add the list of URLs for each keyword.
		Iterator<?> it = this.index.entrySet().iterator();
		while (it.hasNext()) {
			// Get next pair of keyword, URLs.
			Map.Entry pairs = (Map.Entry) it.next();
			// Get keyword.
			String keyword = ((String) pairs.getKey()).toLowerCase();
			// Get URL.
			HashSet<?> urlList = (HashSet<?>) pairs.getValue();
			// Write keyword to file.
			out.print(keyword);
			// Increase keyword count.
			this.indexCount++;
			// Update GUI status.
			this.actions.getIndexerActions().updateStats();
			out.print(" ");
			Iterator<?> urlIt = urlList.iterator();
			while (urlIt.hasNext()) {
				// Write URLs to file.
				out.print(urlIt.next());
				out.print(" ");
			}
			// Go to new line in file.
			out.println();
			// Update log message.
			this.actions.log("Index for keyword \"" + keyword
					+ "\" has been written to file.");
		}
		// Close PrintWriter and FileWriter.
		out.close();
		outputFile.close();
		this.actions.log("Index written to file.");
		this.actions.getIndexerActions().resetButtons();
	}

	/**
	 * Starts the indexing in a new thread
	 */
	public void startIndexing() {
		// Set IndexerRunning state.
		this.indexerRunning = true;
		// Create new thread to run indexer.
		this.processingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				IndexCrawledPages(IndexerImpl.this.inputFileName, IndexerImpl.this.outputFileName);
			}
		});
		// Start Indexer Thread.
		this.processingThread.start();
		// Update Indexer Running state.
		this.indexerRunning = true;
		log("KeywordIndexer started");
	}

	/**
	 * Starts the search.
	 * 
	 * @param keyword
	 *            keyword on which search is run
	 * @return Set set of URLs that contain the keyword
	 */
	public Set<URL> startSearch(final String keyword) {
		// Call the search function to search for a keyword.
		this.searchResults = search(keyword.toLowerCase());
		return this.searchResults;
	}

	/**
	 * Loads an index from the file into memory.
	 * 
	 * @param filename
	 *            name of file containing the index
	 * @return index table
	 * 
	 */
	public Map<String, Set<URL>> startLoadIndex(final String filename) {
		this.index = loadIndexTable(filename);
		return this.index;
	}

	/**
	 * Adds information to the log.
	 * 
	 * @param text
	 *            text to be printed to the log.
	 */
	public void log(String text) {
		this.actions.log(text);
	}

	/**
	 * Set the state of indexerRunning.
	 * 
	 * @param b
	 *            boolean value
	 */
	public void setIndexerRunning(boolean b) {
		this.indexerRunning = b;
	}

	/**
	 * Returns the value of indexerRunning
	 * 
	 * @return State of indexerRunning.
	 */
	public boolean getIndexerRunning() {
		return this.indexerRunning;
	}

	/**
	 * Resumes the running of the indexer
	 * 
	 * @throws IOException
	 *             IOException might be thrown.
	 */
	public void resume() throws IOException {
		// If last stopped at reading file, continue reading file, else
		// process page.
		if (this.readingFromFile) {
			readInputLine(this.indexerBufferReader);
			this.readingFromFile = false;
			this.processingPages = true;
			processPages();
			this.processingPages = false;
		}

		if (this.processingPages) {
			processPages();
			this.processingPages = false;
		}
	}

	/**
	 * Reads the lines from the input file.
	 * 
	 * @param br
	 *            BufferedReader instance.
	 * @throws IOException
	 *             IOException thrown.
	 */

	private void readInputLine(BufferedReader br) throws IOException {
		String strLine;
		// Read URLs line by line
		while ((strLine = (br.readLine())) != null) {
			URL url = new URL(strLine);
			// Read URL from the current line and add to HashSet.
			this.fileUrlsToProcess.add(url);
			// Increase URL count.
			this.urlCount++;
			// Update GUI statuses.
			this.actions.getIndexerActions().updateStats();
			this.actions.log(strLine + " has been retrieved.");
			// Break if paused.
			if (!this.indexerRunning) {
				break;
			}
		}
		// Close BufferedReader instance.
		br.close();
	}

	/**
	 * Prints the search results to the screen
	 * 
	 * @param search
	 *            Set of URLs containing a keyword
	 */
	private void printSearchResults(Set<URL> search) {
		Iterator<URL> seIt = search.iterator();
		// Iterate through search results and print them to GUI.
		if (seIt.hasNext()) {
			log("Printing search results");
		} else {
			log("No search results found");
		}

		while (seIt.hasNext()) {
			// Print Result.
			log(seIt.next().toString());
		}
	}

	/**
	 * @return returns the GUI status.
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * 
	 * @return returns the URL count.
	 */
	public int getUrlCount() {
		return this.urlCount;
	}

	/**
	 * 
	 * @return returns the current url.
	 */
	public String getCurrentUrl() {
		return this.currentUrl;
	}

	/**
	 * 
	 * @return returns the index count.
	 */
	public int getIndexCount() {
		return this.indexCount;
	}

	/**
	 * 
	 * @return returns the keyword count.
	 */
	public int getKeywordCount() {
		return this.index.size();
	}

}
