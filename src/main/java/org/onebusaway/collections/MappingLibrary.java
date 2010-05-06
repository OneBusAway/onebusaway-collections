/*
 * Copyright 2008-2010 Brian Ferris
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MappingLibrary {

  @SuppressWarnings("unchecked")
  public static <T1, T2> List<T2> map(Iterable<T1> values, String property,
      Class<T2> propertyType) {
    List<T2> mappedValues = new ArrayList<T2>();
    SimplePropertyQuery query = new SimplePropertyQuery(property);
    for (T1 value : values)
      mappedValues.add((T2) query.invoke(value));
    return mappedValues;
  }

  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, V> mapToValue(Iterable<V> values,
      String property, Class<K> keyType) {

    Map<K, V> byKey = new HashMap<K, V>();
    SimplePropertyQuery query = new SimplePropertyQuery(property);

    for (V value : values) {
      K key = (K) query.invoke(value);
      byKey.put(key, value);
    }

    return byKey;
  }

  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, List<V>> mapToValueList(Iterable<V> values,
      String property, Class<K> keyType) {
    return mapToValueCollection(values, property, keyType,
        new ArrayList<V>().getClass());
  }

  @SuppressWarnings("unchecked")
  public static <K, V> Map<K, Set<V>> mapToValueSet(Iterable<V> values,
      String property, Class<K> keyType) {
    return mapToValueCollection(values, property, keyType,
        new HashSet<V>().getClass());
  }

  @SuppressWarnings("unchecked")
  public static <K, V, C extends Collection<V>, CIMPL extends C> Map<K, C> mapToValueCollection(
      Iterable<V> values, String property, Class<K> keyType,
      Class<CIMPL> collectionType) {

    Map<K, C> byKey = new HashMap<K, C>();
    SimplePropertyQuery query = new SimplePropertyQuery(property);

    for (V value : values) {

      K key = (K) query.invoke(value);
      C valuesForKey = byKey.get(key);
      if (valuesForKey == null) {

        try {
          valuesForKey = collectionType.newInstance();
        } catch (Exception ex) {
          throw new IllegalStateException(
              "error instantiating collection type: " + collectionType, ex);
        }

        byKey.put(key, valuesForKey);
      }
      valuesForKey.add(value);
    }

    return byKey;
  }

  /*****************************************************************************
   * Private Static Classes
   ****************************************************************************/

  private static final class SimplePropertyQuery {

    private String[] _properties;

    private Method[] _methods;

    public SimplePropertyQuery(String query) {
      _properties = query.split("\\.");
      _methods = new Method[_properties.length];
    }

    public Object invoke(Object value) {
      for (int i = 0; i < _properties.length; i++) {
        Method m = _methods[i];
        if (m == null) {
          try {
            BeanInfo info = Introspector.getBeanInfo(value.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
              if (pd.getName().equals(_properties[i])) {
                m = pd.getReadMethod();
                break;
              }
            }
          } catch (IntrospectionException ex) {
            throw new IllegalStateException("error introspecting bean class: "
                + value.getClass(), ex);
          }

          if (m == null)
            throw new IllegalStateException("could not find property: "
                + _properties[i]);

          _methods[i] = m;
        }

        try {
          value = m.invoke(value);
        } catch (Exception ex) {
          throw new IllegalStateException(
              "error invoking property reader: obj=" + value + " property="
                  + _properties[i]);
        }
      }
      return value;
    }
  }
}
