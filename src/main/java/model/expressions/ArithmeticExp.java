package model.expressions;

import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.types.IntegerType;
import model.values.IValue;
import model.values.IntegerValue;
import model.exceptions.AppException;
import model.expressions.exceptions.ArithmeticExpAppException;
import model.expressions.enums.ArithmeticOp;

public class ArithmeticExp implements IExp {
  private IExp first;
  private IExp second;
  private ArithmeticOp op;

  public ArithmeticExp(IExp first, ArithmeticOp op, IExp second) {
    this.first = first;
    this.second = second;
    this.op = op;
  }

  @Override
  public IValue eval(IGenericDictionary<String, IValue> symTable, IGenericHeap<Integer, IValue> heap)
      throws AppException, ArithmeticExpAppException {
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
        switch (op) {
          case ADD:
            return new IntegerValue(n1 + n2);
          case SUBTRACT:
            return new IntegerValue(n1 - n2);
          case MULTIPLY:
            return new IntegerValue(n1 * n2);
          case DIVIDE:
            if (n2 == 0)
              throw new ArithmeticExpAppException("Division by zero");
            return new IntegerValue(n1 / n2);
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
    return new ArithmeticExp(first, op, second);
  }

  @Override
  public String toString() {
    return "(" + first.toString() + " " + op.toString() + " " + second.toString() + ")";
  }
}
