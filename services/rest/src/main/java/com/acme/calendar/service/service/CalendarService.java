package com.acme.calendar.service.service;

import com.acme.calendar.core.util.LogUtil;
import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.Collection;
import com.acme.calendar.service.repository.PGCCalendarRepository;
import com.acme.calendar.service.repository.PGCollectionsRepository;
import com.acme.calendar.service.utils.DTOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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


    public Calendar create(Calendar calendar) {
        log.debug("{}", LogUtil.method());
        if(calendar.getUuid() != null) {
            calendar.setUuid(calendar.getUuid());
        } else {
            calendar.setUuid(UUID.randomUUID());
        }
        AtomicReference<Collection> nested = new AtomicReference<>();
        if(calendar.getMappings() != null) {
            calendar.getMappings().stream().forEach(pc -> {
                if(pc.getParent() != null) {
                    if(pc.getParent() != null) {
                        nested.set(pgCSetRepository.findById(pc.getParent().getUuid()).orElse(null));
                        if(nested.get() == null) {
                            return;
                        }
                        if(pc.getChildOrder() == -1) {
                            pc.setChildOrder(getOrder(pc.getParent().getUuid()));
                        }
                        pc.setCalendar(calendar);
                        nested.get().getCalendarMapping().add(pc);
                    }
                }
            });
        }
        if(nested.get() != null && !nested.get().getCollectionMappings().isEmpty()) {
            pgCSetRepository.save(nested.get());
        } else {
            pgCCalendarRepository.save(calendar);
        }
        return getByUuid(calendar.getUuid());
    }
    
    public List<Calendar> getAll(Pageable pageable, Sort sort) {
        log.debug("{}", LogUtil.method());
        List<Calendar> results = pgCCalendarRepository.findAll();
        List<Calendar> refined = new ArrayList<>();
        if(results != null) {
            results.stream().forEach(r -> {
                Calendar calendar = new Calendar();
                DTOMapper.INSTANCE.copyCalendarIgnoreEvents(r,calendar);
                refined.add(calendar);
            });
        }
        return refined;
    }

    public Calendar getByUuid(UUID uuid) {
        log.debug("{}", LogUtil.method());
        return pgCCalendarRepository.findById(uuid).orElse(null);
    }

    public Calendar update(Calendar cCalendar) {
        log.debug("{}", LogUtil.method());
        return pgCCalendarRepository.save(cCalendar);
    }

    public void delete(List<UUID> cCalendars) {
        log.debug("{}", LogUtil.method());
        pgCCalendarRepository.deleteAllById(cCalendars);
    }

}
