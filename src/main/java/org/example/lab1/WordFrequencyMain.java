package org.example.lab1;

import org.example.general.WordFrequencyCounter;
import org.example.general.WordFrequencyResult;

import java.nio.file.Path;

public class WordFrequencyMain {
    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }

        String wordToCheck = args[0].toLowerCase();
        String filePath = args[1];
        WordFrequencyResult result = WordFrequencyCounter.countWordFrequency(Path.of(filePath));
        int totalWords = result.totalWords();
        int checkedWordCount = result.wordFrequency().getOrDefault(wordToCheck, 0);
        double frequencyPercentage = (totalWords == 0) ? 0.0 : (checkedWordCount * 100.0 / totalWords);

        System.out.println("Общее количество слов: " + totalWords);
        System.out.println("Частота слова '" + wordToCheck + "': " + checkedWordCount);
        System.out.printf("Частота в процентах: %.2f%% \n", frequencyPercentage);
    }
}

