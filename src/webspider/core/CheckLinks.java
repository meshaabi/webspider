package webspider.core;
@SuppressWarnings("serial")
public class CheckLinks implements Runnable, IWSpiderAPI {
	/**
	 * Called when the begin or cancel buttons are clicked
	 * 
	 * @param event The event associated with the button.
	 */
	@SuppressWarnings("deprecation")
	void begin_actionPerformed(java.awt.event.ActionEvent event)
	{
		if ( this.backgroundThread==null ) 
		{
			  this.backgroundThread = new Thread(this);
			  this.backgroundThread.start();
			  this.goodLinksCount=0;
			  this.badLinksCount=0;
		} else 
		{
			this.spider.stop();
		}
	
	}
	
	/**
	 * Perform the background thread operation. This method
	 * actually starts the background thread.
	 */
	@Override
	public void run()
	{
		try 
		{
			this.errors.setText("");
			this.base = new URL(this.url.getText());
			this.spider = new Spider(this.base);
			
			this.spider.start();
			Runnable doLater = new Runnable()
			{
				@Override
				public void run()
				{
					CheckLinks.this.begin.setText("Begin");
				}
			};
			
			SwingUtilities.invokeLater(doLater);
		
		} catch ( MalformedURLException e ) 
		{
			this.backgroundThread=null;
			UpdateErrors err = new UpdateErrors();
			err.msg = "Bad address.";
			SwingUtilities.invokeLater(err);
		
		}
	}
	
	/**
	 * Called by the spider when a URL is found. It is here
	 * that links are validated.
	 * 
	 * @param base The page that the link was found on.
	 * @param url The actual link address.
	 */
	@Override
	public boolean spiderFoundURL(URL base,URL url)
	{
		UpdateCurrentStats cs = new UpdateCurrentStats();
		cs.msg = url.toString();
		SwingUtilities.invokeLater(cs);
		
		if ( !checkLink(url) ) 
		{
			UpdateErrors err = new UpdateErrors();
			err.msg = url+"(on page " + base + ")\n";
			SwingUtilities.invokeLater(err);
			this.badLinksCount++;
			return false;
		}
		
		this.goodLinksCount++;
		if ( !url.getHost().equalsIgnoreCase(base.getHost()) )
		  return false;
		else
		  return true;
	  }
	
	/**
	 * Called when a URL error is found
	 * 
	 * @param url The URL that resulted in an error.
	 */
	@Override
	public void spiderURLError(URL url)
	{
	}
	
	/**
	 * Called internally to check whether a link is good
	 * 
	 * @param url The link that is being checked.
	 * @return True if the link was good, false otherwise.
	 */
	protected boolean checkLink(URL url)
	{
		try 
		{
			URLConnection connection = url.openConnection();
			connection.connect();
			return true;
		} catch ( IOException e ) 
		{
		  return false;
		}
	}
	
	/**
	 * Called when the spider finds an e-mail address
	 * 
	 * @param email The email address the spider found.
	 */
	  
	@Override
	public void spiderFoundEMail(String email)
	{
	}
	
	/**
	 * Used to update the current status information
	 * in a "Thread-Safe" way
	 * 
	 * @author Jeff Heaton
	 * @version 1.0
	 */
        
}