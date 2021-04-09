package com.dkit.oopca5.DAO;


import com.dkit.oopca5.DTO.Student;
import com.dkit.oopca5.Exceptions.DaoException;

import java.util.List;

public interface StudentDaoInterface
{
    public List<Student> findAllStudents() throws DaoException;

    public Student findStudentByCaoNumberPassword(int caonumber, String password) throws DaoException;

    public String register(int caonumber, String dob, String password) throws DaoException;

    public boolean login(int caonumber, String password) throws DaoException;
}
