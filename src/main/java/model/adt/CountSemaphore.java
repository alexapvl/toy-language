package model.adt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public class CountSemaphore<K, V> implements ICountSemaphore<K,V> {
  private Map<K, V> semaphoreTable;
  private AtomicInteger firstFreeAddress;

  public CountSemaphore() {
    this.semaphoreTable = new ConcurrentHashMap<>();
    this.firstFreeAddress = new AtomicInteger(1);
  }

  @Override
  public synchronized V lookup(K key) throws KeyNotFoundAppException {
    if (!this.semaphoreTable.containsKey(key)) {
      throw new KeyNotFoundAppException("Key not found in semaphore table");
    }
    return this.semaphoreTable.get(key);
  }

  @Override
  public synchronized void put(K key, V value) {
    this.semaphoreTable.put(key, value);
  }

  @Override
  public synchronized void remove(K key) {
    this.semaphoreTable.remove(key);
  }

  @Override
  public synchronized boolean contains(K key) {
    return this.semaphoreTable.containsKey(key);
  }

  @Override
  public String toString() {
    if (this.semaphoreTable.isEmpty()) {
      return "(the semaphtore table is empty)";
    }

    StringBuilder s = new StringBuilder();
    for (K key : this.semaphoreTable.keySet()) {
      s.append(key.toString()).append("->")
        .append(this.semaphoreTable.get(key).toString())
        .append("\n");
    }
    return s.toString();
  }

  @Override
  public synchronized Map<K, V> getCountSemaphore() {
    return this.semaphoreTable;
  }

  @Override
  public synchronized List<V> getValues() {
    return new LinkedList<>(this.semaphoreTable.values());
  }

  @Override
  public synchronized int allocate() {
    return this.firstFreeAddress.getAndIncrement();
  }
}
