/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import webspider.gui.CrawlPanel;
import webspider.gui.IndexerPanel;
import webspider.gui.OptionsPanel;
import webspider.gui.SearchPanel;

/**
 *
 * @author esh
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
    public void setFrame(JFrame frame){
        this.frame = frame;
    }

    public void setPanel(JPanel cpanel) {
        if(this.cpanel != null) frame.remove(this.cpanel);
        this.cpanel = cpanel;
        frame.add(cpanel, BorderLayout.SOUTH);
        frame.validate();
    }

    //backbutton
    public void initBacker(JButton backButton){
        this.backButton = backButton;
    }

    public JButton getBacker(){
        return backButton;
    }
    //frontbutton

    public CrawlerActions getCrawlerActions(){
        return crawlerActions;
    }

    public IndexerActions getIndexerActions(){
        return indexerActions;
    }

    public SearchActions getSearchActions(){
        return searchActions;
    }

    /* LOGGER FUNCTIONS */
    public void initLogger(JTextArea log){
        this.log = log;
        log("webSpider v1.0 : Team BD", Settings.DATE_FORMAT);
    }

    public void initScroll(JScrollPane scroll){
        this.scroll = scroll;
    }

    public void log(String text, String calFormat){
        log.append(time(calFormat) + " : " + text + "\n");
        scrollToBottom();
    }

    public void log(String text){
        log.append(time(Settings.TIME_FORMAT) + " : " + text + "\n");
        scrollToBottom();
    }

    private void scrollToBottom(){
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
    }

    private String time(String format){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }
    /* END */
    public boolean disableTF(Container c){
        System.out.println("first");
        return disableTF(c, 0);
    }

    public boolean disableTF(Container c, int countButtons) {
        Component[] cmps = c.getComponents();
        for (Component cmp : cmps) {
            if (cmp instanceof JTextField || cmp instanceof JLabel || cmp instanceof JComboBox) {
                c.remove(cmp);
            }

            if (cmp instanceof JButton) {
                countButtons++;
                if(countButtons <= 3)
                    c.remove(cmp);
                System.out.println(countButtons);
            }

            if (cmp instanceof Container) {
                if(disableTF((Container) cmp, countButtons)) return true;
            }
        }
        return false;
    }
}
