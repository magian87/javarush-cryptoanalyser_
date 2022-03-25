package com.javarush.cryptoanalyser.decryption;



import com.javarush.cryptoanalyser.exception.CryptographicKeyException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.javarush.cryptoanalyser.CommonUtils.*;
import static com.javarush.cryptoanalyser.encryption.EncryptionUtils.encryptionLine;
import static com.javarush.cryptoanalyser.encryption.EncryptionUtils.encryptionText;
import static com.javarush.cryptoanalyser.Constant.*;

public class StatisticalAnalysis {

    //Получение отношения кол-ва гласных букв к количеству согласных возведенных во 2 степень
    public static double relationshipLetter(String fileName, int offset) throws IOException, CryptographicKeyException {
        existFile(fileName, ERR_STATIC_ANALYSIS_BY_LETTER);
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(fileName), Charset.defaultCharset())) {
            int cntLowelLetters = 0;
            int cntConsonantLetters = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String modifiedLine;
                if (offset != 0) {
                    modifiedLine = encryptionLine(line, -offset);
                } else {
                    modifiedLine = line;
                }
                char[] arr = modifiedLine.toLowerCase().toCharArray();

                for (char ch : arr) {
                    for (char consonantLetter : CONSONANT_LETTERS) {
                        if (ch == consonantLetter) {
                            cntConsonantLetters++;
                            break;
                        }
                    }

                    for (char vowelLetter : VOWEL_LETTERS) {
                        if (ch == vowelLetter) {
                            cntLowelLetters++;
                            break;
                        }
                    }
                }
            }
            float t = (float) cntLowelLetters / cntConsonantLetters;
            return Math.pow(t, 2);
        } catch (IOException e) {
            throw new IOException("Произошла ошибка при работе с файлом: " + fileName);
        }
    }

    //Расшифровка по методу статистического анализа отношения количеста глассных и согласных букв
    public static void staticAnalysisByLetter(String sourceFile, String additionFile, String destinationFile) throws CryptographicKeyException, IOException {
        existFile(additionFile, ERR_STATIC_ANALYSIS_BY_LETTER);
        existFile(sourceFile, ERR_STATIC_ANALYSIS_BY_LETTER);

        double minDeviation = Double.MAX_VALUE;
        //Получение
        double ishDeviation = relationshipLetter (additionFile, 0);
        double curDeviation = relationshipLetter(sourceFile, 0);

        double deviation = Math.abs(ishDeviation - curDeviation);
        int key = 0;

        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            double curDeviation2 = relationshipLetter(sourceFile, j);
            double delta = Math.abs(curDeviation2 - deviation);
            if (minDeviation > delta) {
                minDeviation = delta;
                key = j;
            }
        }
        System.out.printf("Вероятный ключ: %d, среднеквадратичное отклонение: %f\n", key, minDeviation );
        //System.out.printf("j=%d, delta=%f\n", j, delta);
        encryptionText(-key, sourceFile, destinationFile);
    }

    //Получение множества уникальных слов на основе переданного текста (если offset!=0 то идет расшифровка)
    private static HashSet<String> getUniqWords(String fileName, int offset) throws CryptographicKeyException, IOException {
        existFile(fileName, ERR_STATIC_ANALYSIS_BY_WORDS);

        HashSet<String> uniqWords = new HashSet<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(fileName), Charset.defaultCharset())) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String modifiedLine;
                if (offset != 0) {
                    modifiedLine = encryptionLine(line, offset);
                } else {
                    modifiedLine = line;
                }
                uniqWords.addAll(divideStringByWords(modifiedLine));
            }
        } catch (IOException e) {
            throw new IOException("Произошла ошибка при работе с файлом: " + fileName);
        }

        return uniqWords;
    }

    /*
      Расшифровка методом статистического анализа, вариант 2, рабочий
      1.Анализ не зашифрованного текста того же автора, получаю список слов
      2.В момент дешифрования получаю уникальные слова
      3.Ключом будет тот вариант, в котором совпало наибольшее количество слов
      Алгоритм работает безотказно, не понятно, почему при этом ключ отличается от ключа шифрования
     */
    public static void staticAnalysisByWords(String sourceFile, String destinationFile, String additionFile) throws CryptographicKeyException, IOException {
        HashSet<String> originalWords = getUniqWords(additionFile, 0);
        Map<Integer, Integer> words = new HashMap<>();

        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            HashSet<String> wordsFromDecryption = getUniqWords(destinationFile, j);
            HashSet<String> wordsFromAdditionText = new HashSet<>(originalWords);

            //Пересечение множеств
            wordsFromAdditionText.retainAll(wordsFromDecryption);
            words.put(j, wordsFromAdditionText.size());
        }

        int[] keyValue = findKeyValueInMapByMaxValue(words);

        System.out.printf("Найден ключ шифрования=%d, количество совпавших слов=%d\n", keyValue[0], keyValue[1]);
        encryptionText(keyValue[0], destinationFile, sourceFile);
    }


}
