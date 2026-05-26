package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;

    private int steps;

    private WordleDictionary dictionary;

    private final PrintWriter logWriter;

    private final List<Character> correctLetters = new ArrayList<>();
    private final List<Character> wrongLetters = new ArrayList<>();

    public WordleGame(String answer, WordleDictionary dictionary, PrintWriter logWriter) {
        this.answer = answer.toLowerCase().replace('ё', 'е').trim();
        this.steps = 0;
        this.dictionary = dictionary;
        this.logWriter = logWriter;
        logWriter.println("Игра запущена. Заганное слово: " + this.answer);
    }

    private String validateGuess(String guess) throws InputException {
        if (guess == null) {
            throw new InputException("Строка не может быть null!");
        }

        String clean = guess.toLowerCase().replace('ё', 'е').trim();

        if (clean.length() != 5) {
            throw new InputException("Слово должно состоять из 5 букв!");
        }
        if (!dictionary.contains(clean)) {
            throw new WordNotFoundInDictionary("Слова \"" + clean + "\" нет в словаре!");
        }
        return clean;
    }


    public String checkAnswer(String guess) throws GameException {
        logWriter.println("Проверка ответа. Шаг: " + steps);

        if (steps >= 6) {
            throw new GameException("Ходов больше нет!");
        }

        String guessWord = validateGuess(guess);
        steps++;


        char[] guessChars = guessWord.toCharArray();
        char[] answerChars = answer.toCharArray();
        char[] resultChars = new char[answerChars.length];

        for (int i = 0; i < answerChars.length; i++) {
            if (guessChars[i] == answerChars[i]) {
                resultChars[i] = '+';
                answerChars[i] = '+';
                correctLetters.add(guessChars[i]);
            }
        }

        for (int i = 0; i < resultChars.length; i++) {
            if (resultChars[i] == 0) {
                for (int j = 0; j < answerChars.length; j++) {
                    if (guessChars[i] == answerChars[j]) {
                        resultChars[i] = '^';
                        answerChars[j] = '^';
                        correctLetters.add(guessChars[i]);
                        break;
                    }
                }
                if (resultChars[i] == 0) {
                    resultChars[i] = '-';
                    wrongLetters.add(guessChars[i]);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (char c : resultChars) {
            sb.append(c);
        }
        return sb.toString();
    }

    public String getAI() {
        logWriter.println("Компьютер подбирает подсказку через фильтрацию циклом...");

        List<String> allWords = dictionary.getAllWords();
        List<String> matchingWords = new ArrayList<>();

        for (String word : allWords) {
            if (word.equals(answer)) {
                continue;
            }

            if (containsAllCorrect(word) && containsNoWrong(word)) {
                matchingWords.add(word);
            }
        }

        if (matchingWords.isEmpty()) {
            return dictionary.getWord();
        }

        return matchingWords.get(new Random().nextInt(matchingWords.size()));
    }

    private boolean containsAllCorrect(String word) {
        for (char c : correctLetters) {
            if (word.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    private boolean containsNoWrong(String word) {
        for (char c : wrongLetters) {
            if (correctLetters.contains(c)) {
                continue;
            }
            if (word.indexOf(c) != -1) {
                return false;
            }
        }
        return true;
    }
}


