package org.example.lab1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordFrequencyCounter {
    private WordFrequencyCounter() {
    }

    /**
     * Предлоги и союзы, которые по-хорошему исключить из текста, чтобы они не учитывались при подсчете
     */
    private static final Set<String> STOP_WORDS = Set.of(
            "и", "в", "на", "с", "под", "по", "за", "без", "до", "от", "при", "к", "о", "об", "у",
            "а", "но", "или", "либо", "да", "же", "тоже", "также", "зато", "хотя", "поскольку",
            "из", "между", "около", "перед", "через", "для"
    );

    public static WordFrequencyResult countWordFrequency(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filePath));
        Map<String, Integer> wordCountMap = new HashMap<>();
        int totalWords = 0;

        for (String line : lines) {
            String[] words = line.toLowerCase().split("\\W+");
            for (String word : words) {
                if (!word.isEmpty() && !STOP_WORDS.contains(word)) {
                    wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                    totalWords++;
                }
            }
        }
        return new WordFrequencyResult(wordCountMap, totalWords);
    }
}
