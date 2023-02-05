package ru.practicum.server.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.server.event.enums.State;
import ru.practicum.server.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long> {
    List<Event> findAllByInitiatorUserId(Long userId, Pageable pageable);

    Optional<Event> findByEventIdAndInitiatorUserId(Long eventId, Long userId);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE e.initiator.userId IN :ids " +
            "AND e.state IN :states " +
            "AND e.category.categoryId IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<Event> findAllEventsByFilters(@Param("ids") List<Long> ids,
                                       @Param("states") List<State> states,
                                       @Param("categories") List<Long> categories,
                                       @Param("rangeStart") LocalDateTime rangeStart,
                                       @Param("rangeEnd") LocalDateTime rangeEnd,
                                       Pageable pageable);
}
