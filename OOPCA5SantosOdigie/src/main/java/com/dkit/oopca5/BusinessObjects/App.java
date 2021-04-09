package com.dkit.oopca5.BusinessObjects;

import com.dkit.oopca5.DAO.MySqlCourseDao;
import com.dkit.oopca5.DAO.MySqlStudentCoursesDao;
import com.dkit.oopca5.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App
{
    public static void main(String[] args) throws DaoException {

        String url = "jdbc:mysql://localhost/";
        String dbName = "oop_ca5_santos_odigie";
//            String driver = "com.mysql.cj.jdbc.Driver";
        String userName = "root";
        String password = "";
        try
        {
            Connection conn = DriverManager.getConnection(url+dbName,userName,password);
            System.out.println("connected");

            MySqlStudentCoursesDao studentc = new MySqlStudentCoursesDao();
            studentc.getStudentChoices(12345678);

            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
