package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class WriteHeapStmt implements IStmt {
  private String varName;
  private IExp expr;

  public WriteHeapStmt(String varName, IExp expr) {
    this.varName = varName;
    this.expr = expr;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IGenericDictionary<String, IValue> symTable = prg.getSymTable();
    IGenericHeap<Integer, IValue> heap = prg.getHeap();
    if (!symTable.contains(varName)) {
      throw new AppException("Variable " + varName + " is not defined");
    }

    IValue symTableVariable = symTable.lookup(varName);
    if (!(symTableVariable instanceof RefValue)) {
      throw new AppException("Variable " + varName + " is not a RefType");
    }

    RefValue refValue = (RefValue) symTableVariable;
    Integer address = refValue.getAddr();

    if (!heap.contains(address)) {
      throw new AppException("The address " + address + " is not in the heap (it was not allocated)");
    }

    IValue value = this.expr.eval(symTable, heap);
    if (!refValue.getLocationType().equals(value.getType())) {
      throw new AppException("The type of the expression does not match the type of the inner");
    }

    heap.put(address, value);
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new WriteHeapStmt(this.varName, this.expr.deepCopy());
  }

  @Override
  public String toString() {
    return "writeHeap(" + this.varName + ", " + this.expr + ")";
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv)
      throws AppException {
    IType varType = typeEnv.lookup(varName);
    IType exprType = expr.typecheck(typeEnv);
    if (varType.equals(new RefType(exprType))) {
      return typeEnv;
    } else {
      throw new AppException("WriteHeap: right hand side and left hand side have different types");
    }
  }
}
