package model.statements;

import java.io.BufferedReader;
import java.io.IOException;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.IType;
import model.types.IntegerType;
import model.types.StringType;
import model.values.IValue;
import model.values.IntegerValue;
import model.values.StringValue;

public class ReadFileStmt implements IStmt {
  private IExp exp;
  private String varName;

  public ReadFileStmt(IExp exp, String varName) {
    this.exp = exp;
    this.varName = varName;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IGenericDictionary<String, IValue> symTable = prg.getSymTable();
    IGenericDictionary<StringValue, BufferedReader> fileTable = prg.getFileTable();
    IGenericHeap<Integer, IValue> heap = prg.getHeap();

    // check if symTable contains the key
    if (!symTable.contains(this.varName)) {
      throw new AppException("Variable " + this.varName + " is not defined");
    }

    // put that value from the key into a varValue
    IValue varValue;
    try {
      varValue = symTable.lookup(this.varName);
    } catch (KeyNotFoundAppException error) {
      throw new AppException(error.getMessage());
    }

    // check to see if the type is IntegerType
    if (!varValue.getType().equals(new IntegerType())) {
      throw new AppException("Variable " + varValue + " is not of type IntegerType");
    }

    // evaluate the expression to get the value
    IValue fileNameValue;
    try {
      fileNameValue = this.exp.eval(symTable, heap);
    } catch (AppException error) {
      throw new AppException(error.getMessage());
    }
    // check that the value is of type StringType
    if (!fileNameValue.getType().equals(new StringType())) {
      throw new AppException("Variable " + fileNameValue + " is not of type StringType");
    }

    // cast
    StringValue fileName = (StringValue) fileNameValue;
    // get the buffer from the fileTable
    BufferedReader br;
    try {
      br = fileTable.lookup(fileName);
    } catch (KeyNotFoundAppException error) {
      throw new AppException(error.getMessage());
    }
    // if null, means that the file is not opened
    if (br == null) {
      throw new AppException("File " + fileName + " is not opened");
    }

    // read a line from the buffer
    try {
      String line = br.readLine();
      IntegerValue val;

      // if line is null, it means that the
      // variable will be declared with default value 0
      if (line == null) {
        val = new IntegerValue();
      } else {
        // if not, try to parse the string and assign the value to it
        try {
          val = new IntegerValue(Integer.parseInt(line));
        } catch (NumberFormatException error) {
          throw new AppException("Invalid input file structure, not an integer");
        }
      }

      // update the value of the variable in the symTable
      symTable.put(this.varName, val); // update the value inside the symTable
    } catch (IOException error) {
      throw new AppException("Error when reading from file: " + error.getMessage());
    }

    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new ReadFileStmt(this.exp.deepCopy(), varName);
  }

  @Override
  public String toString() {
    return "readFile(" + this.exp + ", " + this.varName + ")";
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv)
      throws AppException {
    IType expType = this.exp.typecheck(typeEnv);
    if (expType.equals(new StringType())) {
      return typeEnv;
    }
    throw new AppException("Expression does not evaluate to a string type");
  }
}
