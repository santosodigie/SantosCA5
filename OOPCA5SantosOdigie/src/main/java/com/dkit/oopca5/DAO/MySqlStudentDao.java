package com.dkit.oopca5.DAO;

import com.dkit.oopca5.DTO.Student;
import com.dkit.oopca5.Exceptions.DaoException;
import com.dkit.oopca5.core.CAOService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlStudentDao extends MySqlDao implements StudentDaoInterface {

    @Override
    public List<Student> findAllStudents() throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT * FROM STUDENT";
            ps = con.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next())
            {
                int caoNumber = rs.getInt("caonumber");
                String dateOfBirth = rs.getString("dob");
                String password = rs.getString("password");
                Student s = new Student(caoNumber, dateOfBirth, password);
                students.add(s);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllStudents() " + e.getMessage());
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
                throw new DaoException("findAllStudents() " + e.getMessage());
            }
        }
        return students;     // may be empty
    }

    @Override
    public String register(int caoNumber, String dob, String password) throws DaoException
    {
//        int caoNumber = 0;
//        String password = null;
//            String dob = null;
        String output;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Student s = null;
        try
        {
            con = this.getConnection();

            String query = "INSERT INTO student (caonumber,dob,password) VALUES (?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, caoNumber);
            ps.setString(2, dob);
            ps.setString(3, password);
            String query2 = "SELECT * FROM student WHERE caonumber=\""+caoNumber+"\"";

            rs = ps.executeQuery(query2);
            int count = 0;
            while (rs.next())
            {
//                int cao_number = rs.getInt("CAO_NUMBER");
                int cao_number = rs.getInt("CAO_NUMBER");
                String dateOfBirth = rs.getString("DATE_OF_BIRTH");
                String pword = rs.getString("PASSWORD");
                s = new Student(cao_number, dateOfBirth, pword);
                if (cao_number == (caoNumber) && dateOfBirth.equals(dob) && pword.equals(password)){
                    count = count+1;
                }
//                int cao_number = rs.getInt("CAO_NUMBER");
//                String dateOfBirth = rs.getString("DATE_OF_BIRTH");
//                String pword = rs.getString("PASSWORD");
//                s = new Student(cao_number, dateOfBirth, pword);
            }
            if (count==0){
                int result = ps.executeUpdate();
                if (result==1){
                    output = "You are Successfully Registered.";
                }
                else{
                    output = "Your Registration Failed.";
                }
            }
            else{
                output= "CAO Numer is Already Registered.";
            }
        } catch (SQLException e)
        {
            throw new DaoException("register() " + e.getMessage());
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
                throw new DaoException("register() " + e.getMessage());
            }
        }
        return output;     // u may be null
    }

    @Override
    public boolean login(int caoNumber, String password) throws DaoException
    {
        String output;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Student s = null;
        boolean theresult = false;
        try
        {
            con = this.getConnection();

//            ps = con.prepareStatement(query);

            String query = "SELECT * FROM student WHERE caonumber=\""+caoNumber+"\" && password=\""+password+"\"";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery(query);
            int count = 0;
            while (rs.next()){
                count = count+1;
            }
            if (count>0){
                output = CAOService.SUCCESSFUL_LOGIN;
                theresult = true;

            }
            else{
                output = CAOService.FAILED_LOGIN;
                theresult = false;
            }
        } catch (SQLException e)
        {
            throw new DaoException("login() " + e.getMessage());
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
                throw new DaoException("login() " + e.getMessage());
            }
        }
        return theresult;     // u may be null
    }

    @Override
    public Student findStudentByCaoNumberPassword(int caoNumber, String password) throws DaoException
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Student s = null;
        try
        {
            con = this.getConnection();

            String query = "SELECT * FROM STUDENT WHERE CAONUMBER = ? AND PASSWORD = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, caoNumber);
            ps.setString(2, password);

            rs = ps.executeQuery();
            if (rs.next())
            {
                int cao_number = rs.getInt("CAO_NUMBER");
                String dateOfBirth = rs.getString("DATE_OF_BIRTH");
                String pword = rs.getString("PASSWORD");
                s = new Student(cao_number, dateOfBirth, pword);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findUserByUsernamePassword() " + e.getMessage());
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
                throw new DaoException("findUserByUsernamePassword() " + e.getMessage());
            }
        }
        return s;     // s may be null
    }

}