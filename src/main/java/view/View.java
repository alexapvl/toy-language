package view;

import java.io.BufferedReader;

import model.expressions.enums.ArithmeticOp;
import model.expressions.enums.RelationalOp;
import model.PrgState;
import model.adt.ILock;
import model.adt.Lock;
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
import model.expressions.RelationalExp;
import model.expressions.ValueExp;
import model.expressions.VariableExp;
import model.statements.WriteHeapStmt;
import model.statements.AssignmentStmt;
import model.statements.CloseRFileStmt;
import model.statements.CompoundStmt;
import model.statements.ForStmt;
import model.statements.ForkStmt;
import model.statements.HeapAllocationStmt;
import model.statements.IStmt;
import model.statements.IfStmt;
import model.statements.LockAcquireStmt;
import model.statements.LockReleaseStmt;
import model.statements.NewLockStmt;
import model.statements.NoOperationStmt;
import model.statements.OpenRFileStmt;
import model.statements.PrintStmt;
import model.statements.ReadFileStmt;
import model.statements.VariableDeclarationStmt;
import model.statements.WhileStmt;
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

  private static IStmt createExample8() {
    // int v; v=4; (while(v>0) print(v); v=v-1); print(v)
    return new CompoundStmt(
        new VariableDeclarationStmt("v", new IntegerType()),
        new CompoundStmt(new AssignmentStmt("v", new ValueExp(new IntegerValue(4))),
            new CompoundStmt(
                new WhileStmt(
                    new RelationalExp(new VariableExp("v"), new ValueExp(new IntegerValue()),
                        RelationalOp.GREATER),
                    new CompoundStmt(
                        new PrintStmt(new VariableExp("v")),
                        new AssignmentStmt("v",
                            new ArithmeticExp(new VariableExp("v"), ArithmeticOp.SUBTRACT,
                                new ValueExp(new IntegerValue(1)))))),
                new PrintStmt(new VariableExp("v")))));
  }

  private static IStmt createExample9() {
    // Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))
    return new CompoundStmt(
        new VariableDeclarationStmt("v", new RefType(new IntegerType())),
        new CompoundStmt(
            new HeapAllocationStmt("v", new ValueExp(new IntegerValue(20))),
            new CompoundStmt(
                new VariableDeclarationStmt("a", new RefType(new RefType(new IntegerType()))),
                new CompoundStmt(
                    new HeapAllocationStmt("a", new VariableExp("v")),
                    new CompoundStmt(
                        new HeapAllocationStmt("v", new ValueExp(new IntegerValue(30))),
                        new PrintStmt(
                            new ReadHeapExp(new ReadHeapExp(new VariableExp("a")))))))));
  }

  private static IStmt createExample10() {
    // int v; Ref int a; v = 10; new(a, 22)
    // fork(wH(a, 30); v = 32; print(v); print(rH(a)));
    // print(v); print(rH(a));
    return new CompoundStmt(
        new VariableDeclarationStmt("v", new IntegerType()),
        new CompoundStmt(
            new VariableDeclarationStmt("a", new RefType(new IntegerType())),
            new CompoundStmt(
                new AssignmentStmt("v", new ValueExp(new IntegerValue(10))),
                new CompoundStmt(
                    new HeapAllocationStmt("a", new ValueExp(new IntegerValue(22))),
                    new CompoundStmt(
                        new ForkStmt(
                            new CompoundStmt(
                                new WriteHeapStmt("a",
                                    new ValueExp(new IntegerValue(30))),
                                new CompoundStmt(
                                    new AssignmentStmt("v",
                                        new ValueExp(new IntegerValue(32))),
                                    new CompoundStmt(
                                        new PrintStmt(new VariableExp("v")),
                                        new PrintStmt(new ReadHeapExp(
                                            new VariableExp("a"))))))),
                        new CompoundStmt(
                            new PrintStmt(new VariableExp("v")),
                            new PrintStmt(new ReadHeapExp(new VariableExp("a")))))))));
  }

  private static IStmt createExample11() {
    // example with type error!
    // int v; v = false; Print(v)
    return new CompoundStmt(
        new VariableDeclarationStmt("v", new IntegerType()),
        new CompoundStmt(
            new AssignmentStmt("v", new ValueExp(new BooleanValue(false))),
            new PrintStmt(new VariableExp("v"))));
  }

  private static IStmt createExample12() {
    // Ref int v1; Ref int v2; int x; int q;
    // new(v1,20);new(v2,30);newLock(x);
    // fork(
    // fork(
    // lock(x);wh(v1,rh(v1)-1);unlock(x)
    // );
    // lock(x);wh(v1,rh(v1)*10);unlock(x)
    // );newLock(q);
    // fork(
    // fork(lock(q);wh(v2,rh(v2)+5);unlock(q));
    // lock(q);wh(v2,rh(v2)*10);unlock(q)
    // );
    // nop;nop;nop;nop;
    // lock(x); print(rh(v1)); unlock(x);
    // lock(q); print(rh(v2)); unlock(q);
    // The final Out should be {190 or 199,350 or 305}
    return new CompoundStmt(
      new VariableDeclarationStmt("v1", new RefType(new IntegerType())), 
      new CompoundStmt(
        new VariableDeclarationStmt("v2", new RefType(new IntegerType())), 
        new CompoundStmt(
          new VariableDeclarationStmt("x", new IntegerType()), 
          new CompoundStmt(
            new VariableDeclarationStmt("q", new IntegerType()), 
            new CompoundStmt(
              new HeapAllocationStmt("v1", new ValueExp(new IntegerValue(20))), 
              new CompoundStmt(
                new HeapAllocationStmt("v2", new ValueExp(new IntegerValue(30))), 
                new CompoundStmt(
                  new NewLockStmt("x"), 
                  new CompoundStmt(
                    new ForkStmt(
                      new CompoundStmt(
                        new ForkStmt(
                          new CompoundStmt(
                            new LockAcquireStmt("x"), 
                            new CompoundStmt(
                              new WriteHeapStmt("v1", new ArithmeticExp(new ReadHeapExp(new VariableExp("v1")), ArithmeticOp.SUBTRACT, new ValueExp(new IntegerValue(1)))), 
                              new LockReleaseStmt("x")))), 
                        new CompoundStmt(
                          new LockAcquireStmt("x"), 
                          new CompoundStmt(
                            new WriteHeapStmt("v1", new ArithmeticExp(new ReadHeapExp(new VariableExp("v1")), ArithmeticOp.MULTIPLY, new ValueExp(new IntegerValue(10)))), 
                            new LockReleaseStmt("x"))))), 
                    new CompoundStmt(
                      new NewLockStmt("q"), 
                      new CompoundStmt(
                        new ForkStmt(
                          new CompoundStmt(
                            new ForkStmt(
                              new CompoundStmt(
                                new LockAcquireStmt("q"), 
                                new CompoundStmt(
                                  new WriteHeapStmt("v2", new ArithmeticExp(new ReadHeapExp(new VariableExp("v2")), ArithmeticOp.ADD, new ValueExp(new IntegerValue(5)))), 
                                  new LockReleaseStmt("q")))), 
                            new CompoundStmt(
                              new LockAcquireStmt("q"), 
                              new CompoundStmt(
                                new WriteHeapStmt("v2", new ArithmeticExp(new ReadHeapExp(new VariableExp("v2")), ArithmeticOp.MULTIPLY, new ValueExp(new IntegerValue(10)))), 
                                new LockReleaseStmt("q"))))), 
                        new CompoundStmt(
                          new NoOperationStmt(), 
                          new CompoundStmt(
                            new NoOperationStmt(), 
                            new CompoundStmt(
                              new NoOperationStmt(), 
                              new CompoundStmt(
                                new NoOperationStmt(), 
                                new CompoundStmt(
                                  new LockAcquireStmt("x"), 
                                  new CompoundStmt(
                                    new PrintStmt(new ReadHeapExp(new VariableExp("v1"))), 
                                    new CompoundStmt(
                                      new LockReleaseStmt("x"), 
                                      new CompoundStmt(
                                        new LockAcquireStmt("q"), 
                                        new CompoundStmt(
                                          new PrintStmt(new ReadHeapExp(new VariableExp("v2"))), 
                                          new LockReleaseStmt("q"))))))))))))))))))));
  }

  private static IStmt createExample13() {
    // Ref int a; new(a,20);
    // (for(v=0;v<3;v=v+1) fork(print(v);v=v*rh(a)));
    // print(rh(a))
    return new CompoundStmt(
      new VariableDeclarationStmt("a", new RefType(new IntegerType())), 
      new CompoundStmt(
        new HeapAllocationStmt("a", new ValueExp(new IntegerValue(20))), 
        new CompoundStmt(
          new ForStmt(
            "v", 
            new ValueExp(new IntegerValue(0)), 
            new ValueExp(new IntegerValue(3)) , 
            new ArithmeticExp(new VariableExp("v"), ArithmeticOp.ADD, new ValueExp(new IntegerValue(1))), 
            new ForkStmt(
              new CompoundStmt(
                new PrintStmt(new VariableExp("v")), 
                new AssignmentStmt("v", new ArithmeticExp(new VariableExp("v"), ArithmeticOp.MULTIPLY, new ReadHeapExp(new VariableExp("a"))))))), 
          new PrintStmt(new ReadHeapExp(new VariableExp("a"))))));
  }

  private static PrgState createPrgState(IStmt originalProgram) {
    IGenericDictionary<String, IValue> symTable = new GenericDictionary<>();
    IGenericStack<IStmt> exeStack = new GenericStack<>();
    IGenericList<IValue> output = new GenericList<>();
    IGenericDictionary<StringValue, BufferedReader> fileTable = new GenericDictionary<>();
    IGenericHeap<Integer, IValue> heap = new GenericHeap<>();
    ILock<Integer, Integer> lockTable = new Lock<>();

    return new PrgState(symTable, exeStack, output, originalProgram, fileTable, heap, lockTable);
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
    Controller ctr8 = createController(createExample8(), "log8.log", false);
    Controller ctr9 = createController(createExample9(), "log9.log", false);
    Controller ctr10 = createController(createExample10(), "log10.log", false);
    Controller ctr11 = createController(createExample11(), "log11.log", false);
    Controller ctr12 = createController(createExample12(), "log12.log", false);
    Controller ctr13 = createController(createExample13(), "log13.log", false);

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
    Command cmm8 = new RunExampleCommand("8", "int v; v=4; (while(v>0) print(v); v=v-1); print(v)", ctr8);
    Command cmm9 = new RunExampleCommand("9",
        "Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))",
        ctr9);
    Command cmm10 = new RunExampleCommand("10",
        "int v; Ref int a; v = 10; new(a, 22); fork(wH(a, 30); v = 32; print(v); print(rH(a))); print(v); print(rH(a));",
        ctr10);
    Command cmm11 = new RunExampleCommand("11", "int v; v = false; Print(v) -> has TYPE ERROR", ctr11);
    Command cmm12 = new RunExampleCommand("12", "Lock Example", ctr12);
    Command cmm13 = new RunExampleCommand("13", "For Example", ctr13);

    TextMenu textMenu = new TextMenu();
    textMenu.addCommand(cmm1);
    textMenu.addCommand(cmm2);
    textMenu.addCommand(cmm3);
    textMenu.addCommand(cmm4);
    textMenu.addCommand(cmm5);
    textMenu.addCommand(cmm6);
    textMenu.addCommand(cmm7);
    textMenu.addCommand(cmm8);
    textMenu.addCommand(cmm9);
    textMenu.addCommand(cmm10);
    textMenu.addCommand(cmm11);
    textMenu.addCommand(cmm12);
    textMenu.addCommand(cmm13);
    textMenu.addCommand(new ExitCommand("0", "Exit"));

    return textMenu;
  }

  public static Controller createControllerForGUI(String key) {
    switch (key) {
      case "1":
        return createController(createExample1(), "log1.log", false);
      case "2":
        return createController(createExample2(), "log2.log", false);
      case "3":
        return createController(createExample3(), "log3.log", false);
      case "4":
        return createController(createExample4(), "log4.log", false);
      case "5":
        return createController(createExample5(), "log5.log", false);
      case "6":
        return createController(createExample6(), "log6.log", false);
      case "7":
        return createController(createExample7(), "log7.log", false);
      case "8":
        return createController(createExample8(), "log8.log", false);
      case "9":
        return createController(createExample9(), "log9.log", false);
      case "10":
        return createController(createExample10(), "log10.log", false);
      case "11":
        return createController(createExample11(), "log11.log", false);
      case "12":
        return createController(createExample12(), "log12.log", false);
      case "13":
        return createController(createExample13(), "log13.log", false);
      default:
        return null;
    }
  }
}
