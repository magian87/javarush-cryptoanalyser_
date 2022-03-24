package com.javarush.cryptoanalyser;

import java.util.Arrays;
import java.util.List;

public interface Constant {
    String SOURCE_FILE = "test/source.txt";
    String DESTINATION_FILE = "test/destination.txt";
    String ADDITIONAL_FILE = "test/add.txt";

    String ERR_STATIC_ANALYSIS_BY_LETTER = "дешифрование методом статистического анализа по отношению гласных букв к согласным не будет выполнено.";
    String ERR_STATIC_ANALYSIS_BY_WORDS = "дешифрование методом статистического анализа по вхождению слов не будет выполнено.";
    String ERR_BRUTE_FORCE = "дешифрование методом BruteFore не будет выполнено.";
    String ERR_ENCRYPTION = "шифрование не будет выполнено.";
    String ERR_DECRYPTION = "дешифрование не будет выполнено.";

    List<Character> ALPHABET_LIST = Arrays.asList('а', 'б', 'в',
            'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у',
            'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'я', '.', ',', '«', '»',
            ':', '!', '?', ' ');

    char[] LOWEL_LETTERS = {'а', 'у', 'о', 'ы', 'э', 'я', 'ю', 'ё', 'и', 'е'};
    char[] CONSONANT_LETTERS = {'б', 'в', 'г', 'д', 'ж', 'з', 'й', 'к', 'л', 'м', 'н', 'п', 'р', 'с', 'т', 'ф', 'х', 'ц', 'ч', 'ш', 'щ'};

}
