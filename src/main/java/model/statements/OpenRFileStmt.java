package model.statements;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

public class OpenRFileStmt implements IStmt {
  private IExp exp;

  public OpenRFileStmt(IExp exp) {
    this.exp = exp;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IValue value;
    IGenericDictionary<String, IValue> symTable = prg.getSymTable();
    IGenericDictionary<StringValue, BufferedReader> fileTable = prg.getFileTable();
    IGenericHeap<Integer, IValue> heap = prg.getHeap();

    try {
      value = this.exp.eval(symTable, heap);
    } catch (AppException error) {
      throw new AppException("Error evaluating expression: " + error.getMessage());
    }

    if (!value.getType().equals(new StringType())) {
      throw new AppException("Expression does not evaluate to a string type");
    }

    StringValue fileName = (StringValue) value;
    if (fileTable.contains(fileName)) {
      throw new AppException("File " + fileName + " is already open");
    }

    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName.getValue()));
      fileTable.put(fileName, br);
    } catch (FileNotFoundException error) {
      throw new AppException("File not found: " + error.getMessage());
    }

    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new OpenRFileStmt(this.exp.deepCopy());
  }

  @Override
  public String toString() {
    return "openRFile(" + this.exp.toString() + ")";
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    IType expType = this.exp.typecheck(typeEnv);
    if (expType.equals(new StringType())) {
      return typeEnv;
    }
    throw new AppException("Expression does not evaluate to a string type");
  }
}
