/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider;
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
            System.out.println("Invalid Command Line Params");
        }else if(args[0].equals("-g") ){
            MainGUI gui = new MainGUI(actions);
            if(args.length == 2){
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
                gui.run();
            }else if(args[0].equals("-cli")){
                Settings.NO_GUI = true;
                try{
                    if(args[1].equals("c")){
                    }else if(args[1].equals("i")){
                    }else if(args[1].equals("s")){
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

}
