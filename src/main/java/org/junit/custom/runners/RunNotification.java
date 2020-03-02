package org.junit.custom.runners;

import org.junit.runner.Description;

import java.io.Serializable;

class RunNotification implements Serializable {
    private final NotificationType type;
    private final Description description;

    public RunNotification(NotificationType type, Description description) {
        this.type = type;
        this.description = description;
    }

    public NotificationType getType() {
        return type;
    }

    public Description getDescription() {
        return description;
    }
}