package org.onebusaway.collections;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public final class PropertyPathExpression {

  private String[] _properties;

  private Method[] _methods = null;

  public static Object evaluate(Object target, String query) {
    return new PropertyPathExpression(query).invoke(target);
  }

  public PropertyPathExpression(String query) {
    _properties = query.split("\\.");
  }

  public String getPath() {
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < _properties.length; i++) {
      if (i > 0)
        b.append('.');
      b.append(_properties[i]);
    }
    return b.toString();
  }

  public void initialize(Class<?> sourceValueType) {

    if (_methods != null)
      return;

    _methods = new Method[_properties.length];

    for (int i = 0; i < _properties.length; i++) {

      Method m = null;

      try {
        BeanInfo info = Introspector.getBeanInfo(sourceValueType);
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
          if (pd.getName().equals(_properties[i])) {
            m = pd.getReadMethod();
            break;
          }
        }
      } catch (IntrospectionException ex) {
        throw new IllegalStateException("error introspecting bean class: "
            + sourceValueType, ex);
      }

      if (m == null)
        throw new IllegalStateException("could not find property: "
            + _properties[i]);

      m.setAccessible(true);
      _methods[i] = m;

      sourceValueType = m.getReturnType();
    }
  }

  /**
   * 
   * @param value
   * @return
   * @throws IllegalStateException on introspection and evaluation errors
   */
  public Object invoke(Object value) {

    if (_methods == null)
      initialize(value.getClass());

    for (int i = 0; i < _properties.length; i++) {
      Method m = _methods[i];

      try {
        value = m.invoke(value);
      } catch (Exception ex) {
        throw new IllegalStateException("error invoking property reader: obj="
            + value + " property=" + _properties[i], ex);
      }
    }
    return value;
  }
}