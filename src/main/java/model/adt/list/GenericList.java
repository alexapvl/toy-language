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

}