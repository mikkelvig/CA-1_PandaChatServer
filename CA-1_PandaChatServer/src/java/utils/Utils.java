/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Mikkel
 */
public class Utils {

    public static Properties initProperties(String propertyFile) {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(propertyFile);
            properties.load(is);
        } catch (IOException ex) {
            System.out.println(String.format("Could not locate the %1$s file.", propertyFile));
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return properties;
    }

    public static void setLogFile(String logFile, String className) {
        Logger logger;
        FileHandler fileTxt;
        java.util.logging.Formatter formatterTxt;
        try {
            logger = Logger.getLogger(className);
            fileTxt = new FileHandler(logFile);
            formatterTxt = new SimpleFormatter();
            fileTxt.setFormatter(formatterTxt);
            logger.addHandler(fileTxt);
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void closeLogger(String logger) {
    for (Handler h : Logger.getLogger(logger).getHandlers()) {
      System.out.println("Closing logger");
      h.close();
    }
  }
    
    public static Logger getLogger(String logFile, String className) {
    Logger logger;
    
    FileHandler fileTxt;
    java.util.logging.Formatter formatterTxt;
    try {
      logger = Logger.getLogger(className);
      fileTxt = new FileHandler(logFile);
       formatterTxt = new SimpleFormatter();
      fileTxt.setFormatter(formatterTxt);
      logger.addHandler(fileTxt);
    } catch (IOException ex) {
      Logger.getLogger(className).log(Level.SEVERE, null, ex);
      return null;
    }
    return logger;
  }
}
