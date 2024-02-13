package com.acme.calendar.service.utils;


import com.acme.calendar.service.model.calendar.Calendar;
import com.acme.calendar.service.model.collections.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {
    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);
    
    void copy(Collection from, @MappingTarget Collection entity);
    
    void copy(Calendar from, @MappingTarget Calendar entity);

    @Mapping(target = "events", ignore = true)
    void copyCalendarIgnoreEvents(Calendar from, @MappingTarget Calendar entity);
}
