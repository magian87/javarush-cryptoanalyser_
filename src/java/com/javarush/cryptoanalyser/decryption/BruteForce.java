package com.javarush.cryptoanalyser.decryption;

import com.javarush.cryptoanalyser.exception.CryptographicKeyException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.javarush.cryptoanalyser.CommonUtils.*;
import static com.javarush.cryptoanalyser.Constant.ALPHABET_LIST;
import static com.javarush.cryptoanalyser.Constant.ERR_BRUTE_FORCE;
import static com.javarush.cryptoanalyser.encryption.EncryptionUtils.encryptionLine;
import static com.javarush.cryptoanalyser.encryption.EncryptionUtils.encryptionText;

public class BruteForce {
    public static void bruteForce(String sourceFile, String destinationFile) throws CryptographicKeyException, IOException {
        existFile(sourceFile, ERR_BRUTE_FORCE);

        Set<String> wordSet = new HashSet<>();
        Map<Integer, Integer> wordsWithQuantityRepetitions = new HashMap<>();
        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(sourceFile), Charset.defaultCharset())) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    wordSet.addAll(divideStringByWords(encryptionLine(line, -j)));
                }
                wordsWithQuantityRepetitions.put(j, wordSet.size());
                wordSet.clear();
            } catch (IOException e) {
                throw new IOException("Произошла ошибка при работе с файлом: "
                        + Path.of(sourceFile).getFileName());
            }
        }

        int[] keyValue = findKeyValueInMapByMaxValue(wordsWithQuantityRepetitions);
        System.out.printf("Найден ключ шифрования=%d, количество уникальных слов = %d \n", keyValue[0], keyValue[1]);
        encryptionText(-keyValue[0], sourceFile, destinationFile);
    }
}
