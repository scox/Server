/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.museum.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.museum.javabeans.Track;
import com.museum.server.Constants;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.museum.javabeans.User;
import com.museum.logging.Log;
import com.museum.server.Protocol;
import com.museum.server.Server;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sam
 * Date: 14/02/2012
 * DatabaseHelper.java
 * 
 */
public class DatabaseHelper {

    private Connection conn;

    public void getConnection() {


        try {
            //load in the jdbc driver for mysql
            Class.forName("com.mysql.jdbc.Driver");
            //establish a database connection (create jdbc connection)
            conn = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);

        } catch (Exception e) {
            System.err.println("Error: " + e);
        }


    }

    public ResultSet getResult(String sql) {
        Statement stmt = null;
        ResultSet rs = null;
        try {

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

        } catch (SQLException ex) {
            //log
        }
        return rs;
    }

    public void closeConnection() {

        try {
            conn.close();
        } catch (SQLException ex) {
        }

    }

    //retrieve the requested audio. synchronised to allow for shared access
    public Track getTrack(User u, String exhibitNumber) {
        synchronized (this) {
            List<Track> t = new ArrayList<Track>();
            ResultSet rs = null;
            PreparedStatement pstmt = null;

            try {

                getConnection();
                String query = "select track_info,filename from audio where a_level = ? "
                        + "and a_language = ? and exhibit_no = ?";

                pstmt = conn.prepareStatement(query); // create a statement
                pstmt.setString(1, u.getLevel()); // set input parameter
                pstmt.setString(2, u.getLanguage()); // set input parameter
                pstmt.setString(3, exhibitNumber); // set input parameter
                rs = pstmt.executeQuery();
                // extract data from the ResultSet
                while (rs.next()) {
                    Log.writeOutput("Track Exists for user " + Protocol.u.getUserID());
                    Track track = new Track();
                    track.setTrackInfo(rs.getString(1));
                    track.setAudioLocation(rs.getString(2));
                    t.add(track);
                }
            } catch (Exception e) {
                t = null;
                //log here
            } finally {


                try {
                    rs.close();
                    pstmt.close();
                    conn.close();

                } catch (Exception e) {
                    t = null;
                    Log.writeOutput("Failed to retreive track");
                }

            }

            if (t.size() > 0) {
                return t.get(0);
            } else {
                return null;
            }

        }
    }

    //check the pin is for a leader/individual. synchronised to allow for shared access
    public User authenticateUser(String pin) {
        synchronized (this) {
            List<User> u = new ArrayList<User>();
            ResultSet rs = null;

            System.out.println("pin" + pin);
            PreparedStatement pstmt = null;
            try {

                getConnection();
                String query = "select transaction_id,pin, cust_language, cust_level,member_pin,customer_type from"
                        + " transaction where pin = ?";

                pstmt = conn.prepareStatement(query); // create a statement

                pstmt.setString(1, pin); // set input parameter
                rs = pstmt.executeQuery();
                // extract data from the ResultSet
                while (rs.next()) {


                    User user = new User();
                    user.setLanguage(rs.getString("cust_language"));
                    user.setLevel(rs.getString("cust_level"));
                    user.setPin(rs.getInt("pin"));
                    user.setUserID(rs.getInt("transaction_id"));
                    user.setMemberPin(rs.getInt("member_pin"));
                    user.setCustomerType(rs.getString("customer_type"));
                    u.add(user);

                }
            } catch (Exception e) {

                u = null;
                Log.writeOutput("Could not get user");
            } finally {
                try {
                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException e) {
                    Log.writeOutput("Sql Exception" + e);
                    u = null;
                    //log here
                }

            }


            if (!u.isEmpty()) {
                Log.writeOutput("User exists");
                return u.get(0);
            } else {
                System.out.print("no users");
                return null;
            }

        }
    }

//check if the pin is for a member. //synchronised to allow for shared access
    public User authenticateMemberUser(String pin) {
        synchronized (this) {
            System.out.println("CHecking group pin");

            List<User> u = new ArrayList<User>();
            ResultSet rs = null;

            PreparedStatement pstmt = null;
            try {

                getConnection();
                String query = "select transaction_id,pin, cust_language, cust_level,member_pin,customer_type,AssignedPort,AssignedIP from"
                        + " transaction where member_pin = ?";

                pstmt = conn.prepareStatement(query); // create a statement

                pstmt.setString(1, pin); // set input parameter
                rs = pstmt.executeQuery();
                // extract data from the ResultSet
                while (rs.next()) {

                    User user = new User();
                    user.setLanguage(rs.getString("cust_language"));
                    user.setLevel(rs.getString("cust_level"));
                    user.setPin(rs.getInt("pin"));
                    user.setUserID(rs.getInt("transaction_id"));
                    user.setMemberPin(rs.getInt("member_pin"));
                    user.setCustomerType(rs.getString("customer_type"));
                    user.setAssignedIP(rs.getString("AssignedIP").trim());
                    user.setAssignedPort(rs.getInt("AssignedPort"));
                    u.add(user);


                }
            } catch (Exception e) {
                Log.writeOutput(e + " exception");
                u = null;
                //log here
            } finally {
                try {
                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (SQLException e) {
                    Log.writeOutput(e + " sqlException");
                    u = null;
                    //log here
                }

            }

            if (!u.isEmpty()) {
                return u.get(0);
            } else {
                System.out.print("no users");
                return null;
            }

        }
    }

    public void updateIP(User u, String ip, int port) {

        try {

            getConnection();

            System.out.println("updating " + ip);
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("UPDATE transaction SET AssignedIP = '" + ip + "', AssignedPort = '" + port +
                    "' WHERE PIN =  '" + u.getPin() + "'");


        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            closeConnection();
        }

    }
}
