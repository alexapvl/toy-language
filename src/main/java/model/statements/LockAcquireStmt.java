package model.statements;

import model.PrgState;
import model.adt.ILock;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.types.IType;
import model.types.IntegerType;
import model.values.IValue;
import model.values.IntegerValue;

public class LockAcquireStmt implements IStmt {
  private String var;

  public LockAcquireStmt(String var) {
    this.var = var;
  }

  public PrgState execute(PrgState state) throws AppException {
    IGenericDictionary<String, IValue> symTable = state.getSymTable();

    if (!symTable.contains(this.var)) {
      throw new AppException("LockAcquireStmt: Variable is not in the SymTable");
    }

    IValue val = symTable.lookup(this.var);
    if (!val.getType().equals(new IntegerType())) {
      throw new AppException("LockAcquireStmt: Variable is not of type IntegerType");
    }

    Integer foundIndex = (Integer) ((IntegerValue) val).getValue();
    ILock<Integer, Integer> lockTable = state.getLockTable();

    if (!lockTable.contains(foundIndex)) {
      throw new AppException("LockAcquireStmt: Found index is not in the LockTable");
    } else if (lockTable.lookup(foundIndex) == -1) { // ready to lock
      lockTable.put(foundIndex, (Integer) state.getId());
    } else { // another program state holds the lock
      state.getExeStack().push(this);
    }

    return null;
  }

  public IStmt deepCopy() {
    return new LockAcquireStmt(this.var);
  }

  public String toString() {
    return "lock(" + this.var + ")";
  }

  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    if (!typeEnv.contains(this.var)) {
      throw new AppException("Typecheck: variable not defined in NewLockStmt");
    }

    if (!typeEnv.lookup(this.var).equals(new IntegerType())) {
      throw new AppException("Typecheck: variable is not of type IntegerType");
    }

    return typeEnv;
  }
}
