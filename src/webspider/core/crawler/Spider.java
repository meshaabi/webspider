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

    @Override
	public void openUserInterface() {
		Settings.BACK_BUTTON = false;
                this.actions.setPanel(new CrawlPanel(this.actions));
                this.actions.openInterface();
	}

	@Override
	public void closeUserInterface() {
		this.actions.closeInterface();
	}

	@Override
	public void startIWSpider(String mySeed) {
		try {
			this.spider = new SpiderImpl(new URL(mySeed), this.actions);
			this.spider.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

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

	@Override
	public void stopIWSpider() {
		checkInit();
		this.spider.stop();
	}

	@Override
	public void resumeIWSpider() {
		checkInit();
		this.spider.start();
	}

	@Override
	public void killIWSpider() {
		checkInit();
		try {
			this.spider.stop();
			this.spider.printToFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}

	


    // Status update functions
    public String getStatus() {
    	checkInit();
    	return this.spider.getStatus();
    }

    public int getLocalLinksCount() {
    	checkInit();
    	return this.spider.getLocalLinks().size();
    }

    public int getDeadLinksCount() {
    	checkInit();
    	return this.spider.getDeadLinks().size();
    }

    public int getNonParsableLinksCount() {
    	checkInit();
    	return this.spider.getNonParsableLinks().size();
    }

    public int getExternalLinksCount() {
    	checkInit();
    	return this.spider.getExternalLinks().size();
    }

    public int getDisallowedLinksCount() {
    	checkInit();
    	return this.spider.getDisallowedLinks().size();
    }
    public boolean isRunning(){
    	return this.spider.isRunning();
    }
    private void checkInit(){
//    	if (this.spider == null){
//			throw new IllegalStateException("Spider hasn't been initialized. Call startIWSpider first");
//		}
    	//use defautl values
    }

	@Override
	public Collection<URL> getNonParsableIWURLs() {
		return this.spider.getNonParsableLinks().getLinks();
	}

	@Override
	public Collection<URL> getDeadIWURLs() {
		return this.spider.getDeadLinks().getLinks();

	}

	@Override
	public Collection<URL> getDisallowedIWURLs() {
		return this.spider.getDisallowedLinks().getLinks();

	}

	@Override
	public Collection<URL> getLocalIWUrls() {
		return this.spider.getLocalLinks().getLinks();

	}

	@Override
	public Collection<URL> getExternalIWURLs() {
		return this.spider.getExternalLinks().getLinks();

	}
}
