package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.types.IType;
import model.values.IValue;

public class VariableDeclarationStmt implements IStmt {
  private String id;
  private IType type;

  public VariableDeclarationStmt(String id, IType type) {
    this.id = id;
    this.type = type;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IGenericDictionary<String, IValue> symTable = prg.getSymTable();

    if (symTable.contains(id)) {
      throw new AppException("Variable " + id + " is already defined");
    }

    symTable.put(id, type.defaultValue());
    return prg;
  }

  @Override
  public IStmt deepCopy() {
    return new VariableDeclarationStmt(id, type);
  }

  @Override
  public String toString() {
    return type.toString() + " " + id;
  }
}
