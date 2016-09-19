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

import java.awt.List;


public class Workpile {
    List list = List.nil;
    int length = 0;
    static int max = 10; 
    Mutex mutex = new Mutex();
    ConditionVar producerCV = new ConditionVar();
    ConditionVar consumerCV = new ConditionVar();
    boolean stop = false;
    
    
    
    public Workpile(int i) {
        max = i;
    }
    public void add(Request request) {
            list = list.cons(request);
            length++;
    }
    public Request remove() {
    Request request = (Request) list.first;
    list = list.next;
    length--;
    return request;
    }
    
    public boolean empty() {
        return length == 0;
    } 
    public boolean full() { 
        return length == max;
    }
}
