package org.example.lab2;

import org.example.general.WordFrequencyCounter;
import org.example.general.WordFrequencyResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

public class ParallelWordFrequencyCounter {

    public static WordFrequencyResult countWordFrequencyParallelStream(String dirPath) throws IOException {
        try (Stream<Path> filePaths = Files.list(Path.of(dirPath))) {
            return filePaths.parallel()
                    .map(WordFrequencyCounter::countWordFrequency)
                    .reduce(new WordFrequencyResult(new HashMap<>(), 0), WordFrequencyResult::merge);
        }
    }
}
