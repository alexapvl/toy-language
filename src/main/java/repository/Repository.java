package repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import model.PrgState;
import model.exceptions.AppException;

public class Repository implements IRepository {
  private List<PrgState> prgList;
  private String logFilePath;

  public Repository(PrgState prg, String logFilePath) {
    this.prgList = new ArrayList<>();
    this.logFilePath = logFilePath;
    this.prgList.add(prg);
  }

  @Override
  public void addPrg(PrgState prg) {
    this.prgList.add(prg);
  }

  @Override
  public void logPrgStateExec(PrgState prg) throws AppException {
    PrintWriter logFile;
    try {
      logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
      logFile.println(prg.toString());
      logFile.println("#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#-#");
      logFile.close();
    } catch (IOException error) {
      System.err.println(error.getMessage());
    }
  }

  @Override
  public List<PrgState> getPrgList() {
    return this.prgList;
  }

  @Override
  public void setPrgList(List<PrgState> prgList) {
    this.prgList = prgList;
  }
}
