package ru.practicum.server.event.enums;

import ru.practicum.server.handler.exception.EventUpdateException;

public enum StateAction {
    SEND_TO_REVIEW, CANCEL_REVIEW;

    public static  State getState(String stateAction) {
        if (stateAction.equals(SEND_TO_REVIEW.toString())) {
            return State.PENDING;
        } else if (stateAction.equals(CANCEL_REVIEW.toString())) {
            return State.CANCELED;
        } else {
            throw new EventUpdateException("Event must not be published");
        }
    }
}
