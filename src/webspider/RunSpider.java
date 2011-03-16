/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider;

import webspider.gui.MainGUI;
import webspider.actions.SpiderActions;

/**
 *
 * @author esh
 */
public class RunSpider {

    private static SpiderActions actions = new SpiderActions();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainGUI gui = new MainGUI(actions);
        gui.run();
    }

}
