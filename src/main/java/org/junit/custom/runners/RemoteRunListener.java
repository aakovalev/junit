package org.junit.custom.runners;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.notification.RunNotifier;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
class RemoteRunListener {
    private int port;
    private final RunNotifier notifier;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private Thread clientThread;

    public RemoteRunListener(int port, RunNotifier notifier) {
        this.port = port;
        this.notifier = notifier;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        clientThread = new Thread(() -> {
            ObjectInputStream in = null;
            try {
                clientSocket = serverSocket.accept();
                in = new ObjectInputStream(clientSocket.getInputStream());
                while (true) {
                    RunNotification notification = (RunNotification) in.readObject();
                    if (NotificationType.FireTestStarted == notification.getType()) {
                        notifier.fireTestStarted(notification.getDescription());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                log.warn("Something went wrong...", e);
            }
            finally {
                close(in);
                close(clientSocket);
                close(serverSocket);
            }
        });
        clientThread.start();
    }

    public void stop() throws InterruptedException, IOException {
        if (clientSocket != null && !clientSocket.isClosed()) {
            close(clientSocket);
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            close(serverSocket);
        }
        if (clientThread != null && clientThread.isAlive()) {
            clientThread.join();
        }
    }

    private void close(Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException e) {
            log.warn("Error while closing the resource", e);
        }
    }
}