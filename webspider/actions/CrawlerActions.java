package webspider.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import webspider.Settings;
import webspider.core.crawler.Crawler;

/**
 * Actions/methods defined to be used with the Crawler
 * @author Shaabi Mohammed
 */
public class CrawlerActions implements ActionListener{
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
    /**
     * Instance of Crawler crawler
     */
    protected Crawler crawler;

    CrawlerActions(SpiderActions actions) {
        this.actions = actions;
        crawler = new Crawler(actions);
    }

    /**
     * Actionlistner handler for actions invoked. Listens to 
     * start, stop, pause and resume
     * @param e the event
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("start")){
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
            stopButton.setEnabled(true);
            if(Settings.BACK_BUTTON)actions.getBacker().setEnabled(false);
            if(!baseurlField.getText().isEmpty()){
                startSpider(baseurlField.getText());
            }else{
                JOptionPane.showMessageDialog(null, "Please enter a host URL!");
            }
        }else if(e.getActionCommand().equals("pause")){
            crawler.stopIWSpider();
            controlButton.setActionCommand("resume");
            controlButton.setText("Resume");
        }else if(e.getActionCommand().equals("resume")){
            crawler.resumeIWSpider();
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
        }else if(e.getActionCommand().equals("stop")){
            crawler.killIWSpider();
            controlButton.setActionCommand("start");
            controlButton.setText("Start");
            stopButton.setEnabled(false);
            if(Settings.BACK_BUTTON)actions.getBacker().setEnabled(true);
        }
    }

    /**
     * function starts crawling input url
     * @param url the base url
     */
    public void startSpider(String url){
        crawler.startIWSpider(url);
    }
    
    /**
     * reset GUI buttons to orignal state
     */
    public void resetButtons(){
        if(Settings.GUI){
            controlButton.setActionCommand("start");
            controlButton.setText("Start");
            stopButton.setEnabled(false);
            actions.getBacker().setEnabled(true);
        }
    }

    // Statistics elements
    /**
     * Update GUI statistic information
     */
    public void updateStats(){
    	if (crawler.isRunning() && Settings.GUI){
            stats_status.setText("Status : " + crawler.getStatus());
            stats_good.setText("Local Links : " + crawler.getLocalLinksCount());
            stats_bad.setText("Dead Links : " + crawler.getDeadLinksCount());
            stats_internal.setText("Non-Parsable Links : " + crawler.getNonParsableLinksCount());
            stats_external.setText("External Links : " + crawler.getExternalLinksCount());
            stats_disallowed.setText("Disallowed Links : " + crawler.getDisallowedLinksCount());
        }
    }
    /**
     * status label setter
     * @param stats_status
     */
    public void initStatus(JLabel stats_status){
        this.stats_status = stats_status;
    }

    /**
     * good urls label setter
     * @param stats_good
     */
    public void initGood(JLabel stats_good){
        this.stats_good = stats_good;
    }

    /**
     * bad labels setter
     * @param stats_bad
     */
    public void initBad(JLabel stats_bad){
        this.stats_bad = stats_bad;
    }

    /**
     * internal links label setter
     * @param stats_internal
     */
    public void initInternal(JLabel stats_internal){
        this.stats_internal = stats_internal;
    }

    /**
     * external link label setter
     * @param stats_external
     */
    public void initExternal(JLabel stats_external){
        this.stats_external = stats_external;
    }

    /**
     * disallowed link label setter
     * @param stats_disallowed
     */
    public void initDisallowed(JLabel stats_disallowed){
        this.stats_disallowed = stats_disallowed;
    }

    //ELEMENTS
    /**
     * control button setter
     * @param controlButton
     */
    public void initContoller(JButton controlButton){
        this.controlButton = controlButton;
    }

    /**
     * kill button setter
     * @param stopButton
     */
    public void initStopper(JButton stopButton){
        this.stopButton = stopButton;
    }

    /**
     * baseurl textfield setter
     * @param baseurlField
     */
    public void initBaseText(JTextField baseurlField){
        this.baseurlField = baseurlField;
    }

}
