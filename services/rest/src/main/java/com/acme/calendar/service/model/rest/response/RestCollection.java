package com.acme.calendar.service.model.rest.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestCollection {
    String type = "collection";
    UUID uuid;
    String title;
    String description;
    Object[] items;
    public Object[] getCollection(int size) {
        items = new Object[size];
        return items;
    }
}
