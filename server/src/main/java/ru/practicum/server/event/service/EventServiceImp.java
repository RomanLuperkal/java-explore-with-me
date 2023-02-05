package ru.practicum.server.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.repository.CategoryRepository;
import ru.practicum.server.event.dto.*;
import ru.practicum.server.event.enums.State;
import ru.practicum.server.event.enums.StateAction;
import ru.practicum.server.event.mapper.EventMapper;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.handler.exception.AccessException;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventServiceImp implements EventService {
    private final EventRepository events;
    private final UserRepository users;
    private final CategoryRepository categories;
    private final EventMapper mapper;

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto eventDto) {
        User user = users.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Category category = categories.findById(eventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Category with id=" + eventDto.getCategory() + " was not found"));
        Event newEvent = mapper.mapToEvent(eventDto);
        if (newEvent.getEventDate().isBefore(LocalDateTime.now().minusHours(2))) {
            throw new AccessException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + eventDto.getEventDate());
        } else {
            newEvent.setInitiator(user);
            newEvent.setCategory(category);
            newEvent.setCreatedOn(LocalDateTime.now());
            return mapper.mapToEventFullDto(events.save(newEvent));
        }
    }

    @Override
    public ListEventShortDto getUserEvents(Long userId, Pageable pageable) {
        if (users.existsById(userId)) {
            return ListEventShortDto
                    .builder()
                    .events(mapper.mapToListEventShortDto(events.findAllByInitiatorUserId(userId, pageable)))
                    .build();
        } else {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        if (users.existsById(userId)) {
            return mapper.mapToEventFullDto(events.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found")));
        } else {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEvent) {
        if (users.existsById(userId)) {
            LocalDateTime eventTime;
            if (updateEvent.getEventDate() != null) {
                eventTime = LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER);
                if (eventTime.isBefore(LocalDateTime.now().minusHours(2))) {
                    throw new AccessException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                            "Value: " + eventTime);
                }
            }
            Event event = events.findByEventIdAndInitiatorUserId(eventId, userId)
                    .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
            if (event.getState().equals(State.PUBLISHED)) {
                throw new AccessException("Only pending or canceled events can be changed");
            }
            if (updateEvent.getCategory() != null) {
                event.setCategory(categories.findById(updateEvent.getCategory()).orElseThrow(
                        () -> new NotFoundException("Category with id=" + updateEvent.getCategory() + " was not found")));
            }
            event.setState(StateAction.getState(updateEvent.getStateAction()));
            return mapper.mapToEventFullDto(events.save(mapper.mapToEvent(updateEvent, event)));
        } else {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }

    @Override
    public ListEventFullDto getEventsByFiltersForAdmin(List<Long> ids, List<String> states, List<Long> categories,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        for (String string : states) {
            if (!State.validString(string)) {
                states.remove(string);
            }
        }
        return ListEventFullDto
                .builder()
                .events(mapper.mapToListEventFullDto(events.findAllEventsByFilters(ids, mapper.mapToListStates(states), categories, rangeStart,
                        rangeEnd, pageable)))
                .build();
    }
}
