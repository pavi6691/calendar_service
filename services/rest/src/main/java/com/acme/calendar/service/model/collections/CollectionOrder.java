package com.acme.calendar.service.model.collections;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "collection_order")
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

}
