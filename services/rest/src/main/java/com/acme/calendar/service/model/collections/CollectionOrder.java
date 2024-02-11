package com.acme.calendar.service.model.collections;
import com.acme.calendar.service.model.calendar.Calendar;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "collection_mapping")
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionOrder {
    
    @EmbeddedId
    private CollectionOrderPK id = new CollectionOrderPK();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("parentId")
    @JsonBackReference
    @JoinColumn(name = "parent_id")
    private Collection parent;

    @JoinColumn(name = "child_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("childId")
    private Collection child;

    @Column(name = "child_order")
    @JsonIgnore
    private int childOrder = -1;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionOrder)) return false;
        CollectionOrder that = (CollectionOrder) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
