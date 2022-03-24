package com.javarush.cryptoanalyser;

import static com.javarush.cryptoanalyser.Constant.ALPHABET_LIST;

public class EncryptionUtils {
    //Процедура шифрования
    public static Character crypt(char ch, int key) throws InvalidKeyCrypt {
        int indexOf = ALPHABET_LIST.indexOf(ch);
        if (indexOf != -1) {

            //Если key>словаря обработать эту ситуациию
            int delta = (indexOf + key) % ALPHABET_LIST.size();
            if (key == 0) {
                throw new InvalidKeyCrypt("Задан не верный ключ шифрования: " + key);
            } else if (key < 0 && delta < 0) {
                delta = ALPHABET_LIST.size() + delta;
            }
            return ALPHABET_LIST.get(delta);
        }
        return ch;
    }

}
