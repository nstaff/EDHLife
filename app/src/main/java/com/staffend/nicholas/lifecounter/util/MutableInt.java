package com.staffend.nicholas.lifecounter.util;

/**
 * Created by Nicholas on 11/20/2015.
 */
public class MutableInt {
    private int i;

    public MutableInt(){
        this.i = 0;
    }

    public MutableInt(int i){
        this.i = i;
    }

    public int increment(){
        return this.i++;
    }

    public int decrement(){
        return this.i--;
    }

    public int getValue(){
        return this.i;
    }

    public Integer getInteger() {return new Integer(this.i);}
}
