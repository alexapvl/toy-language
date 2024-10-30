package model.types;

import model.values.IValue;

public interface IType {
  String toString();

  IValue defaultValue();

  boolean equals(IType other);
}
