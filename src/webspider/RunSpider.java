/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider;
import webspider.Settings;
import webspider.actions.SpiderActions;
import webspider.core.crawler.Spider;
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
        try{
            run(args);
        }catch (Exception e){
            displayGuide();
        }
    }

    private static void run(String[] args) throws ArrayIndexOutOfBoundsException{
        if(args[0].equals("-g") ){
            MainGUI gui = new MainGUI(actions);
            if(args.length == 2){
                if(args[1].equals("c")){
                    actions.getCrawler().openUserInterface();
                }else if(args[1].equals("i")){
                    actions.getIndexer().openUserInterface();
                }else if(args[1].equals("s")){
                    Settings.BACK_BUTTON = false;
                    actions.setPanel(new SearchPanel(actions));
                }
                gui.run();
            }else if(args[0].equals("-cli")){
                Settings.GUI = true;
                    if(args[1].equals("c")){
                    }else if(args[1].equals("i")){
                    }else if(args[1].equals("s")){
                    }
            }
        }
    }

    private static void displayGuide(){
        System.out.println("Inccorect Command Line Arguments");
    }

}
