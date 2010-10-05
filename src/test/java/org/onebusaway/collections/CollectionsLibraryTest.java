package org.onebusaway.collections;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class CollectionsLibraryTest {

  @Test
  public void testSet() {

    Set<String> expected = new HashSet<String>();
    expected.add("a");
    expected.add("b");
    expected.add("c");

    Set<String> actual = CollectionsLibrary.set("a", "b", "c", "a", "b");

    assertEquals(expected, actual);
  }
}
