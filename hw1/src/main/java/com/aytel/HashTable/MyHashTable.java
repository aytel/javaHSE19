package com.aytel.HashTable;

import java.util.Arrays;
import java.util.Map.Entry;

/**
 * A dictionary which uses String both as key and a value.
 * Allows to add, remove or modify elements.
 * Using separate chaining with linked list.
 * Rehashing occurs when number of elements exceeds number of buckets multiplied by (1 + LOAD_FACTOR),
 * which default value is 0.5.
 */
public class MyHashTable {
    private MyListOfPairsStringString[] array;
    private int size;
    private final double LOAD_FACTOR;
    private final int initCapacity;

    /**
     * Makes a hashtable with empty lists.
     * @param loadFactor coefficient of rehashing, must be not less than 0
     * @param capacity number of buckets, must be more than 0
     * @throws IllegalArgumentException if loadFactor < 0 or capacity <= 0.
     */
    public MyHashTable(double loadFactor, int capacity) throws IllegalArgumentException {
        if (loadFactor < 0) {
            throw new IllegalArgumentException("loadFactor must be not less than 0");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be more than 0");
        }
        this.LOAD_FACTOR = loadFactor;
        this.initCapacity = capacity;
        this.array = new MyListOfPairsStringString[initCapacity];
        Arrays.setAll(this.array, i -> new MyListOfPairsStringString());
        this.size = 0;
    }

    public MyHashTable() {
        this(0.5, 10);
    }

    private int hash(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Key must be not null");
        }
        return Math.abs(key.hashCode()) % array.length;
    }

    private void rebuild() {
        var newArray = new MyListOfPairsStringString[2 * array.length];
        Arrays.setAll(newArray, i -> new MyListOfPairsStringString());

        var oldArray = array;
        array = newArray;

        size = 0;
        for (var current : oldArray) {
            for (var entry = current.pop(); entry != null; entry = current.pop()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /** Returns number of elements in hashtable. */
    public int size() {
        return size;
    }

    /**
     * Checks if hashtable contains the element.
     * @return true in case there is an element with the given key in the hashtable and false otherwise
     * @throws IllegalArgumentException if element is null.
     */
    public boolean contains(String key) throws IllegalArgumentException {
        return array[hash(key)].contains(key);
    }

    /**
     * Gets the value of the given key.
     * @return value in case there is an element with the given key in the hashtable and null otherwise
     * @throws IllegalArgumentException if key is null.
     */
    public String get(String key) throws IllegalArgumentException {
        return array[hash(key)].get(key);
    }

    /**
     * Puts a pair (key, value) to the hashtable.
     * @return previous value in case there was an element with the given key in the hashtable and nul otherwise
     * @throws IllegalArgumentException if key or value is null.
     */
    public String put(String key, String value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Value must be not null");
        }

        String foundValue = array[hash(key)].put(key, value);

        if (foundValue == null) {
            size++;
            if (size > array.length * (1 + LOAD_FACTOR)) {
                rebuild();
            }
        }

        return foundValue;
    }

    /**
     * Removes element with the given key from the hashtable if such exists.
     * @return value in case there was an element with the given key in the hashtable and null otherwise
     * @throws IllegalArgumentException if key is null.
     */
    public String remove(String key) throws IllegalArgumentException {
        String foundValue = array[hash(key)].remove(key);

        if (foundValue != null) {
            size--;
        }

        return foundValue;
    }

    /** Removes all elements from the hashtable and sets its size to 0. */
    public void clear() {
        array = new MyListOfPairsStringString[initCapacity];
        Arrays.setAll(array, i -> new MyListOfPairsStringString());
        size = 0;
    }
}
