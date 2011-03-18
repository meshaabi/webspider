package webspider.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import webspider.Settings;
import webspider.core.indexer.Indexer;


/**
 * Actions/methods defined to be used with the Indexer search
 * @author esh
 */
public class SearchActions implements ActionListener{
    private JButton controlButton;

    private JLabel indexlistLabel;
    private JTextField keywordField;
    private JButton indexlistButton;

    private JLabel stats_keyword;
    private JLabel stats_totalkeywords;

    private JFileChooser chooser = new JFileChooser();
    private File inputFile;
    
    private SpiderActions actions;

    SearchActions(SpiderActions actions) {
        this.actions = actions;
        indexer = new Indexer(actions);
    }

     /**
     * Instance of Indexer
     */
    protected Indexer indexer;

    /**
     * Actionlistner handler for actions invoked
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("find")){
            if(inputFile == null){
                JOptionPane.showMessageDialog(null, "Please select an input File.");
            }else if(keywordField.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please enter a search keyword");
            }else{
                startSearch(keywordField.getText());
                updateStats();
            }
            //startSearch(i, null);
        }else if(e.getActionCommand().equals("browseindexlist")){
            int returnVal = chooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
               inputFile = chooser.getSelectedFile();
               indexlistLabel.setText("URL List : " + chooser.getSelectedFile().getAbsolutePath());
               actions.log("Selected Keyword Index File: " + chooser.getSelectedFile().getAbsolutePath());
               indexer.loadIndexTable(inputFile.getAbsolutePath());
               updateStats();
            }
        }
    }

    public void startSearch(String dbfilePath, String keyword){
        actions.log(dbfilePath);
        indexer.loadIndexTable(dbfilePath);
        indexer.search(keyword);
    }

    public void startSearch(String keyword){
        indexer.search(keyword);
    }

    public void resetButtons(){
        indexlistButton.setEnabled(true);
        if(Settings.BACK_BUTTON)actions.getBacker().setEnabled(true);
    }

    // Statistics elements
    /**
     *
     */
    public void updateStats(){
        if(Settings.GUI && !indexer.isNull()){
            stats_keyword.setText("Searching for : " + keywordField.getText());
            stats_totalkeywords.setText("Total Keywords : " + indexer.getKeywordCount());
        }
    }

    /**
     *
     * @param stats_keyword
     */
    public void initKeyword(JLabel stats_keyword){
        this.stats_keyword = stats_keyword;
    }

    /**
     *
     * @param stats_totalkeywords
     */
    public void initTotalKeywords(JLabel stats_totalkeywords){
        this.stats_totalkeywords = stats_totalkeywords;
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
     * @param indexlistLabel
     */
    public void initindexlistLabel(JLabel indexlistLabel){
        this.indexlistLabel = indexlistLabel;
    }

    /**
     *
     * @param kewordField
     */
    public void initKeywordField(JTextField kewordField){
        this.keywordField = kewordField;
    }

    /**
     *
     * @param indexlistButton
     */
    public void initindexlistButton(JButton indexlistButton){
        this.indexlistButton = indexlistButton;
        actions.disableTF(chooser);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setCurrentDirectory(new File("./output/spider"));
        chooser.setFileFilter(new BDMFilter(Settings.FILE_INDEX_EXTENSION));
    }
}
