package com.acme.calendar.service.serialzation;
import com.acme.calendar.core.CalendarConstants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(CalendarConstants.TIMESTAMP_ISO8601);

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getValueAsString();
        try {
            return ZonedDateTime.parse(dateString, FORMATTER);
        } catch (DateTimeException e) {
            throw new IOException("Failed to parse ZonedDateTime: " + dateString, e);
        }
    }
}

