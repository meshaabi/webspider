/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import webspider.Settings;
import webspider.actions.SpiderActions;

/**
 *
 * @author esh
 */
public class CrawlPanel extends JPanel{
    SpiderActions actions;

    public CrawlPanel(SpiderActions actions){
        this.actions = actions;
        init();
        actions.log("webCrawler Initialized, Press start to begin.");
    }

    private void init(){
        setLayout(new GridLayout());
        add(statisticsPanel(), BorderLayout.WEST);
        add(eastPanel(), BorderLayout.EAST);
    }

    private JPanel statisticsPanel(){
        JPanel panel = new JPanel();
            TitledBorder title = BorderFactory.createTitledBorder("Statistics");
            panel.setBorder(title);
            panel.setLayout(new GridLayout(6,0));

            JLabel stats_status = new JLabel("");
            panel.add(stats_status);
            actions.getCrawlerActions().initStatus(stats_status);

            JLabel stats_good = new JLabel("");
            panel.add(stats_good);
            actions.getCrawlerActions().initGood(stats_good);

            JLabel stats_bad = new JLabel("");
            panel.add(stats_bad);
            actions.getCrawlerActions().initBad(stats_bad);

            JLabel stats_internal = new JLabel("");
            panel.add(stats_internal);
            actions.getCrawlerActions().initInternal(stats_internal);

            JLabel stats_external = new JLabel("");
            panel.add(stats_external);
            actions.getCrawlerActions().initExternal(stats_external);

            JLabel stats_disallowed = new JLabel("");
            panel.add(stats_disallowed);
            actions.getCrawlerActions().initDisallowed(stats_disallowed);

            actions.getCrawlerActions().updateStats();
        return panel;
    }

    private JPanel eastPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(inputPanel(), BorderLayout.NORTH);
        panel.add(controlPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel inputPanel(){
        JPanel panel = new JPanel();
            TitledBorder title = BorderFactory.createTitledBorder("Crawler Settings");
            panel.setBorder(title);
            panel.setLayout(new GridLayout(2, 2));

            JLabel baseurlLabel = new JLabel("BaseURL :");
            JTextField baseurl = new JTextField(Settings.DEFAULT_URL);
            actions.getCrawlerActions().initBaseText(baseurl);
            
            panel.add(baseurlLabel);
            panel.add(baseurl);
        return panel;
    }

    private JPanel controlPanel(){
        JPanel panel = new JPanel();
            TitledBorder title = BorderFactory.createTitledBorder("Control Panel");
            panel.setBorder(title);
            panel.setLayout(new GridLayout(1, 2));

            JButton controlButton = new JButton("Start");
            controlButton.setActionCommand("start");
            controlButton.addActionListener(actions.getCrawlerActions());
            actions.getCrawlerActions().initContoller(controlButton);
            panel.add(controlButton);

            JButton stopButton = new JButton("Stop");
            stopButton.setEnabled(false);
            stopButton.setActionCommand("stop");
            stopButton.addActionListener(actions.getCrawlerActions());
            actions.getCrawlerActions().initStopper(stopButton);
            panel.add(stopButton);

            JButton backButton = new JButton("Back");
            backButton.setActionCommand("back");
            backButton.addActionListener(actions);
            actions.initBacker(backButton);
            panel.add(backButton);
        return panel;
    }
}
