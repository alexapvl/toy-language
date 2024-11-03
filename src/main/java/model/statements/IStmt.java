package model.statements;

import model.PrgState;
import model.exceptions.AppException;

public interface IStmt {
  PrgState execute(PrgState state) throws AppException;

  IStmt deepCopy();

  String toString();
}
