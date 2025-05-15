package cpt204_cw3.taskAB;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import java.util.List;
import java.util.Optional;

public class GUIValidationUtil {

    // Shows suggestion dialog with city recommendations
    public static String showSuggestionDialog(String invalidInput, List<String> suggestions) {
        if (suggestions.isEmpty()) {
            showAlert("Invalid Input",
                    String.format("'%s' not found. No suggestions available.", invalidInput),
                    Alert.AlertType.WARNING);
            return null;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(suggestions.get(0), suggestions);
        dialog.setTitle("Input Correction");
        dialog.setHeaderText(String.format("'%s' not found. Did you mean:", invalidInput));
        dialog.setContentText("Choose a city:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    // Displays standard alert dialog
    public static void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}