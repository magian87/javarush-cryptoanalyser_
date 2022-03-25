package com.javarush.cryptoanalyser;

import java.util.Arrays;
import java.util.List;

public class Constant {
    public static final String SOURCE_FILE = "files/source.txt";
    public static final String DESTINATION_FILE = "files/destination.txt";
    public static final String ADDITIONAL_FILE = "files/add.txt";

    public static final String ERR_NOT_TRUTH_NUMBER_MENU = "Не верный пункт меню, введите корректное число";
    public static final String ERR_STATIC_ANALYSIS_BY_LETTER = "дешифрование методом статистического анализа по отношению гласных букв к согласным не будет выполнено.";
    public static final String ERR_STATIC_ANALYSIS_BY_WORDS = "дешифрование методом статистического анализа по вхождению слов не будет выполнено.";
    public static final String ERR_BRUTE_FORCE = "дешифрование методом BruteFore не будет выполнено.";
    public static final String ERR_ENCRYPTION = "шифрование не будет выполнено.";
    public static final String ERR_DECRYPTION = "дешифрование не будет выполнено.";

    public static final List<Character> ALPHABET_LIST = Arrays.asList('а', 'б', 'в',
            'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»',
            ':', '!', '?', ' ');

    public static final List<Character> PUNCTUATION_MARKS = Arrays.asList('.', ',', ':', ';', '?', '!', ')', '(', '«', '»', '"', '-');


    public static final char[] VOWEL_LETTERS = {'а', 'у', 'о', 'ы', 'э', 'я', 'ю', 'ё', 'и', 'е'};
    public static final char[] CONSONANT_LETTERS = {'б', 'в', 'г', 'д', 'ж', 'з', 'й', 'к', 'л', 'м', 'н', 'п', 'р', 'с', 'т', 'ф', 'х', 'ц', 'ч', 'ш', 'щ'};

}
