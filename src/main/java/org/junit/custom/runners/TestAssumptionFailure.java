package org.junit.custom.runners;

import org.junit.runner.Description;

public class TestAssumptionFailure extends TestFailure {
    public TestAssumptionFailure(Description description, Throwable throwable) {
        super(description, throwable);
    }
}