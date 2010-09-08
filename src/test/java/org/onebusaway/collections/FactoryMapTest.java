package org.onebusaway.collections;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FactoryMapTest {

  @Test
  public void test() {

    FactoryMap<String, List<String>> m = new FactoryMap<String, List<String>>(
        new ArrayList<String>());

    List<String> list = m.get("a");
    assertEquals(0, list.size());
    list.add("1");

    list = m.get("b");
    assertEquals(0, list.size());
    list.add("1");

    list = m.get("a");
    assertEquals(1, list.size());
    list.add("2");

    list = m.get("b");
    assertEquals(1, list.size());
    assertEquals("1", list.get(0));

    list = m.get("a");
    assertEquals(2, list.size());
    assertEquals("1", list.get(0));
    assertEquals("2", list.get(1));

    m.remove("b");

    list = m.get("b");
    assertEquals(0, list.size());
  }
}
