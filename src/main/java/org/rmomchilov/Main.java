package org.rmomchilov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> words = loadAllWords().stream()
                .sorted(Comparator.comparingInt(String::length))
                .toList();

        LocalDateTime startTime = LocalDateTime.now();
        HashMap<String, Boolean> resultsTable = new HashMap<>(words.size() + 2);
        resultsTable.put("A", true);
        resultsTable.put("I", true);

        for (String word : words) {
            checkWord(word, resultsTable);
        }

        List<Map.Entry<String, Boolean>> results = resultsTable.entrySet()
                .stream()
                .filter((entry) -> entry.getValue() && entry.getKey().length() == 9)
                .toList();
        results.forEach(entry -> System.out.println(entry.getKey()));
        System.out.println("Result count: " + results.size());

        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("Runtime: " + Duration.between(startTime, endTime).toMillis());
    }

    private static void checkWord(String word, HashMap<String, Boolean> resultsTable) {
        for (int i = 0; i < word.length(); i++) {
            String leftString = word.substring(0, i);
            String rightString = word.substring(i + 1);
            if ((leftString.isEmpty() || resultsTable.containsKey(leftString))
                    && (rightString.isEmpty() || resultsTable.containsKey(rightString))) {
                resultsTable.put(word, true);
                return;
            }
        }

        resultsTable.put(word, false);
    }

    private static List<String> loadAllWords() throws IOException {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            return reader.lines().skip(2).toList();
        }
    }
}