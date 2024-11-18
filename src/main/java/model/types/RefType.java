package model.types;

import model.values.IValue;
import model.values.RefValue;

public class RefType implements IType {
  private IType inner;

  public RefType(IType inner) {
    this.inner = inner;
  }

  public IType getInner() {
    return this.inner;
  }

  @Override
  public boolean equals(IType obj) {
    if (obj instanceof RefType) {
      return this.inner.equals(((RefType) obj).getInner());
    }
    return false;
  }

  @Override
  public String toString() {
    return "Ref(" + this.inner + ")";
  }

  @Override
  public IType deepCopy() {
    return new RefType(this.inner.deepCopy());
  }

  @Override
  public IValue defaultValue() {
    return new RefValue(0, this.inner);
  }

}
