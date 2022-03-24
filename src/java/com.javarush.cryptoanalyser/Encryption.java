package com.javarush.cryptoanalyser;

import java.io.*;
import java.nio.file.Path;
import java.util.*;

import static com.javarush.cryptoanalyser.CommonUtils.existFile;
import static com.javarush.cryptoanalyser.Constant.*;




/*
Каждый метод и каждый класс должны иметь одну ответсвенность
Класс Crypto где есть параметры, не должен быть статическим
* */

public class Encryption {

    private final int CRYPTOGRAPHIC_KEY_DEFAULT = 15;

    //Ключ используемый для шифрования
    private int cryptographicKey;
    //Исходный файл, файл зашифрованный, доп. файл того же автора
    private String sourceFile;
    private String destinationFile;
    private String additionalFile;


    //Что бы не дублировать код, см. процедуру setFileFromMenu
    private final Map<TypeFiles, String> typeFilesMap = new HashMap<>();

     {
        setCryptographicKey(CRYPTOGRAPHIC_KEY_DEFAULT);

        typeFilesMap.put(TypeFiles.SOURCE, "файл для шифрования");
        typeFilesMap.put(TypeFiles.DESTINATION, "файл для расшифровки");
        typeFilesMap.put(TypeFiles.ADDITION, "дополнительный файл");

        setSourceFile(Path.of("").toAbsolutePath().resolve(Path.of(SOURCE_FILE)).toString());
        setDestinationFile(Path.of("").toAbsolutePath().resolve(Path.of(DESTINATION_FILE)).toString());
        setAdditionalFile(Path.of("").toAbsolutePath().resolve(Path.of(ADDITIONAL_FILE)).toString());


    }

    public int getCryptographicKey() {
        return cryptographicKey;
    }

    public void setCryptographicKey(int cryptographicKey) {
        this.cryptographicKey = cryptographicKey;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getDestinationFile() {
        return destinationFile;
    }

    public void setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    public String getAdditionalFile() {
        return additionalFile;
    }

    public void setAdditionalFile(String additionalFile) {
        this.additionalFile = additionalFile;
    }


    private String generateParams() {
        return "ПАРАМЕТРЫ" + "\n"
                + "Файл для Шифрования: " + this.getSourceFile() + "\n"
                + "Файл для расшифровки: " + this.getDestinationFile() + "\n"
                + "Дополнительный файл: " + this.getAdditionalFile() + "\n"
                + "Криптографический ключ: " + this.getCryptographicKey();

    }

    //Отображение текцщих параметров шифрования-дешифрования
    public void showParams() {
        System.out.println(generateParams());
    }


    //Задание файлов используемых файлов
    public void setFileFromMenu(TypeFiles typeFiles) throws FileNotFoundException {
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
            setSourceFile(filename);
        } else if (typeFiles == TypeFiles.DESTINATION) {
            setDestinationFile(filename);
        } else if (typeFiles == TypeFiles.ADDITION) {
            setAdditionalFile(filename);
        }
        setSourceFile(filename);

    }

    //задание криптографического ключа
    public void setKeyFromMenu() {
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

        setCryptographicKey(key);
        //}
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
