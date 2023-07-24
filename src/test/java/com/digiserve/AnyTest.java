package com.digiserve;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class AnyTest {

    @Test
    void test() {
        Integer x = 10;
        Assertions.assertEquals(10, x);
    }

    @Test
    void anotherTest() {
        String x = "<html></html>";
        Assertions.assertTrue(x.startsWith("<"));
    }
}
