package model.expressions;

import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.expressions.enums.RelationalOp;
import model.types.IntegerType;
import model.values.BooleanValue;
import model.values.IValue;
import model.values.IntegerValue;

public class RelationalExp implements IExp {
  private IExp first;
  private IExp second;
  private RelationalOp op;

  public RelationalExp(IExp first, IExp second, RelationalOp op) {
    this.first = first;
    this.second = second;
    this.op = op;
  }

  public IValue eval(IGenericDictionary<String, IValue> symTable, IGenericHeap<Integer, IValue> heap)
      throws AppException {
    IValue v1, v2;

    v1 = first.eval(symTable, heap);
    if (v1.getType().equals(new IntegerType())) {
      v2 = second.eval(symTable, heap);
      if (v2.getType().equals(new IntegerType())) {
        IntegerValue i1, i2;
        i1 = (IntegerValue) v1;
        i2 = (IntegerValue) v2;
        int n1, n2;
        n1 = i1.getValue();
        n2 = i2.getValue();
        // created BooleanValue objects for code readability
        BooleanValue True = new BooleanValue(true);
        BooleanValue False = new BooleanValue(false);
        switch (op) {
          case LESS:
            if (n1 < n2)
              return True;
            else
              return False;
          case LESS_EQUAL:
            if (n1 <= n2)
              return True;
            else
              return False;
          case EQUAL:
            if (n1 == n2)
              return True;
            else
              return False;
          case DIFFERENT:
            if (n1 != n2)
              return True;
            else
              return False;
          case GREATER:
            if (n1 > n2)
              return True;
            else
              return False;
          case GREATER_EQUAL:
            if (n1 >= n2)
              return True;
            else
              return False;
          default:
            throw new AppException("Invalid operator provided for the arithmetic operation: " + this);
        }
      } else
        throw new AppException("Second expression type is not IntegerType, it is: " + v2.getType());
    } else
      throw new AppException("First expression type is not IntegerType, it is: " + v1.getType());
  }

  @Override
  public IExp deepCopy() {
    return new RelationalExp(first.deepCopy(), second.deepCopy(), op);
  }

  @Override
  public String toString() {
    return "(" + first.toString() + " " + op.toString() + " " + second.toString() + ")";
  }

}
