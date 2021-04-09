package com.dkit.oopca5.DAO;
import java.util.*;
import com.dkit.oopca5.DTO.Course;
import com.dkit.oopca5.Exceptions.DaoException;

import java.util.HashMap;

public interface StudentCourseInterfaceDao {
    public List<String> getStudentChoices(int cao)  throws DaoException;

    public void updatechoices(int cao) throws DaoException;

}
