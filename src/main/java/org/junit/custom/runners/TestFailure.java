package org.junit.custom.runners;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import java.io.Serializable;

public class TestFailure implements Serializable  {
    private final Failure failure;

    public TestFailure(Description description, Throwable exception) {
        this.failure = new Failure(description, exception);
    }

    @Override
    public int hashCode() {
        Description description = failure.getDescription();
        Throwable exception = failure.getException();
        Class exceptionClass = exception.getClass();
        String exceptionMessage = exception.getMessage();
        return description.hashCode()
                + exceptionClass.hashCode()
                + exceptionMessage.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (this.getClass().equals(obj.getClass())) {
            Failure thatFailure = ((TestFailure) obj).failure;
            Description description = failure.getDescription();
            Throwable exception = failure.getException();
            Class exceptionClass = exception.getClass();
            return description.equals(thatFailure.getDescription())
                    && exceptionClass.equals(thatFailure.getException().getClass())
                    && exception.getMessage().equals(thatFailure.getMessage());
        }
        return false;
    }

    public Failure getFailure() {
        return this.failure;
    }
}