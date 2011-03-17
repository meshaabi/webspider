/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.actions;

import java.io.File;
import javax.swing.filechooser.FileFilter;
import webspider.Settings;

/**
 *
 * @author esh
 */
public class BDMFilter extends FileFilter{
    @Override
    public boolean accept(File f) {
        return (f.getName().endsWith(Settings.FILE_EXTENSION));
    }

    @Override
    public String getDescription() {
        return Settings.FILE_EXTENSION;
    }

}
