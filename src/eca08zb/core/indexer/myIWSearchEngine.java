package eca08zb.core.indexer;

import java.net.URL;
import java.util.Map;
import java.util.Set;

interface myIWSearchEngine
{
    public void openUserInterface();
    public void closeUserInterface();
    public void IndexCrawledPages(String inputFileName, String outputFileName);
    public Map loadIndexTable(String fileName);
    public Set<URL> search(String keyword);
}
