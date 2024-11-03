package model.expressions;

import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.expressions.exceptions.ArithmeticExpAppException;
import model.values.IValue;
import model.values.BooleanValue;
import model.types.BooleanType;

enum LogicalOp {
  AND,
  OR
}

public class LogicalExp implements IExp {
  IExp first;
  IExp second;
  LogicalOp op;

  public LogicalExp(IExp first, LogicalOp op, IExp second) {
    this.first = first;
    this.second = second;
    this.op = op;
  }

  @Override
  public IValue eval(IGenericDictionary<String, IValue> symTable) throws AppException, ArithmeticExpAppException {
    IValue v1, v2;
    v1 = first.eval(symTable);
    v2 = second.eval(symTable);

    if (v1.getType().equals(new BooleanType())) {
      if (v2.getType().equals(new BooleanType())) {
        BooleanValue b1, b2;
        b1 = (BooleanValue) v1;
        b2 = (BooleanValue) v2;
        boolean n1, n2;
        n1 = b1.getValue();
        n2 = b2.getValue();
        switch (op) {
          case AND:
            return new BooleanValue(n1 && n2);
          case OR:
            return new BooleanValue(n1 || n2);
          default:
            throw new AppException("Invalid operator provided for the logical operation: " + this);
        }
      } else
        throw new AppException("Second expression type is not BooleanType, it is: " + v2.getType());
    } else
      throw new AppException("First expression type is not BooleanType, it is: " + v1.getType());
  }

  @Override
  public IExp deepCopy() {
    return new LogicalExp(first.deepCopy(), op, second.deepCopy());
  }

  @Override
  public String toString() {
    return first.toString() + " " + op + " " + second.toString();
  }
}
