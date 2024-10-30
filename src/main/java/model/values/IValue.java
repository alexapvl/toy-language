package model.values;

import model.types.IType;

public interface IValue {
  String toString();

  boolean equals(IValue other);

  IType getType();
}
