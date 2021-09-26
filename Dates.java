
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Devin Gilmore
 */
public class Dates {

    //Declare variables

    private static Connection connection;
    private static PreparedStatement getDates;
    private static PreparedStatement setDate;
    private static ResultSet resultSet;

    public static void addDate(Date date) {
        //connect
        connection = DBConnection.getConnection();

        try {
            //insert the given date into the DB
            setDate = connection.prepareStatement("insert into date (date) values (?)");
            setDate.setDate(1, date);
            setDate.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    public static ArrayList<Date> getAllDates() {
//Connect
        connection = DBConnection.getConnection();
        ArrayList<Date> dateList = new ArrayList<>();

        try {
            getDates = connection.prepareStatement("select date from date order by date");
            resultSet = getDates.executeQuery();
            //Run through DB and add them to an ArrayList
            while (resultSet.next()) {

        //add to arraylist of dates and then return them.
                dateList.add(resultSet.getDate(1));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return dateList;
    }
}
