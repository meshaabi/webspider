package webspider.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

    /**
     *
     */
    protected Thread backgroundThread;
    /**
     *
     */
    protected Indexer indexer;

    IndexerActions(SpiderActions actions) {
        this.actions = actions;
        indexer = new Indexer(actions);
    }

    /**
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("start")){
                if(inputFile == null){
                    JOptionPane.showMessageDialog(null, "Please select an input File.");
                }else if(inputFile.getName().indexOf("_") == -1){
                    JOptionPane.showMessageDialog(null, "Invalid File name (Should be 'hostname_type.bdmc').");
                }else{
                    controlButton.setActionCommand("pause");
                    controlButton.setText("Pause");
                    stopButton.setEnabled(true);
                    actions.getBacker().setEnabled(false);
                    urllistButton.setEnabled(false);
                    startIndexer(inputFile.getAbsolutePath());
                }
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

    /**
     *
     * @param inputpath
     */
    public void startIndexer(String inputpath){
        File inputFile = new File(inputpath);
        String outputFile = Settings.DEFAULT_PATH + "/" + (inputFile.getName().split("_"))[0] + "_index" + Settings.FILE_INDEX_EXTENSION;
        indexer.IndexCrawledPages(inputFile.getAbsolutePath(), outputFile);
    }

    // Statistics elements
    /**
     *
     */
    public void updateStats(){
        if(Settings.GUI){
            stats_status.setText("Status : " );
            stats_totalurls.setText("Total URLs : ");
            stats_currenturl.setText("Current URL : ");
            stats_keywordsindexed.setText("Keywords Indexed : ");
        }
    }

    /**
     *
     */
    public void resetButtons(){
        if(Settings.GUI){
            controlButton.setActionCommand("start");
            controlButton.setText("Start");
            stopButton.setEnabled(false);
            urllistButton.setEnabled(true);
        }
    }

    /**
     *
     * @param stats_status
     */
    public void initStatus(JLabel stats_status){
        this.stats_status = stats_status;
    }

    /**
     *
     * @param stats_totalurls
     */
    public void initTotalurls(JLabel stats_totalurls){
        this.stats_totalurls = stats_totalurls;
    }

    /**
     *
     * @param stats_currenturl
     */
    public void initCurrenturl(JLabel stats_currenturl){
        this.stats_currenturl = stats_currenturl;
    }

    /**
     * 
     * @param stats_keywordsindexed
     */
    public void initKeywordsindexed(JLabel stats_keywordsindexed){
        this.stats_keywordsindexed = stats_keywordsindexed;
    }

    //ELEMENTS
    /**
     *
     * @param controlButton
     */
    public void initContoller(JButton controlButton){
        this.controlButton = controlButton;
    }

    /**
     *
     * @param stopButton
     */
    public void initStopper(JButton stopButton){
        this.stopButton = stopButton;
    }

    /**
     *
     * @param urllistLabel
     */
    public void initurllistLabel(JLabel urllistLabel){
        this.urllistLabel = urllistLabel;
    }

    /**
     *
     * @param urllistButton
     */
    public void initurllistButton(JButton urllistButton){
        this.urllistButton = urllistButton;
        actions.disableTF(chooser);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(new File("./output/spider"));
        chooser.setFileFilter(new BDMFilter(Settings.CRAWLER_EXTENSION));
    }
}
