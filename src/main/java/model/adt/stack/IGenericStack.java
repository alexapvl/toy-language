package model.adt.stack;

import java.util.List;

import model.adt.stack.exceptions.StackEmptyAppExcetion;

public interface IGenericStack<T> {
  List<T> toList();

  void push(T elem);

  T pop() throws StackEmptyAppExcetion;

  T top() throws StackEmptyAppExcetion;

  int size();

  boolean isEmpty();

  String toString();
}
