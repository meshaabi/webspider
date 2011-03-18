/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import webspider.Settings;
import webspider.core.indexer.Indexer;

/**
 *
 * @author esh
 */
public class IndexerActions implements ActionListener{
    private JButton controlButton;
    private JButton stopButton;
    private JLabel urllistLabel;
    private JButton urllistButton;

    private JLabel stats_status;
    private JLabel stats_totalurls;
    private JLabel stats_currenturl;
    private JLabel stats_keywordsindexed;

    private JFileChooser chooser = new JFileChooser();
    private File inputFile;

    private SpiderActions actions;

    protected Thread backgroundThread;
    protected Indexer indexer;

    IndexerActions(SpiderActions actions) {
        this.actions = actions;
        indexer = new Indexer(actions);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("start")){
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
            stopButton.setEnabled(true);
            actions.getBacker().setEnabled(false);
            urllistButton.setEnabled(false);
            //indexer.IndexCrawledPages(inputFile.getAbsolutePath(), (inputFile.getName().split("_"))[0] + "_index" + Settings.FILE_INDEX_EXTENSION);
            urllistButton.setEnabled(true);
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
            actions.getBacker().setEnabled(true);
            urllistButton.setEnabled(true);
        }else if(e.getActionCommand().equals("browseurllist")){
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
               urllistLabel.setText("URL List : " + chooser.getSelectedFile().getAbsolutePath());
               actions.log("Selected URL List File: " + chooser.getSelectedFile().getAbsolutePath());
               inputFile = chooser.getSelectedFile();
            }
        }
    }

    // Statistics elements
    public void updateStats(){
        stats_status.setText("Status : " );
        stats_totalurls.setText("Total URLs : ");
        stats_currenturl.setText("Current URL : ");
        stats_keywordsindexed.setText("Keywords Indexed : ");
    }

    public void initStatus(JLabel stats_status){
        this.stats_status = stats_status;
    }

    public void initTotalurls(JLabel stats_totalurls){
        this.stats_totalurls = stats_totalurls;
    }

    public void initCurrenturl(JLabel stats_currenturl){
        this.stats_currenturl = stats_currenturl;
    }

    public void initKeywordsindexed(JLabel stats_keywordsindexed){
        this.stats_keywordsindexed = stats_keywordsindexed;
    }

    //ELEMENTS
    public void initContoller(JButton controlButton){
        this.controlButton = controlButton;
    }

    public void initStopper(JButton stopButton){
        this.stopButton = stopButton;
    }

    public void initurllistLabel(JLabel urllistLabel){
        this.urllistLabel = urllistLabel;
    }

    public void initurllistButton(JButton urllistButton){
        this.urllistButton = urllistButton;
        actions.disableTF(chooser);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(new File("./output/spider"));
        chooser.setFileFilter(new BDMFilter(Settings.CRAWLER_EXTENSION));
    }
}
