package com.gft.assignment.tradecapture.converter;

/**
 * Stateful converter for converting collections of data one by one.
 */
public interface Converter<S, T> {
    void init();

    T convert(S source);
}
