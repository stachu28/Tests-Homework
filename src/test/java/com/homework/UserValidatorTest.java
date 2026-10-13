package com.homework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {

    @Test
    void shouldNotThrowForValidEmail() {
        assertDoesNotThrow(
                () -> UserValidator.validateEmail("correct@example.com"));
    }

    @Test
    void shouldThrowWhenEmailHasNoAtSign() {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validateEmail("incorrect.example.com"));
    }

    @Test
    void shouldThrowForEmptyEmail() {
        assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validateEmail(""));
    }

    @Test
    void shouldReturnProperMessageWhenEmailValidationFailed() {
        NullPointerException nullPointerException = assertThrows(NullPointerException.class,
                () -> UserValidator.validateEmail(null));
        assertEquals("Email nie może być null", nullPointerException.getMessage());
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validateEmail(""));
        assertEquals("Email nie może być pusty", illegalArgumentException.getMessage());
        IllegalArgumentException illegalArgumentException2 = assertThrows(IllegalArgumentException.class,
                () -> UserValidator.validateEmail("bad.email.com"));
        assertEquals("Email nie może nie zawierac '@'", illegalArgumentException2.getMessage());

    }

}