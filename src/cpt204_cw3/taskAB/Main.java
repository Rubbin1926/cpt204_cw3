package cpt204_cw3.taskAB;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button cliBtn = new Button("CLI Mode");
        cliBtn.setPrefWidth(250);
        cliBtn.setOnAction(e -> handleCliMode(primaryStage));

        Button guiBtn = new Button("GUI Mode");
        guiBtn.setPrefWidth(250);
        guiBtn.setOnAction(e -> handleGuiMode(primaryStage));

        VBox root = new VBox(20, cliBtn, guiBtn);
        root.setPadding(new Insets(30));

        primaryStage.setTitle("Select Operation Mode");
        primaryStage.setScene(new Scene(root, 300, 150));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void handleCliMode(Stage selectorStage) {
        selectorStage.close();
        Platform.exit();

        new Thread(() -> {
            try {
                String javaHome = System.getProperty("java.home");
                String javaBin = javaHome + "/bin/java";
                String classpath = System.getProperty("java.class.path");
                String className = CLIMode.class.getName();

                ProcessBuilder builder = new ProcessBuilder(
                        javaBin, "-cp", classpath, className
                );
                builder.inheritIO();
                Process process = builder.start();
                process.waitFor();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void handleGuiMode(Stage selectorStage) {
        selectorStage.close();
        Platform.runLater(() -> {
            Stage guiStage = new Stage();
            new GUIMode(guiStage);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}