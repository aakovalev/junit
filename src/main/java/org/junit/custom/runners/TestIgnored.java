package org.junit.custom.runners;

import lombok.EqualsAndHashCode;
import org.junit.runner.Description;

import java.io.Serializable;

@EqualsAndHashCode
public class TestIgnored implements Serializable {
    private Description description;

    public TestIgnored(Description description) {
        this.description = description;
    }

    public Description getDescription() {
        return description;
    }
}