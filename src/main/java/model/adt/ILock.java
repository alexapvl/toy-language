package model.adt;

import java.util.List;
import java.util.Map;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public interface ILock<K, V> {
  V lookup(K key) throws KeyNotFoundAppException;

  void put(K key, V value);

  void remove(K key);

  boolean contains(K key);

  String toString();

  Map<K, V> getLockTable();

  List<V> getValues();

  void setLockTable(Map<K, V> lockTable);

  Integer allocate();

  boolean tryAcquire(K key, V value) throws KeyNotFoundAppException;
}
