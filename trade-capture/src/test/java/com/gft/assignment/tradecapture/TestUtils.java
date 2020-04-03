package com.gft.assignment.tradecapture;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gft.assignment.tradecapture.model.TradeRecord;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class TestUtils {
    public static MappingIterator<TradeRecord> iteratorFromJsonArray(ObjectMapper objectMapper, String filePath) throws IOException {
        ObjectReader reader = objectMapper.readerFor(TradeRecord.class);
        URL jsonUrl = Objects.requireNonNull(TestUtils.class.getClassLoader().getResource(filePath));
        return reader.readValues(jsonUrl);
    }
}
