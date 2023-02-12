package ru.practicum.server.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.dto.CommentDtoResponse;
import ru.practicum.server.comment.dto.CommentDtoUpdate;
import ru.practicum.server.comment.dto.NewCommentDto;
import ru.practicum.server.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("{userId}/comments")
    public ResponseEntity<CommentDtoResponse> addComment(@PathVariable @Min(1) Long userId,
                                     @RequestParam @Min(1) Long eventId,
                                     @RequestBody @Valid NewCommentDto comment) {
        log.info("add new comment with userId={}, eventId={}:{}", userId, eventId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.addComment(userId, eventId, comment));
    }

    @PatchMapping("{userId}/comments/{commentId}")
    public ResponseEntity<CommentDtoResponse> updateComment(@PathVariable @Min(1) Long userId,
                                                            @PathVariable @Min(1) Long commentId,
                                                            @RequestBody @Valid CommentDtoUpdate updateComment) {
        log.info("update comment with commentId={} and userId={}: {}", commentId, userId, updateComment);
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateComment(userId, commentId, updateComment));
    }

    @DeleteMapping("{userId}/comments/{commentId}")
    public ResponseEntity<Void> deleteCommentUser(@PathVariable @Min(1) Long userId,
                                                  @PathVariable @Min(1) Long commentId) {
        log.info("delete comment with commentId={} and userId={}", commentId, userId);
        commentService.deleteCommentUser(commentId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("comments")
    public ResponseEntity<Void> reportCommentUser(@RequestParam @Min(1) Long reportUserId,
                                                  @RequestParam @Min(1) Long commentId) {
        log.info("report userId={} with commentId={}", reportUserId, commentId);
        commentService.reportComment(commentId, reportUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
