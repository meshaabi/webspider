package webspider.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import webspider.Settings;


/**
 *
 * @author esh
 */
public class SearchActions implements ActionListener{
    private JButton controlButton;

    private JLabel indexlistLabel;
    private JTextField keywordField;
    private JButton indexlistButton;

    private JLabel stats_status;
    private JLabel stats_keyword;
    private JLabel stats_totalkeywords;

    private JFileChooser chooser = new JFileChooser();
    private SpiderActions actions;

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
        stats_keyword.setText("Searching for : ");
        stats_totalkeywords.setText("Total Keywords : ");
    }

    public void initStatus(JLabel stats_status){
        this.stats_status = stats_status;
    }

    public void initKeyword(JLabel stats_keyword){
        this.stats_keyword = stats_keyword;
    }

    public void initTotalKeywords(JLabel stats_totalkeywords){
        this.stats_totalkeywords = stats_totalkeywords;
    }

    //ELEMENTS
    public void initContoller(JButton controlButton){
        this.controlButton = controlButton;
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
        chooser.setFileFilter(new BDMFilter(Settings.FILE_INDEX_EXTENSION));
    }
}
