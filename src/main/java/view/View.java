package view;

import java.io.BufferedReader;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.expressions.enums.ArithmeticOp;
import model.PrgState;
import model.adt.dictionary.GenericDictionary;
import model.adt.dictionary.IGenericDictionary;
import model.adt.list.GenericList;
import model.adt.list.IGenericList;
import model.adt.stack.GenericStack;
import model.adt.stack.IGenericStack;
import model.exceptions.AppException;
import model.expressions.ArithmeticExp;
import model.expressions.ValueExp;
import model.expressions.VariableExp;
import model.statements.AssignmentStmt;
import model.statements.CloseRFileStmt;
import model.statements.CompoundStmt;
import model.statements.IStmt;
import model.statements.IfStmt;
import model.statements.OpenRFileStmt;
import model.statements.PrintStmt;
import model.statements.ReadFileStmt;
import model.statements.VariableDeclarationStmt;
import model.types.BooleanType;
import model.types.IntegerType;
import model.types.StringType;
import model.values.BooleanValue;
import model.values.IValue;
import model.values.IntegerValue;
import model.values.StringValue;
import repository.IRepository;
import repository.Repository;
import controller.Controller;

public class View {
  private static IStmt createExample1() {
    // int v; v = 2; Print(v)
    return new CompoundStmt(
        new VariableDeclarationStmt("v", new IntegerType()),
        new CompoundStmt(
            new AssignmentStmt("v", new ValueExp(new IntegerValue(2))),
            new PrintStmt(new VariableExp("v"))));
  }

  private static IStmt createExample2() {
    // int a; int b; a = 2 + 3 * 5; b = a + 1; Print(b)

    return new CompoundStmt(
        new VariableDeclarationStmt("a", new IntegerType()),
        new CompoundStmt(
            new VariableDeclarationStmt("b", new IntegerType()),
            new CompoundStmt(
                new AssignmentStmt("a",
                    new ArithmeticExp(new ValueExp(new IntegerValue(2)), ArithmeticOp.ADD,
                        new ArithmeticExp(new ValueExp(new IntegerValue(3)), ArithmeticOp.MULTIPLY,
                            new ValueExp(new IntegerValue(5))))),
                new CompoundStmt(
                    new AssignmentStmt("b",
                        new ArithmeticExp(new VariableExp("a"), ArithmeticOp.ADD, new ValueExp(new IntegerValue(1)))),
                    new PrintStmt(new VariableExp("b"))))));
  }

  private static IStmt createExample3() {
    // bool a; int v; a = true;(if a then v = 2 else v = 3); Print(v)
    return new CompoundStmt(
        new VariableDeclarationStmt("a", new BooleanType()),
        new CompoundStmt(
            new VariableDeclarationStmt("v", new IntegerType()),
            new CompoundStmt(
                new AssignmentStmt("a", new ValueExp(new BooleanValue(true))),
                new CompoundStmt(
                    new IfStmt(new VariableExp("a"),
                        new AssignmentStmt("v", new ValueExp(new IntegerValue(2))),
                        new AssignmentStmt("v", new ValueExp(new IntegerValue(3)))),
                    new PrintStmt(new VariableExp("v"))))));
  }

  private static IStmt createExample4() {
    // string varf; varf = "test.in"; openRFile(varf); int varc; readFile(varf,
    // varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf)
    return new CompoundStmt(
        new VariableDeclarationStmt("varf", new StringType()),
        new CompoundStmt(
            new AssignmentStmt("varf", new ValueExp(new StringValue("test.in"))),
            new CompoundStmt(
                new OpenRFileStmt(new VariableExp("varf")),
                new CompoundStmt(
                    new VariableDeclarationStmt("varc", new IntegerType()),
                    new CompoundStmt(
                        new ReadFileStmt(new VariableExp("varf"), "varc"),
                        new CompoundStmt(
                            new PrintStmt(new VariableExp("varc")),
                            new CompoundStmt(
                                new ReadFileStmt(new VariableExp("varf"), "varc"),
                                new CompoundStmt(
                                    new PrintStmt(new VariableExp("varc")),
                                    new CloseRFileStmt(new VariableExp("varf"))))))))));

  }

  private static IStmt selectExample(Scanner scanner) {
    IStmt selectedExample = null;

    while (selectedExample == null) {
      System.out.println("1. int v; v = 2; Print(v)");
      System.out.println("2. int a; int b; a = 2 + 3 * 5; b = a + 1; Print(b)");
      System.out.println("3. bool a; int v; a = true; (If a Then v = 2 Else v = 3); Print(v)");
      System.out.println(
          "string varf; varf = \"test.in\"; openRFile(varf); int varc; readFile(varf, varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf)");
      System.out.println("0. Exit");
      System.out.println("\nSelect the program to execute: (1 - 3) or '0' to exit");

      try {
        int choice = scanner.nextInt();
        switch (choice) {
          case 1:
            selectedExample = createExample1();
            break;
          case 2:
            selectedExample = createExample2();
            break;
          case 3:
            selectedExample = createExample3();
            break;
          case 4:
            selectedExample = createExample4();
            break;
          case 0:
            System.exit(0);
          default:
            System.out.println("Invalid choice!");
        }
      } catch (InputMismatchException e) {
        System.out.println("Please enter a valid number!");
        scanner.nextInt();
      }
    }
    return selectedExample;
  }

  private static PrgState createPrgState(IStmt originalProgram) {
    IGenericDictionary<String, IValue> symTable = new GenericDictionary<>();
    IGenericStack<IStmt> exeStack = new GenericStack<>();
    IGenericList<IValue> output = new GenericList<>();
    IGenericDictionary<StringValue, BufferedReader> fileTable = new GenericDictionary<>();

    return new PrgState(symTable, exeStack, output, originalProgram, fileTable);
  }

  private static Controller createController(IStmt originalProgram, String logFilePath, boolean displayFlag) {
    PrgState prg = createPrgState(originalProgram);
    IRepository repo = new Repository(prg, logFilePath);

    return new Controller(repo, displayFlag);
  }

  private static String selectLogFilePath(Scanner scanner) {
    String logFilePath = null;

    System.out.println("Provide the name of the log file(without any extension): ");
    logFilePath = scanner.next();

    return logFilePath + ".log";
  }

  private static void runExample(Controller controller) {
    try {
      controller.allSteps();
    } catch (AppException error) {
      System.err.println(error.getMessage());
    }
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    IStmt selectedExample;
    String logFilePath;
    try (scanner) {
      selectedExample = selectExample(scanner);
      logFilePath = selectLogFilePath(scanner);
    }

    Controller controller = createController(selectedExample, logFilePath, false);

    runExample(controller);
  }
}
