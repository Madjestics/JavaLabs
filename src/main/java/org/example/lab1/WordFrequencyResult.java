package org.example.lab1;

import java.util.Map;

public record WordFrequencyResult(Map<String, Integer> wordFrequency, int totalWords) {
}
