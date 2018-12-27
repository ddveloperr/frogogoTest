package com.example.denisdemin.frogogotest.common;

import android.util.Patterns;

import java.util.regex.Pattern;

public class FieldValidators {

    private static final Pattern VALID_EMAIL = Pattern.compile(Patterns.EMAIL_ADDRESS.pattern(), Pattern.CASE_INSENSITIVE);

    public static boolean isFieldValid(String text) {
        return !(text.trim().length() == 0 || text.isEmpty() || text.equals(""));
    }
    public static boolean isEmailFieldValid(String email){
        return VALID_EMAIL.matcher(email).find();
    }
}
