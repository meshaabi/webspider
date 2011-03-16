package webspider.core;

interface myIWSearchEngine
{
    public void openUserInterface();
    public void closeUserInterface();
    public void IndexCrawledPages(String inputFileName, String outputFileName);
    public String[] loadIndexTable(String fileName);
    public String[] search(String keyword);
}
