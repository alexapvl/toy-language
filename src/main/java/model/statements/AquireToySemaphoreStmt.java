package model.statements;

import java.util.List;

import model.PrgState;
import model.adt.IToySemaphore;
import model.adt.Tuple;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.types.IType;
import model.types.IntegerType;
import model.values.IValue;
import model.values.IntegerValue;

public class AquireToySemaphoreStmt implements IStmt {
  private String var;

  public AquireToySemaphoreStmt(String var) {
    this.var = var;
  }

  public PrgState execute(PrgState state) throws AppException {
    IGenericDictionary<String, IValue> symTable = state.getSymTable();
    if (!symTable.contains(this.var)) {
      throw new AppException("AquireToySemaphoreStmt: Variable is not present in the SymTable.");
    }
    IValue val = symTable.lookup(this.var);
    if (!val.getType().equals(new IntegerType())) {
      throw new AppException("AquireToySemaphoreStmt: Variable is not of type IntegerType.");
    } 

    Integer addressInToySemaphoreTable = (Integer) ((IntegerValue) val).getValue();
    IToySemaphore<Integer, Tuple<Integer, List<Integer>, Integer>> toySemaphoreTable = state.getToySemaphoreTable();
    if (!toySemaphoreTable.contains(addressInToySemaphoreTable)) {
      throw new AppException("AquireToySemaphoreStmt: Address not in ToySemaphoreTable");
    }

    Tuple<Integer, List<Integer>, Integer> toySemaphoreEntry = toySemaphoreTable.lookup(addressInToySemaphoreTable);
    if (toySemaphoreEntry.getV1() - toySemaphoreEntry.getV3() > toySemaphoreEntry.getV2().size()) {
      if (!toySemaphoreEntry.getV2().contains(state.getId())) {
        toySemaphoreEntry.getV2().add(state.getId());
      }
    } else {
      state.getExeStack().push(this.deepCopy());
    }

    return null;
  }

  public IStmt deepCopy() {
    return new AquireToySemaphoreStmt(new String(var));
  }

  public String toString() {
    return "aquire(" + this.var + ")";
  }

  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    return typeEnv;
  }
}
