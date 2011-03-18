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

    /**
     * analizes the command line parameters and runs appropriate application mode
     * @param args the command line arguments
     */
    private static void run(String[] args) throws ArrayIndexOutOfBoundsException{
        if(args.length == 0 || args[0].equals("-g")){
            Settings.GUI = true;
            MainGUI gui = new MainGUI(actions);
            if(args.length >= 1){
                if(args[1].equals("c")){
                    actions.getCrawler().openUserInterface();
                }else if(args[1].equals("i")){
                    actions.getIndexer().openUserInterface();
                }else if(args[1].equals("s")){
                    Settings.BACK_BUTTON = false;
                    actions.setPanel(new SearchPanel(actions));
                }
            }
        }else if(args[0].equals("-cli")){
            if(args[1].equals("c")){
                actions.getCrawlerActions().startSpider(args[2]);
            }else if(args[1].equals("i")){
                actions.getIndexerActions().startIndexer(args[2]);
            }else if(args[1].equals("s")){
                actions.getSearchActions().startSearch(args[2], args[3]);
            }
        }
        initOutputFolder();
    }

    /**
     * Display user help, on invalid parameters
     */
    private static void displayGuide(String[] args){
        System.out.println("Incorrect Command Line Arguments");
    }

    /**
     * Checks if output folder exists else creates it.
     */
    private static void initOutputFolder(){
        File f = new File("./output/spider");
        actions.log("Checking Output Directories");
        if(!f.exists()) {
            actions.log("Output Directories not found, Building Path : " + f.getAbsolutePath());
            f.mkdirs();
        }else{
            actions.log("Directories exist, Application ready!");
        }
    }

}
