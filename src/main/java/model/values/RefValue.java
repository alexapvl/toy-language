package model.values;

import model.types.IType;
import model.types.RefType;

public class RefValue implements IValue {
  private Integer address;
  private IType locationType;

  public RefValue(int address, IType locationType) {
    this.address = address;
    this.locationType = locationType;
  }

  public Integer getAddr() {
    return this.address;
  }

  public IType getLocationType() {
    return this.locationType;
  }

  @Override
  public boolean equals(IValue obj) {
    if (obj instanceof RefValue) {
      return this.address == ((RefValue) obj).getAddr()
          && this.locationType.equals(((RefValue) obj).getLocationType());
    }
    return false;
  }

  @Override
  public String toString() {
    return "(" + this.address + ", " + this.locationType + ")";
  }

  @Override
  public IType getType() {
    return new RefType(this.locationType);
  }

  @Override
  public IValue deepCopy() {
    return new RefValue(this.address, this.locationType.deepCopy());
  }

}
