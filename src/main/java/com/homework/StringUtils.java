package com.homework;

public class StringUtils {

    public static boolean isPalindrome(String text) {
        if (text == null || text.isEmpty()) {
            return true;
        }

        String normalized = text.toLowerCase();
        int left = 0;
        int right = normalized.length() - 1;

        while (left < right) {
            if (normalized.charAt(left) != normalized.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}