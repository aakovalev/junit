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
        RunStartedEvent event = makeRunStartedEvent();
        sender.sendRunStarted(event);
        verify(listener, timeout(TIMEOUT)).onRunStartedEvent(eq(event));
    }

    @Test
    public void testRunFinished() throws Exception {
        RunFinishedEvent event = makeRunFinishedEvent();
        sender.sendRunFinished(event);
        verify(listener, timeout(TIMEOUT)).onRunFinished(any(RunFinishedEvent.class));
    }

    @Test
    public void testTestStarted() throws IOException {
        TestStartedEvent event = makeTestStartedEvent();
        sender.sendTestStarted(event);
        verify(listener, timeout(TIMEOUT)).onTestStarted(eq(event));
    }

    private TestStartedEvent makeTestStartedEvent() {
        Description testDescription = Description
                .createTestDescription(getClass(), TEST_STARTED);
        return new TestStartedEvent(testDescription);
    }

    private RunFinishedEvent makeRunFinishedEvent() {
        return new RunFinishedEvent(new Result());
    }

    private RunStartedEvent makeRunStartedEvent() {
        Description testRunDescription = Description
                .createTestDescription(getClass(), RUN_STARTED);
        return new RunStartedEvent(testRunDescription);
    }
}