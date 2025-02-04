package model.adt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public class Lock<K,V> implements ILock<K,V> {
  private Map<K,V> lockTable;
  private AtomicInteger freeAddress;

  public Lock() {
    this.lockTable = new ConcurrentHashMap<>();
    this.freeAddress = new AtomicInteger(1);
  }

  public synchronized V lookup(K key) throws KeyNotFoundAppException {
    if (!this.contains(key)) {
      throw new KeyNotFoundAppException("Key not found in the LockTable");
    }
    return this.lockTable.get(key);
  }

  public synchronized void put(K key, V value) {
    this.lockTable.put(key, value);
  }

  public synchronized void remove(K key) {
    this.lockTable.remove(key);
  }

  public synchronized boolean contains(K key) {
    return this.lockTable.containsKey(key);
  }

  public synchronized String toString() {
    if (this.lockTable.isEmpty()) {
      return "(the lock table is empty)\n";
    }

    StringBuilder s = new StringBuilder();
    for (K key : this.lockTable.keySet()) {
      s.append(key.toString()).append(" -> ");
      s.append(this.lockTable.get(key).toString());
      s.append("\n");
    }
    return s.toString();
  }

  public synchronized Map<K, V> getLockTable() {
    return this.lockTable;
  }

  public synchronized List<V> getValues() {
    return new LinkedList<>(this.lockTable.values());
  }

  public synchronized void setLockTable(Map<K, V> lockTable) {
    this.lockTable = lockTable;
  }

  public synchronized Integer allocate() {
    return this.freeAddress.getAndIncrement();
  }
  
}
