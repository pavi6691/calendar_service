package com.acme.calendar.service.model.collections;
import com.acme.calendar.service.exceptions.validations.UpdateValidationGroup;
import com.acme.calendar.service.model.IEntry;
import com.acme.calendar.service.model.calendar.CalendarMapping;
import com.acme.calendar.service.serialzation.ZonedDateTimeSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
@EqualsAndHashCode(exclude = {"items", "calendarMapping","collectionMappings"})
public class Collection implements IEntry<Collection> {
        
        @Transient
        String type = "collection";
        @Id
        @Column(name = "guid", nullable = false)
        @NotNull(groups = UpdateValidationGroup.class)
        UUID uuid;
        String title;
        String description;
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime createdInitially;
        @JsonSerialize(using = ZonedDateTimeSerializer.class)
        ZonedDateTime lastUpdatedTime;
        
        @Transient
        IEntry[] items;
        public IEntry[] getCollection(int size) {
            items = new IEntry[size];
            return items;
        }

        @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE}, orphanRemoval = true)
        @OrderBy("childOrder ASC")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Set<CalendarMapping> calendarMapping = new HashSet<>();
        
        @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE}, orphanRemoval = true)
        @OrderBy("childOrder ASC")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private Set<CollectionMapping> collectionMappings = new HashSet<>();
}
