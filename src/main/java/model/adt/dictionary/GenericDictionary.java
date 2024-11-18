package model.adt.dictionary;

import java.util.Map;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GenericDictionary<K, V> implements IGenericDictionary<K, V> {
    // Map is an interface, HashMap is a class that implements Map
    private Map<K, V> dictionary;

    public GenericDictionary() {
        this.dictionary = new HashMap<K, V>();
    }

    @Override
    public V lookup(K key) throws KeyNotFoundAppException {
        if (!this.contains(key)) {
            throw new KeyNotFoundAppException("Key not found in dictionary");
        }
        return this.dictionary.get(key);
    }

    @Override
    public void put(K key, V value) {
        this.dictionary.put(key, value);
    }

    @Override
    public void remove(K key) {
        this.dictionary.remove(key);
    }

    @Override
    public boolean contains(K key) {
        return this.dictionary.containsKey(key);
    }

    @Override
    public String toString() {
        if (this.dictionary.isEmpty()) {
            return "(the symTable is empty)\n";
        }

        StringBuilder s = new StringBuilder();
        for (K key : this.dictionary.keySet()) {
            s.append(key.toString()).append(" -> ");
            s.append(this.dictionary.get(key).toString());
            s.append("\n");
        }
        return s.toString();
    }

    @Override
    public Map<K, V> getMap() {
        return this.dictionary;
    }

    @Override
    public List<V> getValues() {
        return new LinkedList<V>(this.dictionary.values());
    }

    @Override
    public void setMap(Map<K, V> dictionary) {
        this.dictionary = dictionary;
    }
}
