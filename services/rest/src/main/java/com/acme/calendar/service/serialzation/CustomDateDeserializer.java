package com.acme.calendar.service.serialzation;

import com.acme.calendar.core.CalendarConstants;
import com.acme.calendar.core.enums.CalendarAPIError;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.acme.calendar.service.utils.ExceptionUtil.throwRestError;


public class CustomDateDeserializer extends JsonDeserializer<Date> {

    private final SimpleDateFormat formatter = new SimpleDateFormat(CalendarConstants.TIMESTAMP_ISO8601);


    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        if (StringUtils.isEmpty(jsonParser.getText())) {
            return null;
        }
        String dateStr = jsonParser.getText();
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            throwRestError(CalendarAPIError.ERROR_PARSING_TIMESTAMP, dateStr);
        }
        throwRestError(CalendarAPIError.ERROR_SERVER, "could not deserialize date: " + dateStr);
        return null;
    }

}