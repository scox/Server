package server;

import java.net.*;
import java.io.*;

public class MultiServerThread extends Thread {

    private Socket socket = null;

    public MultiServerThread(Socket socket) {
        super("MultiServerThread");
        this.socket = socket;
    }

    public void run() {

        try {
            
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

            while ((packet = in.readLine().getBytes()) != null) {
                   
                String temp = new String(packet);
                if (temp.equals("STOP")) {
                    System.out.println("Closing Thread");
                    break;
                }
                outputLine = p.processInput(temp);
                if(!outputLine.substring(0, 1).equalsIgnoreCase("5")){
                out.println(outputLine);
                }
                else{
                    out.println();
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