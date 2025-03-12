package org.example.lab1;

import java.io.IOException;

public class WordFrequencyMain {
    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }

        String wordToCheck = args[0].toLowerCase();
        String filePath = args[1];
        try {
            WordFrequencyResult result = WordFrequencyCounter.countWordFrequency(filePath);
            int totalWords = result.totalWords();
            int checkedWordCount = result.wordFrequency().getOrDefault(wordToCheck, 0);
            double frequencyPercentage = (totalWords == 0) ? 0.0 : (checkedWordCount * 100.0 / totalWords);

            System.out.println("Общее количество слов: " + totalWords);
            System.out.println("Частота слова '" + wordToCheck + "': " + checkedWordCount);
            System.out.printf("Частота в процентах: %.2f%% \n", frequencyPercentage);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}

