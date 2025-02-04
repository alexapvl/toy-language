package model.statements;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import model.PrgState;
import model.adt.ICountSemaphore;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.IType;
import model.types.IntegerType;
import model.values.IValue;
import model.values.IntegerValue;

public class CreateSemaphoreStmt implements IStmt {
  private String var;
  private IExp exp;

  public CreateSemaphoreStmt(String var, IExp exp) {
    this.var = var;
    this.exp = exp;
  }

  public PrgState execute(PrgState state) throws AppException {
    IGenericDictionary<String, IValue> symTable = state.getSymTable();
    IGenericHeap<Integer, IValue> heap = state.getHeap();
    ICountSemaphore<Integer, Pair<Integer, List<Integer>>> countSemaphoreTable = state.getCountSemaphore();
    IValue val = this.exp.eval(symTable, heap);

    if (!val.getType().equals(new IntegerType())) {
      throw new AppException("Expression inside the create semaphore statement is not IntegerType");
    }
    int number1 = ((IntegerValue) val).getValue();
    int freeAddress = countSemaphoreTable.allocate();

    countSemaphoreTable.put(freeAddress, new Pair<>(number1, new ArrayList<>()));

    if (!symTable.contains(this.var)) {
      throw new AppException("The variable in the semaphore create statement does not exist in the SymTable");
    }

    symTable.put(this.var, new IntegerValue(freeAddress));
    return null;
  }

  public IStmt deepCopy() { // unlikely that we will use it since it is shared among all program states
    return new CreateSemaphoreStmt(new String(this.var), this.exp.deepCopy());
  }

  public String toString() {
    return "CreateSemaphore(" + this.var + "," + this.exp + ")";
  }

  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    try {
      IType typeVar = typeEnv.lookup(var);
      IType typeExp = exp.typecheck(typeEnv);

      if (typeVar.equals(new IntegerType())) {
        if (typeExp.equals(new IntegerType())) {
          return typeEnv;
        } else {
          throw new AppException("Expression type must be IntegerType in the create semaphore statement");
        }
      } else {
        throw new AppException("Variable type must be IntegerType in create semaphore statement");
      }

    } catch (KeyNotFoundAppException e) {
      throw new AppException(e.getMessage());
    }
  }
}
