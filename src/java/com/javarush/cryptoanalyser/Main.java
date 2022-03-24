package com.javarush.cryptoanalyser;

import com.javarush.cryptoanalyser.encryption.Encryption;
import com.javarush.cryptoanalyser.enums.TypeFiles;
import com.javarush.cryptoanalyser.exception.InvalidKeyCrypt;
import com.javarush.cryptoanalyser.Constant;

import java.io.IOException;
import java.util.Scanner;

import static com.javarush.cryptoanalyser.Constant.ERR_NOT_TRUTH_NUMBER_MENU;
import static com.javarush.cryptoanalyser.decryption.BruteForce.bruteForce;
import static com.javarush.cryptoanalyser.encryption.EncryptionUtils.encryptionText;
import static com.javarush.cryptoanalyser.decryption.StatisticalAnalysis.staticAnalysisByLetter;
import static com.javarush.cryptoanalyser.decryption.StatisticalAnalysis.staticAnalysisByWords;

public class Main {


    public static void main(String[] args) {
        Encryption encryption = new Encryption();

        Scanner scanner = new Scanner(System.in);
        int n = 0;
        do {
            System.out.println("1 посмотреть пути к файлам для шифрования\\расшифровки\\криптографический ключ");
            System.out.println("2 Задать файл для шифрования");
            System.out.println("3 Задать файл для расшифровки");
            System.out.println("4 Задать дополнительный файл");
            System.out.println("5 Задать криптографический ключ");
            System.out.println("6 Зашифровать по ключу");
            System.out.println("7 Расшифровать по ключу");
            System.out.println("8 Взлом (Brute Force)");
            System.out.println("9 Взлом (Статистический анализ по отношению гласных к согласным)");
            System.out.println("10 Взлом (Статистический анализ по вхождению слов (рабочий вариант))");
            System.out.println("11 Выход");

            System.out.print("Введите пункт меню: ");
            try {
                String str = scanner.nextLine();
                n = Integer.parseInt(str);
                System.out.println();

                if (!(1 <= n && n <= 11)) {
                    System.out.println(ERR_NOT_TRUTH_NUMBER_MENU);
                } else {
                    switch (n) {
                        case 1 -> encryption.showParams();
                        case 2 -> encryption.setFileFromMenu(TypeFiles.SOURCE);
                        case 3 -> encryption.setFileFromMenu(TypeFiles.DESTINATION);
                        case 4 -> encryption.setFileFromMenu(TypeFiles.ADDITION);
                        case 5 -> encryption.setKeyFromMenu();
                        case 6 -> encryptionText(encryption.getCryptographicKey(), encryption.getSourceFile(), encryption.getDestinationFile());
                        case 7 -> encryptionText(-encryption.getCryptographicKey(), encryption.getDestinationFile(), encryption.getSourceFile());
                        case 8 -> bruteForce(encryption.getDestinationFile(), encryption.getSourceFile());
                        case 9 -> staticAnalysisByLetter(encryption.getDestinationFile(), encryption.getAdditionalFile(), encryption.getSourceFile());
                        case 10 -> staticAnalysisByWords(encryption.getSourceFile(), encryption.getDestinationFile(), encryption.getAdditionalFile());
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println(ERR_NOT_TRUTH_NUMBER_MENU);
            } catch (InvalidKeyCrypt | IOException ex) {
                System.out.println(ex.getMessage());
            }
        } while (n != 11);
    }

}


