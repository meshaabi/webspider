/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.actions;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import webspider.Settings;
import webspider.gui.CrawlPanel;
import webspider.gui.KeywordPanel;
import webspider.gui.OptionsPanel;

/**
 *
 * @author esh
 */
public class SpiderActions implements ActionListener{
    private JTextArea log;
    private JScrollPane scroll;
    private JPanel cpanel;
    private JFrame frame;

    private CrawlerActions crawlerActions = new CrawlerActions(this);
    private KeywordActions keywordActions = new KeywordActions(this);

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("optioncrawl")){
            setPanel(new CrawlPanel(this));
        }else if(e.getActionCommand().equals("optionkeyword")){
            setPanel(new KeywordPanel(this));
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

    public CrawlerActions getCrawlerActions(){
        return crawlerActions;
    }

    public KeywordActions getKeywordActions(){
        return keywordActions;
    }

    /* LOGGER FUNCTIONS */
    public void initLogger(JTextArea log){
        this.log = log;
        log("webSpider v1.0 : Team BDS", Settings.DATE_FORMAT);
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
}
