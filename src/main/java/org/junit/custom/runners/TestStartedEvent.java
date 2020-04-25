package org.junit.custom.runners;

import lombok.EqualsAndHashCode;
import org.junit.runner.Description;

import java.io.Serializable;

@EqualsAndHashCode
public class TestStartedEvent implements Serializable {
    private Description description;

    public TestStartedEvent(Description description) {
        this.description = description;
    }
}
