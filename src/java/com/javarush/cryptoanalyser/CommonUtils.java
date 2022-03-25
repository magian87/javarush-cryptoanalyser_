package com.javarush.cryptoanalyser;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.javarush.cryptoanalyser.Constant.PUNCTUATION_MARKS;
import static java.lang.String.format;

public class CommonUtils {

    public static void existFile(String filename, String afterText) throws FileNotFoundException {
        if (Files.notExists(Path.of(filename)) || filename.isEmpty() || !Files.isRegularFile(Path.of(filename))) {
            String str = "Файл не существует: %s, %s\n";
            throw new FileNotFoundException(format(str, filename, afterText));
        }
    }

    public static int[] findKeyValueInMapByMaxValue(Map<Integer, Integer> map) {
        int[] keyValue = new int[2];

        keyValue[0] = Integer.MIN_VALUE; //key
        keyValue[1] = Integer.MIN_VALUE; //value

        for (Map.Entry<Integer, Integer> kvv : map.entrySet()) {
            if (kvv.getValue() > keyValue[1]) {
                keyValue[1] = kvv.getValue();
                keyValue[0] = kvv.getKey();
            }
        }
        return keyValue;
    }

    public static Set<String> divideStringByWords(String str) {
        Set<String> words = new HashSet<>();
        String[] wordsLine = str.toLowerCase().split(" ");
        for (String word : wordsLine) {
            String wordWithoutPunctuationMark;
            //Если последний символ знак препинания, то убираю его

            if (word.length() > 1 && PUNCTUATION_MARKS.contains(word.charAt(word.length() - 1))) {
                wordWithoutPunctuationMark = word.substring(0, word.length() - 1);
            } else {
                wordWithoutPunctuationMark = word;
            }

            //Убираю из найденных слов те, в которых есть знаки препинания
            if (!wordWithoutPunctuationMark.matches(".*[.,:;?!)(\"-«»-].*")) {
                words.add(wordWithoutPunctuationMark);
            }

        }
        return words;
    }


}
