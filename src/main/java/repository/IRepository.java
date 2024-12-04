package repository;

import java.util.List;

import model.PrgState;
import model.exceptions.AppException;

public interface IRepository {
  void addPrg(PrgState prg);

  void logPrgStateExec(PrgState prg) throws AppException;

  List<PrgState> getPrgList();

  void setPrgList(List<PrgState> prgList);
}
