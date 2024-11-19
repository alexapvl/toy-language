package model.adt.heap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.values.IValue;

public interface IGenericHeap<K, V> {
  V lookup(K key) throws KeyNotFoundAppException;

  void put(K key, V value);

  void remove(K key);

  boolean contains(K key);

  String toString();

  Map<K, V> getHeap();

  List<V> getValues();

  void setHeap(Map<K, V> dictionary);

  Map<Integer, IValue> safeGarbageCollector(Set<Integer> unionSetOfUsedAddr, Map<Integer, IValue> heap);

  Integer allocate();
}
