package ru.practicum.statistic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistic.dto.CreateEndpointHitDto;
import ru.practicum.statistic.dto.ListViewStats;
import ru.practicum.statistic.dto.ResponseEndpointHitDto;
import ru.practicum.statistic.service.StatisticService;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/hit")
    public ResponseEntity<ResponseEndpointHitDto> addEndpointHit(@RequestBody CreateEndpointHitDto endpointHitDto) {
        log.info("addEndpointHit: {}", endpointHitDto);
        return ResponseEntity.status(HttpStatus.OK).body(statisticService.addEndpointHit(endpointHitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<ListViewStats> getStats(@RequestParam String start,
                                                  @RequestParam String end,
                                                  @RequestParam(required = false) List<String> uris,
                                                  @RequestParam(defaultValue = "false") Boolean unique){
        return ResponseEntity.status(HttpStatus.OK).body(statisticService.getStats(start, end, uris, unique));
    }
}
