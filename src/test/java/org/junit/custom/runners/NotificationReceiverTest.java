package org.junit.custom.runners;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertTrue;
import static org.junit.custom.runners.NotificationType.FireTestStarted;
import static org.junit.runner.Description.createSuiteDescription;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
public class NotificationReceiverTest {
    private static final int LISTENER_PORT = 1234;
    private static final int TIMEOUT = 1000;
    private static final String LOCALHOST = "localhost";
    private NotificationReceiver listener;
    private RunNotifier notifier;

    @Before
    public void setUp() {
        notifier = mock(RunNotifier.class);
        listener = new NotificationReceiver(LISTENER_PORT);
    }

    @After
    public void tearDown() throws InterruptedException {
        listener.stop();
    }

    @Ignore
    public void canHandleFireTestStartedNotification() throws IOException {
        listener.start();
        RunNotification notification = createNotification(FireTestStarted);

        sendToListener(notification);

        verify(notifier, timeout(TIMEOUT))
                .fireTestStarted(eq(notification.getDescription()));
    }

    @Test
    public void canStopListener() throws IOException, InterruptedException {
        listener.start();

        listener.stop();

        assertTrue(portIsAvailable(LISTENER_PORT));
    }

    private void sendToListener(RunNotification notification) throws IOException {
        Socket client = new Socket(LOCALHOST, LISTENER_PORT);
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        out.writeObject(notification);
    }

    private RunNotification createNotification(NotificationType type) {
        Description description = createSuiteDescription(type.name());
        return new RunNotification(type, description);
    }

    private boolean portIsAvailable(int port) {
        boolean result = false;
        try (ServerSocket ignored = new ServerSocket(port)) {
            result = true;
        } catch (Exception e) {
            log.info("Could not connect to port {}", port);
            e.printStackTrace();
        }
        return result;
    }
}