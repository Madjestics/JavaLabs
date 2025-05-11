package org.example.general;

import java.util.HashMap;
import java.util.Map;

public record WordFrequencyResult(Map<String, Integer> wordFrequency, int totalWords) {
    public WordFrequencyResult merge(WordFrequencyResult other) {
        Map<String, Integer> combined = new HashMap<>(this.wordFrequency);
        other.wordFrequency.forEach((w, c) -> combined.merge(w, c, Integer::sum));
        return new WordFrequencyResult(combined, this.totalWords + other.totalWords);
    }
}
