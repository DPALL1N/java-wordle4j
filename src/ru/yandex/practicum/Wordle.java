package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        Path dictionaryPath = Paths.get("words_ru.txt");
        try (PrintWriter logWriter = new PrintWriter(new FileWriter("game.log", true))) {
            logWriter.println("Старт игры");

            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary;

            try {
                dictionary = loader.loadDictionary(dictionaryPath, logWriter);
            } catch (DictionaryLoadingException e) {
                System.out.println("Не удалось загрузить словарь. Проверьте game.log");
                return;
            }

            String secretWord = dictionary.getWord();

            WordleGame game = new WordleGame(secretWord, dictionary, logWriter);

            System.out.println("Wordle! Загадано слово из 5 букв. У вас 6 попыток.");

            Scanner scanner = new Scanner(System.in);
            boolean isWon = false;

            while (true) {
                try {
                    System.out.print("Введите ваше слово: ");
                    String guess = scanner.nextLine();

                    // ДОБАВЛЕНО: Обработка автоматического поиска подсказок при пустом вводе (ТЗ)
                    if (guess == null || guess.trim().isEmpty()) {
                        String aiWord = game.getAI();
                        System.out.println("Компьютер предлагает слово: " + aiWord);
                        guess = aiWord; // Подставляем подсказку компьютера как ход игрока
                    }

                    logWriter.println(guess);
                    logWriter.flush(); // Выталкиваем данные в файл сразу, чтобы логи не терялись

                    String result = game.checkAnswer(guess);
                    System.out.println("Результат проверки: " + result);

                    if (result.equals("+++++")) {
                        isWon = true;
                        break;
                    }
                } catch (InputException e) {
                    System.out.println("Ошибка ввода: " + e.getMessage());
                    logWriter.println("Перехвачена ошибка ввода: " + e.getMessage());
                    logWriter.flush();
                } catch (GameException e) {
                    logWriter.println("Перехвачена игровая ошибка: " + e.getMessage());
                    logWriter.flush();
                    break;
                }
            }
            System.out.println("Конец игры!");
            if (isWon) {
                System.out.println("Поздравляем! Вы победили!");
                logWriter.println("Итог: Победа игрока.");
            } else {
                System.out.println("Вы проиграли! Попытки исчерпаны.");
                System.out.println("Было загадано слово: " + secretWord);
                logWriter.println("Итог: Поражение игрока. Загаданное слово: " + secretWord);
            }
            logWriter.println("Конец игры");
            logWriter.flush();
        } catch (IOException e) {
            System.out.println("Не удалось создать или открыть файл логов game.log: " + e.getMessage());
        }

    }

}
