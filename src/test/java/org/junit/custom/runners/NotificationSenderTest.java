package org.junit.custom.runners;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@Slf4j
public class NotificationSenderTest {

    private static final String LOCALHOST = "localhost";
    private static final int PORT = 1234;
    private static final int TIMEOUT = 3000;
    private static final String RUN_STARTED = "testRunStarted";
    private static final String TEST_STARTED = "testStarted";
    private static final String TEST_FINISHED = "testFinished";
    private static final String TEST_IGNORED = "testIgnored";
    private static final String TEST_FAILURE = "testFailure";
    private static final String TEST_ASSUMPTION_FAILURE = "testAssumptionFailure";

    private NotificationReceiver receiver;
    private RunEventListener listener;
    private NotificationSender sender;

    @Before
    public void setUp() throws Exception {
        receiver = new NotificationReceiver(PORT);
        listener = Mockito.mock(RunEventListener.class);
        receiver.addListener(listener);
        receiver.start();
        sender = new NotificationSender(LOCALHOST, PORT);
    }

    @After
    public void tearDown() throws Exception {
        receiver.stop();
    }

    @Test
    public void testRunStarted() throws Exception {
        RunStarted event = makeRunStartedEvent();
        sender.sendRunStarted(event);
        verify(listener, timeout(TIMEOUT)).onRunStarted(eq(event));
    }

    @Test
    public void testRunFinished() throws Exception {
        RunFinished event = makeRunFinishedEvent();
        sender.sendRunFinished(event);
        verify(listener, timeout(TIMEOUT)).onRunFinished(any(RunFinished.class));
    }

    @Test
    public void testTestStarted() throws IOException {
        TestStarted event = makeTestStartedEvent();
        sender.sendTestStarted(event);
        verify(listener, timeout(TIMEOUT)).onTestStarted(eq(event));
    }

    @Test
    public void testTestFinished() throws IOException {
        TestFinished event = makeTestFinishedEvent();
        sender.sendTestFinished(event);
        verify(listener, timeout(TIMEOUT)).onTestFinished(eq(event));
    }

    @Test
    public void testTestIgnored() throws IOException {
        TestIgnored event = makeTestIgnoredEvent();
        sender.sendTestIgnored(event);
        verify(listener, timeout(TIMEOUT)).onTestIgnored(eq(event));
    }

    @Test
    public void testTestFailure() throws IOException {
        TestFailure event = makeTestFailureEvent();
        sender.sendTestFailure(event);
        verify(listener, timeout(TIMEOUT)).onTestFailure(eq(event));
    }

    @Test
    public void testTestAssumptionFailure() throws IOException {
        TestAssumptionFailure event = makeTestAssumptionFailureEvent();
        sender.sendTestAssumptionFailure(event);
        verify(listener, timeout(TIMEOUT)).onTestAssumptionFailure(eq(event));
    }

    private TestAssumptionFailure makeTestAssumptionFailureEvent() {
        return new TestAssumptionFailure(
                makeDescription(TEST_ASSUMPTION_FAILURE),
                new RuntimeException("Test failed"));
    }

    private TestFailure makeTestFailureEvent() {
        return new TestFailure(
                makeDescription(TEST_FAILURE),
                new RuntimeException("Something went wrong"));
    }

    private TestIgnored makeTestIgnoredEvent() {
        return new TestIgnored(makeDescription(TEST_IGNORED));
    }

    private TestFinished makeTestFinishedEvent() {
        return new TestFinished(makeDescription(TEST_FINISHED));
    }

    private Description makeDescription(String description) {
        return Description.createTestDescription(getClass(), description);
    }

    private TestStarted makeTestStartedEvent() {
        return new TestStarted(makeDescription(TEST_STARTED));
    }

    private RunFinished makeRunFinishedEvent() {
        return new RunFinished(new Result());
    }

    private RunStarted makeRunStartedEvent() {
        return new RunStarted(makeDescription(RUN_STARTED));
    }
}