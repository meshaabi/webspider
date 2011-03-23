package webspider.actions;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * File extension filter for the spider file browser
 * '.bdmc' used by the crawler
 * '.bdmi' used by the indexer
 * @author Shaabi Mohammed
 */
public class BDMFilter extends FileFilter{
    String extension;
    /**
     * constructs BDMFiler to accept file with required extension
     * @param extension the extension to filter
     */
    public BDMFilter(String extension){
        this.extension = extension;
    }

    /**
     * Checks if file is of required extension
     */
    @Override
    public boolean accept(File f) {
        return (f.getName().endsWith(extension));
    }

    /**
     * Returns description of allowed file
     */
    @Override
    public String getDescription() {
        return extension;
    }

}
