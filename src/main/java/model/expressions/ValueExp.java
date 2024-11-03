package model.expressions;

import model.values.IValue;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;

public class ValueExp implements IExp {
  private IValue value;

  public ValueExp(IValue value) {
    this.value = value;
  }

  @Override
  public IValue eval(IGenericDictionary<String, IValue> symTable) throws AppException {
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
}
