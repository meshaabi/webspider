/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider;
import java.io.File;
import webspider.actions.SpiderActions;
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
            displayGuide(args);
            e.printStackTrace();
        }
    }

    private static void run(String[] args) throws ArrayIndexOutOfBoundsException{
        if(args[0].equals("-g") ){
            Settings.GUI = true;
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
            }else if(args[0].equals("-cli")){
                    if(args[1].equals("c")){
                    }else if(args[1].equals("i")){
                    }else if(args[1].equals("s")){
                    }
            }
        }
    }

    private static void displayGuide(String[] args){
        for(String arg : args){
            System.out.println(arg);
        }
        System.out.println("Incorrect Command Line Arguments");
    }

   

}
