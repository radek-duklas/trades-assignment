package com.gft.assignment.tradecapture.service;

import com.gft.assignment.tradecapture.model.TradeRecord;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JSONArrayIteratorFactoryTest {
    @Test
    public void shouldProduceIteratorThatProvidesCorrectNumberOfElements() throws URISyntaxException, IOException {
        URL jsonUrl = Objects.requireNonNull(this.getClass().getClassLoader().getResource("data/input.json"));
        JSONArrayIteratorFactory<TradeRecord> arrayIteratorFactory
                = new JSONArrayIteratorFactory<>(new File(jsonUrl.toURI()), TradeRecord.class);
        Stream<TradeRecord> stream = toStream(arrayIteratorFactory);
        Assertions.assertThat(stream).hasSize(3);
    }

    private Stream<TradeRecord> toStream(JSONArrayIteratorFactory<TradeRecord> arrayIteratorFactory) throws IOException {
        Iterator<TradeRecord> iterator = arrayIteratorFactory.iterator();
        Spliterator<TradeRecord> spliterator = Spliterators.spliteratorUnknownSize(iterator, 0);
        return StreamSupport.stream(spliterator, false);
    }
}
