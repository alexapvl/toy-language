package model.types;

import model.values.IValue;
import model.values.StringValue;

public class StringType implements IType {
  @Override
  public String toString() {
    return "StringType";
  }

  @Override
  public IValue defaultValue() {
    return new StringValue("");
  }

  @Override
  public boolean equals(IType other) {
    return (other instanceof StringType);
  }

  @Override
  public IType deepCopy() {
    return new StringType();
  }

}
