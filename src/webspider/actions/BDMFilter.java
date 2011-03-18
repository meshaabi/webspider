package webspider.actions;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author esh
 */
public class BDMFilter extends FileFilter{
    String extension;
    /**
     * constructs BDMFiler to accept file with required extension
     * @param extension
     */
    public BDMFilter(String extension){
        this.extension = extension;
    }

    /**
     * Checks if file is of required extension
     * @param f
     * @return
     */
    @Override
    public boolean accept(File f) {
        return (f.getName().endsWith(extension));
    }

    /**
     * Returns description of allowed file
     * @return
     */
    @Override
    public String getDescription() {
        return extension;
    }

}
