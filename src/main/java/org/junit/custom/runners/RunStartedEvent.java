package org.junit.custom.runners;

import org.junit.runner.Description;

import java.io.Serializable;

public class RunStartedEvent implements Serializable {
    public RunStartedEvent(Description description) {
    }
}
