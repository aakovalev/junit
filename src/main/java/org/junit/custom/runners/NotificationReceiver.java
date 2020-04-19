package org.junit.custom.runners;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
class NotificationReceiver {
    private int port;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private Thread clientThread;
    private RunEventListener runEventListener;

    public NotificationReceiver(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        clientThread = new Thread(() -> {
            ObjectInputStream in = null;
            try {
                clientSocket = serverSocket.accept();
                in = new ObjectInputStream(clientSocket.getInputStream());
                while (true) {
                    RunEventType runEventType = (RunEventType) in.readObject();
                    if (RunEventType.RunStarted == runEventType) {
                        RunStartedEvent runStartedEvent = (RunStartedEvent) in.readObject();
                        runEventListener.onRunStartedEvent(runStartedEvent);
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

    public void stop() throws InterruptedException {
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

    public void addListener(RunEventListener runEventListener) {
        this.runEventListener = runEventListener;
    }
}