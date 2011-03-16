package webspider.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 *
 * @author bdm
 */
public class Indexer extends HTMLEditorKit.ParserCallback  implements myIWSearchEngine{

    protected StringBuffer s;
    protected Collection<URL> fileUrls = new HashSet<URL>();
    protected Map<String,Set<URL>> index = new HashMap<String,Set<URL>>();

    public void parse(Reader in) throws IOException {
        s = new StringBuffer();
        ParserDelegator delegator = new ParserDelegator();
        // the third parameter is TRUE to ignore charset directive
        delegator.parse(in, this, Boolean.TRUE);
    }

    @Override
    public void handleText(char[] text, int pos) {
        s.append(text);
    }

    public String getText() {
        return s.toString();
    }

    public String parser(URL url) throws FileNotFoundException, IOException
    {
        // the HTML to convert
        InputStream is = url.openStream();  // throws an IOException
        BufferedReader d = new BufferedReader(new InputStreamReader(is));
        Indexer parser = new Indexer();
        parser.parse(d);
        return deHtml(parser.getText());
    }

    private String deHtml(String string)
    {
        String nohtml = string.replaceAll("\\<.*?>","");
        String html = nohtml.replaceAll("[^A-Z|^a-z|^0-9|^\\s]","");
        return html;
    }

    public void openUserInterface() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeUserInterface() {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public void IndexCrawledPages(String inputFileName, String outputFileName) {
        try
        {
            FileInputStream fsStream = new FileInputStream(inputFileName);
            DataInputStream in = new DataInputStream(fsStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            URL strLine;
            while((strLine = new URL(br.readLine())) != null)
            {
                fileUrls.add(strLine);
                System.out.println(strLine);
            }
            
        }catch(Exception e)
        {
            System.err.println("Error: " + e.getMessage());
        }
        try {
            processPages();
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Loads an index table from a file into the memory
     *
     * @param fileName      Name of file from which the index table is loaded
     *
     * @return returns the index table
     */
    public String[] loadIndexTable(String fileName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * Returns the list of pages containing a specific keyword
     * @param keyword   the keyword for which the list of pages must be returned
     * @return A list of pages containing the keyword
     */
    public String[] search(String keyword) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void processPages() throws IOException
    {
        for(URL url:fileUrls)
        {
            String[] pageContent = parser(url).split("");
            for(String word:pageContent)
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
    }
}
