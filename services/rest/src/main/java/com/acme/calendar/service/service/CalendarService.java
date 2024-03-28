package com.acme.calendar.service.service;

import com.acme.calendar.core.enums.CalendarAPIError;
import com.acme.calendar.core.util.LogUtil;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.model.rest.responses.CalendarResponse;
import com.acme.calendar.service.repository.PGCCalendarRepository;
import com.acme.calendar.service.repository.PGCollectionsRepository;
import com.acme.calendar.service.utils.DTOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.acme.calendar.service.utils.ExceptionUtil.newRestError;
import static com.acme.calendar.service.utils.ExceptionUtil.throwRestError;

@Slf4j
@Service
public class CalendarService extends AbstractService {

    PGCollectionsRepository pgCSetRepository;
    PGCCalendarRepository pgCCalendarRepository;


    @Autowired
    public CalendarService(PGCCalendarRepository pgCCalendarRepository, PGCollectionsRepository pgCSetRepository) {
        this.pgCCalendarRepository = pgCCalendarRepository;
        this.pgCSetRepository = pgCSetRepository;
    }


    public CalendarResponse create(Calendar calendar) {
        try {
            log.debug("{}", LogUtil.method());
            build(calendar);
            AtomicReference<Collection> nested = new AtomicReference<>();
            if (calendar.getMappings() != null) {
                calendar.getMappings().stream().forEach(pc -> {
                    if (pc.getParent() != null) {
                        if (pc.getParent() != null) {
                            nested.set(pgCSetRepository.findById(pc.getParent().getUuid()).orElse(null));
                            if (nested.get() == null) {
                                throwRestError(CalendarAPIError.ERROR_COLLECTION_NOT_FOUND,pc.getParent().getUuid());
                                return;
                            }
                            if (pc.getChildOrder() == -1) {
                                pc.setChildOrder(getOrder(pc.getParent().getUuid()));
                            }
                            pc.setCalendar(calendar);
                            nested.get().getCalendarMapping().add(pc);
                        }
                    }
                });
            }
            if (nested.get() != null && !nested.get().getCollectionMappings().isEmpty()) {
                pgCSetRepository.save(nested.get());
            } else {
                pgCCalendarRepository.save(calendar);
            }
            return getByUuid(calendar.getUuid(),false);
        } catch (Exception e) {
            throwRestError(CalendarAPIError.INTERNAL_SERVER_ERROR_SERVER,e.getMessage());
            return null;
        }
    }
    public List<CalendarResponse> getAll(Pageable pageable, Sort sort, boolean includeEvents) {
        log.debug("{}", LogUtil.method());
        List<Calendar> results = pgCCalendarRepository.findAll();
        List<CalendarResponse> refined = new ArrayList<>();
        if(results != null) {
            results.stream().forEach(r -> {
                if(includeEvents) {
                    refined.add(DTOMapper.INSTANCE.toCalendarResponseWithEvents(r));
                } else {
                    refined.add(DTOMapper.INSTANCE.toCalendarResponseWithoutEvents(r));
                }
            });
        }
        return refined;
    }

    public CalendarResponse getByUuid(UUID uuid, boolean includeEvents) {
        log.debug("{}", LogUtil.method());
        Calendar calendar = pgCCalendarRepository.findById(uuid).orElse(null);
        if(includeEvents) {
            return DTOMapper.INSTANCE.toCalendarResponseWithEvents(calendar);
        } else {
            return DTOMapper.INSTANCE.toCalendarResponseWithoutEvents(calendar);
        }
    }

    public CalendarResponse update(Calendar updatedCalendar) throws Exception {
        log.debug("{}", LogUtil.method());
        if(updatedCalendar.getLastUpdatedTime() == null) {
            throwRestError(CalendarAPIError.ERROR_ENTRY_HAS_NO_MODIFIED_DATE);
        }
        Calendar existingCalendar = pgCCalendarRepository
                .findById(updatedCalendar.getUuid())
                .orElseThrow(() -> newRestError(CalendarAPIError.ERROR_NOT_EXISTS_UUID, updatedCalendar.getUuid()));
        if(!existingCalendar.getLastUpdatedTime().equals(updatedCalendar.getLastUpdatedTime())) {
            throwRestError(CalendarAPIError.ERROR_ENTRY_HAS_BEEN_MODIFIED,existingCalendar.getUuid(), existingCalendar.getLastUpdatedTime());
        }
        existingCalendar.setTitle(updatedCalendar.getTitle());
        existingCalendar.setDescription(updatedCalendar.getDescription());
        existingCalendar.setLastUpdatedTime(ZonedDateTime.now());
        return DTOMapper.INSTANCE.toCalendarResponseWithoutEvents(pgCCalendarRepository.save(existingCalendar));
    }

    public void delete(List<UUID> cCalendars) {
        log.debug("{}", LogUtil.method());
        pgCCalendarRepository.deleteAllById(cCalendars);
    }

}
