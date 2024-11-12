package repository;

import model.PrgState;
import model.exceptions.AppException;

public interface IRepository {
  void addPrg(PrgState prg);

  PrgState getCurrentPrg();

  void logPrgStateExec() throws AppException;

}
