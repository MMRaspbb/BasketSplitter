package com.ocado.basket;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BasketSplitterTest {

    @Test
    void testInvalidPathToJson() {
        String absolutePath = "/invalid/path/to/json/file";
        try {
            BasketSplitter bs = new BasketSplitter(absolutePath);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong absolute path to file", e.getMessage());
        }
    }

    @Test
    void testValidPathToJson() {
        String absolutePath = "src/test/resources/config1.json";
        try {
            BasketSplitter bs = new BasketSplitter(absolutePath);
            Assertions.assertTrue(true);
        } catch (Exception e) {
            fail("Corret path to file wasn't loaded!");
        }
    }
    @Test
    void testSplit(){
        String absolutePath = "src/test/resources/config1.json";
        BasketSplitter bs = new BasketSplitter(absolutePath);
        List<String> items = List.of("Carrots (1kg)",
                                    "Garden Chair",
                                    "Cold Beer (330ml)",
                                    "Steak (300g)",
                                    "AA Battery (4 Pcs.)",
                                    "Espresso Machine");
        Map<String, List<String>> map = bs.split(items);
        Map<String, List<String>> answer = new HashMap<>();
        answer.put("Courier", Arrays.asList("Garden Chair", "Espresso Machine"));
        answer.put("Express Delivery", Arrays.asList("Carrots (1kg)", "Cold Beer (330ml)", "Steak (300g)", "AA Battery (4 Pcs.)"));
        Assertions.assertEquals(answer, map);
    }
    @Test
    void testComplexSplit(){

    }
}