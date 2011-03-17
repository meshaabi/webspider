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

import webspider.core.crawler.SpiderImpl;

/**
 *
 * @author esh
 */
public class SearchActions implements ActionListener{
    private JButton controlButton;
    private JButton stopButton;
    private JTextField keywordField;

    private JLabel stats_status;
    private JLabel stats_good;
    private JLabel stats_bad;
    private JLabel stats_internal;
    private JLabel stats_external;

    private SpiderActions actions;

    protected Thread backgroundThread;
    protected SpiderImpl spider;
    protected URL base;
    protected int badLinksCount = 0;
    protected int goodLinksCount = 0;

    SearchActions(SpiderActions actions) {
        this.actions = actions;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("find")){
            //spider.start();
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
            stopButton.setEnabled(true);
            actions.getBacker().setEnabled(false);
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

    public void initBaseText(JTextField keywordField){
        this.keywordField = keywordField;
    }
}
