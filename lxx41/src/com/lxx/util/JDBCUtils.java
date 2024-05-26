package com.lxx.util;

import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

public class JDBCUtils {
    public static Connection getConnection() {
       String url="jdbc:mysql:///resum-db?characterEncoding=utf8&useSSL=false";
       String user="root";
       String password="123456";
       try {
           Class.forName("com.mysql.jdbc.Driver");
           System.out.println("数据库连接成功");
           return DriverManager.getConnection(url,user,password);
       }catch(Exception e) {
           e.printStackTrace();
       }
       return null;
    }
}
