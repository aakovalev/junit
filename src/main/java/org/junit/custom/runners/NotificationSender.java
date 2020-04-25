package org.junit.custom.runners;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NotificationSender {
    private final ObjectOutputStream out;

    public NotificationSender(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void sendRunStarted(RunStarted event) throws Exception {
        out.writeObject(RunEventType.RunStarted);
        out.writeObject(event);
    }

    public void sendRunFinished(RunFinished event) throws IOException {
        out.writeObject(RunEventType.RunFinished);
        out.writeObject(event);
    }

    public void sendTestStarted(TestStarted event) throws IOException {
        out.writeObject(RunEventType.TestStarted);
        out.writeObject(event);
    }

    public void sendTestFinished(TestFinished event) throws IOException {
        out.writeObject(RunEventType.TestFinished);
        out.writeObject(event);
    }
}