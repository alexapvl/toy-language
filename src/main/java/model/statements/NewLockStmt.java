package model.statements;

import model.PrgState;
import model.adt.ILock;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.types.IType;
import model.types.IntegerType;
import model.values.IValue;
import model.values.IntegerValue;

public class NewLockStmt implements IStmt {
  private String var;

  public NewLockStmt(String var) {
    this.var = var;
  }

  public PrgState execute(PrgState state) throws AppException {
    IGenericDictionary<String, IValue> symTable = state.getSymTable();

    if (!symTable.contains(this.var)) {
      throw new AppException("NewLockStmt: Variable is not in the SymTable");
    }

    IValue val = symTable.lookup(this.var);
    if (!val.getType().equals(new IntegerType())) {
      throw new AppException("NewLockStmt: Variable is not of type IntegerType");
    }

    ILock<Integer, Integer> lockTable = state.getLockTable();
    int freeAddress = lockTable.allocate();

    symTable.put(this.var, new IntegerValue(freeAddress));
    lockTable.put(freeAddress, -1);

    return null;
  }

  public IStmt deepCopy() {
    return new NewLockStmt(new String(this.var));
  }

  public String toString() {
    return "newLock(" + this.var + ")";
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
