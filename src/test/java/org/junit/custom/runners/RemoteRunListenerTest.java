package org.junit.custom.runners;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static org.junit.custom.runners.NotificationType.FireTestStarted;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RemoteRunListenerTest {

    private static final int LISTENER_PORT = 1234;
    private static final int TIMEOUT = 1000;

    @Test
    public void nothing() throws IOException {
        RunNotifier notifier = mock(RunNotifier.class);
        RemoteRunListener listener = new RemoteRunListener(LISTENER_PORT, notifier);
        listener.start();
        RunNotification notification = createNotification(FireTestStarted);

        send(notification, LISTENER_PORT);

        verify(notifier, timeout(TIMEOUT))
                .fireTestStarted(eq(notification.getDescription()));
    }

    private void send(RunNotification notification, int port) throws IOException {
        Socket client = new Socket("localhost", port);
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        out.writeObject(notification);
    }

    private RunNotification createNotification(NotificationType type) {
        Description description =
                Description.createSuiteDescription("Remote test started");
        return new RunNotification(type, description);
    }
}