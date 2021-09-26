
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Devin Gilmore
 */
public class RoomEntry {

    private static Connection connection;
    private static PreparedStatement reservationQueries;
    private static PreparedStatement reservationdelete;
    private static PreparedStatement roomdelete;
    private static PreparedStatement roomAdd;
    private static PreparedStatement deleteResFromWaitlist;
    private static ResultSet reservationQuerieResult;

    public static void addRoom(String room, int seats) {

        connection = DBConnection.getConnection();
        try {
            //insert the room
            roomAdd = connection.prepareStatement("insert into room (name,seats) values (?,?)");
            roomAdd.setString(1, room);
            roomAdd.setInt(2, seats);
            roomAdd.executeUpdate();
            //Check waitlist
if(WaitListQueries.WaitListisEmpty().isEmpty() == false){
            ArrayList<Date> dates = Dates.getAllDates();
            //go through each date and try adding a reservation from waitlist.
            for (int i = 0; i <= dates.size(); i++) {
                ResultSet waitlistResult = WaitListQueries.findnextwaitlistentry(dates.get(i));

                waitlistResult.next();
                String facultyWL = waitlistResult.getString("faculty");
                Date dateWL = waitlistResult.getDate("date");
                int seatsWL = waitlistResult.getInt("seats");
                Timestamp timestampWL = waitlistResult.getTimestamp("timestamp");
                ReservationEntry.addReservationFromWaitlist(facultyWL, dateWL, seatsWL, timestampWL);
                //if one is made delete it from waitlist.
                deleteResFromWaitlist = connection.prepareStatement("delete from waitlist where faculty = ? and date = ?");
                deleteResFromWaitlist.setString(1, facultyWL);
                deleteResFromWaitlist.setDate(2, dateWL);
                deleteResFromWaitlist.executeUpdate();
            }
}
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    public static void RemoveRoom(String Room) {

        connection = DBConnection.getConnection();
        try {
            //find all the reservations for the specified room.
            reservationQueries = connection.prepareStatement("select date, faculty, timestamp, seats from reservation where room = ? ");
            reservationQueries.setString(1, Room);
            reservationQuerieResult = reservationQueries.executeQuery();

            
            
            
            while (reservationQuerieResult.next()) {
                String faculty = reservationQuerieResult.getString("faculty");
                int seats = reservationQuerieResult.getInt("seats");
                Timestamp timestamp = reservationQuerieResult.getTimestamp("timestamp");
                Date date = reservationQuerieResult.getDate("date");
                if(RoomQueries.getAllPossibleRooms(reservationQuerieResult.getDate("date"), reservationQuerieResult.getInt("seats")).isEmpty()){
                
                //add them to the waitlist
                
                WaitListEntry.addWaitListFromReservation(faculty, date, seats, timestamp);
                }
                else{
                
                
                ReservationEntry.addReservationFromWaitlist(faculty, date, seats, timestamp);
                
                
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        try {
            //delete the reservations
            reservationdelete = connection.prepareStatement("delete from reservation where room = ?");
            reservationdelete.setString(1, Room);
            reservationdelete.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        try {
            //delete the room.
            roomdelete = connection.prepareStatement("delete from room where name = ?");
            roomdelete.setString(1, Room);
            roomdelete.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        
        
        
        

    }
}
