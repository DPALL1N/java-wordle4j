package ru.yandex.practicum;

import java.io.IOException;

public class DictionaryLoadingException extends IOException {
    public DictionaryLoadingException(String message) {
        super(message);
    }

    public DictionaryLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
