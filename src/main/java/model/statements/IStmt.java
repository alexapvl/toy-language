package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.types.IType;

public interface IStmt {
  PrgState execute(PrgState state) throws AppException;

  IStmt deepCopy();

  String toString();

  IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException;
}
