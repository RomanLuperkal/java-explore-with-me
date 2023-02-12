package ru.practicum.server.comment.service;

import ru.practicum.server.comment.dto.CommentDtoResponse;
import ru.practicum.server.comment.dto.CommentDtoUpdate;
import ru.practicum.server.comment.dto.NewCommentDto;

public interface CommentService {
    CommentDtoResponse addComment(Long userId, Long eventId, NewCommentDto newComment);

    CommentDtoResponse updateComment(Long userId, Long commentId, CommentDtoUpdate updateComment);

    void deleteCommentUser(Long commentId, Long userId);

    void reportComment(Long commentId, Long userId);

    void deleteCommentAdmin(Long commentId, Long userId);
}
