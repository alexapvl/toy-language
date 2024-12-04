package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.BooleanType;
import model.values.BooleanValue;
import model.values.IValue;

public class WhileStmt implements IStmt {
  IExp exp;
  IStmt stmt;

  public WhileStmt(IExp exp, IStmt stmt) {
    this.exp = exp;
    this.stmt = stmt;
  }

  public PrgState execute(PrgState prg) throws AppException {
    IGenericDictionary<String, IValue> symTable = prg.getSymTable();
    IGenericHeap<Integer, IValue> heap = prg.getHeap();
    IValue val = exp.eval(symTable, heap);

    if (!val.getType().equals(new BooleanType())) {
      throw new AppException("Expression is not BooleanType");
    }

    BooleanValue boolVal = (BooleanValue) val;
    if (boolVal.getValue()) {
      prg.getExeStack().push(this);
      prg.getExeStack().push(stmt);
    }

    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new WhileStmt(exp.deepCopy(), stmt.deepCopy());
  }

  @Override
  public String toString() {
    return "while(" + exp.toString() + ") {" + stmt.toString() + "}";
  }

}
