/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thread_server;

/**
 *
 * @author Edgar1
 */
import java.io.*;
import java.net.*;


public class Client {
    
        int outstandingRequests = 0;
        Socket socket;
        InputStream  is;
        OutputStream os;
        int delay = 10;
        int count = 0;
        static int total = 0;
        int MessageLength = 70;
    
    public String toString() {
        return("<Client: " + count +">");
    }
    
    
    public Client(Socket s) throws IOException {
        socket = s;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        
        synchronized (getClass()) {
            total++;
            count = total;
        }
    }
    public Request read() throws IOException {
        byte[] b = new byte[MessageLength];
        int n = is.read(b);
        if (n != MessageLength)throw new IOException(this + "Read too few characters " +n);
        incrementOutstandingRequests();
        return new Request(this, b);
    }
    // Methods something like these might be useful...  :-)
    public synchronized void incrementOutstandingRequests() {
        outstandingRequests++;
    }
    public synchronized void decrementOutstandingRequests() {
        outstandingRequests--;
    if (outstandingRequests == 0)
        notifyAll();     
    }
    public synchronized void waitForOutstandingRequests() {
        boolean interrupted=false;
    while (outstandingRequests != 0) {
        try {
            wait();     
             } catch (InterruptedException e) {
                 interrupted = true;
             }
        if (interrupted)
            Thread.currentThread().interrupt();
        }
    }
}

