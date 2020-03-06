package org.junit.custom.runners;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static org.junit.Assert.assertTrue;
import static org.junit.custom.runners.NotificationType.FireTestStarted;
import static org.junit.runner.Description.createSuiteDescription;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RemoteRunListenerTest {

    private static final int LISTENER_PORT = 1234;
    private static final int TIMEOUT = 1000;
    private static final String LOCALHOST = "localhost";
    private RemoteRunListener listener;
    private RunNotifier notifier;

    @Before
    public void setUp() {
        notifier = mock(RunNotifier.class);
        listener = new RemoteRunListener(LISTENER_PORT, notifier);
    }

    @After
    public void tearDown() {
        listener.stop();
    }

    @Test
    public void canHandleFireTestStartedNotification() throws IOException {
        listener.start();
        RunNotification notification = createNotification(FireTestStarted);

        sendToListener(notification);

        verify(notifier, timeout(TIMEOUT))
                .fireTestStarted(eq(notification.getDescription()));
    }

    @Test
    public void canStopListener() throws IOException {
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
        try (Socket socket = new Socket(LOCALHOST, port)) {
            socket.close();
            result = true;
        } catch (Exception e) {
            // Could not connect.
        }
        return result;
    }
}