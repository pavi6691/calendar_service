package com.acme.calendar.service.model.calendar;

import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.collections.MappingPK;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "calendar_mapping")
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarMapping {

    @EmbeddedId
    private MappingPK id = new MappingPK();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("parentId")
    @JoinColumn(name = "parent_id")
    @JsonBackReference()
    private Collection parent;

    @JoinColumn(name = "child_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @MapsId("childId")
    @JsonBackReference(value="calendar-mapping")
    private Calendar calendar;

    @Column(name = "child_order")
    @JsonIgnore
    private int childOrder = -1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarMapping that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
