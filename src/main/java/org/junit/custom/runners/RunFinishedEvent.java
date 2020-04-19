package org.junit.custom.runners;

import lombok.EqualsAndHashCode;
import org.junit.runner.Result;

import java.io.Serializable;

@EqualsAndHashCode
public class RunFinishedEvent implements Serializable {
    private Result result;

    public RunFinishedEvent(Result result) {
        this.result = result;
    }
}