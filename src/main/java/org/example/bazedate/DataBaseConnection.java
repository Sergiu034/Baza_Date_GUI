package org.example.bazedate;

import java.sql.Connection;
import java.sql.DriverManager;


public class DataBaseConnection {

    public Connection getConnection(){

        String url = "jdbc:mysql://localhost:3306/colocviu";
        String user = "root";
        String password = "Sergiu1!";

        try{
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;

        } catch(Exception e) {
            e.printStackTrace();
            return null;

        }
    }
}
