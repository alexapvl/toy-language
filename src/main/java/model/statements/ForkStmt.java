package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.stack.GenericStack;
import model.adt.stack.IGenericStack;
import model.exceptions.AppException;
import model.types.IType;

public class ForkStmt implements IStmt {
  private IStmt stmt;

  public ForkStmt(IStmt stmt) {
    this.stmt = stmt;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IGenericStack<IStmt> newStack = new GenericStack<>();
    return new PrgState(prg.getSymTable().deepCopy(), newStack, prg.getOutput(), this.stmt, prg.getFileTable(),
        prg.getHeap());
  }

  @Override
  public String toString() {
    return "fork(" + stmt.toString() + ")";
  }

  @Override
  public IStmt deepCopy() {
    return new ForkStmt(stmt.deepCopy());
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    return stmt.typecheck(typeEnv);
  }
}
