/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.gui;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import webspider.actions.SpiderActions;

/**
 *
 * @author esh
 */
public class OptionsPanel extends JPanel{
    SpiderActions actions;
    public OptionsPanel(SpiderActions actions){
        this.actions = actions;
        init();
        actions.log("Waiting for user Input");
    }

    private void init() {
        setLayout(new FlowLayout());
        build();
    }

    private void build(){
        TitledBorder title = BorderFactory.createTitledBorder("Please Select an Option (Mouseover Button for more info)");
        setBorder(title);

        JButton crawlButton = new JButton("Crawl Website");
        crawlButton.setToolTipText("Crawls and Analyzes website and Stores statistics to a file");
        crawlButton.setActionCommand("optioncrawl");
        crawlButton.addActionListener(actions);
        add(crawlButton);

        JButton keywordButton = new JButton("Analyze Keywords");
        keywordButton.setToolTipText("Analizes crawled website from a file and generates and keyword index");
        keywordButton.setActionCommand("optionkeyword");
        keywordButton.addActionListener(actions);
        add(keywordButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setToolTipText("Exit");
        exitButton.setActionCommand("exit");
        exitButton.addActionListener(actions);
        add(exitButton);
    }
}
