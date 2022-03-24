package com.javarush.cryptoanalyser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.javarush.cryptoanalyser.CommonUtils.existFile;
import static com.javarush.cryptoanalyser.Constant.ALPHABET_LIST;
import static com.javarush.cryptoanalyser.Constant.ERR_BRUTE_FORCE;
import static com.javarush.cryptoanalyser.EncryptionUtils.encryptionLine;
import static com.javarush.cryptoanalyser.EncryptionUtils.encryptionText;

public class BruteForce {
    public static void bruteForce(String sourceFile, String destinationFile) throws InvalidKeyCrypt, IOException {
        existFile(sourceFile, ERR_BRUTE_FORCE);

        Set<String> wordSet = new HashSet<>();
        Map<Integer, Integer> mapKey = new HashMap<>();
        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(sourceFile), Charset.defaultCharset())) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String newLine2 = encryptionLine(line, -j).toLowerCase();
                    String[] words = newLine2.split(" ");
                    for (String word : words) {
                        if (!word.matches(".*[.,-:?!].*")) { //Убираю из найденных слов те, в которых есть знаки препинания
                            //как доработать данную регулярку, что бы не отбрасывались слова заканчивающиеся на знак препинания?
                            wordSet.add(word);
                        }

                    }
                }
                mapKey.put(j, wordSet.size());
                wordSet.clear();
            } catch (IOException e) {
                throw new IOException("Произошла ошибка при работе с файлом: "
                        + Path.of(sourceFile).getFileName());
            }
        }

        int mapMaxValue = 0;
        int mapKeyMax = 0;


        for (Map.Entry<Integer, Integer> kvv : mapKey.entrySet()) {
            if (kvv.getValue() > mapMaxValue) {
                mapMaxValue = kvv.getValue();
                mapKeyMax = kvv.getKey();
            }
        }
        System.out.printf("Найден ключ шифрования=%d, количество уникальных слов = %d \n", mapKeyMax, mapMaxValue);
        encryptionText(-mapKeyMax, sourceFile, destinationFile);
    }
}
