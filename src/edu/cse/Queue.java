package edu.cse;

import java.util.Arrays;

public class Queue {
    public String queue[] = new String[100];
    private int putNumber;
    private int getNumber;
    Queue() { }
    synchronized boolean put(String input) {
        if (queue[putNumber] == null) {
            queue[putNumber] = input;
            if (putNumber < 99) {
                putNumber++;
                System.out.println(Arrays.toString(queue));
            } else {
                putNumber = 0;
                System.out.println(Arrays.toString(queue));
            }
            return true;
        } else {
            return false;
        }
    }
    synchronized String[] getValues(){
        if (queue[getNumber] != null) {
            String[] arrayToReturn = new String[2];
            arrayToReturn[0]= queue[getNumber];
            arrayToReturn[1]= "true";
            if(putNumber<99) {
                getNumber++;
            } else {
                getNumber=0;
            }
            if (getNumber>0) {
                queue[getNumber - 1] = null;
            }
            return arrayToReturn;
            //need to separate this into new function probably
        } else {
            return null;
        }
    }
}
