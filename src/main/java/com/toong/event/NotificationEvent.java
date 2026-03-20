package com.toong.event;

import com.toong.modal.enums.NotifType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final NotifType type;
    private final String title;
    private final String description;
    private final Long refId;
    private final String refPath;

    public NotificationEvent(Object source, NotifType type, String title, String description, Long refId, String refPath) {
        super(source);
        this.type = type;
        this.title = title;
        this.description = description;
        this.refId = refId;
        this.refPath = refPath;
    }
}
