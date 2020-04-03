package com.gft.assignment.tradecapture.service;

import java.util.Iterator;

public interface DataCollectionConsumer<T> {
    void consumeNewData(Iterator<T> iterator) throws Exception;
    void consumeReplacingData(Iterator<T> iterator) throws Exception;
}
