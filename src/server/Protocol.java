/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import database.DatabaseHelper;
import javabeans.Track;
import javabeans.User;

public class Protocol {

    private static final char PIN_AUTHENTICATION = '1';
    private static final char REQUEST_TRACK = '2';
    private static final char REQUEST_TRACK_INFO = '3';
    private static final char EMERGENCY_STOP = '4';
    private static final char ACK_PACKET = '5';
    private static final char NAK_PACKET = '6';
    private static final char MULTICAST_PACKET = '7';
    private static char STATE = 0;
    private static String PREVIOUS_PACKET;
    private static User u = null;

    public String processInput(String packet) {

       
        //how u handle invalid packets
        Track t = new Track();
        System.out.println(packet + " packet");

        if (packet != null) {

            if(packet.charAt(0) == '6'){
                System.out.println("NAK");
               packet = PREVIOUS_PACKET;
            }
            
            STATE = packet.charAt(0);
            
            
            }

        Server.LOG.writeOutput("Processing client request");
        System.out.println(STATE + " Current State" + PREVIOUS_PACKET);
        String theOutput = null;


//process request
        try{

        switch (STATE) {

            case ACK_PACKET:
                //ACK Packet
                theOutput = "5";
                break;
            

            case PIN_AUTHENTICATION:
                
                u = new DatabaseHelper().authenticateUser(packet.substring(1,5));

                if (u != null) {
                    theOutput = "11";
                } else {
                    System.out.println("No User exists");
                    theOutput = "10";
                }
                break;

            case REQUEST_TRACK:
            
                //getting a track object from this you can get filename
                  t = new DatabaseHelper().getTrack(u, packet.substring(1));
                  System.out.println(t + " ttt");
                if(t!=null){
              
                theOutput = "2" + t.getAudioLocation();
                }
                else{
                    theOutput = "20";
                }
                

                //GSTreamer Stuff here

                break;

            case REQUEST_TRACK_INFO:
                System.out.println(packet);
                t = new DatabaseHelper().getTrack(u, packet.substring(1));

                if(t!=null){
                if (t.getTrackInfo().length() > 0 && t.getTrackInfo() != null) {
                    theOutput = "3" + t.getTrackInfo();
                } else {
                    theOutput = "30";
                }
                }
                else{
                        theOutput = "30";
                }

                break;

            case EMERGENCY_STOP:
                //EMERGENCY STOP Packet coming from kiosk
                theOutput = "4";
                break;


            case MULTICAST_PACKET:
                
                //JAMES WHAT AM I DOING HERE
                //MULTICAST Packet
                // someone all group members a packet IP address of gstreamer
                // and port number
                theOutput = "127.1.1.1 4444";

                break;

            default:
                if(STATE == 0){
                    theOutput = "";
                }
                else{                       
                theOutput = "6";
                }
                break;
        }

        }
        catch (Exception e){
            theOutput = "6";
        }
        PREVIOUS_PACKET = packet;

        return theOutput;

    }
}