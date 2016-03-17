/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compressing.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author ESa10969
 */
public class ZipUtils {
    public static JProgressBar pbar;
    public static JLabel processLab;
    private static int inc;
    private static String parent;
    private static ZipOutputStream out;
    
    public static void compress(String file) throws FileNotFoundException, IOException {
        //Variable definition
        File dir = new File(file);
        
        out = new ZipOutputStream(new FileOutputStream(file + ".zip"));
        parent = file.substring(file.lastIndexOf("\\") + 1);
        compressSubdirAndSubFiles(dir, "");
        out.close();
    }
    
    private static void compressSubdirAndSubFiles(File dir, String subPar) throws IOException {
        //Variable definition
        String[] entries = dir.list();
        String path = dir.getPath();
        
        for (int i = 0; i < entries.length; i++) {
          File f = new File(dir, entries[i]);
          String fPath = f.getPath();
          if (f.isDirectory()) {
            compressSubdirAndSubFiles(f, subPar + fPath.substring(fPath.lastIndexOf("\\") + 1) + "/");
          } else {
            compressFile(f, subPar);
          }
        }
        if(entries.length == 0) {
             ZipEntry entry = new ZipEntry(parent + "/" + subPar + "/"); // Make a ZipEntry
             out.putNextEntry(entry); // Store entry
        }
    }
    
    private static void compressFile(File f, String subPar) {
        //Variable definition
        byte[] buffer = new byte[1024]; // Create a buffer for copying
        int bytes_read;
        
        try {
            pbar.setMinimum(0);
            pbar.setMaximum((int) f.length());
            inc = (int) (f.length() / 1024);
            FileInputStream in = new FileInputStream(f); // Stream to read file
            ZipEntry entry = new ZipEntry(parent + "/" + subPar + f.getPath().substring(f.getPath().lastIndexOf("\\") + 1)); // Make a ZipEntry
            out.putNextEntry(entry); // Store entry
            processLab.setText("Compressing... " + entry.getName());
            while ((bytes_read = in.read(buffer)) != -1) {
              SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                       pbar.setValue(pbar.getValue() + inc);
                    }
              });
              out.write(buffer, 0, bytes_read);
            }
            pbar.setValue(pbar.getValue() + inc);
            in.close(); // Close input stream
        } catch(FileNotFoundException ex) {
            Logger.getLogger(ZipUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ZipUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void decompress(String file, JProgressBar pbar) throws IOException {
        String dest = file.substring(0, file.lastIndexOf("."));
        byte[] buf = new byte[1024];
        ZipInputStream zipinputstream = null;
        ZipEntry zipentry;
        zipinputstream = new ZipInputStream(new FileInputStream(file));
        int inc;
        File oldFile = new File(file);
        
        pbar.setMinimum(0);
        pbar.setMaximum((int) oldFile.length());
        inc = (int) (oldFile.length() / 1024);
        
        zipentry = zipinputstream.getNextEntry();
        while (zipentry != null) {
            //for each entry to be extracted
            String entryName = dest.substring(0, dest.lastIndexOf("\\") + 1) + zipentry.getName();
            entryName = entryName.replace('/', File.separatorChar);
            entryName = entryName.replace('\\', File.separatorChar);
            processLab.setText("Uncompressing... " + zipentry.getName());
            int n;
            FileOutputStream fileoutputstream;
            File newFile = new File(entryName);
            if (zipentry.isDirectory()) {
                if (!newFile.mkdirs()) {
                    break;
                }
                zipentry = zipinputstream.getNextEntry();
                continue;
            }
            new File(entryName.substring(0, entryName.lastIndexOf("\\"))).mkdirs();
            fileoutputstream = new FileOutputStream(entryName);

            while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                       pbar.setValue(pbar.getValue() + inc);
                    }
                });
                fileoutputstream.write(buf, 0, n);
            }
            pbar.setValue(pbar.getValue() + inc);
            fileoutputstream.close();
            zipinputstream.closeEntry();
            zipentry = zipinputstream.getNextEntry();

        }//while
        zipinputstream.close();
    }
}
