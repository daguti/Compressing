/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compressing.Threads;

import compressing.Utils.GzipUtils;
import compressing.Utils.ZipUtils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author ESa10969
 */
public class DecompressingTask extends Thread {
    String file;
    JProgressBar pbar;
    JFrame frame;
    JLabel processLab;
    
    public DecompressingTask(String file, JProgressBar pbar, JFrame frame, JLabel processLab) {
        this.file       = file;
        this.pbar       = pbar;
        this.frame      = frame;
        this.processLab = processLab;
    }
    
    public void run() {
        if(file.substring(file.lastIndexOf(".") + 1).equals("zip")) {
            try {
                ZipUtils.processLab = processLab;
                ZipUtils.decompress(file, pbar);
            } catch (IOException ex) {
                Logger.getLogger(DecompressingTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            GzipUtils.processLab = processLab;
            GzipUtils.decompress(file, pbar);
        }
        pbar.setValue(pbar.getMaximum());
        JOptionPane.showMessageDialog(frame, 
                                      "Successful", 
                                      "Successful", 
                                      JOptionPane.INFORMATION_MESSAGE);
        processLab.setText("");
        pbar.setValue(0);
    }
}
