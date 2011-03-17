/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import webspider.core.IWSpiderAPI;
import webspider.core.crawler.Spider;

/**
 *
 * @author esh
 */
public class CrawlerActions implements ActionListener, IWSpiderAPI{
    private JButton controlButton;
    private JButton stopButton;
    private JTextField baseurlField;

    private JLabel stats_status;
    private JLabel stats_good;
    private JLabel stats_bad;
    private JLabel stats_internal;
    private JLabel stats_external;
    private JLabel stats_disallowed;
    
    private SpiderActions actions;

    protected Thread backgroundThread;
    protected Spider spider;

    CrawlerActions(SpiderActions actions) {
        this.actions = actions;
        spider = new Spider(actions);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("start")){
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
            stopButton.setEnabled(true);
            actions.getBacker().setEnabled(false);
            spider.startIWSpider(baseurlField.getText());
        }else if(e.getActionCommand().equals("pause")){
            controlButton.setActionCommand("resume");
            controlButton.setText("Resume");
        }else if(e.getActionCommand().equals("resume")){
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
        }else if(e.getActionCommand().equals("stop")){
            controlButton.setActionCommand("find");
            controlButton.setText("Find");
            stopButton.setEnabled(false);
            actions.getBacker().setEnabled(true);
        }
    }

    // Statistics elements
    public void updateStats(){
        stats_status.setText("Status : " );
        stats_good.setText("Good Links : ");
        stats_bad.setText("Broken Links : ");
        stats_internal.setText("Internal Links : ");
        stats_external.setText("External Links : ");
        stats_disallowed.setText("Disallowed Links : ");
    }

    public void initStatus(JLabel stats_status){
        this.stats_status = stats_status;
    }

    public void initGood(JLabel stats_good){
        this.stats_good = stats_good;
    }

    public void initBad(JLabel stats_bad){
        this.stats_bad = stats_bad;
    }

    public void initInternal(JLabel stats_internal){
        this.stats_internal = stats_internal;
    }

    public void initExternal(JLabel stats_external){
        this.stats_external = stats_external;
    }

    public void initDisallowed(JLabel stats_disallowed){
        this.stats_disallowed = stats_disallowed;
    }

    //ELEMENTS
    public void initContoller(JButton controlButton){
        this.controlButton = controlButton;
    }

    public void initStopper(JButton stopButton){
        this.stopButton = stopButton;
    }

    public void initBaseText(JTextField baseurlField){
        this.baseurlField = baseurlField;
    }

    public boolean spiderFoundURL(URL base, URL url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void spiderURLError(URL url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void spiderFoundEMail(String email) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
