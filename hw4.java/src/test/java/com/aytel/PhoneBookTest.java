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
    void update() {
        phoneBook.add("a", "10");
        assertFalse(phoneBook.updateName("a", "10", "b"));
        phoneBook.add("a", "10");
        assertTrue(phoneBook.updateName("b", "10", "a"));
        List<Ownership> list = phoneBook.getAll();
        assertEquals(1, phoneBook.getAll().size());
        assertThrows(NoSuchElementException.class, () -> phoneBook.updateName("b", "10", "a"));
    }

    @Test
    void getAll() {
        List<Ownership> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            phoneBook.add("a", Integer.valueOf(i).toString());
            list.add(new Ownership("a", Integer.valueOf(i).toString()));
        }
        List<Ownership> listToCheck = phoneBook.getAll();
        listToCheck.sort(Comparator.comparing((Ownership a) -> a.number));
        assertEquals(list, listToCheck);
    }

    @Test
    void getByName() {
        List<Ownership> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            phoneBook.add("a", Integer.valueOf(i).toString());
            list.add(new Ownership("a", Integer.valueOf(i).toString()));
        }
        List<Ownership> listToCheck = phoneBook.getByName("a");
        listToCheck.sort(Comparator.comparing((Ownership a) -> a.number));
        assertEquals(list, listToCheck);
    }

    @Test
    void getByNumber() {
        List<Ownership> list = new ArrayList<>();
        list.add(new Ownership("a", "0"));
        for (int i = 0; i < 10; i++) {
            phoneBook.add("a", Integer.valueOf(i).toString());
        }
        List<Ownership> listToCheck = phoneBook.getByNumber("0");
        listToCheck.sort(Comparator.comparing((Ownership a) -> a.number));
        assertEquals(list, listToCheck);
    }
}