package com.dkit.oopca5.client;

import com.dkit.oopca5.BusinessObjects.App;
import com.dkit.oopca5.DAO.MySqlCourseDao;
import com.dkit.oopca5.DAO.MySqlStudentCoursesDao;
import com.dkit.oopca5.core.CAOService;
import com.dkit.oopca5.Exceptions.DaoException;
import com.dkit.oopca5.Menus.CAOCourseMenu;
import com.dkit.oopca5.Menus.MainMenu;
import com.dkit.oopca5.DAO.MySqlStudentDao;

import java.io.*;
import java.net.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/*This class prints out the menu.*/

public class Client {
    private Scanner keyboard = new Scanner(System.in);
    private Socket dataSocket = new Socket(CAOService.HOSTNAME, CAOService.PORT_NUM);
    private String response = "";

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

//        try {
//
//            boolean loop = true;
//            MainMenu choice;
//            int getChoice;
//            while (loop) {
//                displayMainMenu();
//                getChoice = keyboard.nextInt();
//                keyboard.nextLine();
//                choice = MainMenu.values()[getChoice];
//                switch (choice) {
//                    case QUIT_APPLICATION:
//                        loop = false;
//                        break;
//                    case REGISTER:
//                        register();
//                        break;
//                    case LOGIN:
//                        login();
//                        break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try
        {
            //Step 1: Establish a connection to the server
            // Like a phone call, first need to dial the number
            // before you can talk
//            Socket dataSocket = new Socket(CAOService.HOSTNAME, CAOService.PORT_NUM);

            //Step 2: Build input and output streams
            OutputStream out = dataSocket.getOutputStream();
            //Decorator pattern
            PrintWriter output = new PrintWriter(new OutputStreamWriter(out));

            InputStream in = dataSocket.getInputStream();
            Scanner input = new Scanner(new InputStreamReader(in));

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
//                String response = "";

                switch(choice)
                {
                    case QUIT_APPLICATION:
                        message = CAOService.END_SESSION;
                        //send message
                        output.println(message);
                        output.flush();

                        //Listen for response
                        response = input.nextLine();
                        if(response.equals(CAOService.SESSION_TERMINATED))
                        {
                            System.out.println("Session ended");
                        }
                        break;
                    case REGISTER:
                        message = register(keyboard);

                        //send message
                        output.println(message);
                        output.flush();
                        //listen for response
                        response = input.nextLine();
                        System.out.println("Received response: " + response);
                        break;

                    case LOGIN:
                        message = login(keyboard);
                        //send message
                        output.println(message);
                        output.flush();
                        //listen for response
                        response = input.nextLine();  //Doesn't like .next OR .nextLine. Find out why.
                        System.out.println("Received response: " + response);
                        break;
                }
                if(response.equals(CAOService.UNRECOGNISED))
                {
                    System.out.println("Sorry, the request is not recognised.");
                }
            }
            System.out.println("Thanks for using the CAO Application App.");
            dataSocket.close();
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

        OutputStream out = dataSocket.getOutputStream();

        //Decorator pattern
        PrintWriter output = new PrintWriter(new OutputStreamWriter(out));

        InputStream in = dataSocket.getInputStream();
        Scanner input = new Scanner(new InputStreamReader(in));
        String message = "";
        boolean loop = true;
        CAOCourseMenu menuOption;
        int option;
        try
        {

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
                        //send message
                        output.println(message);
                        output.flush();

                        //Listen for response
                        response = input.nextLine();
                        if(response.equals(CAOService.SESSION_TERMINATED))
                        {
                            System.out.println("Session ended");
                        }
                        break;
                    case LOGOUT:
                        message = CAOService.LOGOUT;
                        //send message
                        output.println(message);
                        output.flush();
                        //listen for response
                        response = input.nextLine();
                        System.out.println("Received response: " + response);
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
                if(response.equals(CAOService.UNRECOGNISED))
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

        StringBuffer message = new StringBuffer(CAOService.LOGIN_COMMAND);
        String output = "";
        message.append(CAOService.BREAKING_CHARACTER);
        System.out.println("Enter CAO Number:\t");
        int caoNumber = input.nextInt();
        System.out.println("Enter Password:\t");
        String password = input.next();
        MySqlStudentDao login = new MySqlStudentDao();
        boolean cool = false;

//        if(login.login(caoNumber, password))
//        {
//            doAdminMenuLoop(login);
//        }
//        String result2 = login.login(caoNumber, password);
//        System.out.println(result2);
//        String result2 = login.login(caoNumber, password);
////        System.out.println(result2);
//        message.append(result2);
//        message.append(login);

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
//        message.append(login);

        boolean idc;
        String result2 = "";
        if(result2.equals(login.login(caoNumber, password)))
        {

            doAdminMenuLoop(login, caoNumber);
        }
//        String result2 = login.login(caoNumber, password);
        message.append(result2);

        return message.toString();

    }

    public String register(Scanner input) throws DaoException {

        StringBuffer message = new StringBuffer(CAOService.REGISTER_COMMAND);
        message.append(CAOService.BREAKING_CHARACTER);
//        System.out.print("Enter message to echo:> ");
        System.out.println("Enter CAO Number:");
        int caoNumber = input.nextInt();
        message.append(caoNumber);

        System.out.println("Enter Date of Birth:");
        String dateOfBirth = input.next();
        message.append(CAOService.BREAKING_CHARACTER);
        message.append(dateOfBirth);
        System.out.println("Enter Password:");
        String password = input.next();
        message.append(CAOService.BREAKING_CHARACTER);
        message.append(password);
        //String echo = input.nextLine();
        MySqlStudentDao register = new MySqlStudentDao();
        String result1 = register.register(caoNumber, dateOfBirth, password);
        message.append(result1);

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


















//    private static String generateEcho(Scanner keyboard)
//    {
//        StringBuffer message = new StringBuffer(ComboServiceDetails.ECHO);
//        message.append(ComboServiceDetails.COMMAND_SEPARATOR);
//        System.out.print("Enter message to echo:> ");
//        String echo = keyboard.nextLine();
//        message.append(echo);
//
//        return message.toString();
//    }
//
//    private static int getNumber(Scanner keyboard)
//    {
//        boolean numberEntered = false;
//        int number = 0;
//        while(!numberEntered)
//        {
//            try
//            {
//                number = keyboard.nextInt();
//                numberEntered = true;
//            }
//            catch(InputMismatchException e)
//            {
//                System.out.println("Please enter an integer (0-3)");
//                keyboard.nextLine();
//            }
//        }
//        keyboard.nextLine();
//        return number;
//    }
//
//    private static void displayMenu()
//    {
//        System.out.println("0) To quit");
//        System.out.println("1) To echo a message");
//        System.out.println("2) To get server date and time");
//        System.out.println("3) To get stats");
//    }
}
