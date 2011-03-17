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
public class IndexerPanel extends JPanel{
    SpiderActions actions;

    public IndexerPanel(SpiderActions actions){
        this.actions = actions;
        init();
        actions.log("keywordIndexer Initialized, Enter Browse for URL List and start.");
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
            panel.setLayout(new GridLayout(5,0));

            JLabel stats_status = new JLabel("");
            panel.add(stats_status);
            actions.getIndexerActions().initStatus(stats_status);

            JLabel stats_totalurls = new JLabel("");
            panel.add(stats_totalurls);
            actions.getIndexerActions().initTotalurls(stats_totalurls);

            JLabel stats_currenturl = new JLabel("");
            panel.add(stats_currenturl);
            actions.getIndexerActions().initCurrenturl(stats_currenturl);

            JLabel stats_keywordsindexed = new JLabel("");
            panel.add(stats_keywordsindexed);
            actions.getIndexerActions().initKeywordsindexed(stats_keywordsindexed);

            actions.getIndexerActions().updateStats();
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
            TitledBorder title = BorderFactory.createTitledBorder("Search Settings");
            panel.setBorder(title);
            panel.setLayout(new GridLayout(2, 1));

            JLabel urllistLabel = new JLabel("URL List : Please browse for URL List");
            JButton urllistButton = new JButton("...");
            urllistButton.setActionCommand("browseurllist");
            urllistButton.addActionListener(actions.getIndexerActions());
            actions.getIndexerActions().initurllistLabel(urllistLabel);
            actions.getIndexerActions().initurllistButton(urllistButton);

            panel.add(urllistLabel);
            panel.add(urllistButton);
        return panel;
    }

    private JPanel controlPanel(){
        JPanel panel = new JPanel();
            TitledBorder title = BorderFactory.createTitledBorder("Control Panel");
            panel.setBorder(title);
            panel.setLayout(new GridLayout(1, 2));

            JButton controlButton = new JButton("Start");
            controlButton.setActionCommand("start");
            controlButton.addActionListener(actions.getIndexerActions());
            actions.getIndexerActions().initContoller(controlButton);
            panel.add(controlButton);

            JButton stopButton = new JButton("Stop");
            stopButton.setEnabled(false);
            stopButton.setActionCommand("stop");
            stopButton.addActionListener(actions.getIndexerActions());
            actions.getIndexerActions().initStopper(stopButton);
            panel.add(stopButton);

            JButton backButton = new JButton("Back");
            backButton.setActionCommand("back");
            backButton.addActionListener(actions);
            actions.initBacker(backButton);
            panel.add(backButton);
        return panel;
    }
}
