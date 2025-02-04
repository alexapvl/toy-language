package model.adt;

import java.util.List;
import java.util.Map;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public interface ICountSemaphore<K, V> {
  V lookup(K key) throws KeyNotFoundAppException;

  void put(K key, V value);

  void remove(K key);

  boolean contains(K key);

  String toString();

  Map<K, V> getCountSemaphore();

  List<V> getValues();

  int allocate();
}
