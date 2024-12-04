package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.adt.stack.IGenericStack;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.BooleanType;
import model.values.BooleanValue;
import model.values.IValue;

public class IfStmt implements IStmt {
  private IExp exp;
  private IStmt thenStmt;
  private IStmt elseStmt;

  public IfStmt(IExp exp, IStmt thenStmt, IStmt elseStmt) {
    this.exp = exp;
    this.thenStmt = thenStmt;
    this.elseStmt = elseStmt;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IGenericDictionary<String, IValue> symTable = prg.getSymTable();
    IGenericHeap<Integer, IValue> heap = prg.getHeap();
    IValue val;
    try {
      val = this.exp.eval(symTable, heap);
    } catch (AppException error) {
      throw new AppException(error.getMessage());
    }

    if (val.getType().equals(new BooleanType())) {
      IGenericStack<IStmt> exeStack = prg.getExeStack();
      if (((BooleanValue) val).getValue()) {
        exeStack.push(thenStmt);
      } else {
        exeStack.push(elseStmt);
      }
    } else {
      throw new AppException("The condition in the if statement is not of type BooleanType");
    }

    return null;
  }

  @Override
  public IfStmt deepCopy() {
    return new IfStmt(exp.deepCopy(), thenStmt.deepCopy(), elseStmt.deepCopy());
  }

  @Override
  public String toString() {
    return "if (" + this.exp + ") then (" + this.thenStmt + ") else (" + this.elseStmt + ")";
  }

}
