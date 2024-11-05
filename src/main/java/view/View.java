package view;

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
import model.statements.CompoundStmt;
import model.statements.IStmt;
import model.statements.IfStmt;
import model.statements.PrintStmt;
import model.statements.VariableDeclarationStmt;
import model.types.BooleanType;
import model.types.IntegerType;
import model.values.BooleanValue;
import model.values.IValue;
import model.values.IntegerValue;
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

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    IStmt selectedProgram = null;

    while (selectedProgram == null) {
      System.out.println("1. int v; v = 2; Print(v)");
      System.out.println("2. int a; int b; a = 2 + 3 * 5; b = a + 1; Print(b)");
      System.out.println("3. bool a; int v; a = true; (If a Then v = 2 Else v = 3); Print(v)");
      System.out.println("\nSelect the program to execute: (1 - 3)");

      try {
        int choice = scanner.nextInt();
        switch (choice) {
          case 1:
            selectedProgram = createExample1();
            break;
          case 2:
            selectedProgram = createExample2();
            break;
          case 3:
            selectedProgram = createExample3();
            break;
          default:
            System.out.println("Invalid choice!");
        }
      } catch (InputMismatchException e) {
        System.out.println("Please enter a valid number!");
        scanner.nextLine();
      }
    }

    IGenericDictionary<String, IValue> symTable = new GenericDictionary<>();
    IGenericStack<IStmt> stk = new GenericStack<>();
    IGenericList<IValue> output = new GenericList<>();

    PrgState prg = new PrgState(symTable, stk, output, selectedProgram);
    IRepository repo = new Repository(prg);
    Controller controller = new Controller(repo, true);

    try {
      controller.allSteps();
    } catch (AppException error) {
      System.out.println(error.getMessage());
    }

    scanner.close();
  }
}
