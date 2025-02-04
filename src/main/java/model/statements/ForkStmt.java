package model.statements;

import java.util.Stack;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.stack.GenericStack;
import model.adt.stack.IGenericStack;
import model.exceptions.AppException;
import model.types.IType;
import model.values.IValue;

public class ForkStmt implements IStmt {
  private IStmt stmt;

  public ForkStmt(IStmt stmt) {
    this.stmt = stmt;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IGenericStack<IStmt> newStack = new GenericStack<>();
    Stack<IGenericDictionary<String, IValue>> cloneSymTable = new Stack<>();
    for (IGenericDictionary<String, IValue> symTable : prg.getSymTables()) {
      cloneSymTable.add(symTable.deepCopy());
    }
    return new PrgState(cloneSymTable, newStack, prg.getOutput(), this.stmt, prg.getFileTable(),
        prg.getHeap(), prg.getProcedureTable());
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
