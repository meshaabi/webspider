package webspider.core.indexer;

import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * An interface specified by the assignment reuqirements, implemented by the Indexer
 * @author bizso
 *
 */
interface myIWSearchEngine
{
    public void openUserInterface();
    public void closeUserInterface();
    public void IndexCrawledPages(String inputFileName, String outputFileName);
    public Map<String,Set<URL>> loadIndexTable(String fileName);
    public Set<URL> search(String keyword);
}
