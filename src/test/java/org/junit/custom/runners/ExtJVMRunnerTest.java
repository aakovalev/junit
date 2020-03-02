package org.junit.custom.runners;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import static org.junit.Assert.assertTrue;

public class ExtJVMRunnerTest {
    @Test public void nothing() {
        JUnitCore junit = new JUnitCore();
        Result result = junit.run(SampleTest.class);
        assertTrue(result.wasSuccessful());
    }
}