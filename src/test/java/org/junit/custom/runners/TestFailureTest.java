package org.junit.custom.runners;

import org.junit.Test;
import org.junit.runner.Description;

import java.io.IOException;

import static org.junit.Assert.*;

public class TestFailureTest {

    private static final String DESCRIPTION = "failureDescription";
    public static final String ANOTHER_FAILURE = "Another failure";

    @Test
    public void testEqualFailures() {
        Throwable throwable = new RuntimeException("Huston, we have a problem");
        TestFailure failure = makeTestFailure(DESCRIPTION, throwable);
        TestFailure sameFailure = makeTestFailure(DESCRIPTION, throwable);
        assertEquals(failure, sameFailure);
    }

    @Test
    public void testFailuresWithDifferentDescriptions() {
        Throwable throwable
                = new RuntimeException("Huston, we have a problem");
        TestFailure failure = makeTestFailure(ANOTHER_FAILURE, throwable);
        TestFailure otherFailure = makeTestFailure(DESCRIPTION, throwable);
        assertNotEquals(failure, otherFailure);
    }

    @Test
    public void testFailuresWithDifferentTypeOfCausedExceptions() {
        TestFailure failure = makeTestFailure(DESCRIPTION, new IOException("Disk fault!"));
        TestFailure otherFailure = makeTestFailure(DESCRIPTION, new RuntimeException("Disk fault!"));
        assertNotEquals(failure, otherFailure);
    }

    @Test
    public void testFailuresWithDifferentExceptionMessages() {
        TestFailure failure = makeTestFailure(DESCRIPTION, new RuntimeException("It happens!"));
        TestFailure otherFailure = makeTestFailure(DESCRIPTION, new RuntimeException("Oops!"));
        assertNotEquals(failure, otherFailure);
    }

    @Test
    public void testFailureAndNullNotEqual() {
        Throwable throwable = new RuntimeException("Caramba!");
        TestFailure failure = makeTestFailure(DESCRIPTION, throwable);
        assertNotEquals(null, failure);
    }

    private TestFailure makeTestFailure(String description, Throwable throwable) {
        return new TestFailure(makeDescription(description), throwable);
    }

    private Description makeDescription(String description) {
        return Description.createTestDescription(getClass(), description);
    }
}
