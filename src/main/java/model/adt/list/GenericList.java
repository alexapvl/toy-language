package model.adt.list;

import java.util.List;
import java.util.LinkedList;

public class GenericList<T> implements IGenericList<T> {
  private List<T> output;

  public GenericList() {
    this.output = new LinkedList<T>();
  }

  @Override
  public void add(T element) {
    this.output.add(element);
  }

  @Override
  public void clear() {
    this.output.clear();
  }

  @Override
  public List<T> getAll() {
    return this.output;
  }

  @Override
  public String toString() {
    if (this.output.isEmpty()) {
      return "(the output is empty)\n";
    }

    StringBuilder s = new StringBuilder();
    for (T elem : this.output) {
      s.append(elem.toString()).append("\n");
    }

    return s.toString();
  }

}