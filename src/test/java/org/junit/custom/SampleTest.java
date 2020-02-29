package org.junit.custom;

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