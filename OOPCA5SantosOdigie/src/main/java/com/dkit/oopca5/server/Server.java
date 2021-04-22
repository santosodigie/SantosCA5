package com.dkit.oopca5.server;

/* The server package should contain all code to run the server. The server uses TCP sockets and thread per client.
 The server should connect to a MySql database to register clients, allow them to login and choose courses
 The server should listen for connections and once a connection is accepted it should spawn a new CAOClientHandler thread to deal with that connection. The server then returns to listening
 */
import com.dkit.oopca5.core.CAOService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;



public class Server
{
    public static void main(String[] args)
    {
        Server server = new Server();
        server.start();
    }

    public void start()
    {
        try
        {
            ServerSocket ss = new ServerSocket(8080);  // set up ServerSocket to listen for connections on port 8080

            System.out.println("Server: Server started. Listening for connections on port 8080...");

            int clientNumber = 0;  // a number for clients that the server allocates as clients connect

            while (true)    // loop continuously to accept new client connections
            {
                Socket socket = ss.accept();    // listen (and wait) for a connection, accept the connection,
                // and open a new socket to communicate with the client
                clientNumber++;

                System.out.println("Server: Client " + clientNumber + " has connected.");

                System.out.println("Server: Port# of remote client: " + socket.getPort());
                System.out.println("Server: Port# of this server: " + socket.getLocalPort());

                Thread t = new Thread(new ClientHandler(socket, clientNumber)); // create a new ClientHandler for the client,
                t.start();                                                  // and run it in its own thread

                System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                System.out.println("Server: Listening for further connections...");
            }
        } catch (IOException e)
        {
            System.out.println("Server: IOException: " + e);
        }
        System.out.println("Server: Server exiting, Goodbye!");
    }

    public class ClientHandler implements Runnable   // each ClientHandler communicates with one Client
    {
        BufferedReader socketReader;
        PrintWriter socketWriter;
        Socket socket;
        int clientNumber;

        public ClientHandler(Socket clientSocket, int clientNumber)
        {
            try
            {
                InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
                this.socketReader = new BufferedReader(isReader);

                OutputStream os = clientSocket.getOutputStream();
                this.socketWriter = new PrintWriter(os, true); // true => auto flush socket buffer

                this.clientNumber = clientNumber;  // ID number that we are assigning to this client

                this.socket = clientSocket;  // store socket ref for closing

            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            String message;
            try
            {
                while ((message = socketReader.readLine()) != null || message.startsWith("QUIT"))
                {
                    System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + message);


                    if(message.startsWith("REGISTER"))
                    {
                        System.out.println("Client has called you to Register with "+message);

                        socketWriter.println("REGISTERED");
                    }
                    if(message.startsWith("LOGIN"))
                    {
                        socketWriter.println("LOGGED IN");
                    }
                    else if(message.startsWith("DISPLAY COURSE"))
                    {
                        socketWriter.println("Course Displayed");
                    }
                    else if(message.startsWith("DISPLAY ALL"))
                    {
                        socketWriter.println("ALL COURSES DISPLAYED");
                    }
                    else if(message.startsWith("DISPLAY CURRENT"))
                    {
                        System.out.println("Displayed current");
                    }
                    else if(message.startsWith("UPDATE CURRENT"))
                    {
                        System.out.println("Updated Current");
                    }

                    else
                    {
                        socketWriter.println("I'm sorry I don't understand :(");

                    }
                }

                socket.close();

            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }
    }

}















































//public class CAOServer
//{
//    public static void main(String[] args)
//    {
//        try
//        {
//            //Step 1: Set up a listening socket - this just listens for connections
//            //Once a socket connects we create a dataSocket for the rest of the
//            //communication
//            ServerSocket listeningSocket = new ServerSocket(CAOService.PORT_NUM);
//            Socket dataSocket = new Socket();
//
//            boolean continueRunning = true;
//
//            while(continueRunning)
//            {
//                //Step 2
//                //Once a connection is accepted on the listening socket
//                //spawn a dataSocket for the rest of the communication
//                dataSocket = listeningSocket.accept();
//
//                //Step 3 - set up input and output streams
//                OutputStream out = dataSocket.getOutputStream();
//                //Decorator pattern
//                PrintWriter output = new PrintWriter(new OutputStreamWriter(out));
//
//                InputStream in = dataSocket.getInputStream();
//                Scanner input = new Scanner(new InputStreamReader(in));
//
//                String incomingMessage = "";
//                String response;
//
//                while(true)
//                {
//
//                    response = null;
//
//                    //Take the information from the client
//                    incomingMessage = input.nextLine();
//                    System.out.println("Received message: " + incomingMessage);
//
//                    String[] messageComponents = incomingMessage.split(CAOService.BREAKING_CHARACTER);
//                    //echo%%Hello World, echo%%Hello%%World
//                    //0%%1                  0%%1%%2
//                    if (messageComponents[0].equals(CAOService.REGISTER_COMMAND))
//                    {
//                        StringBuffer echoMessage = new StringBuffer("");
//                        if (messageComponents.length > 1)
//                        {
//                            echoMessage.append(messageComponents[1]);
//
//                        }
//                        for (int i = 2; i < messageComponents.length; i++)
//                        {
//                            echoMessage.append(CAOService.BREAKING_CHARACTER);
//                            echoMessage.append(messageComponents[i]);
//                        }
//                        response = echoMessage.toString();
//                    }
//                    else if (messageComponents[0].equals(CAOService.SUCCESSFUL_REGISTER))
//                    {
//                        response = new Date().toString();
//                    }
//                    else if (messageComponents[0].equals(CAOService.FAILED_REGISTER))
//                    {
//                        //TODO Home work
//                        response = "This has not been implemented yet. This is for homework";
//                    }
//                    else if (messageComponents[0].equals(CAOService.END_SESSION))
//                    {
//                        response = CAOService.SESSION_TERMINATED;
//                    }
//                    else
//                    {
//                        response = CAOService.UNRECOGNISED;
//                    }
//
//                    //Send response back
//                    output.println(response);
//                    output.flush();
//                }
//
//            }
//        }
//        catch (IOException e)
//        {
//            System.out.println(e.getMessage());;
//        }
//        catch(NoSuchElementException e)
//        {
//            System.out.println("Shutting down. I think we need threads");
//            System.exit(1);
//        }
//    }
