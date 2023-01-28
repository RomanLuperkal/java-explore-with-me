package ru.practicum.statistic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.statistic.dto.ViewStats;
import ru.practicum.statistic.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.statistic.dto.ViewStats(vs.app, vs.uri, COUNT(vs.ip)) " +
            "FROM EndpointHit AS vs " +
            "WHERE vs.timestamp BETWEEN :start AND :end " +
            "GROUP BY vs.app, vs.uri " +
            "ORDER BY COUNT(vs.ip) DESC")
    List<ViewStats> getViewStatsByStartAndEndTime(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.statistic.dto.ViewStats(vs.app, vs.uri, COUNT(vs.ip)) " +
            "FROM EndpointHit AS vs " +
            "WHERE vs.timestamp BETWEEN :start AND :end " +
            "AND vs.uri IN :uris " +
            "GROUP BY vs.app, vs.uri " +
            "ORDER BY COUNT(vs.ip) DESC")
    List<ViewStats> getUrisViewStatsByStartAndEndTime(@Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end,
                                                      @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.statistic.dto.ViewStats(vs.app, vs.uri, COUNT(DISTINCT vs.ip)) " +
            "FROM EndpointHit AS vs " +
            "WHERE vs.timestamp BETWEEN :start AND :end " +
            "GROUP BY vs.app, vs.uri " +
            "ORDER BY COUNT(DISTINCT vs.ip) DESC")
    List<ViewStats> getUniqueViewStatsByStartAndEndTime(@Param("start") LocalDateTime start,
                                                        @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.statistic.dto.ViewStats(vs.app, vs.uri, COUNT(DISTINCT vs.ip)) " +
            "FROM EndpointHit AS vs " +
            "WHERE vs.timestamp BETWEEN :start AND :end " +
            "AND vs.uri IN :uris " +
            "GROUP BY vs.app, vs.uri " +
            "ORDER BY COUNT(DISTINCT vs.ip) DESC")
    List<ViewStats> getUniqueUrisViewStatsByStartAndEndTime(@Param("start") LocalDateTime start,
                                                      @Param("end") LocalDateTime end,
                                                      @Param("uris") List<String> uris);
}
