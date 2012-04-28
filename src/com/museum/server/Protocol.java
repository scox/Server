package com.museum.server;

import com.museum.database.DatabaseHelper;
import com.museum.javabeans.Track;
import com.museum.javabeans.User;
import com.museum.logging.Log;

/**
 *
 * @author Sam
 * Date: 14/02/2012
 * Protocol.java: This class constructs the response packet by passing it through the protocol
 * 
 */
public class Protocol {

    private static final char PIN_AUTHENTICATION = '1';
    private static final char REQUEST_TRACK = '2';
    private static final char REQUEST_TRACK_INFO = '3';
    private static final char EMERGENCY_STOP = '4';
    private static final char ACK_PACKET = '5';
    private static final char NAK_PACKET = '6';
    private static final char GSTREAMER_SETUP = '7';
    private static char STATE = 0;
    private static String PREVIOUS_PACKET;
    public static User u = null;

    public String processInput(String packet) {


        Track t = new Track();
        System.out.println("Packet " + packet);

        if (packet != null) {
            packet = packet.trim().replaceAll(" ", "");
            if (packet.charAt(0) == '6') {
                System.out.println("NAK");
                packet = PREVIOUS_PACKET;

            }

            try {
                STATE = packet.charAt(0);
            } catch (NullPointerException npe) {
                STATE = 0;
            }


        }

        Log.writeOutput("Processing client request");
        String theOutput = null;


//process request
        try {

            switch (STATE) {

                case ACK_PACKET:
                    //ACK Packet
                    theOutput = "5";
                    break;


                case PIN_AUTHENTICATION:

                    
                    //if ind send just port
                    //else send port and ip
                    
                
                    u = new DatabaseHelper().authenticateUser(packet.substring(1, 5));
     
                    
                    if (u != null) {
                
                         //get g streamer port after init and append
                        int port = u.getPort();
                        theOutput = "111"+port + ",";
                        
                        if(String.valueOf(u.getMemberPin()).trim().length() >0  ){
                            String ip = u.getIP();
                            new DatabaseHelper().updateIP(u, ip,port);
                           theOutput = theOutput + ip; 
                       
                        }
                      
                        
                    } 
              
                    else {
                 
                        u = new DatabaseHelper().authenticateMemberUser(packet.substring(1, 5));
                        System.out.println("checking group pin" + u.getAssignedIP());
                        if (u != null && u.getAssignedIP().length() >0) {
                            theOutput = "110" +u.getPort()+"," + u.getAssignedIP();
                            
                            
                        } else {
                            Log.writeOutput("No User exists or Leader has not logged in");
                            theOutput = "10";
                        }

                    }
                    break;

                case REQUEST_TRACK:

                    // if mac address sent check db for room number and play first track

                    //getting a track object for specific request
                    if (packet.length() < 4) {
                        t = new DatabaseHelper().getTrack(u, packet.substring(1, 1));
                    } //getting a list of tracks if no request based on location.
                    else {
                        t = new DatabaseHelper().getTrack(u, packet.substring(1).trim());
                    }

                    if (t != null) {

                        theOutput = "2" + t.getAudioLocation();
                        
                        //
                        
                        
                    } else {
                        theOutput = "20";
                    }

                    //Stream Audio


                    break;

                case REQUEST_TRACK_INFO:
       
                    t = new DatabaseHelper().getTrack(u, packet.substring(1));

                    if (t != null) {
                        if (t.getTrackInfo().length() > 0 && t.getTrackInfo() != null) {
                            theOutput = "3" + t.getTrackInfo();
                        } else {
                            theOutput = "30";
                        }
                    } else {
                        theOutput = "30";
                    }

                    break;

                case EMERGENCY_STOP:
                    //EMERGENCY STOP Packet coming from kiosk

                    //if request is to restart services
                    if (packet.substring(1, 1).equalsIgnoreCase("0")) {
                        theOutput = "40";
                    } //Emergency stop request
                    else {
                        theOutput = "41";
                    }

                    break;


                case GSTREAMER_SETUP:

                  
                   
                    packet = "74000,127.1.1.1.1#";
                    
               
                    //7PORT,IP
                    
                    
            
                    //IP is what GStreamers running on
                    //GET PORT USED FOR GSTREAM Start at 4000 - 5000 increment
                    //schronised so only one has acces
                    
                    //if group send 7PORT,IP,
                    //else send 7PORT,
                    //
                    
                    //
                    
                    theOutput = "127.1.1.1 4444";

                    break;

                default:
                    if (STATE == 0) {
                        theOutput = "5";
                    } else {
                        theOutput = "6";
                    }
                    break;
            }

        } catch (Exception e) {
            theOutput = "6";
        }
        PREVIOUS_PACKET = packet;
        
        
        System.out.println("output" + theOutput);

        return theOutput;

    }
}