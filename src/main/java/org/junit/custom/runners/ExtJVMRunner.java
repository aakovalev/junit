package org.junit.custom.runners;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.Method;

@Slf4j
public class ExtJVMRunner extends Runner {
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
                    method.invoke(testObject);
                    notifier.fireTestFinished(Description
                            .createTestDescription(testClass, method.getName()));
                    /*
                        int port = Random.nextInt(MAX_PORT);
                        RunNotifier remoteNotifier = new RemoteRunNotifier(port);
                        NotificationLink notificationLink = new NotificationLink(remoteNotifier, notifier);
                        ExternalJVM jvm = new ExternalJVM();
                        String[] args = new String() { testClass, testMethod, port };
                        jvm.start(JUnitBootstrap.class, args);
                     */
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}