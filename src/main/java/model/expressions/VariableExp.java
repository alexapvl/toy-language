package model.expressions;

import model.values.IValue;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.exceptions.ArithmeticExpAppException;

public class VariableExp implements IExp {
  private String id;

  public VariableExp(String id) {
    this.id = id;
  }

  @Override
  public IValue eval(IGenericDictionary<String, IValue> symTable, IGenericHeap<Integer, IValue> heap)
      throws AppException, ArithmeticExpAppException {
    if (symTable.contains(this.id)) {
      try {
        return symTable.lookup(this.id);
      } catch (KeyNotFoundAppException e) {
        throw new AppException(e.getMessage());
      }
    } else {
      throw new AppException("Variable " + this.id + " is not defined");
    }
  }

  @Override
  public IExp deepCopy() {
    return new VariableExp(this.id);
  }

  @Override
  public String toString() {
    return id;
  }

}
