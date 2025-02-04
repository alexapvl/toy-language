package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.types.IType;

public class ReturnStmt implements IStmt {
  public ReturnStmt() {}

  @Override
  public PrgState execute(PrgState state) throws AppException {
    state.getSymTables().pop(); // restore the local variables (get rid of the ones defined in the procedure call)
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new ReturnStmt();
  }

  @Override
  public String toString() {
    return "ReturnStmt";
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    return typeEnv;
  }
}
