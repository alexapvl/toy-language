package model.values;

import model.types.IType;
import model.types.StringType;

public class StringValue implements IValue {
  private String value;

  public StringValue(String value) {
    this.value = value;
  }

  public StringValue() {
    this.value = "";
  }

  public String getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  @Override
  public boolean equals(IValue other) {
    if (other.getType().equals(this.getType())) {
      return this.getValue() == ((StringValue) other).getValue();
    }
    return false;
  }

  @Override
  public IType getType() {
    return new StringType();
  }

  @Override
  public IValue deepCopy() {
    return new StringValue(this.value);
  }
}
