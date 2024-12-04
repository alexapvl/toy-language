package model.statements;

import java.io.BufferedReader;
import java.io.IOException;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

public class CloseRFileStmt implements IStmt {
  private IExp exp;

  public CloseRFileStmt(IExp exp) {
    this.exp = exp;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IGenericDictionary<String, IValue> symTable = prg.getSymTable();
    IGenericDictionary<StringValue, BufferedReader> fileTable = prg.getFileTable();
    IGenericHeap<Integer, IValue> heap = prg.getHeap();

    IValue value;
    try {
      value = this.exp.eval(symTable, heap);
    } catch (AppException error) {
      throw new AppException(error.getMessage());
    }

    if (!value.getType().equals(new StringType())) {
      throw new AppException("Cannot close file because expression is not of type StringType");
    }

    StringValue stringValue = (StringValue) value;
    BufferedReader br;
    try {
      br = fileTable.lookup(stringValue);
    } catch (KeyNotFoundAppException error) {
      throw new AppException(error.getMessage());
    }

    if (br == null) {
      throw new AppException("File " + stringValue + " is not opened");
    }

    try {
      br.close();
    } catch (IOException error) {
      throw new AppException("Error when closing file: " + error.getMessage());
    }

    fileTable.remove(stringValue);

    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new CloseRFileStmt(this.exp.deepCopy());
  }

  @Override
  public String toString() {
    return "closeRFile(" + this.exp + ")";
  }
}
