/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Devin Gilmore
 */
public class DBConnection {

    private static Connection connection;
    private static final String DATABASE_URL = "jdbc:derby://localhost:1527/RoomSchedulerDBDevinGilmoredcg5264";

    public static Connection getConnection() {
        //handle exception

        if (connection == null) {
            //if no connection to database establish a connection.
            try {

                Connection connection = DriverManager.getConnection(DATABASE_URL, "java", "java");
                return (connection);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                System.out.println("Could not open database.");
                System.exit(1);

            }

        }
        return (connection);
    }
}
