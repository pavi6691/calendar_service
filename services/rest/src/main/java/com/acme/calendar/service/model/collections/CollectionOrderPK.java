package com.acme.calendar.service.model.collections;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionOrderPK implements Serializable {
    private UUID parentId;
    private UUID childId;
}
