package model.adt.state;

import model.exceptions.AppException;
import model.types.IType;

public interface ISymTable {
  void declareValue(String name, IType type) throws AppException;

}
