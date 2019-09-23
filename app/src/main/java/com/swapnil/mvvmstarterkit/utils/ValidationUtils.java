package com.swapnil.mvvmstarterkit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isMobileNumberValid(String mobileNumber) {
        return mobileNumber.trim().length() >= 10 && mobileNumber.length() <= 15;
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        final String EMAIL_PATTERN = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{1,4}$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        return (password.length() > 5);
    }

    public static boolean isNameValid(String name) {
        boolean isValid = false;
        String expression = "[a-zA-Z][a-zA-Z ]*";
        CharSequence inputStr = name;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isDateValid(String date) {
        String regex = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();

    }

    public static boolean isValidGuardiansAge(String age) {
        if(!age.isEmpty()) {
            return calculateAge(age) > 17;
        }
        return false;
    }

    public static int calculateAge(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date birthDate = new Date();

        try {
            birthDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int years = 0;
        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTime(birthDate);
        //create calendar object for current day
        Calendar now = Calendar.getInstance();

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        return years;
    }

    public static boolean isPhoneNumberValid(String phoneNo) {
        if (phoneNo.trim().length() < 10) {
            return false;
        } else {
            return true;
        }
    }
}
