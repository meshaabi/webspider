package webspider.actions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import webspider.Settings;
import webspider.core.crawler.Crawler;
import webspider.core.indexer.Indexer;
import webspider.gui.CrawlPanel;
import webspider.gui.IndexerPanel;
import webspider.gui.OptionsPanel;
import webspider.gui.SearchPanel;

/**
 * Actions/methods defined to be used with the Spider application
 * @author Shaabi Mohammed
 */
public class SpiderActions implements ActionListener{
    private JTextArea log;
    private JScrollPane scroll;
    private JButton backButton;
    private JPanel cpanel;
    private JFrame frame;

    private CrawlerActions crawlerActions = new CrawlerActions(this);
    private IndexerActions indexerActions = new IndexerActions(this);
    private SearchActions searchActions = new SearchActions(this);

    /**
     * Actionlistner handler for actions invoked including
     * crawl, index, search, back and exit
     * @param e the event
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("optioncrawl")){
            setPanel(new CrawlPanel(this));
        }else if(e.getActionCommand().equals("optionindex")){
            setPanel(new IndexerPanel(this));
        }else if(e.getActionCommand().equals("optionsearch")){
            setPanel(new SearchPanel(this));
        }else if(e.getActionCommand().equals("back")){
            setPanel(new OptionsPanel(this));
        }else if(e.getActionCommand().equals("exit")){
            System.exit(0);
        }
    }
    
    //Control Panel
    /**
     * Sets the visible frame in the user interface
     * @param frame
     */
    public void setFrame(JFrame frame){
        this.frame = frame;
    }

    /**
     * Sets the panel in the user interface
     * @param cpanel
     */
    public void setPanel(JPanel cpanel) {
        if(this.cpanel != null) frame.remove(this.cpanel);
        this.cpanel = cpanel;
        frame.add(cpanel, BorderLayout.SOUTH);
        frame.validate();
    }

    //backbutton
    /**
     *
     * @param backButton
     */
    public void initBacker(JButton backButton){
        this.backButton = backButton;
    }

    /**
     *
     * @return
     */
    public JButton getBacker(){
        return backButton;
    }
    //frontbutton

    /**
     *
     * @return
     */
    public CrawlerActions getCrawlerActions(){
        return crawlerActions;
    }

    /**
     *
     * @return
     */
    public Crawler getCrawler(){
        return crawlerActions.crawler;
    }

    /**
     *
     * @return
     */
    public Indexer getIndexer(){
        return indexerActions.indexer;
    }

    /**
     *
     * @return
     */
    public IndexerActions getIndexerActions(){
        return indexerActions;
    }

    /**
     *
     * @return
     */
    public SearchActions getSearchActions(){
        return searchActions;
    }

    /**
     * Closes the user interface
     */
    public void closeInterface(){
        frame.setVisible(false);
    }

    /**
     * Opens the user interface
     */
    public void openInterface(){
        frame.setVisible(true);
    }

    /* LOGGER FUNCTIONS */
    /**
     * Initliazes a globbar spider logger
     * @param log
     */
    public void initLogger(JTextArea log){
        this.log = log;
        log("webSpider v1.0 : Team BDM", Settings.DATE_FORMAT);
    }

    /**
     * Initializes the loggin area
     * @param scroll
     */
    public void initScroll(JScrollPane scroll){
        this.scroll = scroll;
    }

    /**
     * Logs an entry to the main user interface
     * @param text
     * @param calFormat
     */
    public void log(String text, String calFormat){
        log.append(time(calFormat) + " : " + text + "\n");
        scrollToBottom();
    }

    /**
     * Logs an entry to the main user interface
     * @param text
     */
    public void log(String text){
        String logstring = time(Settings.TIME_FORMAT) + " : " + text;
        if(Settings.GUI){
            log.append(logstring + "\n");
            scrollToBottom();
        }else{
            System.out.println(logstring);
        }
    }

    /**
     * scrolls to the bottom
     */
    private void scrollToBottom(){
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
    }

    /**
     * gets the curretn time
     * @param format the time format
     * @return the time
     */
    private String time(String format){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }
    /* END */
    /**
     * Disables all the components in the open file dialog
     * that are not required
     * @param c
     * @return
     */
    public boolean disableTF(Container c) {
        Component[] cmps = c.getComponents();
        for (Component cmp : cmps) {
            if (cmp instanceof JTextField || cmp instanceof JLabel || cmp instanceof JComboBox) {
                c.remove(cmp);
            }

            if (cmp instanceof JButton) {
                String name = ((JButton)cmp).getAccessibleContext().getAccessibleName();
                if(!(name.equals("Open") || name.equals("Cancel"))){
                    c.remove(cmp);
                }
            }

            if (cmp instanceof Container) {
                if(disableTF((Container) cmp)) return true;
            }

        }
        return false;
    }
}
