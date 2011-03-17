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
public class SearchPanel extends JPanel{
    SpiderActions actions;

    public SearchPanel(SpiderActions actions){
        this.actions = actions;
        init();
        actions.log("keywordAnalizer Initialized, Enter keyword and press Find.");
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

            JLabel stats_good = new JLabel("");
            panel.add(stats_good);
            actions.getIndexerActions().initGood(stats_good);

            JLabel stats_bad = new JLabel("");
            panel.add(stats_bad);
            actions.getIndexerActions().initBad(stats_bad);

            JLabel stats_internal = new JLabel("");
            panel.add(stats_internal);
            actions.getIndexerActions().initInternal(stats_internal);

            JLabel stats_external = new JLabel("");
            panel.add(stats_external);
            actions.getIndexerActions().initExternal(stats_external);

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
            panel.setLayout(new GridLayout(2, 2));

            JLabel keywordLabel = new JLabel("Keyword :");
            JTextField baseurl = new JTextField(Settings.DEFAULT_KEYWORD);
            actions.getSearchActions().initBaseText(baseurl);

            panel.add(keywordLabel);
            panel.add(baseurl);
        return panel;
    }

    private JPanel controlPanel(){
        JPanel panel = new JPanel();
            TitledBorder title = BorderFactory.createTitledBorder("Control Panel");
            panel.setBorder(title);
            panel.setLayout(new GridLayout(1, 2));

            JButton controlButton = new JButton("Find");
            controlButton.setActionCommand("find");
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
