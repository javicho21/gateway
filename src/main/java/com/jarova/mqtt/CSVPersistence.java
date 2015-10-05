/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jarova.mqtt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.surfthing.Service;

/**
 *
 * @author azhang
 */
public class CSVPersistence extends Service {
    public static String TEXT_FILENAME;
    
    FileWriter writer;
    BufferedWriter bWriter;


    public void saveLog(String message) {
        try {
            System.out.println("Saving " + message + " in to " +TEXT_FILENAME);
            bWriter.write(message);
            bWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(CSVPersistence.class.getName()).log(Level.SEVERE, " CRITICAL ERROR: opening writing to file!", ex);
        }
    }

    public void close() {
        try {
            if (bWriter != null) {
                bWriter.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void start() {
        if (getConfig().getProperty("text.filename") != null) {
            TEXT_FILENAME = getConfig().getProperty("text.filename");
        }
        try {
            writer = new FileWriter(TEXT_FILENAME, false); //se trocar para true, vai fazer append e nao vai zerar o arquivo
            bWriter = new BufferedWriter(writer);
        } catch (IOException ex) {
            Logger.getLogger(CSVPersistence.class.getName()).log(Level.SEVERE, "CRITICAL ERROR: opening log file!" + TEXT_FILENAME, ex);
        }

    }

    @Override
    public void stop() {
        close();
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public CSVPersistence() {}
}
