package org.example.lab3.model;

public record WordStatisticInfo(String filename, String word, Integer count, Double percentage) {
    public static final String FILENAME_COLUMN = "filename";
    public static final String WORD_COLUMN = "word";
    public static final String COUNT_COLUMN = "count";
    public static final String PERCENTAGE_COLUMN = "percentage";
}
