package org.onebusaway.collections;

import java.util.ArrayList;
import java.util.List;

public final class FunctionalLibrary {
  private FunctionalLibrary() {

  }

  public static <T> List<T> filter(Iterable<T> elements,
      String propertyPathExpression, Object value) {
    List<T> matches = new ArrayList<T>();
    PropertyPathExpression query = new PropertyPathExpression(
        propertyPathExpression);
    for (T element : elements) {
      Object result = query.invoke(element);
      if ((value == null && result == null)
          || (value != null && value.equals(result)))
        matches.add(element);
    }
    return matches;
  }

  public static <T> T filterFirst(Iterable<T> elements,
      String propertyPathExpression, Object value) {
    List<T> matches = filter(elements, propertyPathExpression, value);
    return matches.isEmpty() ? null : matches.get(0);
  }
}
