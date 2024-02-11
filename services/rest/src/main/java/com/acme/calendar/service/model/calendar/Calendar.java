package com.acme.calendar.service.model.calendar;
import com.acme.calendar.service.model.IEntry;
import com.acme.calendar.service.model.collections.CollectionOrder;
import com.acme.calendar.service.model.event.Event;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

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
        
        @OneToMany(mappedBy = "calendar", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @JsonManagedReference(value="event-mapping")
        List<Event> events;

        @OneToMany(mappedBy = "calendar", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @OrderBy("childOrder ASC")
        @JsonManagedReference(value="calendar-mapping")
        @JsonIgnore
        private Set<CalendarMapping> mappings = new HashSet<>();
} 
