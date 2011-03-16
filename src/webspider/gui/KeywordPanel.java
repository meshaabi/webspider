/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import webspider.actions.SpiderActions;

/**
 *
 * @author esh
 */
public class KeywordPanel extends JPanel{
    SpiderActions actions;

    public KeywordPanel(SpiderActions actions){
        this.actions = actions;
        init();
        actions.log("Please configure keyWord Analyzer");
    }

    private void init(){
        setLayout(new GridLayout());
        add(statisticsPanel(), BorderLayout.WEST);
        add(controlPanel(), BorderLayout.EAST);
    }

    private JPanel statisticsPanel(){
        JPanel panel = new JPanel();
            TitledBorder title = BorderFactory.createTitledBorder("Statistics");
            panel.setBorder(title);
        return panel;
    }

    private JPanel controlPanel(){
       JPanel panel = new JPanel();
       /*     TitledBorder title = BorderFactory.createTitledBorder("CPanel");
            panel.setBorder(title);
            panel.setLayout(new GridLayout(2, 2));

            JButton controlButton = new JButton("Start");
            controlButton.setActionCommand("start");
            controlButton.addActionListener(actions);
            actions.initContoller(controlButton);
            panel.add(controlButton);

            JButton stopButton = new JButton("Stop");
            stopButton.setEnabled(false);
            stopButton.setActionCommand("stop");
            stopButton.addActionListener(actions);
            actions.initStopper(stopButton);
            panel.add(stopButton);

            JButton backButton = new JButton("Back");
            backButton.setActionCommand("back");
            backButton.addActionListener(actions);
            actions.initBacker(backButton);
            panel.add(backButton);*/
        return panel;
    }
}
