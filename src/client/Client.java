/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket Socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            Socket = new Socket("localhost", 4444);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost.");
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            if (fromServer.equals("Bye.")) {
                break;
            }

            fromUser = stdIn.readLine();
            String packet;

            packet = fromUser; //will be byte array.

            if (packet != null) {
                System.out.println("Client: constructed packet ");

                out.println(packet);
            }
        }

        out.close();
        in.close();
        stdIn.close();
        Socket.close();
    }
}