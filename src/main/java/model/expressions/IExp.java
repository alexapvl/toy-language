package model.expressions;

import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.values.IValue;

public interface IExp {
  IValue eval(IGenericDictionary<String, IValue> symTable) throws AppException, ArithmeticException;

  IExp deepCopy();

  String toString();
}
