/**
 * Copyright (C) 2012 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusaway.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple support for Java bean property path expression parsing and evaluation,
 * where the resulting evaluation produces multiple values.
 * 
 * Example:
 * 
 * <code>interface Container { public List<Entry> getEntries(); }</code>
 * 
 * <code>interface Entry { public String getName(); }</code>
 * 
 * If you call {@link #evaluate(Object, String)} with a Container object and
 * property path of "entries.name", the expression will iterate over each Entry
 * in the Container, collecting the name property of each entry in the results.
 * 
 * @author bdferris
 */
public final class PropertyPathCollectionExpression {

  private String[] _properties;

  private PropertyMethod[] _methods = null;

  private PropertyMethodResolver _resolver = new DefaultPropertyMethodResolver();

  public static void evaluate(Object target, String query,
      Collection<Object> values) {
    PropertyPathCollectionExpression expression = new PropertyPathCollectionExpression(
        query);
    expression.invoke(target, values);
  }

  public static List<Object> evaluate(Object target, String query) {
    PropertyPathCollectionExpression expression = new PropertyPathCollectionExpression(
        query);
    List<Object> values = new ArrayList<Object>();
    expression.invoke(target, values);
    return values;
  }

  /**
   * 
   * @param query the property path expression to evaluate
   */
  public PropertyPathCollectionExpression(String query) {
    _properties = query.split("\\.");
    _methods = new PropertyMethod[_properties.length];
  }

  public void setPropertyMethodResolver(PropertyMethodResolver resolver) {
    _resolver = resolver;
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
   * Invoke the property path expression against the specified object value
   * 
   * @param value the target bean to start the property path expression against.
   * @param results the output collection where evaluated results will be
   *          stored.
   */
  public void invoke(Object value, Collection<Object> results) {
    invoke(value, 0, results);
  }

  private void invoke(Object value, int methodIndex, Collection<Object> results) {
    if (methodIndex == _methods.length) {
      results.add(value);
      return;
    }
    if (value == null) {
      return;
    }
    PropertyMethod m = getPropertyMethod(value.getClass(), methodIndex);
    Object result = null;
    try {
      result = m.invoke(value);
    } catch (Exception ex) {
      throw new IllegalStateException("error invoking property reader: obj="
          + value + " property=" + _properties[methodIndex], ex);
    }
    if (result instanceof Iterable<?>) {
      Iterable<?> iterable = (Iterable<?>) result;
      for (Object child : iterable) {
        invoke(child, methodIndex + 1, results);
      }
    } else if (result instanceof Object[]) {
      Object[] values = (Object[]) result;
      for (Object child : values) {
        invoke(child, methodIndex + 1, results);
      }
    } else {
      invoke(result, methodIndex + 1, results);
    }
  }

  private PropertyMethod getPropertyMethod(Class<?> valueType, int methodIndex) {
    PropertyMethod method = _methods[methodIndex];
    if (method == null) {
      method = _resolver.getPropertyMethod(valueType, _properties[methodIndex]);
      _methods[methodIndex] = method;
    }
    return method;
  }
}