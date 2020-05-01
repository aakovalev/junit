package org.junit.custom.runners;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
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
    public void shouldBeReceiverListener() {
        verify(receiver, times(ONCE)).addListener(eq(link));
    }

    @Test
    public void shouldTriggerNotifierOnRunStarted() {
        Description description = Description
                .createTestDescription(
                        getClass(), RunEventType.RunStarted.name());
        link.onRunStarted(new RunStarted(description));
        verify(notifier, times(ONCE)).fireTestRunStarted(eq(description));
    }
}