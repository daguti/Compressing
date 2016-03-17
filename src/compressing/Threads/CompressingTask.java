/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compressing.Threads;

import compressing.Utils.GzipUtils;
import compressing.Utils.ZipUtils;
import java.io.File;
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
public class CompressingTask extends Thread {
    String file;
    JProgressBar pbar;
    JFrame frame;
    JLabel processLab;
    
    public CompressingTask(String file, JProgressBar pbar, JFrame frame, JLabel processLab) {
        this.file       = file;
        this.pbar       = pbar;
        this.frame      = frame;
        this.processLab = processLab;
    }
    
    public void run(){
        if(new File(file).isDirectory()) {
            try {
                ZipUtils.pbar = pbar;
                ZipUtils.processLab = processLab;
                ZipUtils.compress(file);
            } catch (IOException ex) {
                Logger.getLogger(CompressingTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            GzipUtils.processLab = processLab;
            GzipUtils.compress(file, pbar);
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
