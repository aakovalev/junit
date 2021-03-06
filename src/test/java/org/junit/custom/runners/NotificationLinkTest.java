package org.junit.custom.runners;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.RunNotifier;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NotificationLinkTest {
    private static final int ONCE = 1;

    @Mock
    private NotificationReceiver receiver;
    @Mock
    private RunNotifier notifier;

    private NotificationLink link;

    @Before
    public void setUp() {
        link = new NotificationLink(receiver, notifier);
    }

    @Test
    public void shouldListenToReceiever() {
        verify(receiver, times(ONCE)).addListener(eq(link));
    }

    @Test
    public void shouldTriggerNotifierOnRunStarted() {
        Description description = makeDescription(RunEventType.RunStarted.name());
        link.onRunStarted(new RunStarted(description));
        verify(notifier, times(ONCE)).fireTestRunStarted(eq(description));
    }

    @Test
    public void shouldTriggerNotifierOnRunFinished() {
        Result result = new Result();
        link.onRunFinished(new RunFinished(result));
        verify(notifier, times(ONCE)).fireTestRunFinished(eq(result));
    }

    @Test
    public void shouldTriggerNotifierOnTestStarted() {
        Description description = makeDescription(RunEventType.TestStarted.name());
        link.onTestStarted(new TestStarted(description));
        verify(notifier, times(ONCE)).fireTestStarted(eq(description));
    }

    @Test
    public void shouldTriggerNotifierOnTestFinished() {
        Description description = makeDescription(RunEventType.TestFinished.name());
        link.onTestFinished(new TestFinished(description));
        verify(notifier, times(ONCE)).fireTestFinished(eq(description));
    }

    @Test
    public void shouldTriggerNotifierOnTestFailure() {
        Description description = makeDescription(RunEventType.TestFailure.name());
        TestFailure failure = new TestFailure(
                description, new RuntimeException("It happens!"));
        link.onTestFailure(failure);
        verify(notifier, times(ONCE)).fireTestFailure(eq(failure.getFailure()));
    }

    @Test
    public void shouldTriggerNotifierOnTestIgnored() {
        Description description = makeDescription(RunEventType.TestIgnored.name());
        link.onTestIgnored(new TestIgnored(description));
        verify(notifier, times(ONCE)).fireTestIgnored(eq(description));
    }

    @Test
    public void shouldTriggerNotifierOnTestAssumptionFailure() {
        Description description = makeDescription(RunEventType.TestFailure.name());
        TestAssumptionFailure failure = new TestAssumptionFailure(
                description, new RuntimeException("Test assertion failed"));
        link.onTestAssumptionFailure(failure);
        verify(notifier, times(ONCE)).fireTestAssumptionFailed(eq(failure.getFailure()));
    }

    private Description makeDescription(String descriptionAsText) {
        return Description.createTestDescription(getClass(), descriptionAsText);
    }
}