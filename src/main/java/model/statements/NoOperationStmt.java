package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.types.IType;

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

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) {
    return typeEnv;
  }
}
