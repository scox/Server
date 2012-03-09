/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import server.Lock;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javabeans.Track;
import server.Constants;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javabeans.User;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Sam
 */
public class DatabaseHelper {

    /**
     * @param args the command line arguments
     */
    private Connection conn;
    Lock lock;

    public void getConnection() {


        try {
            //load in the jdbc driver for mysql
            Class.forName("com.mysql.jdbc.Driver");
            //establish a database connection (create jdbc connection)
            conn = DriverManager.getConnection(Constants.URL, Constants.USERNAME, Constants.PASSWORD);

        } catch (Exception e) {
            System.err.println("Error: " + e);
        }//try


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

    public Track getTrack(User u, String exhibitNumber) {

        getConnection();
        List<Track> t = new ArrayList<Track>();
        ResultSet rs = null;
          
        PreparedStatement pstmt = null;
        try {

            String query = "select track_info,filename from audio where a_level = ? "
                    + "and a_language = ? and exhibit_no = ?";

            pstmt = conn.prepareStatement(query); // create a statement
            pstmt.setString(1, u.getLevel()); // set input parameter
            pstmt.setString(2, u.getLanguage()); // set input parameter
            pstmt.setString(3, exhibitNumber); // set input parameter
            rs = pstmt.executeQuery();
            // extract data from the ResultSet
            while (rs.next()) {
                System.out.println("track exists");
                
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
                //log here
            }
        }

        if(t.size()>0){
            return t.get(0);
        }
        else{
            return null;
        }




    }
    
    
    
    
    
    
    
     public User authenticateUser(String pin) {

       
        getConnection();
        List<User> u = new ArrayList<User>();
        ResultSet rs = null;

        PreparedStatement pstmt = null;
        try {

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
            System.out.println(e + " exception");
            u = null;
            //log here
        } finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                 System.out.println(e + " sqlException");
                u = null;
                //log here
            }
        }

        if(!u.isEmpty()){
            System.out.print("users");
            return u.get(0);
        }
        else{
            System.out.print("no users");
           return null;
        }


    }
    
    
    
    
}
