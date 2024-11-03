package model.statements;

import model.PrgState;
import model.adt.stack.IGenericStack;

public class CompoundStmt implements IStmt {
  private IStmt first;
  private IStmt second;

  public CompoundStmt(IStmt first, IStmt second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public PrgState execute(PrgState prg) {
    IGenericStack<IStmt> exeStack = prg.getExeStack();
    exeStack.push(second);
    exeStack.push(first);
    return prg;
  }

  @Override
  public IStmt deepCopy() {
    return new CompoundStmt(first.deepCopy(), second.deepCopy());
  }

  @Override
  public String toString() {
    return "(" + first.toString() + "; " + second.toString() + ")";
  }
}
