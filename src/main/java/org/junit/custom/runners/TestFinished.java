package org.junit.custom.runners;

import lombok.EqualsAndHashCode;
import org.junit.runner.Description;

import java.io.Serializable;

@EqualsAndHashCode
public class TestFinished implements Serializable {
    private Description description;

    public TestFinished(Description description) {
        this.description = description;
    }
}
