package com.acme.calendar.service.model.collections;

import com.acme.calendar.service.model.IEntry;
import com.acme.calendar.service.model.calendar.CalendarMapping;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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
        
        @Transient
        String type = "collection";
        @Id
        @Column(name = "guid", nullable = false)
        UUID uuid;
        String title;
        String description;
        
        @Transient
        IEntry[] items;
        public IEntry[] getCollection(int size) {
            items = new IEntry[size];
            return items;
        }

        @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
        @OrderBy("childOrder ASC")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Set<CalendarMapping> calendarMapping = new HashSet<>();
        
        @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.MERGE, orphanRemoval = true)
        @OrderBy("childOrder ASC")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Set<CollectionMapping> collectionMappings = new HashSet<>();

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
