package com.acme.calendar.service.base;
import com.acme.calendar.service.model.IEntry;
import com.acme.calendar.service.repository.PGCCalendarRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import static org.junit.jupiter.api.Assertions.*;
public abstract class AbstractBaseTest<E extends IEntry<E>> extends TestContainers {
    protected JpaRepository jpaRepository;
    protected AbstractBaseTest(PGCCalendarRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
        try {
            if(testContainers) {
                jpaRepository.deleteAll();
            }
        } catch (Exception e) {}
    }
//    protected IEntry createNewEntry(E entry) {
//        IEntry entryCreated = (E) jpaRepository.save(entry);
//        assertEquals(entryCreated.collections(), entryCreated.collections());
//        assertEquals(entryCreated.title(), entryCreated.title());
//        return entryCreated;
//    }

}
