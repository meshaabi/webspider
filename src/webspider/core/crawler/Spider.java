package webspider.core.crawler;

import static webspider.Settings.DEFAULT_URL;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import webspider.Settings;
import webspider.actions.SpiderActions;
import webspider.gui.CrawlPanel;

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

    /**
     * Opens the user interface for the crawler
     */
    @Override
	public void openUserInterface() {
		Settings.BACK_BUTTON = false;
                this.actions.setPanel(new CrawlPanel(this.actions));
                this.actions.openInterface();
	}

    /**
     * Closes the user interface for the clawer
     */
	@Override
	public void closeUserInterface() {
		this.actions.closeInterface();
	}

	/**
	 * Starts the crawler with an initial see
	 * @param mySeed the initial site url the web crawler should crawl
	 */
	@Override
	public void startIWSpider(String mySeed) {
		try {
			this.spider = new SpiderImpl(new URL(mySeed), this.actions);
			this.spider.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Checks that a given url is permitted for parsing by robots.txt
	 * @param myUrl
	 */
	@Override
	public boolean isIWRobotSafe(String myUrl) {
		try {
			return this.spider.isRobotAllowed(new URL(myUrl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Pauses the spider
	 */
	@Override
	public void stopIWSpider() {
		this.spider.stop();
	}

	/**
	 * Resumes the spider
	 */
	@Override
	public void resumeIWSpider() {
		this.spider.start();
	}

	/**
	 * Kills the spider permanently
	 */
	@Override
	public void killIWSpider() {
		try {
			this.spider.stop();
			this.spider.printToFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

    // Status update functions
    /**
     * Gets the current status of the spider
     */
	public String getStatus() {
    	return this.spider.getStatus();
    }

	/**
	 * Gets the number of local links this spider has found
	 * @return the links count
	 */
    public int getLocalLinksCount() {
    	return this.spider.getLocalLinks().size();
    }

    /**
     * Gets the number of dead links this spider has found
     * @return the links count
     */
    public int getDeadLinksCount() {
    	return this.spider.getDeadLinks().size();
    }
    /**
	 * Gets the number of non parsable links this spider has found
	 * @return the links count
	 */
    public int getNonParsableLinksCount() {
    	return this.spider.getNonParsableLinks().size();
    }
    /**
	 * Gets the number of external links this spider has found
	 * @return the links count
	 */
    public int getExternalLinksCount() {
    	return this.spider.getExternalLinks().size();
    }
    /**
	 * Gets the number of disallowed links this spider has found
	 * @return the links count
	 */
    public int getDisallowedLinksCount() {
    	return this.spider.getDisallowedLinks().size();
    }
    /**
     * Is the spider currentl running?
     * @return
     */
    public boolean isRunning(){
    	return this.spider.isRunning();
    }


    /**
     * Gets all the non parsable URLs
     */
	@Override
	public Collection<URL> getNonParsableIWURLs() {
		return this.spider.getNonParsableLinks().getLinks();
	}

	/**
     * Gets all the dead URLs
     */
	@Override
	public Collection<URL> getDeadIWURLs() {
		return this.spider.getDeadLinks().getLinks();

	}

	/**
     * Gets all the disallowed URLs
     */
	@Override
	public Collection<URL> getDisallowedIWURLs() {
		return this.spider.getDisallowedLinks().getLinks();

	}

	/**
     * Gets all the local URLs
     */
	@Override
	public Collection<URL> getLocalIWUrls() {
		return this.spider.getLocalLinks().getLinks();

	}

	/**
     * Gets all the external URLs
     */
	@Override
	public Collection<URL> getExternalIWURLs() {
		return this.spider.getExternalLinks().getLinks();

	}
}
