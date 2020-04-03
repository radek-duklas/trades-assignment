package com.gft.assignment.tradecapture.converter;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gft.assignment.tradecapture.TestUtils;
import com.gft.assignment.tradecapture.model.TradeRecord;
import com.gft.assignment.tradecapture.model.TradeRecordProcessed;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class TradeRecordConverterTest {

    private TradeRecordConverter converter = new TradeRecordConverter();

    @Test
    void shouldConvertObjects() throws IOException {
        converter.init();

        MappingIterator<TradeRecord> objectMappingIterator = TestUtils.iteratorFromJsonArray(new ObjectMapper(), "data/input.json");

        Spliterator<TradeRecord> spliterator = Spliterators.spliteratorUnknownSize(objectMappingIterator, 0);

        List<TradeRecordProcessed> results = StreamSupport.stream(spliterator, false)
                .map(converter::convert)
                .collect(Collectors.toList());

        assertThat(results).allSatisfy(this::checkTradeRecordProcessed);

        //check if all timestamps are the same and close to current time
        Set<OffsetDateTime> timestamps = results.stream().map(TradeRecordProcessed::getReceivedTimestamp).collect(Collectors.toSet());
        assertThat(timestamps).hasSize(1);
        assertThat(timestamps.iterator().next()).isBefore(OffsetDateTime.now()).isAfter(OffsetDateTime.now().minusSeconds(5));
    }

    private void checkTradeRecordProcessed(TradeRecordProcessed record) {
        assertThat(record).hasNoNullFieldsOrProperties();
    }

}