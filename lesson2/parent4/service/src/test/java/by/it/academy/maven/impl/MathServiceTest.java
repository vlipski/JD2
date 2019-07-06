package by.it.academy.maven.impl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MathServiceTest {
    MathService mathService;

    @org.junit.Before
    public void setUp() throws Exception {
        mathService = new MathService();
    }


    @org.junit.After
    public void tearDown() throws Exception {
        mathService = null;
    }

    @org.junit.Test
    public void average() {
        // given
        List<String> testData = List.of("1", "2");

        List<String> testData2 = new ArrayList<>();
        testData2.add("1");
        testData2.add(null);
        testData2.add("2");
        testData2.add("3");


        // when
        Double d = mathService.average(testData);
        Double d2 = mathService.average(testData2);

        // then
        assertEquals(1.5, d, 0);
        assertEquals(2, d2, 0);
    }
}
