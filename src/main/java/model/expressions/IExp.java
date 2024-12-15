package model.expressions;

import model.expressions.exceptions.ArithmeticExpAppException;
import model.types.IType;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.values.IValue;

public interface IExp {
  IValue eval(IGenericDictionary<String, IValue> symTable, IGenericHeap<Integer, IValue> heap)
      throws AppException, ArithmeticExpAppException;

  IExp deepCopy();

  String toString();

  IType typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException;
}
