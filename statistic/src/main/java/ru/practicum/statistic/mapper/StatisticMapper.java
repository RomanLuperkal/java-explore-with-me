package ru.practicum.statistic.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.statistic.dto.CreateEndpointHitDto;
import ru.practicum.statistic.dto.ResponseEndpointHitDto;
import ru.practicum.statistic.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface StatisticMapper {

    @Mapping(source = "timestamp", target = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EndpointHit mapToEndpointHit(CreateEndpointHitDto createEndpointHitDto);

    @Mapping(source = "timestamp", target = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ResponseEndpointHitDto mapToResponseEndpointHitDto(EndpointHit endpointHit);
}
