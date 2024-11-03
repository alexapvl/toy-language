package model.statements;

import model.PrgState;

public class NoOperationStmt implements IStmt {
  @Override
  public PrgState execute(PrgState state) {
    return state;
  }

  @Override
  public IStmt deepCopy() {
    return new NoOperationStmt();
  }

  @Override
  public String toString() {
    return "NoOperationStmt";
  }
}
