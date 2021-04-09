package com.dkit.oopca5.DAO;

import com.dkit.oopca5.DTO.Course;
import com.dkit.oopca5.Exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlCourseDao extends MySqlDao implements CourseDaoInterface
{
    @Override
    public List<Course> displayAllCourses() throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Course> courses = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT * FROM course";
            ps = con.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next())
            {
                String courseId = rs.getString("courseId");
                String title = rs.getString("title");
                int level = rs.getInt("level");
                String institution = rs.getString("institution");
                Course c = new Course(courseId, level, title, institution);
                courses.add(c);

                System.out.println("Course ID: "+ courseId +", ");
                System.out.println("Level: "+ level +", ");
                System.out.println("Title: "+ title +", ");
                System.out.println("Institution: "+ institution +", ");
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllCourses() " + e.getMessage());
        } finally
        {
            try
            {
                if (rs != null)
                {
                    rs.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (con != null)
                {
                    freeConnection(con);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findAllCourses() " + e.getMessage());
            }
        }
        return courses;     // may be empty
    }

    @Override
    public void displayCourse(String cao) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            con = this.getConnection();
            String query = "SELECT * FROM course where courseid = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, cao);

            rs = ps.executeQuery();

          //  Statement statement = con.createStatement();

            while(rs.next())
            {
                String id = rs.getString("courseid");
                int lvl = rs.getInt("level");
                String heading = rs.getString("title");
                String institute = rs.getString("institution");

                System.out.println("Course ID: "+ id +", ");
                System.out.println("Level: "+ lvl +", ");
                System.out.println("Title: "+ heading +", ");
                System.out.println("Institution: "+ institute +", ");

            }


        }catch (Exception e)
        {
            throw new DaoException("displayCourse() " + e.getMessage());
        }
    }
}
