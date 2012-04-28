/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.museum.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import com.museum.server.Constants;

/**
 *
 * @author Sam
 * Date: 14/02/2012
 * Log.java: Creates the log file if it does not exist and writes the output to it.  
 * 
 */
public class Log {

    public static void writeOutput(String output) {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(Constants.LOG_LOCATION, true)));

            out.print(output);
            out.println();
            out.flush();
            out.close();
        } catch (Exception e) {
            System.out.println("Failed to create log file");
        }


    }
}
