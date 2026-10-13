package com.homework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringUtilsTest {

    @Test
    void shouldReturnTrueForPalindrome() {
        assertTrue(StringUtils.isPalindrome("kajak"));
    }

    @Test
    void shouldIgnoreCase() {
        assertTrue(StringUtils.isPalindrome("Kajak"));
    }

    @Test
    void shouldReturnFalseForNonPalindrome() {
        assertFalse(StringUtils.isPalindrome("java"));
    }

    @Test
    void shouldTreatEmptyStringAsPalindrome() {
        assertTrue(StringUtils.isPalindrome(""));
    }

    @Test
    void shouldTreatNullAsPalindrome() {
        assertTrue(StringUtils.isPalindrome(null));
    }
}