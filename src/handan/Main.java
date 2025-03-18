package handan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    try {
      // Sätter en titel på fönstret till "BankSystem"
      primaryStage.setTitle("BankSystem");

      // Skapar upp en BorderPane
      BorderPane borderPane = new BorderPane();

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