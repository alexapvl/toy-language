package model.adt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public class ToySemaphore<K, V> implements IToySemaphore<K, V> {
  private Map<K, V> toySemaphoreTable;
  private AtomicInteger freeAddress;

  public ToySemaphore() {
    this.toySemaphoreTable = new ConcurrentHashMap<>();
    this.freeAddress = new AtomicInteger(1);
  }

  @Override
  public synchronized V lookup(K key) throws KeyNotFoundAppException {
    if (!this.toySemaphoreTable.containsKey(key)) {
      throw new KeyNotFoundAppException("Key not found in toy semaphore");
    }
    return this.toySemaphoreTable.get(key);
   }

  @Override
  public synchronized void put(K key, V value) {
    this.toySemaphoreTable.put(key, value);
  }

  @Override
  public synchronized void remove(K key) {
    this.toySemaphoreTable.remove(key);
  }

  @Override
  public synchronized boolean contains(K key) {
    return this.toySemaphoreTable.containsKey(key);
  }

  @Override
  public synchronized String toString() {
    if (this.toySemaphoreTable.isEmpty()) {
      return "(the toy semaphore is empty)\n";
    }

    StringBuilder s = new StringBuilder();
    for (K key : this.toySemaphoreTable.keySet()) {
      s.append(key.toString()).append(" -> ");
      s.append(this.toySemaphoreTable.get(key).toString());
      s.append("\n");
    }
    return s.toString();
  }

  @Override
  public synchronized Map<K, V> getSemaphoreTable() {
    return this.toySemaphoreTable;
  }

  @Override
  public synchronized List<V> getValues() {
    return new LinkedList<V>(this.toySemaphoreTable.values());
  }

  @Override
  public synchronized void setToySemaphoreTable(Map<K, V> toySemaphoreTable) {
    this.toySemaphoreTable = toySemaphoreTable;
  }

  @Override
  public synchronized Integer allocate(){
    return this.freeAddress.getAndIncrement();
  }
}
