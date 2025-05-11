package org.example.lab3.model;

import java.util.UUID;

public record FileInfo(UUID id, String path) {
    public static final String FILE_ID_COLUMN = "id";
    public static final String FILE_PATH_COLUMN = "path";
}
