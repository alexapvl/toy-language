package view.command;

import controller.Controller;
import model.adt.dictionary.GenericDictionary;
import model.adt.dictionary.IGenericDictionary;
import model.exceptions.AppException;
import model.types.IType;

public class RunExampleCommand extends Command {
  private Controller controller;

  public RunExampleCommand(String key, String description, Controller controller) {
    super(key, description);
    this.controller = controller;
  }

  @Override
  public void execute() {
    try {
      // do the typechecking here, if the program is not type correct, throw an
      // exception
      IGenericDictionary<String, IType> typeEnv = new GenericDictionary<>();
      this.controller.getRepo().getPrgList().get(0).getOriginalProgram().typecheck(typeEnv);
      this.controller.allSteps();
    } catch (AppException error) {
      System.err.println(error.getMessage());
    }
  }

}
