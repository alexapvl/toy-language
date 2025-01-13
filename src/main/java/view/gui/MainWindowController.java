package view.gui;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import controller.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.PrgState;
import model.adt.dictionary.IGenericDictionary;
import model.adt.heap.IGenericHeap;
import model.adt.list.IGenericList;
import model.adt.stack.IGenericStack;
import model.statements.IStmt;
import model.values.IValue;
import model.values.StringValue;

public class MainWindowController {
  private Controller controller;
  private PrgState selectedProgram;

  @FXML
  private TextField numberOfPrgStatesTextField;

  @FXML
  private TableView<Map.Entry<Integer, IValue>> heapTableView;
  @FXML
  private TableColumn<Map.Entry<Integer, IValue>, String> heapAddressColumn;
  @FXML
  private TableColumn<Map.Entry<Integer, IValue>, String> heapValueColumn;

  @FXML
  private ListView<String> outputListView;
  @FXML
  private ListView<String> fileTableListView;
  @FXML
  private ListView<Integer> prgStateIdentifiersListView;
  @FXML
  private ListView<String> exeStackListView;

  @FXML
  private TableView<Map.Entry<String, IValue>> symTableView;
  @FXML
  private TableColumn<Map.Entry<String, IValue>, String> symTableVarNameColumn;
  @FXML
  private TableColumn<Map.Entry<String, IValue>, String> symTableValueColumn;

  @FXML
  private Button runOneStepButton;

  @FXML
  public void initialize() {
    // Configure how to display heap table entries:
    // For the address column, convert the Integer key to String
    this.heapAddressColumn
      .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().toString()));
    // For the value column, convert the IValue to String
    this.heapValueColumn
      .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().toString()));

    // Configure how to display symbol table entries:
    // For variable names, use the String key directly
    this.symTableVarNameColumn
      .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
    // For variable values, convert the IValue to String
    this.symTableValueColumn
      .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().toString()));

    // Add a listener that triggers whenever a different program state ID is selected from the list
    this.prgStateIdentifiersListView.getSelectionModel().selectedItemProperty().addListener(
      (observable, oldValue, newValue) -> {
        if (newValue != null) {
          // Find the program state with the selected ID from the controller's repository
          // and change the variable selectedProgram
          this.selectedProgram = this.controller.getRepo().getPrgList().stream()
            .filter(prg -> prg.getId() == newValue)
            .findFirst()
            .orElse(null);
          // Update the execution stack and symbol table displays for the newly selected program
          populateExeStack();
          populateSymTable();
        }
      });
  }

  public void setController(Controller c) {
    this.controller = c;
    populateAll();
  }

  public void populateAll() {
    populateHeapTable();
    populateOutput();
    populateFileTable();
    populatePrgStateIdentifiers();
    populateNumberOfProgramStates();

    // select the first program state from the list if none are selected
    if (this.selectedProgram == null && !controller.getRepo().getPrgList().isEmpty()) {
      this.selectedProgram = controller.getRepo().getPrgList().get(0);
      this.prgStateIdentifiersListView.getSelectionModel().select(0);
    }

    // if a program is selected populate the exeStack and symTable
    if (this.selectedProgram != null) {
      populateExeStack();
      populateSymTable();
    }
  }

  private void populateNumberOfProgramStates() {
    this.numberOfPrgStatesTextField
      .setText(String.valueOf(controller.getRepo().getPrgList().size()));
  }

  private void populateHeapTable() {
    IGenericHeap<Integer, IValue> heap = this.controller.getRepo().getPrgList().get(0).getHeap();
    ObservableList<Map.Entry<Integer, IValue>> heapEntries = FXCollections.observableArrayList();
    try {
      heapEntries.addAll(heap.getHeap().entrySet());
    } catch (Exception e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText("Error accessing heap table: " + e.getMessage());
      alert.showAndWait();
    }
    this.heapTableView.setItems(heapEntries);

    // Add a listener to refresh the heap table view whenever its items change
    this.heapTableView.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Map.Entry<Integer, IValue>> change) -> {
      while (change.next()) {
        if (change.wasUpdated()) {
          this.heapTableView.refresh();
        }
      }
    });
  }

  private void populateOutput() {
    ObservableList<String> output = FXCollections.observableArrayList();
    if (!this.controller.getRepo().getPrgList().isEmpty()) {
      IGenericList<IValue> outList = this.controller.getRepo().getPrgList().get(0).getOutput();
      try {
        List<IValue> list = outList.getAll();
        output.addAll(list.stream()
          .map(IValue::toString)
          .collect(Collectors.toList()));
      } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Error accessing output list: " + e.getMessage());
        alert.showAndWait();
      }
    }
    this.outputListView.setItems(output);
  }

  private void populateFileTable() {
    ObservableList<String> files = FXCollections.observableArrayList();
    if (!this.controller.getRepo().getPrgList().isEmpty()) {
      try {
        IGenericDictionary<StringValue, BufferedReader> fileTable = this.controller.getRepo().getPrgList().get(0).getFileTable();
        files.addAll(fileTable.getKeys().stream()
          .filter(file -> file != null)
          .map(StringValue::toString)
          .collect(Collectors.toList()));
      } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Error accessing file table: " + e.getMessage());
        alert.showAndWait(); 
      }
    }
    this.fileTableListView.setItems(files);
  }

  private void populatePrgStateIdentifiers() {
    ObservableList<Integer> identifiers = FXCollections.observableArrayList();
    identifiers.addAll(controller.getRepo().getPrgList().stream()
        .map(PrgState::getId)
        .collect(Collectors.toList()));
    this.prgStateIdentifiersListView.setItems(identifiers);
  }

  private void populateSymTable() {
    ObservableList<Map.Entry<String, IValue>> symTableEntries = FXCollections.observableArrayList();
    if (this.selectedProgram != null) {
      IGenericDictionary<String, IValue> symTable = this.selectedProgram.getSymTable();
      try {
        symTableEntries.addAll(symTable.getMap().entrySet());
      } catch (Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Error accessing symbol table: " + e.getMessage());
        alert.showAndWait();
      }
    }
    this.symTableView.setItems(symTableEntries);
    
    // Add a listener to refresh the symbol table view whenever its items change
    this.symTableView.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends Map.Entry<String, IValue>> change) -> {
      while (change.next()) {
        if (change.wasUpdated()) {
          this.symTableView.refresh();
        }
      }
    });
  }

  private void populateExeStack() {
    ObservableList<String> exeStack = FXCollections.observableArrayList();
    if (this.selectedProgram != null) {
      List<String> stackElements = new ArrayList<>();
      IGenericStack<IStmt> stack = this.selectedProgram.getExeStack();
      List<IStmt> stackList = stack.toList();

      for (IStmt stmt : stackList) {
        stackElements.add(0, stmt.toString());
      }
      exeStack.addAll(stackElements);
    }
    exeStackListView.setItems(exeStack);
  }

  @FXML
  private void runOneStep() {
    if (this.controller == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText("No program selected!");
      alert.showAndWait();
      return;
    }

    List<PrgState> prgList = this.controller.removeCompletedPrograms(this.controller.getRepo().getPrgList());
    
    if (prgList.isEmpty()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText("Nothing left to execute!");
      alert.showAndWait();
      return;
    }
  
    try {
      controller.oneStepForAllPrg(prgList);
      populateAll();
      
      // Force refresh the symbol table and heap table views to show updated values
      this.symTableView.refresh();
      this.heapTableView.refresh();
    } catch (Exception e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText(e.getMessage());
      alert.showAndWait();
      return;
    }
    return;
  }
}
