package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private WordleDictionary dictionary;
    private PrintWriter testLogger;

    @BeforeEach
    void beforeTests() {
        testLogger = new PrintWriter(System.out, true);
        List<String> mockWords = Arrays.asList("шалаш", "книга", "пирог", "герой", "силач");
        dictionary = new WordleDictionary(mockWords, testLogger);
    }

    @Test
    void testDictionaryNormalizationAndFiltering() {
        List<String> rawWords = Arrays.asList("Клёш ", "Арбуз", " пирог");
        WordleDictionary testDict = new WordleDictionary(rawWords, testLogger);
        assertTrue(testDict.contains("арбуз"));
        assertTrue(testDict.contains("клеш"));
        assertTrue(testDict.contains("АРБУЗ"));
    }

    @Test
    void testCheckAnswer() throws GameException {
        WordleGame game = new WordleGame("герой", dictionary, testLogger);
        String result = game.checkAnswer("книга");
        assertEquals("---^-", result);

        String winResult = game.checkAnswer("герой");
        assertEquals("+++++", winResult);
    }

    @Test
    void testWrongInputLength() {
        WordleGame game = new WordleGame("герой", dictionary, testLogger);
        try {
            game.checkAnswer("друг");
            fail("Должно было выбросить InputException из-за длины слова!");
        } catch (InputException e) {
        } catch (GameException e) {
            fail("Выброшено не то исключение! Ожидалось InputException.");
        }

        try {
            game.checkAnswer("молоко");
            fail("Должно было выбросить InputException из-за длины слова!");
        } catch (InputException e) {
        } catch (GameException e) {
            fail("Выброшено не то исключение! Ожидалось InputException.");
        }
    }

    @Test
    void testWordNotFoundException() {
        WordleGame game = new WordleGame("герой", dictionary, testLogger);
        try {
            game.checkAnswer("канат");
            fail("Должно было выбросить WordNotFoundInDictionary!");
        } catch (WordNotFoundInDictionary e) {
        } catch (GameException e) {
            fail("Выброшено базовое GameException вместо WordNotFoundInDictionary.");
        }
    }

    @Test
    void testStepLimit() throws GameException {
        WordleGame game = new WordleGame("герой", dictionary, testLogger);
        for (int i = 0; i < 6; i++) {
            game.checkAnswer("книга");
        }
        try {
            game.checkAnswer("книга");
            fail("Должно было выбросить GameException, так как попытки кончились!");
        } catch (GameException e) {
            assertFalse(e instanceof InputException, "Слово валидное, ошибка должна быть именно о нехватке ходов");
            assertEquals("Ходов больше нет!", e.getMessage());
        }
    }

    @Test
    void testAIWordsFiltersOutWrongLetters() throws GameException {
        WordleGame game = new WordleGame("короб", dictionary, testLogger);
        game.checkAnswer("пирог");
        String word = game.getAI();

        assertNotEquals("пирог", word);
        assertFalse(word.contains("п"));
        assertFalse(word.contains("и"));
        assertFalse(word.contains("г"));
    }
}