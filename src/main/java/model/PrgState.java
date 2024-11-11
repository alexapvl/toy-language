package model;

import model.adt.dictionary.IGenericDictionary;
import model.adt.list.IGenericList;
import model.adt.stack.IGenericStack;
import model.statements.IStmt;
import model.values.IValue;

public class PrgState {
  private IGenericDictionary<String, IValue> symTable;
  private IGenericStack<IStmt> exeStack;
  private IGenericList<IValue> out;
  private IStmt originalProgram;

  public PrgState(IGenericDictionary<String, IValue> symTable, IGenericStack<IStmt> exeStack,
      IGenericList<IValue> out, IStmt originalProgram) {
    this.symTable = symTable;
    this.exeStack = exeStack;
    this.out = out;
    this.originalProgram = originalProgram;

    exeStack.push(originalProgram);
  }

  public IGenericDictionary<String, IValue> getSymTable() {
    return symTable;
  }

  public void setSymTable(IGenericDictionary<String, IValue> symTable) {
    this.symTable = symTable;
  }

  public IGenericStack<IStmt> getExeStack() {
    return exeStack;
  }

  public void setExeStack(IGenericStack<IStmt> exeStack) {
    this.exeStack = exeStack;
  }

  public IGenericList<IValue> getOutput() {
    return out;
  }

  public void setOutput(IGenericList<IValue> out) {
    this.out = out;
  }

  public IStmt getOriginalProgram() {
    return originalProgram;
  }

  public void setOriginalProgram(IStmt originalProgram) {
    this.originalProgram = originalProgram.deepCopy();
  }

  @Override
  public String toString() {
    return "ExeStack:\n" + this.exeStack.toString() + "\nSymTable:\n" + this.symTable.toString()
        + "\nOut:\n" + this.out.toString() + "\nFileTable:\nTo be implemented";
  }

}
