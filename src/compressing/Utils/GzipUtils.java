/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compressing.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author ESa10969
 */
public class GzipUtils {
    public static JLabel processLab;
    
    public static void compress(String file, JProgressBar pbar) {
        //variable definition
        String sourcefile = file;
        File oldfile;
        File new_file;
        FileInputStream     finStream;
        BufferedInputStream bufinStream;
        FileOutputStream    outStream;
        GZIPOutputStream    goutStream;
        byte[] buf = new byte[1024];
        int i;
        int inc;

        try {
            oldfile = new File(sourcefile);
            new_file = new File(sourcefile+".gz");
            finStream = new FileInputStream(oldfile);
            bufinStream = new BufferedInputStream(finStream);
            outStream = new FileOutputStream(new_file);
            goutStream = new GZIPOutputStream(outStream);
            pbar.setMinimum(0);
            pbar.setMaximum((int) oldfile.length());
            inc = (int) (oldfile.length() / 1024);
            processLab.setText("Compressing... " + sourcefile.substring(sourcefile.lastIndexOf("\\") + 1));
            while ((i = bufinStream.read(buf)) >= 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                       pbar.setValue(pbar.getValue() + inc);
                    }
                });
                goutStream.write(buf, 0, i);
            }
            pbar.setValue(pbar.getMaximum());
            bufinStream.close();
            goutStream.close();
        } catch (IOException e) {
            System.out.println("Exception is" + e.getMessage());
        }
    }
    
    public static void decompress(String file, JProgressBar pbar) {
        //variable definition
        String newFile = file.substring(0, file.length() - 3);
        byte[] buf = new byte[1024];
        int inc;
        
        try {
            GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(file));
            FileOutputStream out = new FileOutputStream(newFile);

            int len;
            pbar.setMinimum(0);
            pbar.setMaximum((int) new File(file).length());
            inc = (int) (pbar.getMaximum() / 1024);
            processLab.setText("Uncompressing... " + file.substring(file.lastIndexOf("\\") + 1));
            while ((len = gzis.read(buf)) > 0) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                       pbar.setValue(pbar.getValue() + inc);
                    }
                });
                out.write(buf, 0, len);
            }
            pbar.setValue(pbar.getMaximum());
            gzis.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Exception is" + e.getMessage());
        }
    }
}
