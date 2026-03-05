package ru.samsebemehanik.catalog.exception;

public class ComponentNotFoundException extends RuntimeException {

    public ComponentNotFoundException(String message) {
        super(message);
    }
}
