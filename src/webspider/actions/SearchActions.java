/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
    private JLabel indexlistLabel;
    private JTextField keywordField;
    private JButton indexlistButton;

    private JLabel stats_status;
    private JLabel stats_good;
    private JLabel stats_bad;
    private JLabel stats_internal;
    private JLabel stats_external;

    private JFileChooser chooser = new JFileChooser();

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
            actions.getBacker().setEnabled(false);
            indexlistButton.setEnabled(false);
            //spider.start();
            indexlistButton.setEnabled(true);
            actions.getBacker().setEnabled(true);
        }else if(e.getActionCommand().equals("browseindexlist")){
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
               indexlistLabel.setText("URL List : " + chooser.getSelectedFile().getAbsolutePath());
               actions.log("Selected Keyword Index File: " + chooser.getSelectedFile().getAbsolutePath());
            }
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

    public void initindexlistLabel(JLabel indexlistLabel){
        this.indexlistLabel = indexlistLabel;
    }

    public void initKeywordField(JTextField kewordField){
        this.keywordField = kewordField;
    }

    public void initindexlistButton(JButton indexlistButton){
        this.indexlistButton = indexlistButton;
        actions.disableTF(chooser);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(new File("./output/spider"));
        chooser.setFileFilter(new BDMFilter());
    }
}
