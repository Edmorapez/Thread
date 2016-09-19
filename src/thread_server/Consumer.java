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
public class Consumer implements Runnable {
    Workpile workpile;
    
    public Consumer(Workpile w) {
        workpile = w;
    }
    public void run() {
        while (true) {
            workpile.mutex.lock();
            while (workpile.empty()) {
                workpile.consumerCV.condWait(workpile.mutex);
            }
            Request request = workpile.remove();
            workpile.mutex.unlock();
            
            workpile.producerCV.condSignal();
            request.process();
        }
    }
}