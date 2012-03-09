/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import server.Constants;

/**
 *
 * @author Sam
 */
public class Log {

    public void writeOutput(String output) {

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
