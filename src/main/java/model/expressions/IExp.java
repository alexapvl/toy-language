package model.expressions;

import model.expressions.exceptions.ArithmeticExpAppException;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.values.IValue;

public interface IExp {
  IValue eval(IGenericDictionary<String, IValue> symTable) throws AppException, ArithmeticExpAppException;

  IExp deepCopy();

  String toString();
}
