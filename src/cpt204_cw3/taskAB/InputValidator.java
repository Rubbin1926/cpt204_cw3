package cpt204_cw3.taskAB;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputValidator {
    private final SpellChecker spellChecker;
    private final RoadTripPlanner planner;
    private final Scanner scanner;

    public InputValidator(SpellChecker spellChecker, RoadTripPlanner planner, Scanner scanner) {
        this.spellChecker = spellChecker;
        this.planner = planner;
        this.scanner = scanner;
    }

    public String getValidCity(String cityType, String example) {
        while (true) {
            System.out.printf("Enter %s city (e.g., %s): ", cityType, example);
            String input = sanitizeInput(scanner.nextLine());

            if (isValidCity(input)) {
                return input;
            }

            List<String> suggestions = spellChecker.getSuggestions(input, 3);
            showSuggestions(input, suggestions);
        }
    }

    public List<String> getValidAttractions() {
        while (true) {
            System.out.print("Enter attractions (comma-separated, e.g., Hollywood Sign): ");
            String input = sanitizeInput(scanner.nextLine());

            // Process input into list and remove empty entries
            List<String> originalList = splitAndSanitize(input);
            List<String> processedList = originalList.stream().distinct().collect(Collectors.toList());

            // Validate attraction existence first
            if (!validateAttractionExistence(processedList)) {
                System.out.print("Please re-enter attractions: ");
                continue;
            }

            // Then check for duplicates
            if (originalList.size() > processedList.size()) {
                WarningPrinter.printWarning("Warning: Duplicate attractions detected and removed.");
            }

            return processedList;
        }
    }

    /**
     * Splits input into individual attractions, trims whitespace, and filters empty strings
     */
    private List<String> splitAndSanitize(String input) {
        return Arrays.stream(input.split("\\s*,\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Validates if all attractions in the list exist in the trip planner
     * @return true if all attractions are valid
     */
    private boolean validateAttractionExistence(List<String> attractions) {
        List<String> invalid = attractions.stream()
                .filter(a -> planner.findAttractionIndex(a) == -1)
                .collect(Collectors.toList());

        if (!invalid.isEmpty()) {
            WarningPrinter.printWarning("Invalid attractions: " + invalid);
            return false;
        }
        return true;
    }

    private String sanitizeInput(String input) {
        return input.replaceAll("_", "").trim();
    }

    private boolean isValidCity(String cityName) {
        return planner.findCityIndex(cityName) != -1;
    }

    private void showSuggestions(String input, List<String> suggestions) {
        WarningPrinter.printFormattedWarning("\nCity '%s' not found. Suggestions:\n", input);
        if (suggestions.isEmpty()) {
            WarningPrinter.printWarning("No suggestions available.");
        } else {
            suggestions.forEach(s -> System.out.println("  • " + s));
        }
        WarningPrinter.printWarning("Please try again.\n");
    }

    private List<String> processAttractions(String input) {
        return Arrays.stream(input.split("\\s*,\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean validateAttractions(List<String> attractions) {
        List<String> invalid = attractions.stream()
                .filter(a -> planner.findAttractionIndex(a) == -1)
                .collect(Collectors.toList());

        if (!invalid.isEmpty()) {
            System.out.println("Invalid attractions: " + invalid);
            return false;
        }
        return true;
    }
}