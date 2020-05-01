package org.junit.custom.runners;

import lombok.EqualsAndHashCode;
import org.junit.runner.Result;

import java.io.Serializable;

@EqualsAndHashCode
public class RunFinished implements Serializable {
    private Result result;

    public RunFinished(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }
}