package com.acecosmos.camle;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ace on 05-04-2017.
 */

public class Validation {



    public Validation(){}


    public boolean checkEmail(String email){

        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        Log.d("com.acecosmos.camle","Reached here!!!!!!!!!!!!!!!!");
        return matcher.find();
    }

    public boolean checkMobile(String num){
        Log.d("com.acecosmos.camle",num+num.length());
        if(num.length() == 10){
            Pattern MOBILE_NUMBER_REGEX =
                    Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);

            Matcher matcher = MOBILE_NUMBER_REGEX .matcher(num);
            return matcher.find();

        }else {
            return false;
        }
    }

    public boolean isSet3Chars(String s){
        if(s.length()>2){
            return true;
        }
        else{
            return false;
        }
    }




}
