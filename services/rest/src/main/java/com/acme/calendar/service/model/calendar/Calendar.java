package com.acme.calendar.service.model.calendar;
import com.acme.calendar.service.model.IEntry;
import com.acme.calendar.service.model.event.Event;
import com.acme.calendar.service.serialzation.ZonedDateTimeSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "calendars")
@Entity
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(exclude = {"events", "mappings"})
public class Calendar implements IEntry<Calendar> {
        @Transient
        String type = "calendar";
        @Id
        @Column(name = "guid", nullable = false)
        UUID uuid;
        @Column(nullable = false)
        String title;
        @Column(nullable = false)
        String description;
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime createdInitially;
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime lastUpdatedTime;
        
        @OneToMany(mappedBy = "calendar", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
        @JsonManagedReference(value="event-mapping")
        List<Event> events;

        @OneToMany(mappedBy = "calendar", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
        @OrderBy("childOrder ASC")
        @JsonManagedReference(value="calendar-mapping")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Set<CalendarMapping> mappings = new HashSet<>();
} 
