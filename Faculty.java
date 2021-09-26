
import java.sql.Connection;
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
public class Faculty {

    private static Connection connection;
    private static PreparedStatement addFaculty;
    private static PreparedStatement getFacultyList;
    private static ResultSet resultSet;

    public static void addFaculty(String name) {
        //insert a name into the faculaty database.
        connection = DBConnection.getConnection();
        try {
            addFaculty = connection.prepareStatement("insert into faculty (name) values (?)");
            addFaculty.setString(1, name);
            addFaculty.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static ArrayList<String> getAllFaculty() {

        connection = DBConnection.getConnection();

        //run through the faculty database and return an arraylist of all faculty to populate the combobox.
        ArrayList<String> faculty = new ArrayList<>();
        try {
            getFacultyList = connection.prepareStatement("select name from faculty order by name");
            resultSet = getFacultyList.executeQuery();

            while (resultSet.next()) {
                faculty.add(resultSet.getString(1));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return faculty;
    }
}
