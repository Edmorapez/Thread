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
import Extensions.*;
public class Producer implements Runnable {
    
    Workpile workpile;
    Client client;
    public Producer(Workpile w, Client c) {
        workpile = w;
        client = c;
    }
    public void run() {
        String selfName = Thread.currentThread().getName();
         try {
             for (int i = 0; true; i++) {
                 Request request = client.read();
             
         if (request.string.startsWith("End")) { 
             client.decrementOutstandingRequests();
             client.waitForOutstandingRequests();
             client.os.write(request.bytes);
             client.socket.close();
             System.out.println("Server[" + selfName +"]\tCompleted processing.");
             InterruptibleThread.exit();
         }
         workpile.mutex.lock();
         
         while (workpile.full()) {
             workpile.producerCV.condWait(workpile.mutex);
         }
         workpile.add(request);
         workpile.mutex.unlock();
         workpile.consumerCV.condSignal();
             
         }
         } catch (IOException e) {
             
         try {
             client.socket.close();
         } catch (IOException ioe) {
         }
         System.out.println("Server[" + selfName +"]\tException during processing." + e);
         InterruptibleThread.exit();
    
         
         
         }
         
         
    }///run
}