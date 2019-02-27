package com.aytel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhoneBookTest {

    private PhoneBook phoneBook;

    @BeforeEach
    void init() {
        phoneBook = new PhoneBook("test");
    }

    @Test
    void add() {
        assertFalse(phoneBook.add("a", "10"));
        assertTrue(phoneBook.add("a", "10"));
    }

    @Test
    void remove() {
        assertFalse(phoneBook.remove("a", "10"));
        phoneBook.add("a", "10");
        assertTrue(phoneBook.remove("a", "10"));
    }

    @Test
    void updateName() {
    }

    @Test
    void updateNumber() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getByName() {
    }

    @Test
    void getByNumber() {
    }
}