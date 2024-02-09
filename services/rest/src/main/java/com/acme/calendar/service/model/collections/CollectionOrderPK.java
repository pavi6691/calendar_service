package com.acme.calendar.service.model.collections;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.util.Objects;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectionOrderPK)) return false;
        CollectionOrderPK that = (CollectionOrderPK) o;
        return Objects.equals(getParentId(), that.getParentId()) &&
                Objects.equals(getChildId(), that.getChildId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getParentId(), getChildId());
    }
    
}
