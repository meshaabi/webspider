package webspider.core.indexer;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import webspider.Settings;
import webspider.actions.SpiderActions;
import webspider.gui.IndexerPanel;

/**
 * This class implements the myIWSearchEngine interface and controls the running
 * of the indexer.
 * @author Kushal D'Souza
 */
public class Indexer implements myIWSearchEngine{

    // Variables declared here

    /**
     * Instance of IndexerImpl.
     */
    private IndexerImpl indexer;

    /**
     *  Instance of SpiderActions class.
     */
    private SpiderActions actions;

    
    /**
     * Constructor for Indexer.java.
     * 
     * @param actions instance of SpiderActions class.
     */
    public Indexer(SpiderActions actions){
        this.actions = actions;
    }

    /**
     * Calls the startIndexing function.
     * 
     * @param inputFileName input file name
     * @param outputFileName output file name
     */
    public void IndexCrawledPages(String inputFileName, String outputFileName)
    {
        this.indexer = new IndexerImpl(inputFileName, outputFileName, actions);
        this.indexer.startIndexing();
    }

    /**
     * Calls the startLoadIndex function which loads the index from a file in
     * a new thread.
     * 
     * @param fileName input file from where the index is loaded into memory.
     * @return Map Map containing the index table
     */
    public Map loadIndexTable(String fileName)
    {
        this.indexer = new IndexerImpl(actions);
        Map<String,Set<URL>> indexMap = this.indexer.loadIndexTable(fileName);
        log("Index loaded into memory from file");
        return indexMap;
    }


    /**
     * Calls the start search function.
     * 
     * @param keyword keyword on which search is run
     * @return Set<URL> set of urls containing the keyword
     */
    public Set<URL> search(String keyword)
    {
        Set<URL> results = this.indexer.startSearch(keyword);
        if(results != null)
            printSearchResults(results);
        else
            actions.log("Search returned no results.");
        return results;
    }

    /**
     * Opens the user interface.
     */
    public void openUserInterface() {
        Settings.BACK_BUTTON = false;
        actions.setPanel(new IndexerPanel(actions));
        actions.openInterface();
    }

    /**
     * Closes the user iterface.
     */
    public void closeUserInterface() {
        actions.closeInterface();
    }

    /**
     * Prints the search results to the screen.
     * 
     * @param search Set of URLs containing a keyword
     */
    private void printSearchResults(Set<URL> search)
    {
        // Iterate through the search results and print them.
        Iterator seIt = search.iterator();
        if(seIt.hasNext())
        {
            log("Printing search results");
        }else
        {
            log("No search results found");
        }

        while(seIt.hasNext())
        {
            log(seIt.next().toString());
        }
    }

    /**
     * Logs text to the screen
     * @param text text to be logged to the screen.
     */

    public void log(String text)
    {
        actions.log(text);
    }

    /**
     * Kills the indexer.
     */
    public void killIndexer()
    {
        // Set indexer instance to null.
        this.indexer.setIndexerRunning(false);
        this.indexer = null;
    }

    /**
     * Stops the indexer.
     */
    public void stopIndexer()
    {
        // Set indexerRunning state to false.
        this.indexer.setIndexerRunning(false);
    }

    /**
     * Resumes the indexer.
     */
    public void resume() 
    {
        try {
            // Set indexerRunning state to true.
            this.indexer.setIndexerRunning(true);
            this.indexer.resume();
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return returns the GUI status.
     */
    public String getStatus() {
        return indexer.getStatus();
    }

    /**
     *
     * @return returns the total URL count.
     */
    public int totalURLCount() {
        return this.indexer.getUrlCount();
    }

    /**
     *
     * @return returns the current URL.
     */
    public String currentURL() {
        return this.indexer.getCurrentUrl();
    }

    /**
     *
     * @return Returns the keyword count.
     */
    public int KeywordsIndexedCount() {
        return this.indexer.getIndexCount();
    }

    /**
     *
     * @return Checks if the indexer is running.
     */
    public boolean isRunning() {
        if(this.indexer == null) return false;
        return this.indexer.getIndexerRunning();
    }

    /**
     * Checks if indexer instance is null.
     * @return
     */
    public boolean isNull(){
        if(this.indexer == null) return true;
        else return false;
    }

    /**
     *
     * @return returns the keyword count.
     */
    public int getKeywordCount() {
        return this.indexer.getKeywordCount();
    }
}
