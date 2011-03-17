package webspider.core.crawler;

import java.net.MalformedURLException;
import java.net.URL;


public class Spider implements myIWSpider {

	private SpiderImpl spider;
	@Override
	public void openUserInterface() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeUserInterface() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startIWSpider(String mySeed) {
		try {
			this.spider = new SpiderImpl(new URL(mySeed));
			this.spider.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean isIWRobotSafe(String myUrl) {
		try {
			return this.spider.isRobotAllowed(new URL(myUrl));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void stopIWSpider() {
		this.spider.stop();
	}

	@Override
	public void resumeIWSpider() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void killIWSpider() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getLocalIWUrls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getExternalIWURLs() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
