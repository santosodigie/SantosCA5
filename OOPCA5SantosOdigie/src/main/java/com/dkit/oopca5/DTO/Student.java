package com.dkit.oopca5.DTO;

public class Student {
    private int caoNumber;  // In the CAO system, cao number is unique identifier for student
    private String dateOfBirth; // dd-mm-yy
    private String password;    // min 8 characters



    // Constructor



    public Student(int caoNumber, String dateOfBirth, String password) {
        this.caoNumber = caoNumber;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
    }



    //public boolean verifyLoginCredentials( yyy-mm-dd, password);

    public int getCaoNumber() {
        return caoNumber;
    }

    public void setCaoNumber(int caoNumber) {
        this.caoNumber = caoNumber;
    }

    public String getDayOfBirth() {
        return dateOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dateOfBirth = dayOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        return "\nSTUDENT:" + "\n--------------------------------------------------" +
                "\ncaoNumber=" + caoNumber +
                "\ndateOfBirth='" + dateOfBirth + '\'' +
                "\npassword='" + password + '\'' +
                '}';
    }
}
