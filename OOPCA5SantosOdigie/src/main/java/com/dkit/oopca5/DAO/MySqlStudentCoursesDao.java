package com.dkit.oopca5.DAO;

import com.dkit.oopca5.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.*;

public class MySqlStudentCoursesDao extends MySqlDao implements StudentCourseInterfaceDao {
    Scanner keyboard = new Scanner(System.in);
    @Override
    public List<String> getStudentChoices(int cao) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer,List<String>> selectedChoices = new HashMap<>();

        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM student_courses WHERE caoNumber = ?";

            ps = con.prepareStatement(query);
            ps.setInt(1, cao);

            rs = ps.executeQuery();

            System.out.println("Your courses are as follows: ");
            while(rs.next())
            {
                String choices = rs.getString("courseid");
                System.out.println(choices);
            }


            ps.executeQuery();
            System.out.println("Successfully added course to "+cao);

        } catch(Exception e)
        {

        }
        return null;
    }

    @Override
    public void updatechoices(int cao) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<Integer,List<String>> selectedChoices = new HashMap<>();

        System.out.println("You can add up to 5 course, please select the amount of courses you want");
        int amount = keyboard.nextInt();



        try
        {
            con = this.getConnection();

            for(int i =0; i < amount; i++)
            {
                int counter = i+1;
                System.out.println("Please enter option number " +counter);
                String courseOptions = keyboard.next();

                String query = "INSERT INTO student_courses (caoNumber, courseid) VALUES (?, ?)";

                ps = con.prepareStatement(query);
                ps.setInt(1, cao);
                ps.setString(2, courseOptions);

                ps.executeUpdate();


                System.out.println("Successfully added course to "+cao);

            }



        } catch(Exception e)
        {

        }


    }
}
