package org.onebusaway.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class MinTest {

  @Test
  public void test() {

    Min<String> m = new Min<String>();

    assertTrue(m.isEmpty());

    m.add(1.0, "a");

    assertEquals(1.0, m.getMinValue(), 0.0);
    assertEquals("a", m.getMinElement());
    List<String> els = m.getMinElements();
    assertEquals(1, els.size());
    assertTrue(els.contains("a"));

    m.add(2.0, "b");

    assertEquals(1.0, m.getMinValue(), 0.0);
    assertEquals("a", m.getMinElement());
    els = m.getMinElements();
    assertEquals(1, els.size());
    assertTrue(els.contains("a"));

    m.add(0.0, "c");

    assertEquals(0.0, m.getMinValue(), 0.0);
    assertEquals("c", m.getMinElement());
    els = m.getMinElements();
    assertEquals(1, els.size());
    assertTrue(els.contains("c"));

    m.add(0.0, "d");

    assertEquals(0.0, m.getMinValue(), 0.0);
    assertEquals("c", m.getMinElement());
    els = m.getMinElements();
    assertEquals(2, els.size());
    assertTrue(els.contains("c"));
    assertTrue(els.contains("d"));
  }
}
