package com.lxx.util;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

public class JDBCUtils {
    public static Connection getConnection() {
       String url="jdbc:mysql://127.0.0.1:3306/test";
       String user="root";
       String password="123456";
       try {
           Class.forName("com.mysql.jdbc.Driver");
           return DriverManager.getConnection(url,user,password);
       }catch(Exception e) {
           e.printStackTrace();
       }
       return null;
    }
}
