/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eca08zb;

/**
 *
 * @author esh
 */
public class Settings {
    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 800;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String FILE_INDEX_EXTENSION = ".bdmi";
    public static final String DEFAULT_KEYWORD = "something";
    public static boolean BACK_BUTTON = true;
    public static boolean NO_GUI = false;
    //Crawler Constants
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
	public static final String CRAWLER_PATH = "./output/spider/";

    //Indexer Contants
    public static final String STOPFILE_NAME = "./stopfile.txt";
}
