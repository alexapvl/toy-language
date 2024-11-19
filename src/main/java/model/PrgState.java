package model;

import java.io.BufferedReader;

import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.adt.list.IGenericList;
import model.adt.stack.IGenericStack;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

public class PrgState {
  private IGenericDictionary<String, IValue> symTable;
  private IGenericStack<IStmt> exeStack;
  private IGenericList<IValue> out;
  private IStmt originalProgram;
  private IGenericDictionary<StringValue, BufferedReader> fileTable;
  private IGenericHeap<Integer, IValue> heap;

  public PrgState(IGenericDictionary<String, IValue> symTable, IGenericStack<IStmt> exeStack,
      IGenericList<IValue> out, IStmt originalProgram, IGenericDictionary<StringValue, BufferedReader> fileTable,
      IGenericHeap<Integer, IValue> heap) {
    this.symTable = symTable;
    this.exeStack = exeStack;
    this.out = out;
    this.originalProgram = originalProgram;
    this.fileTable = fileTable;
    this.heap = heap;

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

  public IGenericDictionary<StringValue, BufferedReader> getFileTable() {
    return this.fileTable;
  }

  public void setFileTable(IGenericDictionary<StringValue, BufferedReader> fileTable) {
    this.fileTable = fileTable;
  }

  public IGenericHeap<Integer, IValue> getHeap() {
    return this.heap;
  }

  public void setHeap(IGenericHeap<Integer, IValue> heap) {
    this.heap = heap;
  }

  @Override
  public String toString() {
    return "ExeStack:\n" + this.exeStack + "\nSymTable:\n" + this.symTable
        + "\nOut:\n" + this.out + "\nFileTable:\n" + this.fileTable + "\nHeap:\n" + this.heap;
  }

}
