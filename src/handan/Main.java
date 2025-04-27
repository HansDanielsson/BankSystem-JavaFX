/**
 * Huvudmodul som definierar ett GUI.
 * @author Hans Danielsson, handan-2
 */
package handan;

import java.util.List;
import java.util.stream.Stream;

//Importsatser för JavaFX med olika API rutiner
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

  private static final String SAVED = "Sparad - Ok";
  private static final String NOTSAVED = "Ej sparad";

  // Skapar upp en BorderPane
  private static BorderPane borderPane = new BorderPane();

  // Menyer till BorderPane
  private static Menu menuFile = new Menu("File");

  private static final String[] strMenuFile = { "Spara banken", "Läs in banken", "Spara transaktioner",
      "Visa transaktioner", "Avsluta" };

  private static MenuItem[] menuItemFile = new MenuItem[strMenuFile.length];
  private static Menu menuCustomer = new Menu("Kund");
  private static final String[] strMenuCustomer = { "Spara", "Hämta", "Byt namn", "Ta bort", "Lista" };

  private static MenuItem[] menuItemCustomer = new MenuItem[strMenuCustomer.length];
  private static Menu menuAccount = new Menu("Konton");
  private static final String[] strMenuAccount = { "Spar", "Kredit", "Saldo", "Sätta in", "Ta ut", "Transaktioner",
      "Ta bort" };

  private static MenuItem[] menuItemAccount = new MenuItem[strMenuAccount.length];
  // Meny-hanterare, innehåller alla menyer
  private static MenuBar menuBar = new MenuBar(menuFile, menuCustomer, menuAccount);
  // Spara knappen till olika syften
  private static final String[] strButton = { "Spara", "Hämta", "Byt namn", "Ta bort", "Spar", "Kredit", "Saldo",
      "Sätt in", "Ta ut", "Transaktioner", "Ta bort" };

  private static Label[] labelPNo = new Label[strButton.length];

  private static TextField[] tfPNo = new TextField[strButton.length];

  // Deklaration av text/inmatning, som används på olika ställen

  private static Label[] labelName = new Label[strButton.length];
  private static TextField[] tfName = new TextField[strButton.length];

  private static Label[] labelSurname = new Label[strButton.length];
  private static TextField[] tfSurname = new TextField[strButton.length];

  /**
   * Man behöver bara använda en tfKontoList som används för alla listor på alla
   * "sidor"
   */
  private static ObservableList<String> tfKontoList = FXCollections.observableArrayList();
  private static ObservableList<String> tfResultList = FXCollections.observableArrayList();

  private static Label[] labelKontoNr = new Label[strButton.length];

  // Ignorerar varningen, det blir rätt kod ändå.
  @SuppressWarnings("unchecked")
  private static ListView<String>[] tfKontoNr = new ListView[strButton.length];

  private static Label[] labelBelopp = new Label[strButton.length];

  private static TextField[] tfBelopp = new TextField[strButton.length];

  private static Button[] saveButton = new Button[strButton.length];
  private static VBox[] vbox = new VBox[strButton.length];

  // Bilder att visa i högra delen
  private static final Image imageBag = new Image("file:src/handan/files/bag-96x96.png");
  private static final Image imagePiggy = new Image("file:src/handan/files/piggy-bank-96x96.png");
  private static final Image imageSafe = new Image("file:src/handan/files/safe-96x96.png");
  // Skapa 3 ImageView för att visa bilderna
  private static ImageView bagImageView = new ImageView(imageBag);
  private static ImageView piggyImageView = new ImageView(imagePiggy);
  private static ImageView safeImageView = new ImageView(imageSafe);
  private static VBox imageVBox = new VBox(20);
  private static ListView<String> centralResult = new ListView<>(tfResultList);
  private static Label statusText = new Label();

  /**
   * Hjälprutin som kollar att belopp är utan decimaler. Systemet är byggt på
   * modellen integer.
   *
   * @param index - Beloppet som ska kollas upp
   */
  private static void bankBeloppCheck(short index) {
    String str = tfBelopp[index].getText();
    int dotPos = str.indexOf("."); // Ta bort allt efter .
    int commaPos = str.indexOf(","); // Ta bort allt efter ,
    int cutPos = (dotPos == -1) ? commaPos : (commaPos == -1 ? dotPos : Math.min(dotPos, commaPos));

    if (cutPos > -1) {
      str = str.substring(0, cutPos);
    }

    tfBelopp[index].setText(str);
  }

  /**
   * Rutin som utför menyval under Konto
   *
   * @param index - Menyvalet
   */
  private static void bankMenuAccount(short index) {
    switch (index) {
    case 0, 1, 2, 3, 4, 5, 6: // Konto kommando
      borderPane.setLeft(vbox[index + 4]);
      break;
    default:
      setStatusError("Konton, index = " + index);
      break;
    }
  }

  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Hjälprutin som sätter texten i centrum rutan. Kontrollen är utfört redan
   * innan anropet att strResult pekar på något.
   *
   * @param strResult
   */
  private static void putCenterText(List<String> strResult) {
    tfResultList.clear();
    tfResultList.addAll(strResult);
  }

  /**
   * Rutin som visar i status texten ett felmeddelande
   *
   * @param str - Meddelandet
   */
  private static void setStatusError(String str) {
    statusText.setText(str);
    statusText.setTextFill(Color.RED);
  }

  /**
   * Rutin som visar i status texten ett ok-meddelande
   *
   * @param str - Meddelandet
   */
  private static void setStatusOk(String str) {
    statusText.setText(str);
    statusText.setTextFill(Color.GREEN);
  }

  // bank-hanteraren
  private BankLogic bank = new BankLogic();

  /**
   * Rutin som fixar alla konton till ett pNr, Givet att pNr är ifyllt
   *
   * @param index - Menyvalet
   */
  private void bankAccountList(short index) {
    switch (index) {
    case 6, 7, 8, 9, 10:
      List<String> result = bank.getAccountList(tfPNo[index].getText());
      tfKontoList.clear();
      if (result != null) {
        tfKontoList.addAll(result);
      }
      break;
    default:
      break;
    }
  }

  /**
   * Rutin som utför kommandon under Kund
   *
   * @param index - Menyvalet
   */
  private void bankMenuCustomer(short index) {
    switch (index) {
    case 0, 1, 2, 3 -> borderPane.setLeft(vbox[index]);
    case 4 -> getBankAllCustomers();
    default -> setStatusError("Kund, index = " + index);
    }
  }

  /**
   * Rutin som utför kommando under File
   *
   * @param index - Menyvalet
   */
  private void bankMenuFile(short index) {
    switch (index) {
    case 0 -> setStatusError("Läs in banken");
    case 1 -> setStatusError("Spara banken");
    case 2 -> setStatusError("Spara transaktioner");
    case 3 -> setStatusError("Visa transaktioner");
    case 4 -> System.exit(0);
    default -> setStatusError("File, index = " + index);
    }
  }

  /**
   * Rutin som anropas när användaren har tryckt på en Button-knapp
   *
   * @param index - Knappens syfte
   */
  private void bankMenuSave(short index) {
    switch (index) {
    case 0 -> createBankCustomer();
    case 1 -> getBankCustomer();
    case 2 -> changeBankCustomerName();
    case 3 -> deletBankCustomer();
    case 4 -> createBankSavingAccount();
    case 5 -> createBankCreditAccount();
    case 6 -> getBankAccount();
    case 7 -> depositBankAccount();
    case 8 -> withdrawBankAccount();
    case 9 -> getBankTransactions();
    case 10 -> closeBankAccount();
    default -> setStatusError("Save button, index = " + index);
    }
  }

  /**
   * Rutin som byte namn på en kund(pNr)
   */
  private void changeBankCustomerName() {
    if (bank.changeCustomerName(tfName[2].getText(), tfSurname[2].getText(), tfPNo[2].getText())) {
      setStatusOk(SAVED);
    } else {
      setStatusError(NOTSAVED);
    }
  }

  /**
   * Rutin som tar bort ett konto(accountId) på kund(pNr)
   */
  private void closeBankAccount() {
    String strKonto = tfKontoNr[10].getSelectionModel().getSelectedItem();
    try {
      if (!strKonto.isBlank()) {
        String str = bank.closeAccount(tfPNo[10].getText(), Integer.parseInt(strKonto));
        if (str != null) {
          putCenterText(List.of(str));
        }
      }
    } catch (Exception e) {
      setStatusError("Felaktigt kontonummer: " + strKonto);
    }

  }

  /**
   * Rutin som skapar ett kreditkonto för person(pNo)
   */
  private void createBankCreditAccount() {
    int accountId = bank.createCreditAccount(tfPNo[5].getText());
    if (accountId > 0) {
      putCenterText(List.of("Kontonummer: " + accountId));
    } else {
      setStatusError(NOTSAVED);
    }
  }

  /**
   * Rutin som skapar en kund med f-namn, e-namn och pNr
   */
  private void createBankCustomer() {
    if (bank.createCustomer(tfName[0].getText(), tfSurname[0].getText(), tfPNo[0].getText())) {
      setStatusOk(SAVED);
    } else {
      setStatusError(NOTSAVED);
    }
  }

  /**
   * Rutin som skapar ett sparkonto för kund pNr
   */
  private void createBankSavingAccount() {
    int accountId = bank.createSavingsAccount(tfPNo[4].getText());
    if (accountId > 0) {
      putCenterText(List.of("Kontonummer: " + accountId));
    } else {
      setStatusError(NOTSAVED);
    }
  }

  /**
   * Rutin som tar bort en kund(pNo)
   */
  private void deletBankCustomer() {
    List<String> result = bank.deleteCustomer(tfPNo[3].getText());
    if (result != null) {
      putCenterText(result);
    }
  }

  /**
   * Rutin som sätter in pengar på ett konto
   */
  private void depositBankAccount() {
    String strKonto = tfKontoNr[7].getSelectionModel().getSelectedItem();
    String strBelopp = tfBelopp[7].getText();
    try {
      if (bank.deposit(tfPNo[7].getText(), Integer.parseInt(strKonto), Integer.parseInt(strBelopp))) {
        setStatusOk(SAVED);
      } else {
        setStatusError(NOTSAVED);
      }
    } catch (Exception e) {
      setStatusError("Felaktiga värden: " + strKonto + "/" + strBelopp);
    }
  }

  /**
   * Rutin som hämtar saldo för ett konto
   */
  private void getBankAccount() {
    String strKonto = tfKontoNr[6].getSelectionModel().getSelectedItem();
    try {
      if (!strKonto.isBlank()) {
        String str = bank.getAccount(tfPNo[6].getText(), Integer.parseInt(strKonto));
        if (str != null) {
          putCenterText(List.of(str));
        }
      }
    } catch (Exception e) {
      setStatusError("Felaktigt Kontonummer: " + strKonto);
    }
  }

  /**
   * Rutin som hämtar alla kunder och visar det i fönster Center
   */
  private void getBankAllCustomers() {
    List<String> result = bank.getAllCustomers();
    if (result != null) {
      putCenterText(result);
    }
  }

  /**
   * Rutin som tar fram en kund med konton
   */
  private void getBankCustomer() {
    List<String> result = bank.getCustomer(tfPNo[1].getText());
    if (result != null) {
      putCenterText(result);
    }
  }

  /**
   * Rutin som hämtar alla transaktioner för ett konto
   */
  private void getBankTransactions() {
    String strKonto = tfKontoNr[9].getSelectionModel().getSelectedItem();
    if (strKonto != null && !strKonto.isBlank()) {
      try {
        int kontoNummer = Integer.parseInt(strKonto);
        List<String> result = bank.getTransactions(tfPNo[9].getText(), kontoNummer);

        if (result != null && !result.isEmpty()) {
          List<String> newResult = Stream.concat(Stream.of("Transaktioner för konto: " + strKonto), result.stream())
              .toList();

          putCenterText(newResult);
        } else {
          setStatusOk("Inga transaktioner för konto: " + strKonto);
        }
      } catch (NumberFormatException e) {
        setStatusError("Felaktigt kontonummerformat: " + strKonto);
      } catch (Exception e) {
        setStatusError("Ett fel uppstod vid hämtning av transaktioner för konto: " + strKonto);
        e.printStackTrace();
      }
    }
  }

  /**
   * Rutin som initierar en VBox med olika kontroller
   *
   * @param index           - Vilken VBox som ska initieras
   * @param withName        - Om det ska vara med förnamn
   * @param withSurname     - Om det ska vara med efternamn
   * @param withAccountList - Om det ska vara med kontolnr
   * @param withAmount      - Om det ska vara med belopp
   */
  private void initVBox(short index, boolean withName, boolean withSurname, boolean withAccountList,
      boolean withAmount) {
    vbox[index] = new VBox(10);

    labelPNo[index] = new Label("Personnummer: ");
    tfPNo[index] = new TextField();
    vbox[index].getChildren().addAll(labelPNo[index], tfPNo[index]);

    if (withName) {
      labelName[index] = new Label("Förnamn: ");
      tfName[index] = new TextField();
      vbox[index].getChildren().addAll(labelName[index], tfName[index]);
    }

    if (withSurname) {
      labelSurname[index] = new Label("Efternamn: ");
      tfSurname[index] = new TextField();
      vbox[index].getChildren().addAll(labelSurname[index], tfSurname[index]);
    }

    if (withAccountList) {
      labelKontoNr[index] = new Label("Kontonr: ");
      tfKontoNr[index] = new ListView<>(tfKontoList);
      vbox[index].getChildren().addAll(labelKontoNr[index], tfKontoNr[index]);
    }

    if (withAmount) {
      labelBelopp[index] = new Label("Belopp: ");
      tfBelopp[index] = new TextField();
      vbox[index].getChildren().addAll(labelBelopp[index], tfBelopp[index]);
    }

    saveButton[index] = new Button(strButton[index]);
    final short finalIndex = index;
    saveButton[index].setOnAction(_ -> bankMenuSave(finalIndex));
    tfPNo[index].focusedProperty().addListener(_ -> bankAccountList(finalIndex));
    if (withAmount) {
      tfBelopp[index].focusedProperty().addListener(_ -> bankBeloppCheck(finalIndex));
    }
    vbox[index].getChildren().add(saveButton[index]);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    short i;
    try {
      // Sätter en titel på fönstret till "BankSystem"
      primaryStage.setTitle("BankSystem");

      // === Meny: File ===
      for (i = 0; i < strMenuFile.length; i++) {
        menuItemFile[i] = new MenuItem(strMenuFile[i]);
        final short indexFile = i;
        menuItemFile[i].setOnAction(_ -> bankMenuFile(indexFile));
        if (i == strMenuFile.length - 1) {
          menuFile.getItems().add(new SeparatorMenuItem());
        }
        menuFile.getItems().add(menuItemFile[i]);
      }

      // === Meny: Kund ===
      for (i = 0; i < strMenuCustomer.length; i++) {
        menuItemCustomer[i] = new MenuItem(strMenuCustomer[i]);
        final short indexCustomer = i;
        menuItemCustomer[i].setOnAction(_ -> bankMenuCustomer(indexCustomer));
        menuCustomer.getItems().add(menuItemCustomer[i]);
      }

      // === Meny: Konton ===
      for (i = 0; i < strMenuAccount.length; i++) {
        menuItemAccount[i] = new MenuItem(strMenuAccount[i]);
        final short indexAccount = i;
        menuItemAccount[i].setOnAction(_ -> bankMenuAccount(indexAccount));
        menuAccount.getItems().add(menuItemAccount[i]);
      }

      // === Initierar varje "sida" ===
      for (i = 0; i < strButton.length; i++) {
        switch (i) {
        case 0, 2 -> initVBox(i, true, true, false, false); // Spara kund, Byt namn
        case 6, 9, 10 -> initVBox(i, false, false, true, false); // Hämta saldo, Transaktioner, Ta bort konto
        case 7, 8 -> initVBox(i, false, false, true, true); // Sätta in/ta ut
        default -> initVBox(i, false, false, false, false); // Hämta kund, Ta bort kund, Skapa spar- och kredit-konton
        }
      }

      imageVBox.getChildren().addAll(bagImageView, piggyImageView, safeImageView);

      borderPane.setTop(menuBar);
      borderPane.setCenter(centralResult);
      borderPane.setBottom(statusText);
      borderPane.setRight(imageVBox);

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

  /**
   * Rutin som tar ut pengar från ett konto
   */
  private void withdrawBankAccount() {
    try {
      int accountId = Integer.parseInt(tfKontoNr[8].getSelectionModel().getSelectedItem());
      int amount = Integer.parseInt(tfBelopp[8].getText());
      if (bank.withdraw(tfPNo[8].getText(), accountId, amount)) {
        setStatusOk(SAVED);
      } else {
        setStatusError(NOTSAVED);
      }
    } catch (Exception e) {
      setStatusError(
          "Felaktiga värden: " + tfKontoNr[8].getSelectionModel().getSelectedItem() + "/" + tfBelopp[8].getText());
    }

  }
}