package HashTable;

import java.util.Arrays;
import java.util.Map.Entry;

public class MyHashTable {
    private MyListPairStringString[] array;
    private int size;
    private final double ALPHA = 0.5;

    MyHashTable() {
        this.array = new MyListPairStringString[1];
        this.array[0] = new MyListPairStringString();
        this.size = 0;
    }

    private int hash(String key) {
        return key.hashCode() % array.length;
    }

    private void rebuild() {
        MyListPairStringString[] newArray = new MyListPairStringString[2 * array.length];
        Arrays.setAll(newArray, i -> new MyListPairStringString());

        MyListPairStringString[] oldArray = array;
        array = newArray;

        size = 0;
        for (MyListPairStringString cur : oldArray) {
            for (Entry<String, String> entry = cur.pop(); entry != null; entry = cur.pop()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * @return number of elements in table
     */
    public int size() {
        return size;
    }

    /**
     * @param key
     * @return true in case there is an element with the given key in the table and false otherwise
     */
    public boolean contains(String key) {
        return array[hash(key)].contains(key);
    }

    /**
     * @param key
     * @return value in case there is an element with the given key in the table and null otherwise
     */
    public String get(String key) {
        return array[hash(key)].get(key);
    }

    /**
     * Puts a pair (key, value) to the table.
     * @param key
     * @param val
     * @return previouus value in case there was an element with the given key in the table and nul otherwise
     */
    public String put(String key, String val) {
        String ret = array[hash(key)].put(key, val);

        if (ret == null) {
            size++;
            if (size > array.length * (1 + ALPHA)) {
                rebuild();
            }
        }

        return ret;
    }

    /**
     * Removes element with the given key from the table if such exests.
     * @param key
     * @return value in case there was an element with the given key in the table and null otherwise
     */
    public String remove(String key) {
        String ret = array[hash(key)].remove(key);

        if (ret != null) {
            size--;
        }

        return ret;
    }

    /**
     * Removes all elements from the table and sets its size to 0.
     */
    public void clear() {
        for (MyListPairStringString cur : array) {
            cur.clear();
        }
        size = 0;
    }
}
