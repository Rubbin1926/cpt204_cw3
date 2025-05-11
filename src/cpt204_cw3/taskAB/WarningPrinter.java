package cpt204_cw3.taskAB;

/**
 * Utility class for printing warning messages in red color
 */
public class WarningPrinter {
    private static final String RED_COLOR = "\u001B[31m";
    private static final String RESET_COLOR = "\u001B[0m";

    /**
     * Prints a warning message in red color
     * @param message the warning message to display
     */
    public static void printWarning(String message) {
        System.out.println(RED_COLOR + message + RESET_COLOR);
    }

    /**
     * Formats and prints a warning message in red color
     * @param format the format string
     * @param args the arguments referenced by the format specifiers
     */
    public static void printFormattedWarning(String format, Object... args) {
        System.out.print(RED_COLOR);
        System.out.printf(format, args);
        System.out.print(RESET_COLOR);
    }
}