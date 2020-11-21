public interface KWHashMap <K,V> {
        V get(K key);
        boolean isEmpty();
        V put(K key, V value);
        V remove(K key); //was Object Key before
        int size();
    }
