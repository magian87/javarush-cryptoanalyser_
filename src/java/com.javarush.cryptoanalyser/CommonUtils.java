package com.javarush.cryptoanalyser;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;

public class CommonUtils {

    public static void existFile(String filename, String afterText) throws FileNotFoundException {
        if (Files.notExists(Path.of(filename)) || filename.isEmpty()) {
            String str = "Файл не существует: %s, %s\n";
            throw new FileNotFoundException(format(str, filename, afterText));
        }
    }



}
