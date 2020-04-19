package org.junit.custom.runners;

import lombok.EqualsAndHashCode;
import org.junit.runner.Description;

import java.io.Serializable;

@EqualsAndHashCode
public class RunStartedEvent implements Serializable {
    private Description description;

    public RunStartedEvent(Description description) {
        this.description = description;
    }
}
