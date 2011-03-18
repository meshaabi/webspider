package webspider.core.crawler;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import webspider.Settings;
import webspider.gui.CrawlPanel;
import webspider.actions.SpiderActions;
import static webspider.Settings.*;

/**
 * Spider interface to be accessed by GUI
 * @author Zsolt Bitvai, Shaabi Mohammed
 *
 */
public class Spider implements myIWSpider {
    private SpiderImpl spider;
    private SpiderActions actions;

    public Spider(SpiderActions actions) {
        this.actions = actions;
		try {
			this.spider = new SpiderImpl(new URL(DEFAULT_URL), this.actions);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    }

<<<<<<< HEAD
    /**
     * Opens the graphical user interface
     */
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
    @Override
	public void openUserInterface() {
		Settings.BACK_BUTTON = false;
                actions.setPanel(new CrawlPanel(actions));
                actions.openInterface();
	}

<<<<<<< HEAD
    /**
     * Closes the graphical user interface
     */
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	@Override
	public void closeUserInterface() {
		actions.closeInterface();
	}

<<<<<<< HEAD
	/**
	 * Starts running the spider with a seed
	 */
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	@Override
	public void startIWSpider(String mySeed) {
		try {
			this.spider = new SpiderImpl(new URL(mySeed), this.actions);
			this.spider.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

<<<<<<< HEAD
	/**
	 * Checks that a given url is safe to process given the robots.txt
	 */
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	@Override
	public boolean isIWRobotSafe(String myUrl) {
		checkInit();
		try {
			return this.spider.isRobotAllowed(new URL(myUrl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

<<<<<<< HEAD
	/**
	 * Pauses the spider 
	 */
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	@Override
	public void stopIWSpider() {
		checkInit();
		this.spider.stop();
	}

<<<<<<< HEAD
	/**
	 * Resumes the Spider
	 */
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	@Override
	public void resumeIWSpider() {
		checkInit();
		this.spider.start();
	}

<<<<<<< HEAD
	/**
	 * Kills the spider
	 */
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	@Override
	public void killIWSpider() {
		checkInit();
		try {
			this.spider.stop();
			this.spider.printToFile();
			this.spider = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

<<<<<<< HEAD
	/**
	 * Gets all the processed local urls
	 */
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	@Override
	public String[] getLocalIWUrls() {
		checkInit();
		List<String> stringURLs = new ArrayList<String>();
		for (URL url : this.spider.getLocalLinks()){
			stringURLs.add(url.toString());
		}
		return stringURLs.toArray(new String[]{});
	}

<<<<<<< HEAD
	/**
	 * Gets all teh external urls
	 */
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
	@Override
	public String[] getExternalIWURLs() {
		checkInit();
		List<String> stringURLs = new ArrayList<String>();
		for (URL url : this.spider.getExternalLinks()){
			stringURLs.add(url.toString());
		}
		return stringURLs.toArray(new String[]{});
	}

<<<<<<< HEAD
	
=======
>>>>>>> 2be8f5d2438dfa7be8f2dd7388c413ae9ec873b9
    // Status update functions
    public String getStatus() {
    	checkInit();
    	return this.spider.getStatus();
    }

    public int getLocalLinks() {
    	checkInit();
    	return this.spider.getLocalLinks().size();
    }

    public int getDeadLinks() {
    	checkInit();
    	return this.spider.getDeadLinks().size();
    }

    public int getNonParsableLinks() {
    	checkInit();
    	return this.spider.getNonParsableLinks().size();
    }

    public int getExternalLinks() {
    	checkInit();
    	return this.spider.getExternalLinks().size();
    }

    public int getDisallowedLinks() {
    	checkInit();
    	return this.spider.getDisallowedLinks().size();
    }
    
    private void checkInit(){
//    	if (this.spider == null){
//			throw new IllegalStateException("Spider hasn't been initialized. Call startIWSpider first");
//		}
    	//use defautl values
    }
}
