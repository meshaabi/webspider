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
 * Crawler interface to be accessed by GUI and applications
 * @author Zsolt Bitvai, Shaabi Mohammed
 *
 */
public class Crawler implements myIWSpider {
    private CrawlerImpl crawler;
    private SpiderActions actions;

    public Crawler(SpiderActions actions) {
        this.actions = actions;
        try {
			this.crawler = new CrawlerImpl(new URL(DEFAULT_URL), actions);
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
			this.crawler = new CrawlerImpl(new URL(mySeed), this.actions);
			this.crawler.start();
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
			return this.crawler.isRobotAllowed(new URL(myUrl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the crawl delay between urls to read
	 * @return
	 */
	public long getCrawlDelay(){
		return this.crawler.getCrawlDelay();
	}

	/**
	 * Pauses the crawler
	 */
	@Override
	public void stopIWSpider() {
		
		this.crawler.stop();
	}

	/**
	 * Resumes the crawler
	 */
	@Override
	public void resumeIWSpider() {
		this.crawler.start();
	}

	/**
	 * Kills the crawler permanently
	 */
	@Override
	public void killIWSpider() {
		try {
			this.crawler.stop();
			this.crawler.printToFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

    // Status update functions
    /**
     * Gets the current status of the crawler
     */
	public String getStatus() {
    	return this.crawler.getStatus();
    }

	/**
	 * Gets the number of local links this crawler has found
	 * @return the links count
	 */
    public int getLocalLinksCount() {
    	return this.crawler.getLocalLinks().size();
    }

    /**
     * Gets the number of dead links this crawler has found
     * @return the links count
     */
    public int getDeadLinksCount() {
    	return this.crawler.getDeadLinks().size();
    }
    /**
	 * Gets the number of non parsable links this crawler has found
	 * @return the links count
	 */
    public int getNonParsableLinksCount() {
    	return this.crawler.getNonParsableLinks().size();
    }
    /**
	 * Gets the number of external links this crawler has found
	 * @return the links count
	 */
    public int getExternalLinksCount() {
    	return this.crawler.getExternalLinks().size();
    }
    /**
	 * Gets the number of disallowed links this crawler has found
	 * @return the links count
	 */
    public int getDisallowedLinksCount() {
    	return this.crawler.getDisallowedLinks().size();
    }
    /**
     * Is the crawler currently running?
     * @return
     */
    public boolean isRunning(){
    	return this.crawler.isRunning();
    }


    /**
     * Gets all the non parsable URLs
     */
	@Override
	public Collection<URL> getNonParsableIWURLs() {
		return this.crawler.getNonParsableLinks().getLinks();
	}

    /**
     * Gets all the dead URLs
     */
	@Override
	public Collection<URL> getDeadIWURLs() {
		return this.crawler.getDeadLinks().getLinks();

	}

	/**
     * Gets all the disallowed URLs
     */
	@Override
	public Collection<URL> getDisallowedIWURLs() {
		return this.crawler.getDisallowedLinks().getLinks();

	}

	/**
     * Gets all the local URLs
     */
	@Override
	public Collection<URL> getLocalIWUrls() {
		return this.crawler.getLocalLinks().getLinks();

	}

	/**
     * Gets all the external URLs
     */
	@Override
	public Collection<URL> getExternalIWURLs() {
		return this.crawler.getExternalLinks().getLinks();

	}
}