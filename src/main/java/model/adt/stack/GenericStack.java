package model.adt.stack;

import java.util.List;
import java.util.Stack;

import model.adt.stack.exceptions.StackEmptyAppExcetion;

public class GenericStack<T> implements IGenericStack<T> {
  private Stack<T> stack;

  public GenericStack() {
    this.stack = new Stack<>();
  }

  @Override
  public List<T> toList() {
    return this.stack.stream().toList();
  }

  @Override
  public void push(T elem) {
    this.stack.push(elem);
  }

  @Override
  public T pop() throws StackEmptyAppExcetion {
    if (this.stack.isEmpty()) {
      throw new StackEmptyAppExcetion("Stack is empty");
    }
    return this.stack.pop();
  }

  @Override
  public T top() throws StackEmptyAppExcetion {
    if (this.stack.isEmpty()) {
      throw new StackEmptyAppExcetion("Stack is empty");
    }
    return this.stack.peek();
  }

  @Override
  public int size() {
    return this.stack.size();
  }

  @Override
  public boolean isEmpty() {
    return this.stack.isEmpty();
  }

  @Override
  public String toString() {
    if (this.stack.isEmpty()) {
      return "(the stack is empty)";
    }

    StringBuilder result = new StringBuilder();
    for (T elem : this.stack) {
      result.append(elem.toString()).append("\n");
    }
    return result.toString();
  }

}