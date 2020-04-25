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

    private TestStarted makeTestStartedEvent() {
        Description testDescription = Description
                .createTestDescription(getClass(), TEST_STARTED);
        return new TestStarted(testDescription);
    }

    private RunFinished makeRunFinishedEvent() {
        return new RunFinished(new Result());
    }

    private RunStarted makeRunStartedEvent() {
        Description testRunDescription = Description
                .createTestDescription(getClass(), RUN_STARTED);
        return new RunStarted(testRunDescription);
    }
}