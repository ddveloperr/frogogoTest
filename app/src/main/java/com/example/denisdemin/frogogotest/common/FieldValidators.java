package com.example.denisdemin.frogogotest.common;

import java.util.regex.Pattern;

public class FieldValidators {

    private static final Pattern VALID_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isFieldValid(String text) {
        return !(text.trim().length() == 0 || text.isEmpty() || text.equals(""));
    }
    public static boolean isEmailFieldValid(String email){
        return VALID_EMAIL.matcher(email).find();
    }
}
