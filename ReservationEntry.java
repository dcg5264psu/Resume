/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Devin Gilmore
 */
public class ReservationEntry {

    private static Connection connection;
    private static Timestamp timestamp;
    private static ResultSet roomQuery;
    private static ResultSet foundFaculty;
    private static ResultSet foundFacultyWL;
    private static PreparedStatement addReservation;
    private static PreparedStatement deleteResFromWaitlistExc;
    private static PreparedStatement findFacultyWL;
    private static PreparedStatement findFaculty;
    private static PreparedStatement deleteResFromWaitlist;
    private static PreparedStatement deleteReservation;
    private static PreparedStatement findSeatsPerRoom;

    //private static ArrayList<String> rooms;
    public static void AddReservation(String faculity, Date date, int seats) {
        connection = DBConnection.getConnection();
        ArrayList<String> rooms = new ArrayList<>();
        try {

            addReservation = connection.prepareStatement("insert into reservation(faculty,date,room,seats,timestamp)"
                    + " values (?,?,?,?,?)");
//assign the info for reservation entry
            addReservation.setString(1, faculity);
            addReservation.setDate(2, date);

            rooms = RoomQueries.getAllPossibleRooms(date, seats);

            findSeatsPerRoom = connection.prepareStatement("select name, seats from room order by seats");
            roomQuery = findSeatsPerRoom.executeQuery();

            while (roomQuery.next()) {
//check if there are enough seats in the rrom and if the room is in the available room arraylist.

                if (roomQuery.getInt(2) >= seats && (rooms.contains(roomQuery.getString(1)))) {

                    addReservation.setString(3, roomQuery.getString(1));
                    break;

                }

            }

            addReservation.setInt(4, seats);
        //code in the room queries

            //create the timestamp and then add all to the reservation dB
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());

            addReservation.setTimestamp(5, currentTimestamp);
            addReservation.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static void addReservationFromWaitlist(String faculity, Date date, int seats, Timestamp timestamp) {
        connection = DBConnection.getConnection();
        ArrayList<String> rooms = new ArrayList<>();
        try {
            addReservation = connection.prepareStatement("insert into reservation(faculty,date,room,seats,timestamp)"
                    + " values (?,?,?,?,?)");
//assign the info for reservation entry
            addReservation.setString(1, faculity);
            addReservation.setDate(2, date);

            rooms = RoomQueries.getAllPossibleRooms(date, seats);

            findSeatsPerRoom = connection.prepareStatement("select name, seats from room order by seats");
            roomQuery = findSeatsPerRoom.executeQuery();

            while (roomQuery.next()) {
//check if there are enough seats in the rrom and if the room is in the available room arraylist.

                if (roomQuery.getInt(2) >= seats && (rooms.contains(roomQuery.getString(1)))) {

                    addReservation.setString(3, roomQuery.getString(1));
                    break;

                }

            }

            addReservation.setInt(4, seats);
        //code in the room queries

            //create the timestamp and then add all to the reservation dB
            addReservation.setTimestamp(5, timestamp);
            addReservation.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static ArrayList<String> findReservationFaculty() {

        connection = DBConnection.getConnection();
        ArrayList<String> faculty = new ArrayList<>();

        try {
            //find the reservations for use in GUI
            findFaculty = connection.prepareStatement("select faculty from reservation");
            foundFaculty = findFaculty.executeQuery();
//Add them to list and return list
            while (foundFaculty.next()) {

                if (faculty.contains(foundFaculty.getString("faculty")) == false) {

                    faculty.add(foundFaculty.getString("faculty"));

                }
             
            findFacultyWL = connection.prepareStatement("select faculty from waitlist");
            foundFacultyWL = findFacultyWL.executeQuery();
            
                while(foundFacultyWL.next()){
                
                if (faculty.contains(foundFacultyWL.getString("faculty")) == false) {

                    faculty.add(foundFacultyWL.getString("faculty"));

                }
                
                }
            
            
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return faculty;

    }

    public static ArrayList<Date> findReservationDate(String faculty) {

        connection = DBConnection.getConnection();
        ArrayList<Date> dates = new ArrayList<>();

        try {
            //make sure that the facultys date is in the selection box.
            findFaculty = connection.prepareStatement("select date from reservation where faculty = ?");
            findFaculty.setString(1, faculty);
            foundFaculty = findFaculty.executeQuery();

            while (foundFaculty.next()) {
                //make sure there are no dublicates.
                if (dates.contains(foundFaculty.getDate("date")) == false) {

                    dates.add(foundFaculty.getDate("date"));

                }

            }
            
            
            findFacultyWL = connection.prepareStatement("select date from waitlist where faculty = ?");
            findFacultyWL.setString(1, faculty);
            foundFacultyWL = findFacultyWL.executeQuery();
            
                while(foundFacultyWL.next()){
                
                if (dates.contains(foundFacultyWL.getDate("date")) == false) {

                    dates.add(foundFacultyWL.getDate("date"));

                }
                
                }
            
            
            
            

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return dates;

    }

    public static void dropReservation(Date date, String faculty) {
        connection = DBConnection.getConnection();
        try {
            //delete the reservation
            deleteReservation = connection.prepareStatement("delete from reservation where date = ? and faculty = ?");
            deleteReservation.setDate(1, date);
            deleteReservation.setString(2, faculty);
            deleteReservation.executeUpdate();
            ResultSet waitlistResult = WaitListQueries.findnextwaitlistentry(date);
            try {
                //add a reservation from WL.
                waitlistResult.next();
                String facultyWL = waitlistResult.getString("faculty");
                Date dateWL = waitlistResult.getDate("date");
                int seatsWL = waitlistResult.getInt("seats");
                Timestamp timestampWL = waitlistResult.getTimestamp("timestamp");
                ReservationEntry.addReservationFromWaitlist(facultyWL, dateWL, seatsWL, timestampWL);
                //if thereis one then delete the previous
                deleteResFromWaitlist = connection.prepareStatement("delete from waitlist where faculty = ? and date = ?");
                deleteResFromWaitlist.setString(1, facultyWL);
                deleteResFromWaitlist.setDate(2, dateWL);
                deleteResFromWaitlist.executeUpdate();
            } catch (NullPointerException nullP) {

            }
            
            //Delete WL entry
            deleteResFromWaitlistExc = connection.prepareStatement("delete from waitlist where faculty = ? and date = ?");
                deleteResFromWaitlistExc.setString(1, faculty);
                deleteResFromWaitlistExc.setDate(2, date);
                deleteResFromWaitlistExc.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();

        }

    }

}
