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
            
            Log.writeOutput("Starting new server thread");
          
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
            try{
            while ((packet = in.readLine().getBytes()) != null) {
                   
                String temp = new String(packet);
                
                //put packet through the protocol and construct response packet
                outputLine = p.processInput(temp);
                
                // do not send if ack packet is recieved.
                if(!outputLine.substring(0, 1).equalsIgnoreCase("5")){
                out.println(outputLine);
                }
                
            }
            }
            catch(Exception e){
                
                System.out.println("Waiting");
                
            }
                
            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
           Log.writeOutput("IO Exception");
        }
        }
 
            
    }
