package webspider;

/**
 *
 * @author esh
 */
public class Settings {
    /**
     * GUI windows default height
     */
    public static final int WINDOW_HEIGHT = 600;
    /**
     * gui window default width
     */
    public static final int WINDOW_WIDTH = 800;
    /**
     * Log date format
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * Log time format
     */
    public static final String TIME_FORMAT = "HH:mm:ss";
    /**
     * Indexeer output file extension
     */
    public static final String FILE_INDEX_EXTENSION = ".bdmi";
    /**
     * Default search keyword for textbox
     */
    public static final String DEFAULT_KEYWORD = "something";
    /**
     * Switch for UI type, to display exit button or Back to menu button
     */
    public static boolean BACK_BUTTON = true;
    /**
     * Switch to check whether if GUI is loaded or software running through command line
     */
    public static boolean GUI = false;
    /**
     * Default output directory
     */
    
    //Crawler Constants
    /**
     *
     */
    public static final String DEFAULT_URL = "http://poplar.dcs.shef.ac.uk/~u0082/intelweb2/";
    /**
     * URL for robots.txt
     */
    public static final String DEFAULT_ROBOTS_TXT_URL = "http://poplar.dcs.shef.ac.uk/~u0082/intelweb2/robots.txt";
    /**
     * file extension used by crawler
     */
    public static final String CRAWLER_EXTENSION = ".bdmc";
    /**
     * output file path
     */
    public static final String DEFAULT_PATH = "./output/spider/";
    //Indexer Contants
    /**
     * path of file o
     */
    public static final String STOPFILE_NAME = "./stopfile.txt";
}
