package com.aytel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PhoneBookTest {
    private PhoneBook phoneBook;

    @BeforeEach
    void init() {
        phoneBook = new PhoneBook("test");
    }

    @AfterEach
    void clear() {
        phoneBook.clear();
    }

    @Test
    void add() {
        assertTrue(phoneBook.add("a", "10"));
        assertFalse(phoneBook.add("a", "10"));
    }

    @Test
    void remove() {
        assertFalse(phoneBook.remove("a", "10"));
        phoneBook.add("a", "10");
        assertTrue(phoneBook.remove("a", "10"));
    }

    @Test
    void updateName() {
        phoneBook.add("a", "10");
        assertTrue(phoneBook.updateName("a", "10", "b"));
        phoneBook.add("a", "10");
        assertFalse(phoneBook.updateName("b", "10", "a"));
        assertEquals(1, phoneBook.getAll().size());
        assertThrows(NoSuchElementException.class, () -> phoneBook.updateName("b", "10", "a"));
    }

    @Test
    void updateNumber() {
        phoneBook.add("a", "10");
        assertTrue(phoneBook.updateNumber("a", "10", "20"));
        phoneBook.add("a", "10");
        assertFalse(phoneBook.updateNumber("a", "20", "10"));
        assertEquals(1, phoneBook.getAll().size());
        assertThrows(NoSuchElementException.class, () -> phoneBook.updateNumber("b", "10", "a"));
    }

    @Test
    void getAll() {
        var list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            phoneBook.add("a", Integer.valueOf(i).toString());
            list.add(new Ownership("a", Integer.valueOf(i).toString()));
        }
        var listToCheck = phoneBook.getAll();
        listToCheck.sort(Comparator.comparing((Ownership a) -> a.phoneNumber));
        assertEquals(list, listToCheck);
    }

    @Test
    void getByName() {
        var list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            phoneBook.add("a", Integer.valueOf(i).toString());
            list.add(new Ownership("a", Integer.valueOf(i).toString()));
        }
        var listToCheck = phoneBook.getByName("a");
        listToCheck.sort(Comparator.comparing((Ownership a) -> a.phoneNumber));
        assertEquals(list, listToCheck);
    }

    @Test
    void getByNumber() {
        var list = new ArrayList<>();
        list.add(new Ownership("a", "0"));
        for (int i = 0; i < 10; i++) {
            phoneBook.add("a", Integer.valueOf(i).toString());
        }
        var listToCheck = phoneBook.getByNumber("0");
        listToCheck.sort(Comparator.comparing((Ownership a) -> a.phoneNumber));
        assertEquals(list, listToCheck);
    }
}