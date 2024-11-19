package view;

import java.io.BufferedReader;

import model.expressions.enums.ArithmeticOp;
import model.PrgState;
import model.adt.dictionary.GenericDictionary;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.GenericHeap;
import model.adt.heap.IGenericHeap;
import model.adt.list.GenericList;
import model.adt.list.IGenericList;
import model.adt.stack.GenericStack;
import model.adt.stack.IGenericStack;
import model.expressions.ArithmeticExp;
import model.expressions.ReadHeapExp;
import model.expressions.ValueExp;
import model.expressions.VariableExp;
import model.statements.WriteHeapStmt;
import model.statements.AssignmentStmt;
import model.statements.CloseRFileStmt;
import model.statements.CompoundStmt;
import model.statements.HeapAllocationStmt;
import model.statements.IStmt;
import model.statements.IfStmt;
import model.statements.OpenRFileStmt;
import model.statements.PrintStmt;
import model.statements.ReadFileStmt;
import model.statements.VariableDeclarationStmt;
import model.types.BooleanType;
import model.types.IntegerType;
import model.types.RefType;
import model.types.StringType;
import model.values.BooleanValue;
import model.values.IValue;
import model.values.IntegerValue;
import model.values.StringValue;
import repository.IRepository;
import repository.Repository;
import view.command.Command;
import view.command.ExitCommand;
import view.command.RunExampleCommand;
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
                        new ArithmeticExp(new ValueExp(new IntegerValue(3)),
                            ArithmeticOp.MULTIPLY,
                            new ValueExp(new IntegerValue(5))))),
                new CompoundStmt(
                    new AssignmentStmt("b",
                        new ArithmeticExp(new VariableExp("a"), ArithmeticOp.ADD,
                            new ValueExp(new IntegerValue(1)))),
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
                                    new CloseRFileStmt(
                                        new VariableExp("varf"))))))))));

  }

  private static IStmt createExample5() {
    // Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)
    return new CompoundStmt(
        new VariableDeclarationStmt("v", new RefType(new IntegerType())),
        new CompoundStmt(
            new HeapAllocationStmt("v", new ValueExp(new IntegerValue(20))),
            new CompoundStmt(
                new VariableDeclarationStmt("a", new RefType(new RefType(new IntegerType()))),
                new CompoundStmt(new HeapAllocationStmt("a", new VariableExp("v")),
                    new CompoundStmt(new PrintStmt(new VariableExp("v")),
                        new PrintStmt(new VariableExp("a")))))));
  }

  private static IStmt createExample6() {
    // Ref int v; new(v, 20); Ref Ref int a; new(a, v); print(readHeap(v));
    // print(readHeap(readHeap(a)) + 5);
    return new CompoundStmt(
        new VariableDeclarationStmt("v", new RefType(new IntegerType())),
        new CompoundStmt(
            new HeapAllocationStmt("v", new ValueExp(new IntegerValue(20))),
            new CompoundStmt(
                new VariableDeclarationStmt("a", new RefType(new RefType(new IntegerType()))),
                new CompoundStmt(
                    new HeapAllocationStmt("a", new VariableExp("v")),
                    new CompoundStmt(
                        new PrintStmt(new ReadHeapExp(new VariableExp("v"))),
                        new PrintStmt(new ArithmeticExp(
                            new ReadHeapExp(new ReadHeapExp(new VariableExp("a"))),
                            ArithmeticOp.ADD,
                            new ValueExp(new IntegerValue(5)))))))));
  }

  private static IStmt createExample7() {
    // Ref int v; new(v,20); print(readHeap(v)); writeHeap(v,30); print(readHeap(v)
    // + 5);
    return new CompoundStmt(
        new VariableDeclarationStmt("v", new RefType(new IntegerType())),
        new CompoundStmt(
            new HeapAllocationStmt("v", new ValueExp(new IntegerValue(20))),
            new CompoundStmt(
                new PrintStmt(new ReadHeapExp(new VariableExp("v"))),
                new CompoundStmt(
                    new WriteHeapStmt("v", new ValueExp(new IntegerValue(30))),
                    new PrintStmt(
                        new ArithmeticExp(
                            new ReadHeapExp(new VariableExp("v")),
                            ArithmeticOp.ADD,
                            new ValueExp(new IntegerValue(5))))))));
  }

  private static PrgState createPrgState(IStmt originalProgram) {
    IGenericDictionary<String, IValue> symTable = new GenericDictionary<>();
    IGenericStack<IStmt> exeStack = new GenericStack<>();
    IGenericList<IValue> output = new GenericList<>();
    IGenericDictionary<StringValue, BufferedReader> fileTable = new GenericDictionary<>();
    IGenericHeap<Integer, IValue> heap = new GenericHeap<>();

    return new PrgState(symTable, exeStack, output, originalProgram, fileTable, heap);
  }

  private static Controller createController(IStmt originalProgram, String logFilePath, boolean displayFlag) {
    PrgState prg = createPrgState(originalProgram);
    IRepository repo = new Repository(prg, logFilePath);

    return new Controller(repo, displayFlag);
  }

  public static TextMenu createTextMenu() {
    Controller ctr1 = createController(createExample1(), "log1.log", false);
    Controller ctr2 = createController(createExample2(), "log2.log", false);
    Controller ctr3 = createController(createExample3(), "log3.log", false);
    Controller ctr4 = createController(createExample4(), "log4.log", false);
    Controller ctr5 = createController(createExample5(), "log5.log", false);
    Controller ctr6 = createController(createExample6(), "log6.log", false);
    Controller ctr7 = createController(createExample7(), "log7.log", false);

    Command cmm1 = new RunExampleCommand("1", "int v; v = 2; Print(v)", ctr1);
    Command cmm2 = new RunExampleCommand("2", "int a; int b; a = 2 + 3 * 5; b = a + 1; Print(b)", ctr2);
    Command cmm3 = new RunExampleCommand("3", "bool a; int v; a = true;(if a then v = 2 else v = 3); Print(v)",
        ctr3);
    Command cmm4 = new RunExampleCommand("4",
        "string varf; varf = \"test.in\"; openRFile(varf); int varc; readFile(varf,varc); print(varc); readFile(varf, varc); print(varc); closeRFile(varf)",
        ctr4);
    Command cmm5 = new RunExampleCommand("5", "Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)",
        ctr5);
    Command cmm6 = new RunExampleCommand("6",
        "Ref int v; new(v, 20); Ref Ref int a; new(a, v); print(readHeap(v)); print(readHeap(readHeap(a)) + 5);",
        ctr6);
    Command cmm7 = new RunExampleCommand("7",
        "Ref int v; new(v,20); print(readHeap(v)); writeHeap(v,30); print(readHeap(v) + 5);", ctr7);

    TextMenu textMenu = new TextMenu();
    textMenu.addCommand(cmm1);
    textMenu.addCommand(cmm2);
    textMenu.addCommand(cmm3);
    textMenu.addCommand(cmm4);
    textMenu.addCommand(cmm5);
    textMenu.addCommand(cmm6);
    textMenu.addCommand(cmm7);
    textMenu.addCommand(new ExitCommand("0", "Exit"));

    return textMenu;
  }
}
