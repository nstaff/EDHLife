package com.staffend.nicholas.lifecounter.util;

import android.widget.TextView;

import com.staffend.nicholas.lifecounter.models.Player;

import java.util.ArrayList;

/**
 * Generic utility class for common use items
 * Code review notes 8/29/2016: These methods should be removed from here and objects built as necessary
 * Created by Nicholas on 12/10/2015.
 */
public class Util {
    public Util(){};

    /**
     * Changes the value of a text view that is used as a life counter.
     * @param change the number to change the value by
     * @param txv the text view to change
     * @return the final value
     */
    public static int changeNumericTextViewValue(int change, TextView txv){
        int value = 0;
        try{
            value = Integer.parseInt(String.valueOf(txv.getText()));
            value += change;
            txv.setText(String.valueOf(value));

        }catch (NumberFormatException e){
            value = 0;
        }
        finally {
            return value;
        }
    }

    /**
     * Method to get id numbers of players from a list of players
     * Code review notes: Refactor into a PlayerList class whcih extends array list
     * @param list The array list of players to get IDs from
     * @return an array of id numbers
     */
    public static long[] getIdArrayFromPlayersList(ArrayList<Player> list){
        long[] array = new long[list.size()];
        for(int i = 0; i < array.length;i++){
            array[i] = list.get(i).getId();
        }
        return array;
    }
}
