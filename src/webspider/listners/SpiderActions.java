/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package webspider.listners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import webspider.Settings;

/**
 *
 * @author esh
 */
public class SpiderActions implements ActionListener{
    private JTextArea log;
    private JScrollPane scroll;
    private JButton controlButton;
    private JButton stopButton;
    private JPanel cpanel;

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("start")){
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
            stopButton.setEnabled(true);
        }else if(e.getActionCommand().equals("pause")){
            controlButton.setActionCommand("resume");
            controlButton.setText("Resume");
        }else if(e.getActionCommand().equals("resume")){
            controlButton.setActionCommand("pause");
            controlButton.setText("Pause");
        }else if(e.getActionCommand().equals("stop")){
            controlButton.setActionCommand("start");
            controlButton.setText("Start");
            stopButton.setEnabled(false);
        }
    }

    //Control Panel
    public void setCPanel(JPanel cpanel){
        if(cpanel != null) cpanel.removeAll();
        this.cpanel = cpanel;
    }

    // Button stuff
    public void initContoller(JButton controlButton){
        this.controlButton = controlButton;
    }

    public void initStopper(JButton stopButton){
       this.stopButton = stopButton;
    }

    /* LOGGER FUNCTIONS */
    public void initLogger(JTextArea log){
        this.log = log;
        log("webSpider v1.0 : Team BDS", Settings.DATE_FORMAT);
    }

    public void initScroll(JScrollPane scroll){
        this.scroll = scroll;
    }

    public void log(String text, String calFormat){
        log.append(time(calFormat) + " : " + text + "\n");
        scrollToBottom();
    }

    public void log(String text){
        log.append(time(Settings.TIME_FORMAT) + " : " + text + "\n");
        scrollToBottom();
    }

    private void scrollToBottom(){
        scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
    }

    private String time(String format){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }
    /* END */
}
