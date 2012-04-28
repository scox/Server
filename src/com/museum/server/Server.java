
package com.museum.server;

import java.net.*;
import java.io.*;
import java.util.Calendar;
import com.museum.logging.Log;

/**
 *
 * @author Sam
 * Date: 14/02/2012
 * Server.java: Class sets up the server.  It listens for clients
 * and creates a new thread when a client connects
 * 
 */
public class Server {

    public static Log LOG = new Log();
    public static int PORT = 4000;
    public static String IP = "224.0.0.0";
    public static int ipInt = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        LOG.writeOutput(Calendar.getInstance().getTime().toString());
        boolean listening = true;

        try {
            //Local port to listen on
            serverSocket = new ServerSocket(4444);
           LOG.writeOutput("Starting Server");
            System.out.println("Starting Server");
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }

        //Create new thread for each client
        while (listening) {
            new MultiServerThread(serverSocket.accept()).start();
        }

        serverSocket.close();
    }
    
    
    public static synchronized int getPort(){
        
        return PORT ++;
        
    }
    

}
