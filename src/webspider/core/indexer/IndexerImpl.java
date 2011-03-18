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


/**
 * This class is used to create an inverted vertex of a list of pages. A text
 * file is given as input to the class and it reads the list of URLs from the
 * text file. It then processes the web pages that each URL is associated with
 * and obtains a list of keywords for the page using the text that is present
 * on the page. An inverted index is created from these keywords and saved to
 * an output file.
 *
 * @author bdm ( Kushal Lyndon D'Souza, Shaabi Mohammed, Bitvais Zsolt )
 * 
 */
public class IndexerImpl extends HTMLEditorKit.ParserCallback{

    // The variables used in the class are declared.
    private String inputFileName;
    private String outputFileName;
    private StringBuffer s;
    private Collection<URL> fileUrlsToProcess = new HashSet<URL>();
    private Collection<URL> fileUrlsProcessed = new HashSet<URL>();
    private Map<String,Set<URL>> index = new HashMap<String,Set<URL>>();
    private Set<String> stopwords = new HashSet<String>();
    private String stopFileName = Settings.STOPFILE_NAME;
    private Thread processingThread;
    private boolean indexerRunning = false;
    private SpiderActions actions;
    private Thread searchThread;
    private Set<URL> searchResults;
    private Thread indexThread;
    private boolean readingFromFile = false;
    private boolean processingPages = false;
    private BufferedReader indexerBufferReader;

    /*
     * Constructor for IndexerImpl class
     * @param action instance of SpiderActions class
     */
    IndexerImpl(String inputFileName, String outputFileName, SpiderActions actions) {
        this.actions = actions;
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    /*
     * Reads a list of stopwords from a file and saves these into a HashSet.
     */
    public void addStopWords()
    {
        try
        {
            // Opens file to read stopwords from.
            FileInputStream fsStream = new FileInputStream(stopFileName);
            DataInputStream in = new DataInputStream(fsStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            // Reads stopwords line by line from the file.
            while((strLine = br.readLine()) != null)
            {               
                // Add stopwords to HashSet.
                stopwords.add(strLine);
            }
            br.close();
            in.close();
            fsStream.close();

        }catch(Exception e)
        {
            actions.log(e.getMessage());
        }
    }

    /*
     * Loads a list of URLs from the input file and saves the inverted indices
     * onto the output file
     *
     * @param inputFileName     The input file from where the list of URLs are
     *                          read.
     *
     * @param outputFileName    The output file to which the inverted indices
     *                          are stored.
     *
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public void IndexCrawledPages(String inputFileName, String outputFileName)
    {
        addStopWords();
        this.readingFromFile = true;
        actions.log("Reading in URLs from file");
        try
        {
            // Open file to read the URLs
            FileInputStream fsStream = new FileInputStream(inputFileName);
            DataInputStream in = new DataInputStream(fsStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            this.indexerBufferReader = br;
            readInputLine(br);
            br.close();
            in.close();
            fsStream.close();
        }catch(Exception e)
        {
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        this.readingFromFile = false;
        this.processingPages = true;
        try {
            // Run the processPages function.
            actions.log("Starting to process pages");
            processPages();
            this.processingPages = false;
        } catch (IOException ex) {
            Logger.getLogger(IndexerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            actions.log("Writing index to outputfile "+ outputFileName);
            writeIndexToFile(outputFileName);
        } catch (IOException ex) {
            Logger.getLogger(IndexerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Goes through the list of URLs and then process each web page
     * associated with the URL so as to obtain the keywords from the page
     */
    private void processPages() throws IOException
    {
    	Iterator<URL> toProcessIterator = fileUrlsToProcess.iterator();
        while(toProcessIterator.hasNext() && indexerRunning)
        {
        	URL url = toProcessIterator.next();
            
            // Parse page content using the parser function
            String[] pageContent = parser(url).split(" ");
            // Add each word along with the URL to a Map with the keyword as
            // the key and the set of URLs as the index.
            for(String word:pageContent)
            {
                if(!stopwords.contains(word))
                {
                    if(index.get(word)==null)
                    {
                        Set<URL> set = new HashSet<URL>();
                        set.add(url);
                        index.put(word,set );
                    }else
                    {
                        index.get(word).add(url);                        
                    }
                }
            }
            toProcessIterator.remove();
            fileUrlsProcessed.add(url);
            actions.log("Index for " + url.toString() + " has been created.");
        }
        this.processingPages = false;

    }

    /*
     * Parses a webpage by removing all the javascript code as well as css
     * styles. It then removes the tags from the page returning only the text
     * contained on the page.
     *
     * @param url URL of the page.
     * @return String containing the text on the page.
     */
    public String parser(URL url) throws FileNotFoundException, IOException
    {
        // the HTML to convert
        InputStream is = url.openStream();  // throws an IOException
        BufferedReader d = new BufferedReader(new InputStreamReader(is));
        parse(d);
        d.close();
        is.close();
        return deHtml(getText());
    }

    /*
     * Fuction is used by the parser function. This function actually does
     * parsing of the webpage.It then removes the tags from the page returning
     * only the text contained on the page.
     *
     * @param in Is an instance of the Reader class.
     */
    public void parse(Reader in) throws IOException {
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        // the third parameter is TRUE to ignore charset directive
        delegator.parse(in, this, Boolean.TRUE);
    }

    /*
     * Removes all the special charecters present in the string. This function
     * also checks for HTML tags and removes them. However, it will not remove
     * any javascript or css code from the page.
     *
     * @param string string containing text from a page.
     *
     * @return String which has had all the special charecters and any remaining
     * HTML tages removed.
     */
    private String deHtml(String string)
    {
    	// User regular expression to remove links.
    	String noLinks = string.replaceAll("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?","");
        // Use regular expression to remove HTML tags.
        String nohtml = string.replaceAll("\\<.*?>","");
        // Use regular expression to remove special charecters.
        String html = nohtml.replaceAll("[^A-Z|^a-z|^0-9|^\\s]+","");
        // Replaces the "|" charecter and multiple white space with single space, trims the ends.
        String finalHtml = html.replaceAll("\\|","").replaceAll("\\s+", " ").trim();
        return finalHtml;
    }
    
    @Override
    /*
     * Appends text to a given StringBuffer
     * @param text text to append to the StringBuffer
     * @ param pos poition at which text must be appended.
     */
    public void handleText(char[] text, int pos) {
        s.append(text).append(" ");
    }

    /*
     * Converts the StringBuffer to a string.
     *
     * @return String returns the string.
     */
    public String getText() {
        return s.toString();
    }

    public void openUserInterface() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeUserInterface() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * Loads an index table from a file into the memory
     *
     * @param fileName      Name of file from which the index table is loaded
     *
     * @return returns the index table
     */
    public Map loadIndexTable(String fileName) 
    {
        // Clear index contents
        index.clear();
        try
        {
            // Open file to read the index
            FileInputStream fsStream;
            fsStream = new FileInputStream(fileName);
            DataInputStream in = new DataInputStream(fsStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            try
            {
                while ((strLine = br.readLine()) != null)
                {
                    // Read each line and create index.
                    String[] parts = strLine.split(" ");
                    String currentKeyword = parts[0];
                    Set<URL> urls = new HashSet<URL>();
                    index.put(currentKeyword, urls);
                    for(int x = 1;x<parts.length;x++)
                    {
                        URL url = new URL(parts[x]);
                        index.get(currentKeyword).add(url);
                    }

                }
                br.close();
                in.close();
                fsStream.close();
            } catch (IOException ex)
            {
                Logger.getLogger(IndexerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(IndexerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return index;
    }

    /*
     * Returns the list of pages containing a specific keyword
     * @param keyword   the keyword for which the list of pages must be returned
     * @return A list of pages containing the keyword
     */
    public Set<URL> search(String keyword)
    {
        if(index.containsKey(keyword))
        {
            Set<URL> urls = index.get(keyword);
            return urls;
        }else
        {
            return null;
        }
    }
    
    /*
     * Writes the index that is present in the memory to a file.
     * @param filename filename to which index must be written.
     * 
     */
    public void writeIndexToFile(String fileName) throws IOException
    {
        // Open FileWriter instance to write to a file.
        FileWriter outputFile = new FileWriter(fileName);
        PrintWriter out = new PrintWriter(outputFile);
        // Iterate through index and add the list of URLs for each keyword.
        Iterator it = index.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry pairs = (Map.Entry)it.next();
            String keyword = (String)pairs.getKey();
            HashSet urlList = (HashSet)pairs.getValue();
            out.print(keyword);
            out.print(" ");
            Iterator urlIt = urlList.iterator();
            while(urlIt.hasNext())
            {
                  out.print(urlIt.next());
                  out.print(" ");
            }
            out.println();
            actions.log("Index for keyword \"" + keyword + "\" has been written to file.");
        }
        out.close();
        outputFile.close();
        actions.log("Index written to file.");
    }

    /*
     * Starts the indexing in a new thread
     */
    public void startIndexing()
    {
        this.indexerRunning = true;
        this.processingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				IndexCrawledPages(inputFileName, outputFileName);
			}
		});
		this.processingThread.start();
		this.indexerRunning = true;
		log("KeywordIndexer started");
    }

    /*
     * Starts the search in a new thread
     * @param keyword keyword on which search is run
     * @return Set set of URLs that contain the keyword
     */
    public Set<URL> startSearch(final String keyword)
    {
        this.searchThread= new Thread(new Runnable() {

            public void run() {
                searchResults = search(keyword);
            }
        });
        this.searchThread.start();
        return searchResults;
    }

    /*
     * Loads an index from the file into memory
     * @return index table
     * @param filename name of file containing the index
     */
    public Map startLoadIndex( final String filename )
    {
        this.indexThread = new Thread(new Runnable() {

            public void run() {
                index = loadIndexTable(filename);
            }
        });
        this.indexThread.start();
        return index;
    }

    /*
     * Adds information to the log.
     * @param text text to be printed to the log.
     */
    public void log(String text)
    {
        actions.log(text);
    }

    /*
     * Set the value of indexerRunning
     * @param b boolean value
     */
    public void setIndexerRunning(boolean b) {
        this.indexerRunning = b;
    }

    /*
     * Returns the value of indexerRunning
     */
    public boolean getIndexerRunning()
    {
        return indexerRunning;
    }

    /*
     * Resumes the running of the indexer
     */
    public void resume() throws IOException
    {
        if(readingFromFile)
        {
            readInputLine(this.indexerBufferReader);
            this.readingFromFile = false;
            this.processingPages = true;
            processPages();
            this.processingPages = false;
        }
        
        if(processingPages)
        {
            processPages();
            this.processingPages = false;
        }
    }

    /*
     * Reads the lines from the input file
     * @param br BufferedReader instance
     */

    private void readInputLine(BufferedReader br) throws IOException
    {
        String strLine;
        while((strLine = (br.readLine())) != null)
        {
            URL url = new URL(strLine);
            // Read URL from the current line and add to HashSet.
            fileUrlsToProcess.add(url);
            actions.log(strLine + " has been retrieved.");
            if(!indexerRunning)
            {
                break;
            }
        }
        br.close();
    }
}
