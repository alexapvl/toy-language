package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.stack.IGenericStack;
import model.exceptions.AppException;
import model.types.IType;

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
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new CompoundStmt(first.deepCopy(), second.deepCopy());
  }

  @Override
  public String toString() {
    return first.toString() + ", " + second.toString();
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    return second.typecheck(first.typecheck(typeEnv));
  }
}
