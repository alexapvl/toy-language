package model.adt.heap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public class GenericHeap<K, V> implements IGenericHeap<K, V> {
  private Integer firstFreeAddress;
  private Map<K, V> heap;

  public GenericHeap() {
    this.firstFreeAddress = 1;
    this.heap = new HashMap<K, V>();
  }

  @Override
  public V lookup(K key) throws KeyNotFoundAppException {
    if (!this.contains(key)) {
      throw new KeyNotFoundAppException("Key not found in heap");
    }
    return this.heap.get(key);
  }

  @Override
  public void put(K key, V value) {
    this.heap.put(key, value);
  }

  @Override
  public void remove(K key) {
    this.heap.remove(key);
  }

  @Override
  public boolean contains(K key) {
    return this.heap.containsKey(key);
  }

  @Override
  public String toString() {
    if (this.heap.isEmpty()) {
      return "(the heap is empty)\n";
    }

    StringBuilder s = new StringBuilder();
    for (K key : this.heap.keySet()) {
      s.append(key.toString()).append(" -> ");
      s.append(this.heap.get(key).toString());
      s.append("\n");
    }
    return s.toString();
  }

  @Override
  public Map<K, V> getHeap() {
    return this.heap;
  }

  @Override
  public List<V> getValues() {
    return new LinkedList<V>(this.heap.values());
  }

  @Override
  public void setHeap(Map<K, V> heap) {
    this.heap = heap;
  }

  @Override
  public Integer allocate() {
    return this.firstFreeAddress++;
  }
}
