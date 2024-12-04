package model;

import java.io.BufferedReader;
import java.util.concurrent.atomic.AtomicInteger;

import model.adt.dictionary.GenericDictionary;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.adt.heap.IGenericHeap;
import model.adt.list.IGenericList;
import model.adt.stack.IGenericStack;
import model.adt.stack.exceptions.StackEmptyAppExcetion;
import model.exceptions.AppException;
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
  private static AtomicInteger nextId = new AtomicInteger(1);
  private int id;

  public PrgState(IGenericDictionary<String, IValue> symTable, IGenericStack<IStmt> exeStack,
      IGenericList<IValue> out, IStmt originalProgram, IGenericDictionary<StringValue, BufferedReader> fileTable,
      IGenericHeap<Integer, IValue> heap) {
    this.id = getNextId();
    this.symTable = symTable;
    this.exeStack = exeStack;
    this.out = out;
    this.originalProgram = originalProgram;
    this.fileTable = fileTable;
    this.heap = heap;

    exeStack.push(originalProgram);
  }

  private synchronized int getNextId() {
    return nextId.getAndIncrement();
  }

  public int getId() {
    return this.id;
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
    return "Program ID: " + this.id + "\nExeStack:\n" + this.exeStack + "\nSymTable:\n" + this.symTable
        + "\nOut:\n" + this.out + "\nFileTable:\n" + this.fileTable + "\nHeap:\n" + this.heap;
  }

  public boolean isNotCompleted() {
    return !this.exeStack.isEmpty();
  }

  public PrgState oneStep() throws AppException {
    IGenericStack<IStmt> exeStack = this.getExeStack();
    if (exeStack.isEmpty()) {
      throw new AppException("The execution stack is empty, there are no more steps which can be made.");
    }

    IStmt currentStmt;
    try {
      currentStmt = exeStack.pop();
    } catch (StackEmptyAppExcetion error) {
      throw new AppException(error.getMessage());
    }
    return currentStmt.execute(this);
  }

  public IGenericDictionary<String, IValue> cloneSymTable() {
    IGenericDictionary<String, IValue> newSymTable = new GenericDictionary<>();
    IGenericDictionary<String, IValue> symTable = this.getSymTable();
    for (String key : symTable.getMap().keySet()) {
      try {
        newSymTable.put(key, symTable.lookup(key));
      } catch (KeyNotFoundAppException error) {
        System.err.println(error.getMessage());
      }
    }
    return newSymTable;
  }

}
