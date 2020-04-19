package org.junit.custom.runners;

import org.junit.Test;
import org.junit.runner.Description;
import org.mockito.Mockito;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class NotificationSenderTest {

    private static final String LOCALHOST = "localhost";
    private static final int PORT = 1234;
    private static final int TIMEOUT = 3;

    @Test
    public void testRunStarted() throws Exception {
        NotificationReceiver receiver = new NotificationReceiver(PORT);
        receiver.start();
        RunEventListener listener = Mockito.mock(RunEventListener.class);
        receiver.addListener(listener);
        NotificationSender sender = new NotificationSender(LOCALHOST, PORT);
        RunStartedEvent event = makeRunStartedEvent();

        sender.sendRunStarted(event);

        verify(listener, timeout(TIMEOUT)).onRunStartedEvent(eq(event));
    }

    private RunStartedEvent makeRunStartedEvent() {
        Description testRunDescription = Description
                .createTestDescription(getClass(), "testRunStarted");
        return new RunStartedEvent(testRunDescription);
    }
}