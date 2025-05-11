package org.example.general;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class WordFrequencyCounter {
    private WordFrequencyCounter() {
    }

    private static final Logger LOGGER = Logger.getLogger(WordFrequencyCounter.class.getName());

    /**
     * Предлоги и союзы, которые по-хорошему исключить из текста, чтобы они не учитывались при подсчете
     */
    private static final Set<String> STOP_WORDS = Set.of(
            "и", "в", "на", "с", "под", "по", "за", "без", "до", "от", "при", "к", "о", "об", "у",
            "а", "но", "или", "либо", "да", "же", "тоже", "также", "зато", "хотя", "поскольку",
            "из", "между", "около", "перед", "через", "для", "то", "тут", "он", "я", "ax", "ох", "ой"
    );

    public static WordFrequencyResult countWordFrequency(Path filePath) {
        List<String> lines = Collections.emptyList();
        try {
           lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            LOGGER.log(new LogRecord(Level.SEVERE, "Ошибка загрузки файла: " + e.getMessage()));
        }
        Map<String, Integer> wordCountMap = new HashMap<>();
        int totalWords = 0;

        for (String line : lines) {
            String[] words = line.toLowerCase().split("[^\\p{L}]+");
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
