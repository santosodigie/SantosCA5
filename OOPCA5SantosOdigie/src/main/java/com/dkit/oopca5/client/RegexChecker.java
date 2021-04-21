package com.dkit.oopca5.client;

/* This class should contain static methods to verify input in the application
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker
{
    public static void main(String[] args) {
        String test = "2000-09-40";
        if(isValidPassword("Flasher@212"))
        {
            System.out.println("1 step closer");
        }
        else
        {
            System.out.println("wham");
        }

    }


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

    // Java program to validate
// the password using ReGex


        // Function to validate the password.
        public static boolean  isValidPassword(String password)
        {

            // Regex to check valid password.
            String regex = "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";

            // Compile the ReGex
            Pattern p = Pattern.compile(regex);

            // If the password is empty
            // return false
            if (password == null) {
                return false;
            }

            // Pattern class contains matcher() method
            // to find matching between given password
            // and regular expression.
            Matcher m = p.matcher(password);

            // Return if the password
            // matched the ReGex
            return m.matches();
        }

        // Driver Code.



}
