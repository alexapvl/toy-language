package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import view.command.Command;

public class TextMenu {
  private Map<String, Command> commands;

  public TextMenu() {
    this.commands = new HashMap<String, Command>();
  }

  public void addCommand(Command command) {
    this.commands.put(command.getKey(), command);
  }

  private void printMenu() {
    for (String key : this.commands.keySet()) {
      String line = String.format("%4s : %s", this.commands.get(key).getKey(), this.commands.get(key).getDescription());
      System.out.println(line);
    }
  }

  public void show() {
    Scanner scanner = new Scanner(System.in);
    try (scanner) {
      while (true) {
        printMenu();
        System.out.printf("Input the option: ");
        String key = scanner.nextLine();
        Command command = commands.get(key);
        if (command == null) {
          System.out.println("Invalid Option");
          continue;
        }
        command.execute();
      }
    }
  }
}
