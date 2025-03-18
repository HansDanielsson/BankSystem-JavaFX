package handan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  // bank hanteraren
  private BankLogic bank = new BankLogic();

  // Skapar upp en BorderPane
  private BorderPane borderPane = new BorderPane();

  // Menyer till BorderPane
  private Menu menuFile = new Menu("File");
  private MenuItem menuReadBank = new MenuItem("Läs in banken");
  private MenuItem menuSaveBank = new MenuItem("Spara banken");
  private MenuItem menuSaveTrans = new MenuItem("Spara transaktioner");
  private MenuItem menuExit = new MenuItem("Avsluta");

  private Menu menuCustomer = new Menu("Kund");
  private MenuItem menuCreate = new MenuItem("Ny kund");
  private MenuItem menuList = new MenuItem("Lista kunder");

  // Meny-hanterare, innehåller alla menyer
  private MenuBar menuBar = new MenuBar(menuFile, menuCustomer);

  private Label labelName = new Label("Förnamn: ");
  private TextField tfName = new TextField();
  private Label labelSurname = new Label("Efternamn: ");
  private TextField tfSurname = new TextField();
  private Label labelPNo = new Label("Personnummer: ");
  private TextField tfPNo = new TextField();
  private Button saveCustomer = new Button("Spara");
  private VBox vboxName = new VBox(10);

  private Label statusText = new Label();

  private void createNewCustomer() {
    if (bank.createCustomer(tfName.getText(), tfSurname.getText(), tfPNo.getText())) {
      setStatusOk();
    } else {
      setStatusError();
    }
  }

  private void printStatus() {
    System.out.println("Nu skrivs det");
  }

  private void setStatusError() {
    statusText.setText("Ej sparad");
    statusText.setTextFill(Color.RED);
  }

  private void setStatusOk() {
    statusText.setText("Sparad - Ok");
    statusText.setTextFill(Color.GREEN);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    try {
      // Sätter en titel på fönstret till "BankSystem"
      primaryStage.setTitle("BankSystem");

      // Initiera Action/Händelse som utför olika saker
      menuExit.setOnAction(_ -> System.exit(0));
      menuCreate.setOnAction(_ -> borderPane.setCenter(vboxName));
      saveCustomer.setOnAction(_ -> createNewCustomer());

      // Fixar alla menyer på plats
      menuFile.getItems().addAll(menuReadBank, menuSaveBank, menuSaveTrans, new SeparatorMenuItem(), menuExit);
      menuCustomer.getItems().addAll(menuCreate, menuList);

      borderPane.setTop(menuBar);
      borderPane.setBottom(statusText);

      // Initiering av vbox som innehåller olika element
      vboxName.getChildren().addAll(labelName, tfName, labelSurname, tfSurname, labelPNo, tfPNo, saveCustomer);

      // Skapar upp en scene med borderPane, bredd, höjd samt bakgrundsfärg som
      // argument
      Scene scene = new Scene(borderPane, 1400, 600, Color.LIGHTGREY);
      scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

      // Lägger in Scene i Stage
      primaryStage.setScene(scene);

      // Visa fönstret
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}