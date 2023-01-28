package ru.practicum.statistic.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseEndpointHitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
