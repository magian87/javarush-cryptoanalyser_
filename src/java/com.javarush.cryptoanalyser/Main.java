package com.javarush.cryptoanalyser;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private final static String NOT_TRUTH_NUMBER_MENU = "Не верный пункт меню, введите корректное число";

    public static void main(String[] args) {
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
                    System.out.println(NOT_TRUTH_NUMBER_MENU);
                } else {
                    switch (n) {
                        case 1 -> Crypto.showParams();
                        case 2 -> Crypto.setFileFromMenu(TypeFiles.SOURCE);

                        //Crypto.typeFiles.put("d","dd");
                        //Он Map же final, почему можно добавлять элементы?
                        case 3 -> Crypto.setFileFromMenu(TypeFiles.DESTINATION);
                        case 4 -> Crypto.setFileFromMenu(TypeFiles.ADDITION);
                        case 5 -> Crypto.setKeyFromMenu();
                        case 6 -> Crypto.cryptText(Crypto.getKey(), Crypto.getSourceFile(), Crypto.getDestinationFile());
                        case 7 -> Crypto.cryptText(-Crypto.getKey(), Crypto.getDestinationFile(), Crypto.getSourceFile());
                        case 8 -> Crypto.bruteForce(Crypto.getDestinationFile());
                        case 9 -> Crypto.staticAnalizByLetter();
                        case 10 -> Crypto.staticAnalizByWords();
                    }

                }
            } catch (NumberFormatException e) {
                System.out.println(NOT_TRUTH_NUMBER_MENU);
            } catch (InvalidKeyCrypt | IOException ex) {
                System.out.println(ex.getMessage());
            }
        } while (n != 11);
    }

}


