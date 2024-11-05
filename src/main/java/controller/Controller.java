package controller;

import model.PrgState;
import model.adt.stack.IGenericStack;
import model.adt.stack.exceptions.StackEmptyAppExcetion;
import model.exceptions.AppException;
import model.statements.IStmt;
import repository.IRepository;

public class Controller {
  private IRepository repo;

  public Controller(IRepository repo) {
    this.repo = repo;
  }

  public PrgState oneStep(PrgState prg) throws AppException {
    IGenericStack<IStmt> exeStack = prg.getExeStack();
    if (exeStack.isEmpty()) {
      throw new AppException("The execution stack is empty, there are no more steps which can be made.");
    }

    IStmt currentStmt;
    try {
      currentStmt = exeStack.pop();
    } catch (StackEmptyAppExcetion error) {
      throw new AppException(error.getMessage());
    }
    return currentStmt.execute(prg);
  }

  public void allSteps() throws AppException {
    PrgState currentPrg = repo.getCurrentPrg();
    System.out.println(currentPrg);
    while (!currentPrg.getExeStack().isEmpty()) {
      oneStep(currentPrg);
      System.out.println(currentPrg);
    }
  }

}
