package com.museum.server;

import com.museum.logging.Log;
import java.net.*;
import java.io.*;
/**
 *
 * @author Sam
 * Date: 14/02/2012
 * MultServerThread.java: Allows for multiple clients.Creates a separate thread to handle each client request.
 * 
 */
public class MultiServerThread extends Thread {

    private Socket socket = null;

    public MultiServerThread(Socket socket) {
        super("MultiServerThread");
        this.socket = socket;
    }

    public void run() {

        try {
            
            new Log().writeOutput("Starting new server thread");
            System.out.println("Starting Server");
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    socket.getInputStream()));
            

            String outputLine;
            Protocol p = new Protocol();
            outputLine = p.processInput(null);
            out.println(outputLine);
            byte[] packet;

            //read in packet from client
            while ((packet = in.readLine().getBytes()) != null) {
                   
                String temp = new String(packet);
                
                //put packet through the protocol and construct response packet
                outputLine = p.processInput(temp);
                
                System.out.println(outputLine + " packet");
                if(!outputLine.substring(0, 1).equalsIgnoreCase("5")){
                System.out.println(outputLine + " packet");
                out.println(outputLine);
                }
                else{
                    //Do not send response if client sent ACK
//                    out.println();
                }
              
            }
            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO Exception");
        }
    }
}