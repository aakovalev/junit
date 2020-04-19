package org.junit.custom.runners;

import org.junit.Test;
import org.junit.runner.Description;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class NotificationSenderTest {

    private static final String LOCALHOST = "localhost";
    private static final int PORT = 1234;
    private static final int TIMEOUT = 3;

    @Test
    public void testRunStarted() throws Exception {
        CountDownLatch runStartedReceived = new CountDownLatch(1);
        NotificationReceiver receiver = new NotificationReceiver(PORT);
        receiver.start();
        receiver.addListener(event -> {
            runStartedReceived.countDown();
        });
        NotificationSender sender = new NotificationSender(LOCALHOST, PORT);
        Description testRunDescription = Description
                .createTestDescription(getClass(), "testRunStarted");
        RunStartedEvent event = new RunStartedEvent(testRunDescription);

        sender.sendRunStarted(event);

        boolean eventWasReceived = runStartedReceived.await(TIMEOUT, TimeUnit.SECONDS);
        assertTrue(eventWasReceived);
    }
}