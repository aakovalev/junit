package org.junit.custom.runners;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.notification.RunNotifier;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
class RemoteRunListener {
    private int port;
    private final RunNotifier notifier;
    private ObjectInputStream in;

    public RemoteRunListener(int port, RunNotifier notifier) {
        this.port = port;
        this.notifier = notifier;
    }

    public void start() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);
        new Thread(() -> {
            try (Socket socket = serverSocket.accept()) {
                in = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    RunNotification notification = (RunNotification) in.readObject();
                    if (NotificationType.FireTestStarted == notification.getType()) {
                        notifier.fireTestStarted(notification.getDescription());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Something went wrong...", e);
            }
            close(serverSocket);
        }).start();
    }

    private void close(Closeable resource) {
        try {
            resource.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (in != null) {
            close(in);
        }
    }
}