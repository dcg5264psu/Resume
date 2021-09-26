/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author Devin Gilmore
 */
public class WaitListEntry {

    private static Connection connection;
    private static PreparedStatement waitListEntryStatement;
    public static ResultSet waitListResultSet;

    public static void addWaitList(String faculty, Date date, int seats) {
        connection = DBConnection.getConnection();
        //insert into waitlist the reservation info to be used at a later time.
        try {

            waitListEntryStatement = connection.prepareStatement("insert into waitlist (faculty,date,seats,timestamp)" + "values (?,?,?,?)");
            waitListEntryStatement.setString(1, faculty);
            waitListEntryStatement.setDate(2, date);
            waitListEntryStatement.setInt(3, seats);
            //create a timestamp as to when the reservation was made.
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            waitListEntryStatement.setTimestamp(4, currentTimestamp);

            waitListEntryStatement.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void addWaitListFromReservation(String faculty, Date date, int seats, Timestamp timestamp) {
        connection = DBConnection.getConnection();
    //insert into waitlist the reservation info to be used at a later time. 
        //without creating a timestamp.
        try {

            waitListEntryStatement = connection.prepareStatement("insert into waitlist (faculty,date,seats,timestamp)" + "values (?,?,?,?)");
            waitListEntryStatement.setString(1, faculty);
            waitListEntryStatement.setDate(2, date);
            waitListEntryStatement.setInt(3, seats);
            waitListEntryStatement.setTimestamp(4, timestamp);

            waitListEntryStatement.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    
    
    
    
    
    
}
