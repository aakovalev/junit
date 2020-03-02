package org.junit.custom.runners;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.notification.RunNotifier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
class RemoteRunListener {
    private int port;
    private final RunNotifier notifier;

    public RemoteRunListener(int port, RunNotifier notifier) {
        this.port = port;
        this.notifier = notifier;
    }

    public void start() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);
        new Thread(() -> {
            try {
                Socket socket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                while (true) {
                    RunNotification notification = (RunNotification) in.readObject();
                    if (NotificationType.FireTestStarted == notification.getType()) {
                        notifier.fireTestStarted(notification.getDescription());
                    }
                }
            } catch (IOException e) {
                System.out.println("Something went wrong: " + e.getMessage());
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}