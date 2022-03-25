package com.javarush.cryptoanalyser.encryption;

import com.javarush.cryptoanalyser.enums.TypeFiles;
import com.javarush.cryptoanalyser.exception.CustomNumberFormatException;

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


    //Задание используемых файлов
    public void setFileFromMenu(Scanner scanner, TypeFiles typeFiles) throws FileNotFoundException {
        String value = typeFilesMap.get(typeFiles);
        System.out.print("Введите " + value + ": ");
        String filename = scanner.nextLine();
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
    public void setKeyFromMenu(Scanner scanner) throws CustomNumberFormatException {
        System.out.print("Введите криптографический ключ: ");
        int key;
        try {
            key = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            throw new CustomNumberFormatException("ОШИБКА: криптографический ключ должен быть числом");
        }
        if (key <= 0) {
            System.out.println("Криптографический ключ должен быть больше 0");
        } else if (key >= ALPHABET_LIST.size()) {
            System.out.println("Криптографический ключ должен быть меньше либо равным количеству символов в алфамите: " + ALPHABET_LIST.size());
        }
        setCryptographicKey(key);
    }
}
