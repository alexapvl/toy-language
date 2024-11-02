package model.adt.dictionary;

import java.util.Map;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;

public interface IGenericDictionary<K, V> {
  V lookup(K key) throws KeyNotFoundAppException;

  void put(K key, V value);

  void remove(K key);

  boolean contains(K key);

  String toString();

  Map<K, V> getMap();

  void setMap(Map<K, V> dictionary);
}