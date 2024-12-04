package controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import model.PrgState;
import model.exceptions.AppException;
import model.values.IValue;
import model.values.RefValue;
import repository.IRepository;

public class Controller {
  private IRepository repo;
  private boolean displayFlag; // if the display flag is set, it will display the program state after each step
                               // of the execution
  private ExecutorService executor;

  public Controller(IRepository repo, boolean displayFlag) {
    this.repo = repo;
    this.displayFlag = displayFlag;
  }

  public boolean getDisplayFlag() {
    return this.displayFlag;
  }

  public Set<Integer> getUsedAddresses() {
    Set<Integer> usedAddresses = new HashSet<>();
    // Get used addresses from all symTables since there is one symTable for each
    // program state
    for (PrgState prg : this.repo.getPrgList()) {
      for (IValue value : prg.getSymTable().getValues()) {
        if (value instanceof RefValue) {
          usedAddresses.add(((RefValue) value).getAddr());
        }
      }
    }

    // Get used addresses from the shared heap among all program states - case in
    // which we have RefValues in the heap
    for (IValue value : this.repo.getPrgList().get(0).getHeap().getValues()) {
      if (value instanceof RefValue) {
        usedAddresses.add(((RefValue) value).getAddr());
      }
    }

    return usedAddresses;
  }

  public Map<Integer, IValue> safeGarbageCollector(Set<Integer> usedAddresses, Map<Integer, IValue> heap) {
    Map<Integer, IValue> newHeap = new HashMap<Integer, IValue>();
    for (Integer key : heap.keySet()) {
      if (usedAddresses.contains(key)) {
        newHeap.put(key, heap.get(key));
      }
    }
    return newHeap;
  }

  public void allSteps() throws AppException {
    executor = Executors.newFixedThreadPool(2);
    // remove the completed programs
    List<PrgState> prgList = removeCompletedPrograms(repo.getPrgList());
    while (prgList.size() > 0) {
      prgList.get(0).getHeap().setHeap(
          safeGarbageCollector(getUsedAddresses(), prgList.get(0).getHeap().getHeap()));
      oneStepForAllPrg(prgList);
      prgList = removeCompletedPrograms(repo.getPrgList());
    }
    executor.shutdownNow();
    repo.setPrgList(prgList);
  }

  public void oneStepForAllPrg(List<PrgState> prgList) throws AppException {
    try {
      // before execution, print each program state in the log file
      prgList.forEach(prg -> {
        try {
          repo.logPrgStateExec(prg);
        } catch (AppException e) {
          System.err.println(e.getMessage());
        }
      });
      List<Callable<PrgState>> callList = prgList.stream()
          .map((PrgState p) -> (Callable<PrgState>) (() -> {
            return p.oneStep();
          }))
          .collect(Collectors.toList());
      List<PrgState> newPrgList = executor.invokeAll(callList).stream()
          .map(future -> {
            try {
              return future.get();
            } catch (ExecutionException | InterruptedException e) {
              System.err.println(e.getMessage());
              return null;
            }
          })
          .filter(p -> p != null)
          .collect(Collectors.toList());
      // add the new created threads to the list of existing threads
      prgList.addAll(newPrgList);
    } catch (InterruptedException e) {
      System.err.println(e.getMessage());
    }
    // after the execution, print the PrgState List into the log file
    prgList.forEach(prg -> {
      try {
        repo.logPrgStateExec(prg);
      } catch (AppException e) {
        System.err.println(e.getMessage());
      }
    });
    repo.setPrgList(prgList);
  }

  public List<PrgState> removeCompletedPrograms(List<PrgState> inPrgList) {
    return inPrgList.stream().filter(prg -> prg.isNotCompleted()).collect(Collectors.toList());
  }
}
