package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.adt.stack.IGenericStack;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.expressions.RelationalExp;
import model.expressions.enums.RelationalOp;
import model.types.IType;
import model.values.IValue;

public class SwitchStmt implements IStmt {
  private IExp exp;
  private IExp expCase1;
  private IExp expCase2;
  private IExp expCaseDefault;
  private IStmt stmtCase1;
  private IStmt stmtCase2;
  private IStmt stmtCaseDefault;

  public SwitchStmt(IExp exp, IExp expCase1, IExp expCase2, IExp expCaseDefault, 
                      IStmt stmtCase1, IStmt stmtCase2, IStmt stmtCaseDefault) {
    this.exp = exp;
    this.expCase1 = expCase1;
    this.expCase2 = expCase2;
    this.expCaseDefault = expCaseDefault;
    this.stmtCase1 = stmtCase1;
    this.stmtCase2 = stmtCase2;
    this.stmtCaseDefault = stmtCaseDefault;
  }

  @Override
  public PrgState execute(PrgState state) throws AppException {
    IGenericStack<IStmt> exeStack = state.getExeStack();
    IGenericDictionary<String, IValue> symTable = state.getSymTable();
    IGenericHeap<Integer, IValue> heap = state.getHeap();

    IValue val = this.exp.eval(symTable, heap);
    IValue val1 = this.expCase1.eval(symTable, heap);
    IValue val2 = this.expCase2.eval(symTable, heap);
    
    if(val.getType().equals(val1.getType()) && val.getType().equals(val2.getType())) {
      IfStmt ifStmt = 
        new IfStmt(
            new RelationalExp(this.exp, this.expCase1, RelationalOp.EQUAL), this.stmtCase1, 
            new IfStmt(
                new RelationalExp(this.exp, this.expCase2, RelationalOp.EQUAL), this.stmtCase2, this.stmtCaseDefault)
            );
      exeStack.push(ifStmt);
    } else {
      throw new AppException("Main expression in the switch statement is not of the same type as the case 1 expression");
    }
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new SwitchStmt(this.exp.deepCopy(), this.expCase1.deepCopy(), this.expCase2.deepCopy(), this.expCaseDefault.deepCopy(),
                          this.stmtCase1.deepCopy(), this.stmtCase2.deepCopy(), this.stmtCaseDefault.deepCopy());
  }

  @Override
  public String toString() {
    return "switch(" + this.exp.toString() + ");case(" + this.expCase1.toString() + "):" + this.stmtCase1.toString() + " | case(" + this.expCase2.toString() + "):" + this.stmtCase2.toString() + " | default:" + this.stmtCaseDefault.toString();
  }

  @Override
  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    IType expType = this.exp.typecheck(typeEnv);
    IType expCase1Type = this.expCase1.typecheck(typeEnv);
    IType expCase2Type = this.expCase2.typecheck(typeEnv);
    IType expCaseDefaultType = this.expCaseDefault.typecheck(typeEnv);
    if (expType.equals(expCase1Type) && expType.equals(expCase2Type) && expType.equals(expCaseDefaultType)) {
      // if the expressions are all of the same type as the main expression
      // typecheck the statements of the program
      this.stmtCase1.typecheck(typeEnv);
      this.stmtCase2.typecheck(typeEnv);
      this.stmtCaseDefault.typecheck(typeEnv);
      return typeEnv;
    } else {
      throw new AppException("The expressions in the switch statement are not of the same type so they cannot be compared.");
    }
  }
}

