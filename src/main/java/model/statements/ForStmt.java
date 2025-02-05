package model.statements;

import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.expressions.IExp;
import model.expressions.RelationalExp;
import model.expressions.VariableExp;
import model.expressions.enums.RelationalOp;
import model.types.IType;
import model.types.IntegerType;
public class ForStmt implements IStmt {
  private String var;
  private IExp exp1;
  private IExp exp2;
  private IExp exp3;
  private IStmt stmt;

  public ForStmt(String var, IExp exp1, IExp exp2, IExp exp3, IStmt stmt) {
    this.var = var;
    this.exp1 = exp1;
    this.exp2 = exp2;
    this.exp3 = exp3;
    this.stmt = stmt;
  }

  public PrgState execute(PrgState state) throws AppException {
    CompoundStmt equivalentStatement = new CompoundStmt(
                              new VariableDeclarationStmt(this.var, new IntegerType()), 
                              new CompoundStmt(
                                new AssignmentStmt(this.var, this.exp1), 
                                new WhileStmt(
                                  new RelationalExp(new VariableExp(this.var), this.exp2, RelationalOp.LESS), 
                                  new CompoundStmt(
                                    this.stmt, 
                                    new AssignmentStmt(this.var, this.exp3)))));

    state.getExeStack().push(equivalentStatement);
    return null;
  }

  public IStmt deepCopy() {
    return new ForStmt(new String(this.var), this.exp1.deepCopy(), this.exp2.deepCopy(), this.exp3.deepCopy(), this.stmt.deepCopy());

  }

  public String toString() {
    return "for(" + this.var + "=" + this.exp1 + ";" + this.var + "<" + this.exp2 + ";" + this.var + "=" + this.exp3 + ")" + "{" + this.stmt + "}";
  }

  public IGenericDictionary<String, IType> typecheck(IGenericDictionary<String, IType> typeEnv) throws AppException {
    typeEnv.put(var, new IntegerType());

    IType type1 = this.exp1.typecheck(typeEnv);
    IType type2 = this.exp2.typecheck(typeEnv);
    IType type3 = this.exp3.typecheck(typeEnv);

    if (!type1.equals(new IntegerType())) {
      throw new AppException("Type of first expression in for stmt is not IntegerType");
    }

    if (!type2.equals(new IntegerType())) {
      throw new AppException("Type of second expression in for stmt is not IntegerType");
    }

    if (!type3.equals(new IntegerType())) {
      throw new AppException("Type of third expression in for stmt is not IntegerType");
    }

    return typeEnv;
  }
}
