package model.expressions;

import model.values.IValue;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.exceptions.ArithmeticExpAppException;
import model.types.IType;

public class ValueExp implements IExp {
  private IValue value;

  public ValueExp(IValue value) {
    this.value = value;
  }

  @Override
  public IValue eval(IGenericDictionary<String, IValue> symTable, IGenericHeap<Integer, IValue> heap)
      throws AppException, ArithmeticExpAppException {
    return value;
  }

  @Override
  public IExp deepCopy() {
    return new ValueExp(value.deepCopy());
  }

  @Override
  public String toString() {
    return this.value.toString();
  }

  @Override
  public IType typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    return this.value.getType();
  }
}
