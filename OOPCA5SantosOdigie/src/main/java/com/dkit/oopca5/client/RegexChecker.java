package com.dkit.oopca5.client;

/* This class should contain static methods to verify input in the application
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker
{


    public static boolean isValid(String text) {
        if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
            return false;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        try {
            df.parse(text);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }
    public static boolean iseightdigits(String number){
        if(number == null || !number.matches("\\d{8}"))
        {
            System.out.println("Please enter a 8 digit number");
            return false;
        }
        else
        {
            return true;
        }

    }

        public static boolean  isValidPassword(String password)
        {
            //one capital number and special character must be between 8 and 20

            String regex = "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";

            Pattern p = Pattern.compile(regex);


            if (password == null) {
                return false;
            }

            Matcher m = p.matcher(password);

            return m.matches();
        }

        public static boolean iscourseid(String courseid)
        {
            if(courseid.matches("[a-zA-Z]{2}\\d{3}"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }

}
