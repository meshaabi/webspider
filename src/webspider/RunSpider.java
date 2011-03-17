/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider;
import webspider.actions.CrawlerActions;
import webspider.actions.SpiderActions;
import webspider.gui.CrawlPanel;
import webspider.gui.IndexerPanel;
import webspider.gui.MainGUI;
import webspider.gui.SearchPanel;

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
        if(args.length == 0){
            Settings.NO_GUI = true;
        }else if(args[0].equals("-g") ){
            MainGUI gui = new MainGUI(actions);
            if(args[1].equals("c")){
                Settings.BACK_BUTTON = false;
                actions.setPanel(new CrawlPanel(actions));
            }else if(args[1].equals("i")){
                Settings.BACK_BUTTON = false;
                actions.setPanel(new IndexerPanel(actions));
            }else if(args[1].equals("s")){
                Settings.BACK_BUTTON = false;
                actions.setPanel(new SearchPanel(actions));
            }
        }
        
    }

}
