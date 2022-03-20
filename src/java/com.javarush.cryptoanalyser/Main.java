package com.javarush.cryptoanalyser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private final static String NOT_TRUTH_NUMBER_MENU = "Не верный пункт меню, введите корректное число";

    public static void main(String[] args) {
        Crypto.setKey(15);

        Scanner scanner = new Scanner(System.in);
        int n =0;
        do {
            System.out.println("1 посмотреть пути к файлам для шифрования\\расшифровки\\криптографический ключ");
            System.out.println("2 Задать файл для шифрования");
            System.out.println("3 Задать файл для расшифровки");
            System.out.println("4 Задать дополнительный файл");
            System.out.println("5 Задать криптографический ключ");
            System.out.println("6 Зашифровать по ключу");
            System.out.println("7 Расшифровать по ключу");
            System.out.println("8 Взлом (Brute Force)");
            System.out.println("9 Взлом (Статистический анализ)");
            System.out.println("10 Выход");

            System.out.print("Введите пункт меню: ");
            try {
                String str = scanner.nextLine();
                n = Integer.parseInt(str);
                System.out.println();

                if (!(1 <= n && n <= 10)) {
                    System.out.println(NOT_TRUTH_NUMBER_MENU);
                } else {
                    switch (n) {
                        case 1:
                            Crypto.showParams();
                            break;
                        case 2:
                            Crypto.setFileFromMenu(Crypto.TypeFilesEnum.source);
                            //Crypto.typeFiles.put("d","dd");
                            //Он Map же final, почему можно добавлять элементы?
                            break;
                        case 3:
                            Crypto.setFileFromMenu(Crypto.TypeFilesEnum.destination);
                            break;
                        case 4:
                            Crypto.setFileFromMenu(Crypto.TypeFilesEnum.addition);
                            break;
                        case 5:
                            Crypto.setKeyFromMenu();
                            break;
                        case 6:
                            Crypto.cryptText(Crypto.getKey(), Crypto.getSourceFile(), Crypto.getDestinationFile());
                            break;
                        case 7:
                            Crypto.cryptText(-Crypto.getKey(), Crypto.getDestinationFile(), Crypto.getSourceFile() );
                            break;
                        case 8:
                            Crypto.BruteForce(Crypto.getDestinationFile());
                            break;
                        case 9:
                            Crypto.staticAnaliz();
                            break;
                    }

                }
            } catch (NumberFormatException e) {
                System.out.println(NOT_TRUTH_NUMBER_MENU);
            }
        } while (n != 10);
    }

}


