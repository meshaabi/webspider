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
import webspider.core.Spider;
import webspider.gui.OptionsPanel;

/**
 *
 * @author esh
 */
public class CrawlerActions implements ActionListener, IWSpiderAPI{
    private JButton controlButton;
    private JButton stopButton;
    private JButton backButton;
    private JTextField baseurlField;

    private JLabel stats_status;
    private JLabel stats_good;
    private JLabel stats_bad;
    private JLabel stats_internal;
    private JLabel stats_external;
    
    private SpiderActions actions;

    protected Thread backgroundThread;
    protected Spider spider;
    protected URL base;
    protected int badLinksCount = 0;
    protected int goodLinksCount = 0; 

    CrawlerActions(SpiderActions actions) {
        this.actions = actions;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("start")){
            spider.start();
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
            stopButton.setEnabled(true);
            backButton.setEnabled(false);
        }else if(e.getActionCommand().equals("pause")){
            controlButton.setActionCommand("resume");
            controlButton.setText("Resume");
        }else if(e.getActionCommand().equals("resume")){
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
        }else if(e.getActionCommand().equals("stop")){
            controlButton.setActionCommand("start");
            controlButton.setText("Start");
            stopButton.setEnabled(false);
            backButton.setEnabled(true);
        }else if(e.getActionCommand().equals("back")){
            actions.setPanel(new OptionsPanel(actions));
        }
    }

    // Statistics elements
    public void updateStats(){
        stats_status.setText("Status : " );
        stats_good.setText("Good Links : ");
        stats_bad.setText("Broken Links : ");
        stats_internal.setText("Internal Links : ");
        stats_external.setText("External Links : ");
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

    //ELEMENTS
    public void initContoller(JButton controlButton){
        this.controlButton = controlButton;
    }

    public void initStopper(JButton stopButton){
        this.stopButton = stopButton;
    }

    public void initBacker(JButton backButton){
        this.backButton = backButton;
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
