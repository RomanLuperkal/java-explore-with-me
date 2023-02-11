package ru.practicum.server.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.comment.dto.CommentDtoResponse;
import ru.practicum.server.comment.dto.CommentDtoUpdate;
import ru.practicum.server.comment.dto.NewCommentDto;
import ru.practicum.server.comment.enums.CommentState;
import ru.practicum.server.comment.mapper.CommentMapper;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.comment.repository.CommentRepository;
import ru.practicum.server.event.enums.State;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.handler.exception.AccessException;
import ru.practicum.server.handler.exception.CommentException;
import ru.practicum.server.handler.exception.NotFoundException;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentServiceImp implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper mapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDtoResponse addComment(Long userId, Long eventId, NewCommentDto newComment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event;
        if (user.getAreCommentsBlocked()) {
            throw new AccessException("A user with id=" + userId + "has comments blocked");
        }
        event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new AccessException("It is not possible to add a comment to an event in the status " + event.getState());
        }
        Comment comment = mapper.mapToComment(newComment);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        return mapper.mapToCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDtoResponse updateComment(Long userId, Long commentId, CommentDtoUpdate updateComment) {
        Comment comment = commentRepository.findByCommentIdAndAuthorUserId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Comment with commentId=" + commentId
                        + " and userId=" + userId + " not found"));
        if (LocalDateTime.now().isAfter(comment.getCreated().plusHours(2))) {
            throw new CommentException("Сan't edit a comment that was created more than 2 hours ago");
        }
        comment.setState(CommentState.EDITED);
        return mapper.mapToCommentResponse(commentRepository.save(mapper.mapToComment(updateComment, comment)));
    }
}