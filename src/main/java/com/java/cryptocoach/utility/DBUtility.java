package com.java.cryptocoach.utility;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;

public class DBUtility {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/cryptocoach";
    private static final String DATABASE_USERNAME = "student";
    private static final String DATABASE_PASSWORD = "student";

    private Connection connection;
    private static DBUtility instance;

    /*
     * Appends data from a JSON file to the Crypto DB.
     */
    public void appendFromJson() {
        int response = JOptionPane.showConfirmDialog(null,
                "Do you want to add a JSON file?",
                "Add File",
                JOptionPane.YES_NO_OPTION);

        if (response != JOptionPane.YES_OPTION) {
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String filePath = fileChooser.getSelectedFile().getAbsolutePath();

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(content);

            String sqlStr = "INSERT INTO Crypto (coin_name, January, February, March, April, May, June, July, August, September, October, November, December) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stat = connection.prepareStatement(sqlStr);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                stat.setString(1, jsonObject.getString("coin_name"));
                stat.setDouble(2, jsonObject.getDouble("January"));
                stat.setDouble(3, jsonObject.getDouble("February"));
                stat.setDouble(4, jsonObject.getDouble("March"));
                stat.setDouble(5, jsonObject.getDouble("April"));
                stat.setDouble(6, jsonObject.getDouble("May"));
                stat.setDouble(7, jsonObject.getDouble("June"));
                stat.setDouble(8, jsonObject.getDouble("July"));
                stat.setDouble(9, jsonObject.getDouble("August"));
                stat.setDouble(10, jsonObject.getDouble("September"));
                stat.setDouble(11, jsonObject.getDouble("October"));
                stat.setDouble(12, jsonObject.getDouble("November"));
                stat.setDouble(13, jsonObject.getDouble("December"));

                stat.executeUpdate();
            }

            stat.close();
        } catch (Exception e) {
            ExceptionUtility.saveStackTrace(e);
        }
    }

    /*
     * Executes a query against the database.
     *
     * @param {String} query - The SQL query being executed.
     * @return {Pair<ResultSet, Statement>} - The data being queried.
     */
    public Pair<ResultSet, Statement> executeQuery(String query) {
        try {
            Statement stat = connection.createStatement();
            ResultSet results = stat.executeQuery(query);

            return new Pair<>(results, stat);
        } catch (Exception e) {
            // on an exception, the garbage collector will automatically free the resources.
            // we don't need to free it here.
            ExceptionUtility.saveStackTrace(e);
        }

        return null;
    }

    /*
     * Executes a command against the database.
     *
     * @param {String} command - The SQL command being executed.
     */
    public void execute(String command) {
        try {
            // create the statement, and execute the command against the database.
            Statement stat = connection.createStatement();
            stat.execute(command);
            stat.close();
        } catch (Exception e) {
            // on an exception, the garbage collector will automatically free the resources.
            // we don't need to free it here.
            ExceptionUtility.saveStackTrace(e);
        }
    }

    /*
     * Closes the database connection.
     */
    public void close() {
        try {
            // close the connection.
            connection.close();
        }
        catch (Exception e) {
            // on an exception, the garbage collector will automatically free the resources.
            // we don't need to free it here.
            ExceptionUtility.saveStackTrace(e);
        }
    }

    /*
     * Initializes the SQL driver, and establishes the connection.
     *
     * @constructor
     */
    public DBUtility()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        }
        catch (Exception e) {
            // on an exception, the garbage collector will automatically free the resources.
            // we don't need to free it here.
            ExceptionUtility.saveStackTrace(e);
        }
    }

    /*
     * Initializes the singleton instance, and fetches it.
     *
     * @return {DBUtility} - The allocated instance.
     */
    public static DBUtility get() {
        if (instance == null) {
            try {
                instance = new DBUtility();
            } catch (Exception e) {
                // on an exception, the garbage collector will automatically free the resources.
                // we don't need to free it here.
                ExceptionUtility.saveStackTrace(e);
            }
        }

        return instance;
    }
}
