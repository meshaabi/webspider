package webspider.core.crawler;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import eca08zb.Settings;
import eca08zb.actions.SpiderActions;
import eca08zb.core.crawler.SpiderImpl;

import static eca08zb.core.crawler.SpiderImpl.*;


public class SpiderTest extends TestCase {
	
	SpiderImpl spider;
	
	
	@Override
	@Before
	public void setUp() throws Exception {
		this.spider = new SpiderImpl(new URL(Settings.DEFAULT_URL),new SpiderActions());
	}

	@Override
	@After
	public void tearDown() throws Exception {
		//empty
	}
	
	public void testSetRequestProperties() throws IOException
	{
		
			URL url = new URL(Settings.DEFAULT_URL);
			URLConnection connection = url.openConnection();
			this.spider.setRequestProperties(connection);
			assertEquals(USER_AGENT_VALUE,connection.getRequestProperty(USER_AGENT_FIELD));		
			
	}

	public void testPause() throws IOException, InterruptedException
	{
			final Thread spiderThread = new Thread(new Runnable(){

				@Override
				public void run() {
					SpiderTest.this.spider.start();
				}
				
			});	
			spiderThread.start();
			Thread.sleep(1000); // wait for other thread to finish
			SpiderTest.this.spider.stop();
			assertTrue(spiderThread.isInterrupted());
			
	}
	
	public void testInitDisallowedURLs() throws MalformedURLException{
		final URL notAllowedUrl = new URL("http://poplar.dcs.shef.ac.uk/~u0082/intelweb2/MAINTAINERS.txt".toLowerCase());

		Set<URL> disallowedURLs = this.spider.getRobotDisallowedURLs();
		assertTrue(disallowedURLs.contains(notAllowedUrl));
	}
	
	public void testIsRobotAllowed() throws MalformedURLException{
		assertTrue(this.spider.isRobotAllowed(new URL("http://google.com")));
		
		final URL notAllowedUrl = new URL("http://poplar.dcs.shef.ac.uk/~u0082/intelweb2/MAINTAINERS.txt".toLowerCase());
		assertFalse(this.spider.isRobotAllowed(notAllowedUrl));
	}
	
	public void testIsInternal() throws MalformedURLException{
		assertFalse(this.spider.isLocal(new URL("http://google.com")));
		final URL notAllowedUrl = new URL("http://poplar.dcs.shef.ac.uk/~u0082/intelweb2/MAINTAINERS.txt".toLowerCase());
		assertTrue(this.spider.isLocal(notAllowedUrl));
	}
}
