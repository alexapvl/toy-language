package model.statements;

import java.util.List;

import javafx.util.Pair;
import model.PrgState;
import model.adt.ICountSemaphore;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.exceptions.AppException;
import model.types.IType;
import model.types.IntegerType;
import model.values.IValue;
import model.values.IntegerValue;

public class ReleaseSemaphoreStmt implements IStmt {
  private String var;

  public ReleaseSemaphoreStmt(String var) {
    this.var = var;
  }

  @Override
  public PrgState execute(PrgState state) throws AppException {
    IGenericDictionary<String, IValue> symTable = state.getSymTable();
    try {
      IValue val = symTable.lookup(var);
      if (!val.getType().equals(new IntegerType())) {
        throw new AppException("Variable in the aquire semapthore statement is not of type IntegerType");
      }
      Integer address = ((IntegerValue) val).getValue();
      ICountSemaphore<Integer, Pair<Integer, List<Integer>>> semaphoreTable = state.getCountSemaphore();

      if (!semaphoreTable.contains(address)) {
        throw new AppException("Address not found in the semaphore table");
      }

      Pair<Integer, List<Integer>> semaphoreValue = semaphoreTable.lookup(address);
      List<Integer> List1 = semaphoreValue.getValue();

      Integer prgStateID = state.getId();
      if (List1.contains(prgStateID)) {
        List1.remove(prgStateID); // CHECK HERE
      }

    } catch (KeyNotFoundAppException e) {
      throw new AppException(e.getMessage());
    }

    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new AcquireSemaphoreStmt(new String(this.var));
  }

  @Override
  public String toString() {
    return "release(" + this.var + ")";
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    try {
      IType type = typeEnv.lookup(var);
      if (type.equals(new IntegerType())) {
        return typeEnv;
      }
      throw new AppException("Variable in aquire semaphore statement is not of type IntegerType");
    } catch (KeyNotFoundAppException e) {
    throw new AppException(e.getMessage());
    }
  }
  
}
