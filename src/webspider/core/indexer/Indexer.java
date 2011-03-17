/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.core.indexer;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import webspider.actions.SpiderActions;

/**
 *
 * @author esh
 */
public class Indexer implements myIWSearchEngine{

    private IndexerImpl indexer;
    SpiderActions actions;

    public Indexer(SpiderActions actions){
        this.actions = actions;
    }
    
    public void IndexCrawledPages(String inputFileName, String outputFileName) {
        this.indexer = new IndexerImpl(inputFileName, outputFileName, actions);
        this.indexer.start();
    }

    public Map loadIndexTable(String fileName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<URL> search(String keyword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void openUserInterface() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeUserInterface() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
