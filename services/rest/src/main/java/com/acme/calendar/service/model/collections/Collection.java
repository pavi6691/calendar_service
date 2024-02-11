package com.acme.calendar.service.model.collections;

import com.acme.calendar.service.model.IEntry;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.calendar.CalendarMapping;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Table(name = "collections")
@Entity(name = "collection")
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Collection implements IEntry<Collection> {
        @Id
        @Column(name = "guid", nullable = false)
        UUID uuid;
        String title;
        String description;

        @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @OrderBy("childOrder ASC")
        private Set<CalendarMapping> calendarMapping = new HashSet<>();
        
        @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        @OrderBy("childOrder ASC")
        private Set<CollectionOrder> mappings = new HashSet<>();

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Collection that)) return false;
                return Objects.equals(getUuid(), that.getUuid());
        }

        @Override
        public int hashCode() {
                return Objects.hash(getUuid());
        }
}
