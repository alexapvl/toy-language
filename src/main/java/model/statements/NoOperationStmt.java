package model.statements;

import model.PrgState;

public class NoOperationStmt implements IStmt {
  @Override
  public PrgState execute(PrgState prg) {
    return null;
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
