package model.statements;

import java.util.ArrayList;
import java.util.List;

import model.PrgState;
import model.adt.IToySemaphore;
import model.adt.Tuple;
import model.adt.dictionary.IGenericDictionary;
import model.adt.dictionary.exceptions.KeyNotFoundAppException;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.types.IType;
import model.types.IntegerType;
import model.values.IValue;
import model.values.IntegerValue;

public class NewToySemaphoreStmt implements IStmt {
  private String var;
  private IExp exp1;
  private IExp exp2;

  public NewToySemaphoreStmt(String var, IExp exp1, IExp exp2) {
    this.var = var;
    this.exp1 = exp1;
    this.exp2 = exp2;
  }

  public PrgState execute(PrgState state) throws AppException {
    IGenericDictionary<String, IValue> symTable = state.getSymTable();
    IGenericHeap<Integer, IValue> heap = state.getHeap();

    IValue val1 = this.exp1.eval(symTable, heap);
    IValue val2 = this.exp2.eval(symTable, heap);

    if (!val1.getType().equals(new IntegerType())) {
      throw new AppException("First expression is not of type IntegerType in NewToySemaphoreStmt");
    }
    if (!val2.getType().equals(new IntegerType())) {
      throw new AppException("Second expression is not of type IntegerType in NewToySemaphoreStmt");
    }

    IntegerValue number1 = (IntegerValue) val1;
    IntegerValue number2 = (IntegerValue) val2;

    IToySemaphore<Integer, Tuple<Integer, List<Integer>, Integer>> toySemaphoreTable =  state.getToySemaphoreTable();
    Integer freeAddress = toySemaphoreTable.allocate();
    toySemaphoreTable.put(freeAddress, new Tuple<>(number1.getValue(), new ArrayList<>(), number2.getValue()));

    try {
      IValue val = symTable.lookup(this.var);
      if (!val.getType().equals(new IntegerType())) {
        throw new AppException("Variable in semaphore is not of type IntegerType");
      }
      symTable.put(this.var, new IntegerValue(freeAddress));
    } catch (KeyNotFoundAppException e) {
      throw new AppException(e.getMessage());
    }

    return null;
  }

  public IStmt deepCopy() {
    return new NewToySemaphoreStmt(new String(this.var), this.exp1.deepCopy(), this.exp2.deepCopy());
  }

  public String toString() {
    return "newSemaphore(" + this.var + "," + this.exp1 + "," + this.exp2 + ")";
  }

  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    if (!typeEnv.contains(this.var)) {
      throw new AppException("Variable from the NewToySemaphoreStmt does not exist");
    }
    this.exp1.typecheck(typeEnv);
    this.exp2.typecheck(typeEnv);

    return typeEnv;
  }
}
