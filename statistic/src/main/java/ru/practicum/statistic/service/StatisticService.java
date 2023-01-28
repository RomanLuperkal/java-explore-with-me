package ru.practicum.statistic.service;

import ru.practicum.statistic.dto.CreateEndpointHitDto;
import ru.practicum.statistic.dto.ListViewStats;
import ru.practicum.statistic.dto.ResponseEndpointHitDto;

import java.util.List;

public interface StatisticService {
    ResponseEndpointHitDto addEndpointHit(CreateEndpointHitDto createEndpointHitDto);

    ListViewStats getStats(String start, String end, List<String> uris, Boolean unique);
}
