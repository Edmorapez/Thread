/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
    package thread_server;

    import java.io.*;
    import java.net.*;
    import Extensions.*;
    
    public class Server {
    ServerSocket serverSocket;
    static int port = 6500;
    static int delay = 10;
    static int spin = 10;
    static boolean DEBUG = false;
    static int nConsumers = 10;
    static int MAX_LENGTH = 10;
    static boolean KILL = false;
    public static void main(String[] args) {
        Server server = new Server();
    
    Thread t;
    int stopperTimeout = 10;
    // 10s
    if (args.length > 0) {
        port = Integer.parseInt(args[0]);
    }
    if (args.length > 1) {
        delay = Integer.parseInt(args[1]);
    }
    if (args.length > 2) {
        spin = Integer.parseInt(args[2]);
    } 
     if (args.length > 3) { 
        nConsumers = Integer.parseInt(args[3]);
    } 
    if (args.length > 4) {
        stopperTimeout = Integer.parseInt(args[4]);
    }
    if (System.getProperty("DEBUG") != null) { 
        DEBUG = true;
    } 
    if (System.getProperty("KILL") != null) { 
        KILL = true;
    }
    System.out.println("Server(port: " + port +" delay: " + delay + "ms spin: " + spin +"us nConsumers: " + nConsumers + " stopperTimeout " +stopperTimeout + "s)");
    if (KILL) {
        new Thread(new Killer(120)).start();
    }
    server.runServer();
    }
    
    
    public void runServer() {// Executes in main thread
        Socket socket;
        Workpile workpile = new Workpile(MAX_LENGTH);
        try {
            serverSocket = new ServerSocket(port);
        
        System.out.println("Server now listening on port " +port);
        for (int i = 1; i < nConsumers; i++) {
            Thread t = new Thread(new Consumer(workpile));
            t.start();
        }
        while (true) {
            socket = serverSocket.accept();
            Client client = new Client(socket);
            Thread t = new Thread(new Producer(workpile, client));
            t.start();
            System.out.println("Server[" + t.getName() +"]\tStarted new client: " + client);
            }
        } catch (IOException e) {
                
            System.out.println("Cannot get I/O streams in newClient()" + e);}
    }
        public Server() {}
        
    
    }