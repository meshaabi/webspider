/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import webspider.listners.SpiderActions;

/**
 *
 * @author esh
 */
public class CPanel extends JPanel{
    SpiderActions actions;
    public CPanel(Container frame, SpiderActions actions){
        this.actions = actions;
        init();
        frame.add(this, BorderLayout.SOUTH);
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
            TitledBorder title = BorderFactory.createTitledBorder("CPanel");
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
        return panel;
    }
}
