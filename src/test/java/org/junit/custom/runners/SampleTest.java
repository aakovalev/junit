package org.junit.custom.runners;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(ExtJVMRunner.class)
public class SampleTest {
    @Test
    public void nothing() {
        assertTrue(true);
    }
}