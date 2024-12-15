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

    if (symTable.contains(this.id)) {
      throw new AppException("Variable " + this.id + " is already defined");
    }

    symTable.put(this.id, type.defaultValue());
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new VariableDeclarationStmt(this.id, type);
  }

  @Override
  public String toString() {
    return type.toString() + " " + this.id;
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    typeEnv.put(this.id, this.type);
    return typeEnv;
  }
}
