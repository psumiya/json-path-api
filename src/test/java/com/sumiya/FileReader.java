package com.sumiya;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileReader {

    private FileReader() {
    }

    public static String getFileContentAsString(String filenameWithPath) {
        try {
            return Files.readString(Paths.get(filenameWithPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
