package model.adt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.util.Pair;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public class CountSemaphore implements ICountSemaphore<Integer, Pair<Integer, List<Integer>>> {
  private Map<Integer, Pair<Integer, List<Integer>>> semaphoreTable;
  private AtomicInteger firstFreeAddress;

  public CountSemaphore() {
    this.semaphoreTable = new ConcurrentHashMap<>();
    this.firstFreeAddress = new AtomicInteger(1);
  }

  @Override
  public synchronized Pair<Integer, List<Integer>> lookup(Integer key) throws KeyNotFoundAppException {
    if (!this.semaphoreTable.containsKey(key)) {
      throw new KeyNotFoundAppException("Key not found in semaphore table");
    }
    return this.semaphoreTable.get(key);
  }

  @Override
  public synchronized void put(Integer key, Pair<Integer, List<Integer>> value) {
    this.semaphoreTable.put(key, value);
  }

  @Override
  public synchronized void remove(Integer key) {
    this.semaphoreTable.remove(key);
  }

  @Override
  public synchronized boolean contains(Integer key) {
    return this.semaphoreTable.containsKey(key);
  }

  @Override
  public String toString() {
    if (this.semaphoreTable.isEmpty()) {
      return "(the semaphtore table is empty)";
    }

    StringBuilder s = new StringBuilder();
    for (Integer key : this.semaphoreTable.keySet()) {
      s.append(key.toString()).append("->")
        .append(this.semaphoreTable.get(key).toString())
        .append("\n");
    }
    return s.toString();
  }

  @Override
  public synchronized Map<Integer, Pair<Integer, List<Integer>>> getCountSemaphore() {
    return this.semaphoreTable;
  }

  @Override
  public synchronized Collection<Pair<Integer, List<Integer>>> getValues() {
    return this.semaphoreTable.values();
  }

  @Override
  public synchronized int allocate() {
    return this.firstFreeAddress.getAndIncrement();
  }
}
