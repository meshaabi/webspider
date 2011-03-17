/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.actions;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author esh
 */
public class BDMFilter extends FileFilter{
    String extension;
    public BDMFilter(String extension){
        this.extension = extension;
    }

    @Override
    public boolean accept(File f) {
        return (f.getName().endsWith(extension));
    }

    @Override
    public String getDescription() {
        return extension;
    }

}
