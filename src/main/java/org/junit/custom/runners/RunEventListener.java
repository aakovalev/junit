package org.junit.custom.runners;

public interface RunEventListener {
    void onRunStarted(RunStarted event);

    void onRunFinished(RunFinished event);

    void onTestStarted(TestStarted event);

    void onTestFinished(TestFinished event);

    void onTestIgnored(TestIgnored event);

    void onTestFailure(TestFailure event);

    void onTestAssumptionFailure(TestAssumptionFailure event);
}