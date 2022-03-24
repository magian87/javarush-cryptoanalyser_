package com.javarush.cryptoanalyser;

import java.io.*;
import java.nio.file.Path;

import static com.javarush.cryptoanalyser.CommonUtils.existFile;
import static com.javarush.cryptoanalyser.Constant.*;

public class EncryptionUtils {

    //Процедура шифрования\дешифрования символа
    public static Character encryptionChar(char ch, int offset) throws InvalidKeyCrypt {
        int indexOf = ALPHABET_LIST.indexOf(ch);
        if (indexOf != -1) {

            //Если offset>словаря обработать эту ситуациию
            int delta = (indexOf + offset) % ALPHABET_LIST.size();
            if (offset == 0) {
                throw new InvalidKeyCrypt("Задан не верный ключ шифрования: " + offset);
            } else if (offset < 0 && delta < 0) {
                delta = ALPHABET_LIST.size() + delta;
            }
            return ALPHABET_LIST.get(delta);
        }
        return ch;
    }

    //Процедура шифрования\дешифрования строки
    public static String encryptionLine(String line, int offset) throws InvalidKeyCrypt {
        char[] lineChar = line.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : lineChar) {
            stringBuilder.append(encryptionChar(c, offset));
        }
        return stringBuilder.toString();
    }

    //Процедура шифрования\дешифрования текста
    public static void encryptionText(int offset, String sourceFile, String destinationFile) throws IOException, InvalidKeyCrypt {
        if (offset>0) {
            existFile(sourceFile, ERR_ENCRYPTION);
        } else {
            existFile(destinationFile, ERR_DECRYPTION);
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceFile));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destinationFile))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(encryptionLine(line,offset)+"\n");
            }
        } catch (IOException e) {
            throw new IOException("Произошла ошибка при работе с файлом\\файллами: "
                    + Path.of(sourceFile).getFileName() + ", "
                    + Path.of(destinationFile).getFileName());
        }
        if (offset>0) {
            System.out.println("Файл был успешно зашифрован в файл: "+ destinationFile);
        } else {
            System.out.println("Файл был успешно дешифрован в файл: "+ destinationFile);
        }
    }

}
