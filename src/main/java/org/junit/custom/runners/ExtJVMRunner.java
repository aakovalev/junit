package org.junit.custom.runners;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.Method;
import java.util.Random;

@Slf4j
public class ExtJVMRunner extends Runner {
    private static final int MAX_PORT = 0xffff;
    private Class testClass;

    public ExtJVMRunner(Class testClass) {
        super();
        this.testClass = testClass;
    }

    @Override
    public Description getDescription() {
        return Description.createTestDescription(testClass, "Running test in external JVM...");
    }

    @Override
    public void run(RunNotifier notifier) {
        log.info("Running {}", testClass);
        try {
            Object testObject = testClass.newInstance();
            for (Method method : testClass.getMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    notifier.fireTestStarted(Description
                            .createTestDescription(testClass, method.getName()));
                    /*
                        int port = getReceiverPort();
                        NotificationReceiver receiver = new NotificationReceiver(port);
                        receiver.addListener(
                            listener that notifies local run notifier using
                            events from external runner);
                        ExternalJVM jvm = new ExternalJVM();
                        String[] args = new String() { testClass, testMethod, port };
                        jvm.start(JUnitBootstrap.class, args);
                     */
                    notifier.fireTestFinished(Description
                            .createTestDescription(testClass, method.getName()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getReceiverPort() {
        return new Random().nextInt(MAX_PORT);
    }
}