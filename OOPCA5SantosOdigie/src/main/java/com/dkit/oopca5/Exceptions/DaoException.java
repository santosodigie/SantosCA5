package com.dkit.oopca5.Exceptions;

/**
 * A 'homemade' Exception to report exceptions
 *  arising in the the Data Access Layer.
 */
import java.sql.SQLException;

import java.sql.SQLException;

public class DaoException extends SQLException
{
    public DaoException()
    {

    }
    public DaoException(String aMessage)
    {
        super(aMessage);
    }
}
