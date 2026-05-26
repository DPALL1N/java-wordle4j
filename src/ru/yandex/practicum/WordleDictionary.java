package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;
    private final Random random = new Random();
    private final PrintWriter logWriter;

    public WordleDictionary(List<String> words, PrintWriter logWriter) {
        this.logWriter = logWriter;
        this.words = new ArrayList<>();
        for (String word : words) {
            if (word.length() == 5) {
                this.words.add(word.toLowerCase().replace('ё', 'е').trim());
            }
        }
        logWriter.println("Словарь успешно создан. Количество слов: " + this.words.size());
    }

    public boolean contains(String guess) {
        if (guess == null) {
            return false;
        }
        String cleanGuess = guess.toLowerCase().replace('ё', 'е').trim();
        return this.words.contains(cleanGuess);
    }

    public String getWord() {
        if (this.words.isEmpty()) {
            return "";
        }
        return  this.words.get(random.nextInt(this.words.size()));
    }

    public List<String> getAllWords() {
        return new ArrayList<>(this.words);
    }
}
