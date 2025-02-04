package model.statements;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import model.PrgState;
import model.adt.IProcedureTable;
import model.adt.dictionary.GenericDictionary;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.IType;
import model.values.IValue;

public class ProcedureCallStmt implements IStmt {
  private String procName;
  private List<IExp> procExpr;

  public ProcedureCallStmt(String procName, List<IExp> procExpr) {
    this.procName = procName;
    this.procExpr = procExpr;
  }

  @Override
  public PrgState execute(PrgState state) throws AppException {
    IProcedureTable<String, Pair<List<String>, IStmt>>procedureTable = state.getProcedureTable();

    if (!procedureTable.contains(this.procName)) {
      throw new AppException("Procedure name is not defined");
    }

    // extract the list of parameters from the procedure table
    List<String> params = procedureTable.lookup(this.procName).getKey();

    // create a new symTable and populate it with the new values of the variables given the mappings of the 
    // parameters list and the expressions list in the call statement
    IGenericDictionary<String, IValue> topSymTable = state.getTopSymTable();
    IGenericHeap<Integer, IValue> heap = state.getHeap();
    IGenericDictionary<String, IValue> procedureSymTable = new GenericDictionary<>();
    for (int index = 0; index < params.size(); index++) {
      procedureSymTable.put(params.get(index), this.procExpr.get(index).eval(topSymTable, heap));
    }

    state.getSymTables().push(procedureSymTable);
    // extract the procedure body from the procedure table
    IStmt procBody = procedureTable.lookup(this.procName).getValue();
    state.getExeStack().push(new ReturnStmt());
    state.getExeStack().push(procBody);
    return null;
  }

  @Override
  public IStmt deepCopy() {
    List<IExp> procExprCopy = new ArrayList<>();
    for (IExp exp : this.procExpr) {
      procExprCopy.add(exp.deepCopy());
    }
    return new ProcedureCallStmt(new String(this.procName), procExprCopy);
  }

  @Override
  public String toString() {
    return "call " + this.procName + "(" + this.procExpr + ")";
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    for (IExp exp : this.procExpr) {
      exp.typecheck(typeEnv);
    }
    return typeEnv;
  }
}
