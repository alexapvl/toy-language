package model.expressions;

import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.exceptions.AppException;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class ReadHeapExp implements IExp {
  private IExp exp;

  public ReadHeapExp(IExp exp) {
    this.exp = exp;
  }

  @Override
  public IValue eval(IGenericDictionary<String, IValue> symTable, IGenericHeap<Integer, IValue> heap)
      throws AppException, ArithmeticException {

    IValue value = this.exp.eval(symTable, heap);
    if (!(value instanceof RefValue)) {
      throw new AppException("The expression does not evaluate to a RefValue");
    }

    RefValue refValue = (RefValue) value;
    Integer address = refValue.getAddr();
    if (!heap.contains(address)) {
      throw new AppException("The address " + address + " is not in the heap (it was not allocated)");
    }

    return heap.lookup(address);
  }

  @Override
  public IExp deepCopy() {
    return new ReadHeapExp(this.exp.deepCopy());
  }

  @Override
  public String toString() {
    return "readHeap(" + this.exp + ")";
  }

  @Override
  public IType typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    IType type = this.exp.typecheck(typeEnv);
    if (type instanceof RefType) {
      RefType refType = (RefType) type;
      return refType.getInner();
    } else {
      throw new AppException("The expression does not evaluate to a RefType");
    }
  }
}
