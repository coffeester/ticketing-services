package com.coffeester.ticketing.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amitsehgal on 1/31/16.
 */
public class Validation {

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence charSequence = email;

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(charSequence);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
