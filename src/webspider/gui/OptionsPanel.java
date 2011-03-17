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
        actions.log("Spider Main menu, waiting for user Input");
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

        JButton indexButton = new JButton("Index Keywords");
        indexButton.setToolTipText("Indexes keywords from crawled URLs");
        indexButton.setActionCommand("optionindex");
        indexButton.addActionListener(actions);
        add(indexButton);

        JButton searchButton = new JButton("Search");
        searchButton.setToolTipText("Searches for keywords from an Indexed Website");
        searchButton.setActionCommand("optionsearch");
        searchButton.addActionListener(actions);
        add(searchButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setToolTipText("Exit");
        exitButton.setActionCommand("exit");
        exitButton.addActionListener(actions);
        add(exitButton);
    }
}
