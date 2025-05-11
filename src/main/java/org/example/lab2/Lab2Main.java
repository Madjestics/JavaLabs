package org.example.lab2;

import org.example.general.WordFrequencyResult;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Lab2Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            return;
        }

        String targetWord = args[0].toLowerCase();
        String dirPath = args[1];

        try {
            long start = System.currentTimeMillis();
            WordFrequencyResult result = ParallelWordFrequencyCounter.countWordFrequencyParallelStream(dirPath);
            long end = System.currentTimeMillis();

            int totalWords = result.totalWords();
            int checkedWordCount = result.wordFrequency().getOrDefault(targetWord, 0);
            double percentage = totalWords == 0 ? 0.0 : (checkedWordCount * 100.0 / totalWords);

            System.out.println("Время обработки файлов: " + (end-start) + "мс");
            System.out.println("Общее количество слов: " + totalWords);
            System.out.println("Частота слова '" + targetWord + "': " + checkedWordCount);
            System.out.printf("Частота в процентах: %.2f%% \n", percentage);
        } catch (UncheckedIOException | IOException e) {
            System.err.println("Ошибка чтения файлов: " + e.getMessage());
        }
    }
}
