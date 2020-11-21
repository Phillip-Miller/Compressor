import java.util.*;
//deletions have not yet been implemented

/**
 * Supports a dictionary using javas native Hash function on whatever input and then modding by table size
 * Table enteries are linked lists to resolve collisions
 * Table size is prime to minimize collisions
 * @param <K> Key
 * @param <V> Value
 */
public class HashTableChain<K,V> implements KWHashMap<K,V>{

    public static class Entry<K,V>{
        private  final K key;
        private V value;
        public Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
        private  K getKey() {
            return key;
        }
        private V getValue() {
            return value;
        }
        private V setValue(V value) {
            V oldvalue = this.value;
            this.value = value;
            return oldvalue;
        }
        public String toString(){
            return "(" +  String.valueOf(getKey()) + ", " + String.valueOf(getValue()) + ")";
        }


    }
    /** Hash table implementation using open addressing. */
    private LinkedList<Entry<K,V>>[] table;
    private int CAPACITY = 101;
    private double LOAD_THRESHOLD = 0.70; //keys/capacity
    public int numKeys;
    private int numDeletes;
    private final Entry<K, V> DELETED = new Entry<>(null, null);
    public int reHashed = 0;

    // Constructor
    public HashTableChain() {
        this.table = new  LinkedList[CAPACITY];
    }
    public HashTableChain(int customCapacity) {
        table = new LinkedList[customCapacity];
        CAPACITY = customCapacity;
    }
    //iterator not implemented yet
    public void quickPrint(){
        for(LinkedList<Entry<K, V>> list : table) {
            if (list != null){
                for (Entry<K, V> entry : list) {
                    System.out.println(entry);
                }
            }
        }
    }
    public V get(K key){ //@FIXME maybe this should be an object Key
        LinkedList<Entry<K,V>> list = table[index(key)];
        if(list == null)
            return null;
        for(Entry<K,V> entry : list){
            if( entry.getKey().equals(key)){ //key should still be of type string
                return entry.getValue();
            }
        }
        return null;
    }
    public boolean isEmpty(){
        return table.length == 0;
    }

    /**
     * @param key
     * @param value
     * @return previous value or null if first entry
     */
    public V put(K key, V value) {
        if(numKeys/CAPACITY > LOAD_THRESHOLD){
            this.reHash();
            this.reHashed++;
        }

        Entry<K,V> putEntry = new Entry<>(key,value);
        int tableIndex = index(key);

        if(table[tableIndex] == null){ //First time index is visited
            table[tableIndex] =  new LinkedList<>();
        }

        else if (table[tableIndex].size()>0){ //Index already has a table
            for(Entry<K,V> entry : table[tableIndex]){
                if(entry.getKey().equals(key)){ //Searching for match if there is an Linkedlist
                    V oldValue = entry.getValue(); entry.setValue(value);
                    numKeys++; return oldValue;
                }
            }
        }

        table[tableIndex].add(putEntry); //if there was no list or no match
        numKeys++;
        return null;

    }
    public V remove(K key){
        return this.put(key,null);
    }

    /**
     * @return size of the table, not number of keys
     */
    public int size(){
        return table.length; //maybe return num keys
    }

    /**
     * @param key
     * @return index that a given key should be inserted at
     */
    private int index(K key){
        int index = key.hashCode() % table.length;
        if (index < 0)
            index += table.length;
        return index;
    }
    private void reHash(){
        this.reHashed ++;
        LinkedList<Entry<K, V>>[] oldTable = table.clone();
        CAPACITY =  nextPrime(table.length*2);
        table = new LinkedList[CAPACITY];

        for(int i = 0; i<oldTable.length; i++){
            if(oldTable[i] != null){ //hopefully you found an arraylist here
                for (Entry<K,V> entry : oldTable[i]) { //iterate through the arrayList
                    this.put(entry.getKey(),entry.getValue());
                }
            }
        }
    }

    /**
     * @param  startValue int
     * @return the nearest prime to the value that you input
     */
    public static int nextPrime(int startValue){
        while(true) {
            startValue++;
            if (startValue % 2 != 0 && startValue % 3 != 0 &&startValue % 4 != 0 &&startValue % 5 != 0 &&startValue % 6 != 0 &&startValue % 7 != 0 && startValue % 8 != 0 &&startValue % 9 != 0 ){
                break;
            }
        }
        return startValue;
    }

}
