package model.expressions;

import model.values.IValue;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.exceptions.AppException;

public class VariableExp implements IExp {
  private String id;

  public VariableExp(String id) {
    this.id = id;
  }

  @Override
  public IValue eval(IGenericDictionary<String, IValue> symTable) throws AppException {
    if (symTable.contains(id)) {
      try {
        return symTable.lookup(id);
      } catch (KeyNotFoundAppException e) {
        throw new AppException(e.getMessage());
      }
    } else {
      throw new AppException("Variable " + id + " is not defined");
    }
  }

  @Override
  public IExp deepCopy() {
    return new VariableExp(id);
  }

  @Override
  public String toString() {
    return id;
  }

}
