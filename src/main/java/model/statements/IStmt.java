package model.statements;

import model.adt.state.PrgState;
import model.exceptions.AppException;

public interface IStmt {
  PrgState execute(PrgState state) throws AppException;

  String toString();
}
