/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.net.*;
import java.io.*;
import java.util.Calendar;
import logging.Log;

public class Server {

    public static Log LOG = new Log();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        LOG.writeOutput(Calendar.getInstance().getTime().toString());
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(4444);
            System.out.println("Starting Server");
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }

        while (listening) {
            new MultiServerThread(serverSocket.accept()).start();
        }

        serverSocket.close();
    }
}
