/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import webspider.listners.SpiderActions;

/**
 *
 * @author esh
 */
public class Logger {
    JTextArea logText;
    JScrollPane log;

    Logger(Container frame, SpiderActions actions) {
        initLogger(actions);
        frame.add(log, BorderLayout.CENTER);
    }

    private void initLogger(SpiderActions actions){
        logText = new JTextArea();
        log = new JScrollPane(logText);
        
        logText.setFont(Font.decode("Monospace-12"));
        logText.setEditable(false);

        actions.initLogger(logText);
    }
}
