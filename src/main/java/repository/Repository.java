package repository;

import java.util.ArrayList;
import java.util.List;

import model.PrgState;

public class Repository implements IRepository {
  private List<PrgState> prgList;

  public Repository(PrgState prg) {
    this.prgList = new ArrayList<>();
    this.prgList.add(prg);
  }

  @Override
  public void addPrg(PrgState prg) {
    this.prgList.add(prg);
  }

  @Override
  public PrgState getCurrentPrg() {
    return this.prgList.get(0);
  }
}
