package HashTable;

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
        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = new MyListPairStringString();
        }

        MyListPairStringString[] oldArray = array;
        array = newArray;

        size = 0;
        for (MyListPairStringString cur : oldArray) {
            for (MyListPairStringString.Node node = cur.head; node != null; node = node.next) {
                put(node.key, node.val);
            }
        }
    }

    /**
     * @return Number of elements in the table
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
     * Adds the pair (key, value) to the table.
     * @param key
     * @param val
     * @return previous value of the key in case it existed and null otherwise
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
     * Removes element with the given key from the table.
     * @param key
     * @return value of the key in case it existed and null otherwise
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
