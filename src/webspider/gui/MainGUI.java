/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import webspider.Settings;
import webspider.actions.SpiderActions;

/**
 *
 * @author esh
 */
public final class MainGUI extends JFrame{
    SpiderActions actions;
    public MainGUI(SpiderActions actions){
        super("Java WebCrawler : Intelligent Web");
        this.actions = actions;
        init();
        build();
        pack();
        run();
    }

    public void run(){
        setVisible(true);
    }

    public void init() {
        actions.setFrame(this);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT));
    }

    public void build(){
        Container frame = getContentPane();
        setLayout(new BorderLayout());
        Logger log = new Logger(frame, actions); // constructor adds content to pane
        actions.setPanel(new OptionsPanel(actions));
    }
}
