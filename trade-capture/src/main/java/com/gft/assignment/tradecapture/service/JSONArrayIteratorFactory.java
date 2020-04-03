package com.gft.assignment.tradecapture.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provides an {@link Iterator} for given JSON file assuming its root element is a JSON array.
 * @param <T>
 */
@Slf4j
public class JSONArrayIteratorFactory<T> {
    private JsonFactory factory;
    private File file;
    private Class<T> clazz;

    public JSONArrayIteratorFactory(File file, Class<T> clazz) {
        this.file = file;
        this.clazz = clazz;
        factory = new MappingJsonFactory();
    }

    /**
     * Return the entire read array as a {@link List}
     * @return a list containing all elements from JSON array
     */
    public List<T> asList() throws IOException {
        List<T> result = new ArrayList<>();
        iterator().forEachRemaining(result::add);
        return result;
    }

    /**
     * Provides an {@link Iterator} that allows iterating over JSON array.
     * @return an Iterator associated with JSON array
     */
    public Iterator<T> iterator() throws IOException {
        return new JSONParserIterator<>(initParser(), clazz);
    }

    private JsonParser initParser() throws IOException {
        JsonParser parser = factory.createParser(file);
        JsonToken current = parser.nextToken();
        if (current != JsonToken.START_ARRAY) {
            throw new IOException("Root element must be an array");
        }
        return parser;
    }

    static class JSONParserIterator<T> implements Iterator<T> {
        private JsonParser jsonParser;
        private Class<T> clazz;

        JSONParserIterator(JsonParser jsonParser, Class<T> clazz) {
            this.jsonParser = jsonParser;
            this.clazz = clazz;
        }

        @Override
        public boolean hasNext() {
            try {
                boolean result = jsonParser.nextToken() != JsonToken.END_ARRAY;
                if (!result) {
                    close();
                }
                return result;
            } catch (IOException e) {
                close();
                throw new RuntimeException("Error reading next JSON token", e);
            }
        }

        @Override
        public T next() {
            try {
                return jsonParser.readValueAs(clazz);
            } catch (IOException e) {
                close();
                throw new RuntimeException("Error reading current JSON object as " + clazz.getSimpleName(), e);
            }
        }

        public void close() {
            try {
                jsonParser.close();
            } catch (IOException e) {
                log.error("Problem occurred during JSonParser closing", e);
            }

        }
    }
}
