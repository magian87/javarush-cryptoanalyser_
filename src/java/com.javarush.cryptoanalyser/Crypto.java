package com.javarush.cryptoanalyser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.*;

public class Crypto {

    private final static String SOURCE_FILE = "/home/bulat/test/source.txt";
    private final static String DESTINATION_FILE = "/home/bulat/test/destination.txt";
    private final static String ADDITIONAL_FILE = "/home/bulat/test/add.txt";

    private static final List<Character> ALPHABET_LIST = Arrays.asList('а', 'б', 'в',
            'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»',
            ':', '!', '?', ' ');

    private static final char[] LOWEL_LETTERS = {'а', 'у', 'о', 'ы', 'э', 'я', 'ю', 'ё', 'и', 'е'};
    private static final char[] CONSONANT_LETTERS = {'б', 'в', 'г', 'д', 'ж', 'з', 'й', 'к', 'л', 'м', 'н', 'п', 'р', 'с', 'т', 'ф', 'х', 'ц', 'ч', 'ш', 'щ'};

    //Ключ используемый для шифрования
    private static int key;
    //Исходный файл, файл зашифрованный, доп. файл того же автора
    private static String sourceFile = SOURCE_FILE;
    private static String destinationFile = DESTINATION_FILE;
    private static String additionalFile = ADDITIONAL_FILE;


    public enum TypeFilesEnum {
        source, destination, addition
    }

    //Что бы не дублировать код, см. процедуру setFileFromMenu
    private static final Map<String, String> typeFilesMap = new HashMap<>();

    static {
        typeFilesMap.put(TypeFilesEnum.source.toString(), "файл для шифрования");
        typeFilesMap.put(TypeFilesEnum.destination.toString(), "файл для расшифровки");
        typeFilesMap.put(TypeFilesEnum.addition.toString(), "дополнительный файл");
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


    static class InvalidKeyCrypt extends Exception {

    }


    //Отображение текцщих параметров шифрования-дешифрования
    public static void showParams() {
        System.out.println("ПАРАМЕТРЫ");
        System.out.println("Файл для Шифрования: " + sourceFile);
        System.out.println("Файл для расшифровки: " + destinationFile);
        System.out.println("Дополнительный файл: " + additionalFile);
        System.out.println("Криптографический ключ: " + getKey());
    }

    //Задание файлов используемых файлов
    public static void setFileFromMenu(TypeFilesEnum typeFilesEnum) {
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
        String value = typeFilesMap.get(typeFilesEnum.toString());
        System.out.print("Введите " + value + ": ");
        String filename = scanner1.nextLine();
        if (Files.notExists(Path.of(filename))) {
            System.out.println("Введен не существующий файл");
        } else {
            if (typeFilesEnum.toString().equalsIgnoreCase("source")) {
                Crypto.setSourceFile(filename);
            } else if (typeFilesEnum.toString().equalsIgnoreCase("distinatiion")) {
                Crypto.setDestinationFile(filename);
            } else if (typeFilesEnum.toString().equalsIgnoreCase("addition")) {
                Crypto.setAdditionalFile(filename);
            }
            Crypto.setSourceFile(filename);
        }
        //}

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

    //Процедура шифрования
    private static Character crypt(char ch, int key) throws InvalidKeyCrypt {
        int indexOf = ALPHABET_LIST.indexOf(ch);
        if (indexOf != -1) {

            //Если key>словаря обработать эту ситуациию
            int delta = (indexOf + key) % ALPHABET_LIST.size();
            if (key == 0) {
                throw new InvalidKeyCrypt();
            } else if (key < 0 && delta < 0) {
                delta = ALPHABET_LIST.size() + delta;
            }
            return ALPHABET_LIST.get(delta);
        }
        return ch;
    }

    //Процедура расшифровки строки
    private static String enCryptLine(String line, int key) throws InvalidKeyCrypt {
        char[] lineChar = line.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        //Warning:(166, 9) 'for' loop replaceable with enhanced 'for'
        //что это значит, как исправить?
        for (int i = 0; i < lineChar.length; i++) {
            stringBuilder.append(crypt(lineChar[i], -key));
        }
        return stringBuilder.toString();
    }

    //Процедура шифрования файла
    public static void cryptText(int p_key, String p_source_file, String p_distination_file) {
        if (Files.notExists(Path.of(p_source_file))) {
            System.out.println("Исходный файл не существует: " + p_source_file);
            return;
        }
        if (Files.notExists(Path.of(p_distination_file))) {
            System.out.println("Результирующий файл не существует: " + p_source_file);
            return;
        }


        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(p_source_file));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(p_distination_file))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                char[] strArr = line.toCharArray();
                for (int i = 0; i < strArr.length; i++) {
                    bufferedWriter.append(crypt(strArr[i], p_key));
                }
                bufferedWriter.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyCrypt invalidKeyCrypt) {
            System.out.println("Задан не верный ключ шифрования: " + p_key);
        }
    }


    public static void BruteForce(String p_filename) {
        if (Files.notExists(Path.of(p_filename))) {
            System.out.println("Передан не существующий файл для расшифровки: " + p_filename);
            return;
        }
        String line;
        Set<String> wordSet = new HashSet<>();
        Map<Integer, Integer> mapKey = new HashMap<>();
        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(p_filename), Charset.defaultCharset())) {
                //bufferedReader.mark(0); //т.к. не получилось воспользоваться, просто закомментировал
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
                //bufferedReader.reset();
                //bufferedReader.mark(0);


            } catch (IOException | InvalidKeyCrypt e) {
                e.printStackTrace();
            }
        }

        int mapMaxValue = 0;
        int map_Key = 0;


        for (Map.Entry<Integer, Integer> kvv : mapKey.entrySet()) {
            if (kvv.getValue() > mapMaxValue) {
                mapMaxValue = kvv.getValue();
                map_Key = kvv.getKey();
            }
        }
        System.out.printf("key=%d, cnt words = %d \n", map_Key, mapMaxValue);

        Crypto.cryptText(-map_Key, Crypto.getDestinationFile(), Crypto.getSourceFile());
    }

    //Получение отношения кол-ва гласных букв к количеству согласных возведенных во 2 степень
    private static double relationshipLetter(String file, int offset) {
        if (Files.notExists(Path.of(file))) {
            System.out.println("Файл не существует " + file);
            return 0;
        }
        try (BufferedReader bufferedReader = Files.newBufferedReader(Path.of(file), Charset.defaultCharset())) {
            int cntGl = 0;
            int cntSogl = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String newLine2;
                if (offset != 0) {
                    newLine2 = enCryptLine(line, -offset);
                } else {
                    newLine2 = line;
                }
                char[] arr = newLine2.toLowerCase().toCharArray();

                for (int i = 0; i < arr.length; i++) {
                    for (int k = 0; k < CONSONANT_LETTERS.length; k++) {
                        if (arr[i] == CONSONANT_LETTERS[k]) {
                            cntSogl++;
                            break;
                        }
                    }

                    for (int j = 0; j < LOWEL_LETTERS.length; j++) {
                        if (arr[i] == LOWEL_LETTERS[j]) {
                            cntGl++;
                            break;
                        }
                    }
                }
            }
            float t = (float) cntGl / cntSogl;
            return Math.pow(t, 2);
        } catch (InvalidKeyCrypt invalidKeyCrypt) {
            invalidKeyCrypt.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void staticAnaliz() {
        if (Files.notExists(Path.of(Crypto.additionalFile))) {
            System.out.println("Передан не существующий файл дополнительный файл: " + Crypto.additionalFile);
            return;
        }
        if (Files.notExists(Path.of(Crypto.getDestinationFile()))) {
            System.out.println("Передан не существующий файл для расшифровки: " + Crypto.getDestinationFile());
            return;
        }
        double minDeviation = 10; //Double.MAX_VALUE думаю избыточно
        //Получение
        double ishDeviation = Crypto.relationshipLetter(Crypto.additionalFile, 0);
        double curDeviation = Crypto.relationshipLetter(Crypto.getDestinationFile(), 0);

        System.out.printf("ish = %f, cur = %f\n", ishDeviation, curDeviation);
        double otkl = Math.abs(ishDeviation - curDeviation);
        int key = 0;

        for (int i = 0, j = 1; i < ALPHABET_LIST.size(); i++, j++) {
            double curDeviation2 = Crypto.relationshipLetter(Crypto.getDestinationFile(), j);
            double delta = Math.abs(curDeviation2 - otkl);
            if (minDeviation > delta) {
                minDeviation = delta;
                key = j;
            }
        }
        System.out.println("Вероятный ключ: " + key);
        //System.out.printf("j=%d, delta=%f\n", j, delta);
        Crypto.cryptText(-key, Crypto.getDestinationFile(), Crypto.getSourceFile());
    }
}
