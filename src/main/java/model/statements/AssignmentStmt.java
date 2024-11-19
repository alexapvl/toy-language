package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.values.IValue;

public class AssignmentStmt implements IStmt {
  private String id;
  private IExp exp;

  public AssignmentStmt(String id, IExp exp) {
    this.id = id;
    this.exp = exp;
  }

  @Override
  public PrgState execute(PrgState state) throws AppException {
    IGenericDictionary<String, IValue> symTable = state.getSymTable();
    IGenericHeap<Integer, IValue> heap = state.getHeap();
    if (symTable.contains(this.id)) {
      IValue val;
      try {
        val = exp.eval(symTable, heap);
      } catch (AppException error) {
        throw new AppException(error.getMessage());
      }

      try {
        if (val.getType().equals(symTable.lookup(this.id).getType())) {
          symTable.put(this.id, val);
        } else {
          throw new AppException("Type of expression and type of variable " + this.id + " do not match");
        }
      } catch (KeyNotFoundAppException error) {
        throw new AppException(error.getMessage());
      }

    } else
      throw new AppException("Variable " + this.id + " is not defined");

    return state;
  }

  @Override
  public IStmt deepCopy() {
    return new AssignmentStmt(this.id, exp.deepCopy());
  }

  @Override
  public String toString() {
    return this.id + "=" + exp.toString();
  }

}
