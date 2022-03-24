package com.javarush.cryptoanalyser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.javarush.cryptoanalyser.CommonUtils.existFile;
import static com.javarush.cryptoanalyser.Constant.*;
import static com.javarush.cryptoanalyser.EncryptionUtils.crypt;




/*
Каждый метод и каждый класс должны иметь одну ответсвенность
Класс Crypto где есть параметры, не должен быть статическим
* */

public class Crypto {


    //Ключ используемый для шифрования
    private static int key;
    //Исходный файл, файл зашифрованный, доп. файл того же автора
    private static String sourceFile;
    private static String destinationFile;
    private static String additionalFile;


    //Что бы не дублировать код, см. процедуру setFileFromMenu
    private static final Map<TypeFiles, String> typeFilesMap = new HashMap<>();

    static {
        typeFilesMap.put(TypeFiles.SOURCE, "файл для шифрования");
        typeFilesMap.put(TypeFiles.DESTINATION, "файл для расшифровки");
        typeFilesMap.put(TypeFiles.ADDITION, "дополнительный файл");



        Crypto.setSourceFile(Path.of("").toAbsolutePath().resolve(Path.of(SOURCE_FILE)).toString());
        Crypto.setDestinationFile(Path.of("").toAbsolutePath().resolve(Path.of(DESTINATION_FILE)).toString());
        Crypto.setAdditionalFile(Path.of("").toAbsolutePath().resolve(Path.of(ADDITIONAL_FILE)).toString());

        Crypto.setKey(15);
    }

    public static int getKey() {
        return key;
    }

    public static void setKey(int key) {
        Crypto.key = key;
    }

    public static String getSourceFile() {
        return sourceFile;
    }

    public static void setSourceFile(String sourceFile) {
        Crypto.sourceFile = sourceFile;
    }

    public static String getDestinationFile() {
        return destinationFile;
    }

    public static void setDestinationFile(String destinationFile) {
        Crypto.destinationFile = destinationFile;
    }

    public static String getAdditionalFile() {
        return additionalFile;
    }

    public static void setAdditionalFile(String additionalFile) {
        Crypto.additionalFile = additionalFile;
    }


    private static String generateParams() {
        return "ПАРАМЕТРЫ" + "\n"
                + "Файл для Шифрования: " + Crypto.getSourceFile() + "\n"
                + "Файл для расшифровки: " + Crypto.getDestinationFile() + "\n"
                + "Дополнительный файл: " + Crypto.getAdditionalFile() + "\n"
                + "Криптографический ключ: " + Crypto.getKey();

    }

    //Отображение текцщих параметров шифрования-дешифрования
    public static void showParams() {
        System.out.println(Crypto.generateParams());
    }


    //Задание файлов используемых файлов
    public static void setFileFromMenu(TypeFiles typeFiles) throws FileNotFoundException {
        //
        //        Почему нельзя использовать try -with-resources, появляются ошибки, как будто закрывается
        //        основной Scanner из модуля Main. Вот StackTrace. Что делать и как исправить?
        //        Exception in thread "main" java.util.NoSuchElementException
        //        at java.base/java.util.Scanner.throwFor(Scanner.java:937)
        //        at java.base/java.util.Scanner.next(Scanner.java:1594)
        //        at java.base/java.util.Scanner.nextInt(Scanner.java:2258)
        //        at java.base/java.util.Scanner.nextInt(Scanner.java:2212)
        //        at com.javarush.cryptoanalyser.Main.main(Main.java:41)
        //try
        Scanner scanner1 = new Scanner(System.in);//{
        String value = typeFilesMap.get(typeFiles);
        System.out.print("Введите " + value + ": ");
        String filename = scanner1.nextLine();
        existFile(filename, "новый файл для " + value + " не будет задан.");

        if (typeFiles == TypeFiles.SOURCE) {
            Crypto.setSourceFile(filename);
        } else if (typeFiles == TypeFiles.DESTINATION) {
            Crypto.setDestinationFile(filename);
        } else if (typeFiles == TypeFiles.ADDITION) {
            Crypto.setAdditionalFile(filename);
        }
        Crypto.setSourceFile(filename);

    }

    //задание криптографического ключа
    public static void setKeyFromMenu() {
        //try (
        Scanner scanner1 = new Scanner(System.in);//) {
        System.out.print("Введите криптографический ключ: ");
        int key = 0;
        try {
            key = scanner1.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("ОШИБКА: криптографический ключ должен быть числом");
        }
        if (key <= 0) {
            System.out.println("Криптографический ключ должен быть больше 0");
        } else if (key >= ALPHABET_LIST.size()) {
            System.out.println("Криптографический ключ должен быть меньше либо равным количеству символов в алфамите: " + ALPHABET_LIST.size());
        }

        Crypto.setKey(key);
        //}
    }



    //Процедура расшифровки строки
    private static String enCryptLine(String line, int key) throws InvalidKeyCrypt {
        char[] lineChar = line.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        //Warning:(166, 9) 'for' loop replaceable with enhanced 'for'
        //что это значит, как исправить?
        for (char c : lineChar) {
            stringBuilder.append(crypt(c, -key));
        }
        return stringBuilder.toString();
    }


    //Процедура шифрования файла
    public static void cryptText(int key, String sourceFile, String destinationFile) throws IOException, InvalidKeyCrypt {
        if (key>0) {
            existFile(sourceFile, ERR_CRYPTE );
        } else {
            existFile(destinationFile, ERR_DECRYPTE);
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destinationFile))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                char[] strArr = line.toCharArray();
                for (char c : strArr) {
                    bufferedWriter.append(crypt(c, key));
                }
                bufferedWriter.append("\n");
            }
        } catch (IOException e) {
            throw new IOException("Произошла ошибка при работе с файлом\\файллами: "
                    + Path.of(sourceFile).getFileName() + ", "
                    + Path.of(destinationFile).getFileName());
        }
        if (key>0) {
            System.out.println("Файл был успешно зашифрован в файл: "+ destinationFile);
        } else {
            System.out.println("Файл был успешно дешифрован в файл: "+ destinationFile);
        }
    }


    public static void bruteForce(String filename) throws InvalidKeyCrypt, IOException {
        existFile(filename, ERR_BRUTE_FORCE);

        Set<String> wordSet = new HashSet<>();
        Map<Integer, Integer> mapKey = new HashMap<>();
        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(filename), Charset.defaultCharset())) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String newLine2 = enCryptLine(line, j).toLowerCase();
                    String[] words = newLine2.split(" ");
                    for (String word : words) {
                        if (!word.matches(".*[.,-:?!].*")) { //Убираю из найденных слов те, в которых есть знаки препинания
                            //как доработать данную регулярку, что бы не отбрасывались слова заканчивающиеся на знак препинания?
                            wordSet.add(word);
                        }

                    }
                    //System.out.println(bufferedReader.markSupported());
                }
                //Записываю ключ и количество найденных слов без знааков препинания
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
        Crypto.cryptText(-mapKeyMax, Crypto.getDestinationFile(), Crypto.getSourceFile());
        System.out.println("Файл был расшифрован: " + Crypto.getSourceFile());
    }

    //Получение отношения кол-ва гласных букв к количеству согласных возведенных во 2 степень
    private static double relationshipLetter(String file, int offset) throws IOException, InvalidKeyCrypt {
        existFile(file, ERR_STATIC_ANALIZ_BY_LETTER);
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(file), Charset.defaultCharset())) {
            int cntLowelLetters = 0;
            int cntConsonantLetters = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String newLine2;
                if (offset != 0) {
                    newLine2 = enCryptLine(line, -offset);
                } else {
                    newLine2 = line;
                }
                char[] arr = newLine2.toLowerCase().toCharArray();

                for (char ch : arr) {
                    for (char consonantLetter : CONSONANT_LETTERS) {
                        if (ch == consonantLetter) {
                            cntConsonantLetters++;
                            break;
                        }
                    }

                    for (char lowelLetter : LOWEL_LETTERS) {
                        if (ch == lowelLetter) {
                            cntLowelLetters++;
                            break;
                        }
                    }
                }
            }
            float t = (float) cntLowelLetters / cntConsonantLetters;
            return Math.pow(t, 2);
        } catch (IOException e) {
            throw new IOException("Произошла ошибка при работе с файлом: " + file);
        }
    }

    public static void staticAnalizByLetter() throws InvalidKeyCrypt, IOException {
        existFile(Crypto.getAdditionalFile(), ERR_STATIC_ANALIZ_BY_LETTER);
        existFile(Crypto.getDestinationFile(), ERR_STATIC_ANALIZ_BY_LETTER);

        double minDeviation = Double.MAX_VALUE;
        //Получение
        double ishDeviation = Crypto.relationshipLetter(Crypto.additionalFile, 0);
        double curDeviation = Crypto.relationshipLetter(Crypto.getDestinationFile(), 0);

        double deviation = Math.abs(ishDeviation - curDeviation);
        int key = 0;

        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            double curDeviation2 = Crypto.relationshipLetter(Crypto.getDestinationFile(), j);
            double delta = Math.abs(curDeviation2 - deviation);
            if (minDeviation > delta) {
                minDeviation = delta;
                key = j;
            }
        }
        System.out.printf("Вероятный ключ: %d, среднеквадратичное отклонение: %f\n", key, minDeviation );
        //System.out.printf("j=%d, delta=%f\n", j, delta);
        Crypto.cryptText(-key, Crypto.getDestinationFile(), Crypto.getSourceFile());
        System.out.println("Файл был расшиврован: "+ Crypto.getSourceFile());
    }

    //Получение множества уникальных слов на основе переданного текста (если offset!=0 то идет расшифровка)
    private static HashSet<String> getUniqWords(String filename, int offset) throws InvalidKeyCrypt, IOException {
        existFile(filename, ERR_STATIC_ANALIZ_BY_WORDS);

        HashSet<String> uniqWords = new HashSet<>();

        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(filename), Charset.defaultCharset())) {
            //Во многих местах повторяется данный код:
            //try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(filename), Charset.defaultCharset())) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String newLine2;
                if (offset != 0) {
                    newLine2 = enCryptLine(line, -offset);
                } else {
                    newLine2 = line;
                }

                String[] words = newLine2.toLowerCase().split(" ");
                for (String word : words) {
                    if (!word.matches(".*[.,-:?!].*")) { //Убираю из найденных слов те, в которых есть знаки препинания
                        //как доработать данную регулярку, что бы не отбрасывались слова заканчивающиеся на знак препинания?
                        uniqWords.add(word);
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Произошла ошибка при работе с файлом: " + filename);
        }

        return uniqWords;
    }

    /*
      Расшифровка методом статистического анализа, вариант 2, рабочий
      1.Анализ не зашифрованного текста того же автора, получаю список слов
      2.В момент дешифрования получаю уникальные слова
      3.Ключом будет тот вариант, в котором совпало наибольшее количество слов
      Алгоритм работает безотказно, не понятно, почему при этом ключ отличается от
      ключа шифрования
     */
    public static void staticAnalizByWords() throws InvalidKeyCrypt, IOException {
        HashSet<String> originalWords = getUniqWords(Crypto.getAdditionalFile(), 0);
        Map<Integer, Integer> unionMn = new HashMap<>();

        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            HashSet<String> currentWords = getUniqWords(Crypto.getDestinationFile(), j);
            HashSet<String> orign = new HashSet<>(originalWords);

            orign.retainAll(currentWords);
            unionMn.put(j, orign.size());
        }

        int kkey = -1;
        int maxValue = -1;

        for (Map.Entry<Integer, Integer> map : unionMn.entrySet()) {
            if (map.getValue() > maxValue) {
                maxValue = map.getValue();
                kkey = map.getKey();
            }
        }

        System.out.printf("Найден ключ шифрования=%d, количество совпавших слов=%d\n", kkey, maxValue);
        Crypto.cryptText(-key, Crypto.getDestinationFile(), Crypto.getSourceFile());
        System.out.println("Файл был успешно расшифрован: " + Crypto.getSourceFile());
    }

     /*В нескольких местах используется данный код, отличие в том, что происходит в цикле while
    Как избавиться от этого дублирования кода?
    Я пока вижу только один варинат, объявляю enum и в зависимости от этого значения переданного
    enum делаю анализ. но мне этот вариант не нравится. знаю, что в других языках можно передавать
    процедуру в качестве параметра. в какую сторону искать?
    Этот код не функциональный, написан для того, что бы вопрос подкрепить примером
     */

    /*
    private static void test(String filename) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(filename), Charset.defaultCharset())) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //1 вариант Подсчет количества гласных и согласных букв
                //2 варинат получение кол-ва слов
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Итоговый анализ ранее полученных данных
    }
    */


}
