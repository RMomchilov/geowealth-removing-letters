package org.rmomchilov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class Main {

    public static final int INITIAL_CAPACITY = 50000;

    public static void main(String[] args) throws IOException {
        List<String> dictionary = loadAllWords();
        TreeSet<String> dictionaryTree = new TreeSet<>(dictionary);
        List<String> words = dictionary.stream()
                .filter(word -> word.length() == 9)
                .toList();

        LocalDateTime startTime = LocalDateTime.now();
        HashMap<String, Boolean> decompositionTable = new HashMap<>(INITIAL_CAPACITY);
        decompositionTable.put("A", true);
        decompositionTable.put("I", true);

        List<String> decomposableWords = new ArrayList<>();
        for (String word : words) {
            Boolean decomposable = checkWord(word, dictionaryTree, decompositionTable);
            if (decomposable) {
                decomposableWords.add(word);
            }
        }

        decomposableWords.forEach(System.out::println);
        System.out.println("Result count: " + decomposableWords.size());

        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("Runtime: " + Duration.between(startTime, endTime).toMillis() + " ms");
    }

    private static Boolean checkWord(String word, TreeSet<String> dictionaryTree, HashMap<String, Boolean> decompositionTable) {
        if (word.isEmpty()) {
            return false;
        } else if (!word.contains("A") && !word.contains("I")) {
            decompositionTable.put(word, false);
            return false;
        } else if (!dictionaryTree.contains(word)) {
            decompositionTable.put(word, false);
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            String leftString = word.substring(0, i);
            String rightString = word.substring(i + 1);
            if (decompositionTable.containsKey(leftString + rightString)) {
                if (decompositionTable.get(leftString + rightString)) {
                    decompositionTable.put(word, true);
                    return true;
                }
            } else {
                Boolean decomposable = checkWord(leftString + rightString, dictionaryTree, decompositionTable);
                if (decomposable) {
                    decompositionTable.put(word, true);
                    return true;
                }
            }
        }

        decompositionTable.put(word, false);
        return false;
    }

    private static List<String> loadAllWords() throws IOException {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            return reader.lines().skip(2).toList();
        }
    }
}