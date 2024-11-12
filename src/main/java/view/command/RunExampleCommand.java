package view.command;

import controller.Controller;
import model.exceptions.AppException;

public class RunExampleCommand extends Command {
  private Controller controller;

  public RunExampleCommand(String key, String description, Controller controller) {
    super(key, description);
    this.controller = controller;
  }

  @Override
  public void execute() {
    try {
      this.controller.allSteps();
    } catch (AppException error) {
      System.err.println(error.getMessage());
    }
  }

}
