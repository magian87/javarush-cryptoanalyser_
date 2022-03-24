package com.javarush.cryptoanalyser.decryption;

import com.javarush.cryptoanalyser.exception.InvalidKeyCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.javarush.cryptoanalyser.CommonUtils.existFile;
import static com.javarush.cryptoanalyser.Constant.ALPHABET_LIST;
import static com.javarush.cryptoanalyser.Constant.ERR_BRUTE_FORCE;
import static com.javarush.cryptoanalyser.encryption.EncryptionUtils.encryptionLine;
import static com.javarush.cryptoanalyser.encryption.EncryptionUtils.encryptionText;

public class BruteForce {
    public static void bruteForce(String sourceFile, String destinationFile) throws InvalidKeyCrypt, IOException {
        existFile(sourceFile, ERR_BRUTE_FORCE);

        Set<String> wordSet = new HashSet<>();
        Map<Integer, Integer> wordsWithQuantityRepetitions = new HashMap<>();
        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(sourceFile), Charset.defaultCharset())) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] words = encryptionLine(line.toLowerCase(), -j).split(" ");
                    for (String word : words) {
                        if (!word.matches(".*[.,-:?!].*")) { //Убираю из найденных слов те, в которых есть знаки препинания
                            //как доработать данную регулярку, что бы не отбрасывались слова заканчивающиеся на знак препинания?
                            wordSet.add(word);
                        }

                    }
                }
                wordsWithQuantityRepetitions.put(j, wordSet.size());
                wordSet.clear();
            } catch (IOException e) {
                throw new IOException("Произошла ошибка при работе с файлом: "
                        + Path.of(sourceFile).getFileName());
            }
        }

        int maxUniqueWords = Integer.MIN_VALUE;
        int key = Integer.MIN_VALUE;

        for (Map.Entry<Integer, Integer> kvv : wordsWithQuantityRepetitions.entrySet()) {
            if (kvv.getValue() > maxUniqueWords) {
                maxUniqueWords = kvv.getValue();
                key = kvv.getKey();
            }
        }
        System.out.printf("Найден ключ шифрования=%d, количество уникальных слов = %d \n", key, maxUniqueWords);
        encryptionText(-key, sourceFile, destinationFile);
    }
}
