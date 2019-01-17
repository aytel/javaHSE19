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

    public int size() {
        return size;
    }

    public boolean contains(String key) {
        return array[hash(key)].contains(key);
    }

    public String get(String key) {
        return array[hash(key)].get(key);
    }

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

    public String remove(String key) {
        String ret = array[hash(key)].remove(key);

        if (ret != null) {
            size--;
        }

        return ret;
    }

    public void clear() {
        for (MyListPairStringString cur : array) {
            cur.clear();
        }
        size = 0;
    }
}
