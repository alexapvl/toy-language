package model.values;

import model.types.IType;
import model.types.BooleanType;

public class BooleanValue implements IValue {
  private boolean value;

  public BooleanValue(boolean value) {
    this.value = value;
  }

  public BooleanValue() {
    this.value = false;
  }

  public boolean getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return Boolean.toString(this.value);
  }

  @Override
  public boolean equals(IValue other) {
    if (other.getType().equals(this.getType())) {
      return this.getValue() == ((BooleanValue) other).getValue();
    }
    return false;
  }

  @Override
  public IType getType() {
    return new BooleanType();
  }

  @Override
  public IValue deepCopy() {
    return new BooleanValue(this.value);
  }
}
