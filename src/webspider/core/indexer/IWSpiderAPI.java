package webspider.core.indexer;

import java.net.*;

public interface IWSpiderAPI
{
    public boolean spiderFoundURL(URL base,URL url);
    public void spiderURLError(URL url);
    public void spiderFoundEMail(String email);
}