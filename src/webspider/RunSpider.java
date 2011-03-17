/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider;
import webspider.actions.SpiderActions;
import webspider.gui.MainGUI;

/**
 *
 * @author esh
 */
public class RunSpider {
    //hello
    private static SpiderActions actions = new SpiderActions();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.out.println(args.toString());
        MainGUI gui = new MainGUI(actions);
        gui.run();
    }

}
