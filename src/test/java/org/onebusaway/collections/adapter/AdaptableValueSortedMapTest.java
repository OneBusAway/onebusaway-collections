package org.onebusaway.collections.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

public class AdaptableValueSortedMapTest {

  private SortedMap<Integer, String> _m2;
  private TreeMap<Integer, TestBean> _m;

  @Before
  public void before() {

    _m = new TreeMap<Integer, TestBean>();
    _m.put(1, new TestBean("a"));
    _m.put(2, new TestBean("b"));
    _m.put(3, new TestBean("c"));

    _m2 = AdapterLibrary.adaptSortedMap(_m, new ValueAdapter());
  }

  @Test
  public void testClear() {
    _m2.clear();
    assertTrue(_m2.isEmpty());
    assertTrue(_m.isEmpty());
  }

  @Test
  public void testContainsKey() {
    assertTrue(_m2.containsKey(1));
    assertFalse(_m2.containsKey(4));
  }

  @Test
  public void testSubMap() {
    SortedMap<Integer, String> m = _m2.subMap(1, 2);
    assertEquals(1, m.size());
    assertEquals(new Integer(1), m.firstKey());
    assertEquals(new Integer(1), m.lastKey());
    assertEquals("a", m.get(1));
  }

  private static class TestBean {

    private final String _value;

    public TestBean(String value) {
      _value = value;
    }
  }

  private static class ValueAdapter implements IAdapter<TestBean, String> {

    @Override
    public String adapt(TestBean source) {
      return source._value;
    }

  }
}
