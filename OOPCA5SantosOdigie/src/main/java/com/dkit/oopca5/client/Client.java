package com.dkit.oopca5.client;

import com.dkit.oopca5.BusinessObjects.App;
import com.dkit.oopca5.DAO.MySqlCourseDao;
import com.dkit.oopca5.DAO.MySqlStudentCoursesDao;
import com.dkit.oopca5.core.CAOService;
import com.dkit.oopca5.Exceptions.DaoException;
import com.dkit.oopca5.Menus.CAOCourseMenu;
import com.dkit.oopca5.Menus.MainMenu;
import com.dkit.oopca5.DAO.MySqlStudentDao;
import com.dkit.oopca5.client.RegexChecker;

import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/*This class prints out the menu.*/

public class Client {
    Scanner keyboard = new Scanner(System.in);
    Socket socket = new Socket("localhost", 8080);


    public Client() throws IOException {
    }

    public static void main(String[] args) throws IOException {

        new Client().start();

    }
    private void start() {

        doMainMenuLoop();
    }

    public void doMainMenuLoop()
    {

        try
        {

            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);
            Scanner socketReader = new Scanner(socket.getInputStream());

//            Scanner keyboard = new Scanner(System.in);

            String message = "";
            MainMenu choice;
            int getChoice;

            while(!message.equals(CAOService.END_SESSION))
            {
                displayMainMenu();
                getChoice = keyboard.nextInt();
                keyboard.nextLine();
                choice = MainMenu.values()[getChoice];


                switch(choice)
                {

                    case QUIT_APPLICATION:
                        message = CAOService.END_SESSION;
                        socketWriter.println(message);


                        System.out.println("reply from Server: " + socketReader.nextLine());

                        break;
                    case REGISTER:
                        message = register(keyboard);
                        socketWriter.println(message);

                        System.out.println("reply: " + socketReader.nextLine());
                        break;

                    case LOGIN:
                        message = login(keyboard);
                        socketWriter.println(message);

                        System.out.println("reply: " + socketReader.nextLine());
                        break;
                }
                if(message.equals(CAOService.UNRECOGNISED))
                {
                    System.out.println("Sorry, the request is not recognised.");
                }
            }
            System.out.println("Thanks for using the CAO Application App.");
            socket.close();
        }
        catch (UnknownHostException e)
        {
            System.out.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        } catch (DaoException throwables) {
            throwables.printStackTrace();
        }
    }

    private void doAdminMenuLoop(MySqlStudentDao loginResult, int caoNum) throws IOException {

        OutputStream out = socket.getOutputStream();

        //Decorator pattern
        PrintWriter output = new PrintWriter(new OutputStreamWriter(out));

        InputStream in = socket.getInputStream();
        Scanner input = new Scanner(new InputStreamReader(in));
        String message = "";
        boolean loop = true;
        CAOCourseMenu menuOption;
        int option;
        try
        {
            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);
            Scanner socketReader = new Scanner(socket.getInputStream());

            while(!message.equals(CAOService.LOGOUT))
            {
                printCaoCourseMenu();

                option = keyboard.nextInt();
                keyboard.nextLine();
                menuOption = CAOCourseMenu.values()[option];
                switch (menuOption)
                {
                    case QUIT:
                        message = CAOService.END_SESSION;


                        break;
                    case LOGOUT:
                        message = CAOService.LOGOUT;
                        socketWriter.println(message);
                        System.out.println("reply from Server: " + socketReader.nextLine());
                        doMainMenuLoop();
                        break;
                    case DISPLAY_COURSE:
                        message = CAOService.DISPLAY_COURSE;
                        output.println(message);
                        output.flush();


                        System.out.println("Enter Course ID");
                        String courseID = keyboard.nextLine();
                        MySqlCourseDao getacourse = new MySqlCourseDao();
                        getacourse.displayCourse(courseID);


                        break;
                    case DISPLAY_ALL_COURSES:
                        MySqlCourseDao allcourse = new MySqlCourseDao();
                        allcourse.displayAllCourses();
//                        displayAllCourses();
                        break;
                    case DISPLAY_CURRENT_CHOICES:
                        MySqlStudentCoursesDao current = new MySqlStudentCoursesDao();
                        current.getStudentChoices(caoNum);
                        break;
                    case UPDATE_CURRENT_CHOICES:
                        MySqlStudentCoursesDao pickchoices = new MySqlStudentCoursesDao();
                        pickchoices.updatechoices(caoNum);
                        break;
                }
                if(message.equals(CAOService.UNRECOGNISED))
                {
                    System.out.println("Sorry, the request is not recognised.");
                }
            }


        }
        catch (InputMismatchException ime)
        {
            System.out.println("InputMismatchException. The token retrieved does not match the pattern for the expected type. Please enter a valid option");
            keyboard.nextLine();
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            System.out.println("ArrayIndexOutOfBoundsException. The index is either negative or greater than or equal to the size of the array.");
            keyboard.nextLine();
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("IllegalArgumentException. A method has been passed an illegal argument. Try again");
            keyboard.nextLine();
        } catch (DaoException throwables) {
            throwables.printStackTrace();
        }
    }


    private String login(Scanner input) throws DaoException, IOException {
        System.out.println("Login: ");


        String output = "";

        System.out.println("Enter CAO Number:\t");
        int caoNumber = input.nextInt();
        System.out.println("Enter Password:\t");
        String password = input.next();
        MySqlStudentDao login = new MySqlStudentDao();
        boolean cool = false;

        if(login.login(caoNumber, password))
        {
            output = CAOService.SUCCESSFUL_LOGIN;
            if(output == CAOService.SUCCESSFUL_LOGIN)
            {
                cool = true;
            }
            while(cool = true) {

//            output = CAOService.SUCCESSFUL_LOGIN;
                System.out.println("Logged in");
                doAdminMenuLoop(login, caoNumber);
            }
        }

        String message = CAOService.LOGIN_COMMAND + CAOService.BREAKING_CHARACTER + caoNumber + CAOService.BREAKING_CHARACTER + password;

        return message.toString();

    }

    public String register(Scanner input) throws DaoException {

        boolean r;
        r = false;
        int caoNumber=0;

        while(r == false) {
            System.out.println("Enter CAO Number:");
            caoNumber = input.nextInt();
            if(RegexChecker.iseightdigits(Integer.toString(caoNumber)))
            {
                r = true;
            }

        }

        boolean s;
        s = false;
        String dateOfBirth="";
        while(s == false) {
            System.out.println("Enter Date of Birth:");
            dateOfBirth = input.next();
            if(RegexChecker.isValid(dateOfBirth))
            {
                s = true;
            }

        }

        boolean a;
        a = false;
        String password="";
        while(a == false) {
            System.out.println("Enter Password:");
            password = input.next();
            if(RegexChecker.isValidPassword(password))
            {
                a = true;
            }

        }
        MySqlStudentDao register = new MySqlStudentDao();
        register.register(caoNumber, dateOfBirth, password);

        String message = CAOService.REGISTER_COMMAND + CAOService.BREAKING_CHARACTER + caoNumber + CAOService.BREAKING_CHARACTER + dateOfBirth + CAOService.BREAKING_CHARACTER + password;


        return message.toString();
    }

    private void displayMainMenu()
    {
        System.out.println("\n Options to select:");
        for(int i=0; i < MainMenu.values().length;i++)
        {
            System.out.println("\t"  + i + ". " + MainMenu.values()[i].toString());
        }
        System.out.print("Enter a number to select option (enter 0 to quit):>");
    }

    private void printCaoCourseMenu()
    {
        System.out.println("CAO Course Menu.");
        System.out.println("\n Select One The Following Options: ");
        for(int i = 0; i < CAOCourseMenu.values().length; i++)
        {
            System.out.println("\t" + i + ". " + CAOCourseMenu.values()[i].toString());
        }
        System.out.print("Enter a number to select option (enter 0 to quit):>");
    }
}