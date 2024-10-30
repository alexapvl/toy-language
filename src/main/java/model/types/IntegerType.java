package model.types;

import model.values.IValue;
import model.values.IntegerValue;

public class IntegerType implements IType {
  @Override
  public String toString() {
    return "IntegerType";
  }

  @Override
  public IValue defaultValue() {
    return new IntegerValue(0);
  }

  @Override
  public boolean equals(IType other) {
    return (other instanceof IntegerType);
  }

}
