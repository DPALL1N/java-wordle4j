package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    public WordleDictionary loadDictionary(Path path, PrintWriter printWriter) throws DictionaryLoadingException {
        List<String> dictionary = new ArrayList<>();
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                dictionary.add(line);
            }
            if (dictionary.isEmpty()) {
                throw  new DictionaryLoadingException("Файл словаря пустой или не имеет подходящих слов!");
            }
        } catch (IOException e) {
            e.printStackTrace(printWriter);
            throw new DictionaryLoadingException(e.getMessage(), e);
        }
        return new WordleDictionary(dictionary, printWriter);
    }
}
