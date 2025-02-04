package model.adt;

import java.util.List;
import java.util.Map;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public interface IProcedureTable<K, V> {
  V lookup(K key) throws KeyNotFoundAppException;

  void put(K key, V value);

  void remove(K key);

  boolean contains(K key);

  String toString();

  Map<K, V> getProcedures();

  List<V> getValues();

  List<K> getKeys();

  void setMap(Map<K, V> dictionary);
}
