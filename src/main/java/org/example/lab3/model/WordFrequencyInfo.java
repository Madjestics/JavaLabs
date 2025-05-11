package org.example.lab3.model;

import java.util.UUID;

public record WordFrequencyInfo(UUID file_id, String word, Integer count, Double percentage) {
}
