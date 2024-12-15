package model.expressions;

import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.values.IValue;
import model.values.BooleanValue;
import model.types.BooleanType;
import model.types.IType;
import model.expressions.enums.LogicalOp;

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
  public IValue eval(IGenericDictionary<String, IValue> symTable, IGenericHeap<Integer, IValue> heap)
      throws AppException {
    IValue v1, v2;
    v1 = first.eval(symTable, heap);
    v2 = second.eval(symTable, heap);

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
    return "(" + first.toString() + " " + op + " " + second.toString() + ")";
  }

  @Override
  public IType typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    IType type1, type2;
    type1 = first.typecheck(typeEnv);
    type2 = second.typecheck(typeEnv);
    if (type1.equals(new BooleanType())) {
      if (type2.equals(new BooleanType())) {
        return new BooleanType();
      } else
        throw new AppException("Second expression type is not BooleanType, it is: " + type2);
    } else
      throw new AppException("First expression type is not BooleanType, it is: " + type1);
  }
}
