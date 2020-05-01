package org.junit.custom.runners;

import org.junit.runner.notification.RunNotifier;

public class NotificationLink implements RunEventListener {
    private final NotificationReceiver receiver;
    private final RunNotifier notifier;

    public NotificationLink(NotificationReceiver receiver, RunNotifier notifier) {
        this.receiver = receiver;
        this.notifier = notifier;
        this.receiver.addListener(this);
    }

    @Override
    public void onRunStarted(RunStarted event) {
        notifier.fireTestRunStarted(event.getDescription());
    }

    @Override
    public void onRunFinished(RunFinished event) {

    }

    @Override
    public void onTestStarted(TestStarted event) {

    }

    @Override
    public void onTestFinished(TestFinished event) {

    }

    @Override
    public void onTestIgnored(TestIgnored event) {

    }

    @Override
    public void onTestFailure(TestFailure event) {

    }

    @Override
    public void onTestAssumptionFailure(TestAssumptionFailure event) {

    }
}
