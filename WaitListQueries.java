
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class WaitListQueries {

    //for use in second part.

    private static Connection connection;
    private static PreparedStatement waitlistQuerie;
    private static PreparedStatement isEmpty;
    private static ResultSet result;
    private static ResultSet isEmptyResult;

    public static ResultSet findnextwaitlistentry(Date date) {

        connection = DBConnection.getConnection();

        try {
            //get all waitlist by date return result set.
            waitlistQuerie = connection.prepareStatement("select faculty,date,seats,timestamp from waitlist where date = ? order by timestamp");
            waitlistQuerie.setDate(1, date);
            result = waitlistQuerie.executeQuery();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return result;
    }

    
    
    
    public static ArrayList<String> WaitListisEmpty(){
    
        connection = DBConnection.getConnection();
        ArrayList<String> waitlist = new ArrayList<>();
        try{
            //make finding if table is empty easier.
        isEmpty = connection.prepareStatement("select faculty from waitlist");
        isEmptyResult = isEmpty.executeQuery();
        
        while(isEmptyResult.next()){
            waitlist.add(isEmptyResult.getString("faculty"));
        
        }
        
        }
    
     catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return waitlist;
    }
}
