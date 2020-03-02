package org.junit.custom.runners;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RemoteRunListenerTest {
    @Test
    public void nothing() throws IOException {
        int port = 1234;
        RunNotifier notifier = mock(RunNotifier.class);
        RemoteRunListener listener = new RemoteRunListener(port, notifier);
        listener.start();
        NotificationType type = NotificationType.FireTestStarted;
        Description description = Description
                .createSuiteDescription("Remote test started");
        RunNotification notification =
                new RunNotification(type, description);
        send(notification, port);
        verify(notifier, timeout(1000))
                .fireTestStarted(eq(description));
    }

    private void send(RunNotification notification, int port) throws IOException {
        Socket client = new Socket("localhost", port);
        ObjectOutputStream out =
                new ObjectOutputStream(client.getOutputStream());
        out.writeObject(notification);
    }
}