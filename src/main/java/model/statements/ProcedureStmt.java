package model.statements;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import model.PrgState;
import model.adt.IProcedureTable;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.types.IType;
import model.types.IntegerType;

public class ProcedureStmt implements IStmt {
  private String procName;
  private List<String> params;
  private IStmt procStmt;

  public ProcedureStmt(String procName, List<String> params, IStmt procStmt) {
    this.procName = procName;
    this.params = params;
    this.procStmt = procStmt;
  }

  @Override
  public PrgState execute(PrgState state) throws AppException {
    IProcedureTable<String, Pair<List<String>, IStmt>> procedureTable = state.getProcedureTable();
    if (procedureTable.contains(this.procName)) {
      throw new AppException("Procedure name already exists");
    }

    procedureTable.put(this.procName, new Pair<>(this.params, this.procStmt));
    return null;
  }

  @Override
  public IStmt deepCopy() {
    List<String> paramsCopy = new ArrayList<>();
    for (String param : this.params) {
      paramsCopy.add(new String(param));
    }
    return new ProcedureStmt(new String(this.procName), paramsCopy, procStmt.deepCopy());
  }

  @Override
  public String toString() {
    return "procedure " + this.procName + "(" + this.params + ") -> " + this.procStmt;
  }

  @Override
   public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    IGenericDictionary<String, IType> typeEnvClone = typeEnv.deepCopy();
    // we typecheck on the clone of the type environment in order to simulate a call of a function
    // before the actual call in the execution, we also keep the integrity of the initial typeEnv
    // and pass it to the next typecheck
    try {
      for (String param : this.params) {
        typeEnvClone.put(param, new IntegerType());
      }
      this.procStmt.typecheck(typeEnvClone);
    } catch (AppException e) {
      throw new AppException(e.getMessage());
    }
    return typeEnv;
   }
}
