/*
 * Copyright 2010 Brian Ferris
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.onebusaway.collections;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * Simple support for Java bean property path expression parsing and evaluation.
 * 
 * Consider a simple Order bean class with a property named {@code customer} of
 * type Customer that has its own property named {@code name}. A path expression
 * of {@code customer.name}, evaluated on an Order instance will internally make
 * a call to the {@code getCustomer()} method on the Order object, and then make
 * a call to the {@code getName()} method on the Customer object returned in the
 * previous method call. The result of the expression will be the cusomter's
 * name.
 * 
 * Instances of {@link PropertyPathExpression} are thread-safe for concurrent
 * use across threads with one restriction. A call to {@link #initialize(Class)}
 * must be made in advance of concurrent access to ensure that class
 * introspection has been completed.
 * 
 * @author bdferris
 * 
 */
public final class PropertyPathExpression {

  private String[] _properties;

  private transient Method[] _methods = null;

  /**
   * A static convenience method for evaluating a property path expression on a
   * target object. If you need to repeatedly evaluate the same property path
   * expression, consider creating a {@link PropertyPathExpression} object
   * directly so that bean introspection information can be cached.
   * 
   * @param target the target bean instance to evaluate against
   * @param query the property path expression to evaluate
   * @return the result of the evaluation of the property path expression
   */
  public static Object evaluate(Object target, String query) {
    return new PropertyPathExpression(query).invoke(target);
  }

  /**
   * 
   * @param query the property path expression to evaluate
   */
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

  /**
   * Opportunistically complete and cache bean introspection given a source
   * value target type.
   * 
   * @param sourceValueType the class of objects that will be passed in calls to
   *          {@link #invoke(Object)}
   * @throws IllegalStateException on introspection errors
   */
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
   * Invoke the property path expression against the specified object value
   * 
   * @param value the target bean to start the property path expression against
   * @return the result of the property path expression evaluation
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