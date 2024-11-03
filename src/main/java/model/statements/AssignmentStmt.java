package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
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
    if (symTable.contains(id)) {
      IValue val;
      try {
        val = exp.eval(symTable);
      } catch (AppException error) {
        throw new AppException(error.getMessage());
      }

      try {
        if (val.getType().equals(symTable.lookup(id).getType())) {
          symTable.put(id, val);
        } else {
          throw new AppException("Type of expression and type of variable " + id + " do not match");
        }
      } catch (KeyNotFoundAppException error) {
        throw new AppException(error.getMessage());
      }

    } else
      throw new AppException("Variable " + id + " is not defined");

    return state;
  }

  @Override
  public IStmt deepCopy() {
    return new AssignmentStmt(id, exp.deepCopy());
  }

  @Override
  public String toString() {
    return id + "=" + exp.toString();
  }

}
