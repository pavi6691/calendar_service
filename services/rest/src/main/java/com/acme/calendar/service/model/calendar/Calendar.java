package com.acme.calendar.service.model.calendar;
import com.acme.calendar.service.model.IEntry;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.event.Event;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
        @JsonManagedReference
        List<Event> events;
        
        @JoinColumn(name = "collection_guid")
        @ManyToOne
        @JsonBackReference
        Collection collection;

        @Column(name = "order_seq")
        @JsonIgnore
        int order;
} 
