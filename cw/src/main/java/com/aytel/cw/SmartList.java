package com.aytel.cw;

import java.util.*;


/**
 * List of elements, which is optimized for small sizes.
 */
public class SmartList<T> extends AbstractList<T> implements List<T> {

    private int size;
    private Object elements;

    /** Creates empty list. */
    public SmartList() {
        size = 0;
        elements = null;
    }

    /** Creates list with elements from the given collection. */
    public SmartList(Collection<? extends T> collection) {
        this();
        this.addAll(collection);
    }

    /** {@link List#get(int index)} */
    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1) {
            return (T) elements;
        }
        if (size <= 5) {
            return ((T[])elements)[index];
        }
        return ((List<T>)elements).get(index);
    }

    /** {@link List#size()} */
    @Override
    public int size() {
        return size;
    }

    /** {@link List#add(T element)} */
    @Override
    @SuppressWarnings("unchecked")
    public boolean add(T element) {
        boolean result = contains(element);

        if (size == 0) {
            elements = element;
        }
        if (size == 1) {
            T cur = (T) elements;
            elements = new Object[5];
            ((Object[])elements)[0] = cur;
            ((Object[])elements)[1] = element;
        }
        if (size >= 2 && size < 5) {
            ((Object[])elements)[size] = element;
        }
        if (size == 5) {
            T[] cur = (T[]) elements;
            elements = new ArrayList(Arrays.asList(cur));
            ((List<T>)elements).add(element);
        }
        if (size > 5) {
            ((List<T>)elements).add(element);
        }

        size++;

        return result;
    }

    /** {@link List#remove(Object element)} */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object element) {
        boolean result = contains(element);

        if (size == 0) {
            return result;
        }
        if (size == 1) {
            if (result) {
                elements = null;
                size--;
            }
            return result;
        }
        if (size == 2) {
            if (result) {
                T[] cur = ((T[])elements);
                elements = (Objects.equals(cur[0], element) ? cur[0] : cur[1]);
                size--;
            }
            return result;
        }
        if (size <= 5) {
            if (result) {
                var cur = new ArrayList<T>(Arrays.asList((T[])elements));
                (cur).remove(element);
                elements = cur.toArray();
                size--;
            }
            return result;
        }
        if (size == 6) {
            if (result) {
                var cur = new Object[5];
                ((List<T>)elements).remove(element);
                int pointer = 0;
                for (T toAdd : (List<T>)elements) {
                    cur[pointer++] = toAdd;
                }
                elements = cur;
                size--;
            }
            return result;
        }
        if (result) {
            ((List<T>) elements).remove(element);
            size--;
        }
        return result;
    }

    /** {@link List#contains(Object element)} */
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object element) {
        if (size == 0) {
            return false;
        }
        if (size == 1) {
            return Objects.equals(elements, element);
        }
        if (size <= 5) {
            return (new ArrayList<T>(Arrays.asList((T[])elements))).contains(element);
        }
        return ((List<T>)elements).contains(element);
    }

    /** {@link List#remove(int index)} */
    @Override
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        T value = get(index);

        if (size == 1) {
            elements = null;
            size--;
            return value;
        }
        if (size == 2) {
            T[] cur = (T[])elements;
            elements = cur[index ^ 1];
            size--;
            return value;
        }
        if (size <= 5) {
            var cur = new ArrayList<T>(Arrays.asList((T[])elements));
            (cur).remove(index);
            elements = cur.toArray();
            size--;
            return value;
        }
        if (size == 6) {
            var cur = new Object[5];
            ((List<T>)elements).remove(index);
            int pointer = 0;
            for (T toAdd : (List<T>)elements) {
                cur[pointer++] = toAdd;
            }
            elements = cur;
            size--;
            return value;
        }
        ((List<T>) elements).remove(index);
        size--;
        return value;
    }

    /** {@link List#set(int index, T element)} */
    @Override
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        T value = get(index);

        if (size == 1) {
            elements = element;
        }
        if (size >= 2 && size <= 5) {
            ((T[])elements)[index] = element;
        }
        if (size > 5) {
            ((List<T>)elements).set(index, element);
        }

        return value;
    }
}
