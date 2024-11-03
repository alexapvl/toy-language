package model.statements;

import model.PrgState;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.values.IValue;

public class PrintStmt implements IStmt {
  private IExp exp;

  public PrintStmt(IExp exp) {
    this.exp = exp;
  }

  @Override
  public PrgState execute(PrgState prg) throws AppException {
    IValue val;
    try {
      val = exp.eval(prg.getSymTable());
    } catch (AppException error) {
      throw new AppException(error.getMessage());
    }

    prg.getOutput().add(val);
    return prg;
  }

  @Override
  public IStmt deepCopy() {
    return new PrintStmt(exp.deepCopy());
  }

  @Override
  public String toString() {
    return "print(" + exp.toString() + ")";
  }
}
