package org.junit.custom.runners;

import lombok.EqualsAndHashCode;
import org.junit.runner.Description;

import java.io.Serializable;

@EqualsAndHashCode
public class RunStarted implements Serializable {
    private Description description;

    public RunStarted(Description description) {
        this.description = description;
    }
}
