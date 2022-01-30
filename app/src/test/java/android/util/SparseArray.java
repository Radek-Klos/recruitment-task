package android.util;

import java.util.HashMap;

/**
 * An android component used by an external library utilized by our ViewModels
 * and thus the need for this simple dummy version of the class
 */
public class SparseArray<E> {

    private HashMap<Integer, E> mHashMap;

    public SparseArray() {
        mHashMap = new HashMap<>();
    }

    public SparseArray(int initialCapacity) {
        mHashMap = new HashMap<>(initialCapacity);
    }

    public void put(int key, E value) {
        mHashMap.put(key, value);
    }

    public E get(int key) {
        return mHashMap.get(key);
    }
}
