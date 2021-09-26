/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Devin Gilmore
 */
public class RoomQueries {

    //private static ArrayList<String> rooms;
    private static Connection connection;
    private static PreparedStatement allRooms;
    private static ResultSet resultSet;
    private static ResultSet resultSetRoom;
    private static PreparedStatement seats_PerRoom;

    public static ArrayList<String> getAllRooms() {

        connection = DBConnection.getConnection();
        ArrayList<String> rooms = new ArrayList<>();
//add rooms to arraylist.

        try {
            seats_PerRoom = connection.prepareStatement("select name from room");
            resultSetRoom = seats_PerRoom.executeQuery();

            while (resultSetRoom.next()) {

                rooms.add(
                        resultSetRoom.getString("name"));

            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return rooms;
    }

    public static ArrayList<String> getAllPossibleRooms(Date date, int seats) {

        connection = DBConnection.getConnection();
        ArrayList<String> rooms = new ArrayList<>();
//add rooms to arraylist.

        try {
            seats_PerRoom = connection.prepareStatement("select name, seats from room");
            resultSetRoom = seats_PerRoom.executeQuery();

            while (resultSetRoom.next()) {
                //check if there is enough available seats for the room requested.
                if (seats <= resultSetRoom.getInt(2)) {
                    rooms.add(
                            resultSetRoom.getString("name"));
                }
            }

            //check to see if the room is already reserved for that date and remove it from the arraylist.
            allRooms = connection.prepareStatement("select room, date from reservation where date = ?");
            allRooms.setDate(1, date);
            resultSet = allRooms.executeQuery();
            while (resultSet.next()) {
                if (rooms.contains(resultSet.getString(1))) {
                    rooms.remove(resultSet.getString(1));

                }

            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return (rooms);
    }
}
