package com.staffend.nicholas.lifecounter.util;

import android.widget.TextView;

import com.staffend.nicholas.lifecounter.models.Player;

import java.util.ArrayList;

/**
 * Created by Nicholas on 12/10/2015.
 */
public class Util {
    public Util(){};

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

    public static String[] makeArrayFromArrayList(ArrayList<String> list){
        String[] array = new String[list.size()];
        array = list.toArray(array);
        return array;
    }

    public static String[] makeStringArrayFromPlayerList(ArrayList<Player> list){
        String[] array = new String[list.size()];
        for(int i =0; i < array.length; i++){
            array[i] = list.get(i).toString();
        }
        return array;
    }

    public static long[] getIdArrayFromPlayersList(ArrayList<Player> list){
        long[] array = new long[list.size()];
        for(int i = 0; i < array.length;i++){
            array[i] = list.get(i).getId();
        }
        return array;
    }
    public static void showEditTextDialog(String message){
        
    }
}
