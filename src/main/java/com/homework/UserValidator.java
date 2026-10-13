package com.homework;

public class UserValidator {

    public static void validateEmail(String email) {
        if (email == null) {
            throw new NullPointerException("Email nie może być null");
        }
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Email nie może być pusty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email nie może nie zawierac '@'");
        }
    }
}