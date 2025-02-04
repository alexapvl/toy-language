package model.adt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public class ProcedureTable<K, V> implements IProcedureTable<K, V> {
  private Map<K, V> procedureTable;

  public ProcedureTable() {
    this.procedureTable = new ConcurrentHashMap<>();
  }

  public synchronized V lookup(K key) throws KeyNotFoundAppException {
    if (!this.contains(key)) {
      throw new KeyNotFoundAppException("Key not found in dictionary");
  }
    return this.procedureTable.get(key);
  }

  public synchronized void put(K key, V value) {
    this.procedureTable.put(key, value);
  }

  public synchronized void remove(K key) {
    this.procedureTable.remove(key);
  }

  public synchronized boolean contains(K key) {
    return this.procedureTable.containsKey(key);

  }

  public synchronized String toString() {
  if (this.procedureTable.isEmpty()) {
      return "(the procedure table is empty)\n";
  }

  StringBuilder s = new StringBuilder();
  for (K key : this.procedureTable.keySet()) {
      s.append(key.toString()).append(" -> ");
      s.append(this.procedureTable.get(key).toString());
      s.append("\n");
  }
  return s.toString();
  }

  public synchronized Map<K, V> getProcedures() {
    return this.procedureTable;
  }

  public synchronized List<V> getValues() {
    return new LinkedList<V>(this.procedureTable.values());

  }

  public synchronized List<K> getKeys() {
    return new LinkedList<K>(this.procedureTable.keySet());
  }

  public synchronized void setMap(Map<K, V> dictionary) {
    this.procedureTable = dictionary;
  }
}
