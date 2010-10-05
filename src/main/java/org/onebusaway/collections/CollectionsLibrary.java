package org.onebusaway.collections;

import java.util.HashSet;
import java.util.Set;

public class CollectionsLibrary {

  public static <T> Set<T> set(T... values) {
    Set<T> set = new HashSet<T>();
    for (T value : values)
      set.add(value);
    return set;
  }

}
