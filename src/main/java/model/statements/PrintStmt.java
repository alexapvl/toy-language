package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.IType;
import model.values.IValue;

public class PrintStmt implements IStmt {
  private IExp exp;

  public PrintStmt(IExp exp) {
    this.exp = exp;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IGenericDictionary<String, IValue> symTable = prg.getSymTable();
    IGenericHeap<Integer, IValue> heap = prg.getHeap();
    IValue val;
    try {
      val = exp.eval(symTable, heap);
    } catch (AppException error) {
      throw new AppException(error.getMessage());
    }

    prg.getOutput().add(val);
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new PrintStmt(exp.deepCopy());
  }

  @Override
  public String toString() {
    return "print(" + exp.toString() + ")";
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv)
      throws AppException {
    return typeEnv;
  }
}
