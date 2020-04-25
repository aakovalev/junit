package org.junit.custom.runners;

import lombok.EqualsAndHashCode;
import org.junit.runner.Description;

import java.io.Serializable;

@EqualsAndHashCode
public class TestStarted implements Serializable {
    private Description description;

    public TestStarted(Description description) {
        this.description = description;
    }
}
