package controller;

import model.PrgState;
import model.adt.stack.IGenericStack;
import model.adt.stack.exceptions.StackEmptyAppExcetion;
import model.exceptions.AppException;
import model.statements.IStmt;
import repository.IRepository;

public class Controller {
  private IRepository repo;
  private boolean displayFlag; // if the display flag is set, it will display the program state after each step
                               // of the execution

  public Controller(IRepository repo, boolean displayFlag) {
    this.repo = repo;
    this.displayFlag = displayFlag;
  }

  public boolean getDisplayFlag() {
    return this.displayFlag;
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
    while (!currentPrg.getExeStack().isEmpty()) {
      oneStep(currentPrg);
      if (this.displayFlag) {
        System.out.println(currentPrg);
      }
    }
  }
}
