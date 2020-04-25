package org.junit.custom.runners;

public interface RunEventListener {
    void onRunStartedEvent(RunStartedEvent event);

    void onRunFinished(RunFinishedEvent event);

    void onTestStarted(TestStartedEvent event);
}