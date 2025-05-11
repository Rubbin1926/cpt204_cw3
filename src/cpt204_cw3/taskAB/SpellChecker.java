package cpt204_cw3.taskAB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpellChecker {
    private final List<String> dictionary;

    public SpellChecker(List<String> dictionary) {
        this.dictionary = new ArrayList<>(dictionary);
    }

    public List<String> getSuggestions(String input, int maxSuggestions) {
        List<WordDistance> candidates = new ArrayList<>();

        for (String word : dictionary) {
            int distance = calculateLevenshteinDistance(input, word);
            if (distance <= 3) { // Only consider reasonably close matches
                candidates.add(new WordDistance(word, distance));
            }
        }

        candidates.sort(Comparator.comparingInt(WordDistance::distance));

        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < Math.min(maxSuggestions, candidates.size()); i++) {
            suggestions.add(candidates.get(i).word());
        }
        return suggestions;
    }

    private int calculateLevenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int substitutionCost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(
                            Math.min(
                                    dp[i - 1][j] + 1,     // Deletion
                                    dp[i][j - 1] + 1      // Insertion
                            ),
                            dp[i - 1][j - 1] + substitutionCost  // Substitution
                    );
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    private record WordDistance(String word, int distance) {}
}