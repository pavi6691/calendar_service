package com.acme.calendar.service.model.event;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.serialzation.ZonedDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Table(name = "events")
@Entity(name = "events")
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    @Id
    @Column(name = "guid", nullable = false)
    UUID uuid;
    @Column(nullable = false)
    String title;
    @Column(nullable = false)
    String description;
    @Column(nullable = false)
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    ZonedDateTime startTime;
    @Column(nullable = false)
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    ZonedDateTime endTime;
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    ZonedDateTime createdInitially;
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    ZonedDateTime lastUpdatedTime;
    @Column(columnDefinition = "TEXT")
    String rrule;

    @ManyToOne()
    @JoinColumn(name = "calendar_guid", nullable = false)
    @JsonBackReference(value="event-mapping")
    Calendar calendar;
}
