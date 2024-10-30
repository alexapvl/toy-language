package model.values;

import model.types.IType;
import model.types.IntegerType;

public class IntegerValue implements IValue {
  private int value;

  public IntegerValue(int value) {
    this.value = value;
  }

  public IntegerValue() {
    this.value = 0;
  }

  public int getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return Integer.toString(this.value);
  }

  @Override
  public boolean equals(IValue other) {
    if (other.getType().equals(this.getType())) {
      return this.getValue() == ((IntegerValue) other).getValue();
    }
    return false;
  }

  @Override
  public IType getType() {
    return new IntegerType();
  }

}
