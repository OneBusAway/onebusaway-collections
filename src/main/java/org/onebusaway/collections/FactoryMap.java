/*
 * Copyright 2008 Brian Ferris
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

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class FactoryMap<K, V> extends HashMap<K, V> {

  private static final long serialVersionUID = 1L;

  private IValueFactory<K, V> _valueFactory;

  public interface IValueFactory<KF, VF> {
    public VF create(KF key);
  }

  public static <K, V> Map<K, V> create(Map<K, V> map, V defaultValue) {
    return new MapImpl<K, V>(map, new ClassInstanceFactory<K, V>(
        defaultValue.getClass()));
  }

  public static <K, V> Map<K, V> create(Map<K, V> map,
      IValueFactory<K, V> factory) {
    return new MapImpl<K, V>(map, factory);
  }

  public static <K, V> SortedMap<K, V> createSorted(SortedMap<K, V> map,
      V defaultValue) {
    return new SortedMapImpl<K, V>(map, new ClassInstanceFactory<K, V>(
        defaultValue.getClass()));
  }

  public static <K, V> SortedMap<K, V> createSorted(SortedMap<K, V> map,
      IValueFactory<K, V> factory) {
    return new SortedMapImpl<K, V>(map, factory);
  }

  public FactoryMap(V factoryInstance) {
    this(new ClassInstanceFactory<K, V>(factoryInstance.getClass()));
  }

  public FactoryMap(IValueFactory<K, V> valueFactory) {
    _valueFactory = valueFactory;
  }

  @SuppressWarnings("unchecked")
  @Override
  public V get(Object key) {
    if (!containsKey(key))
      put((K) key, createValue((K) key));
    return super.get(key);
  }

  private V createValue(K key) {
    return _valueFactory.create(key);
  }

  private static class ClassInstanceFactory<K, V> implements
      IValueFactory<K, V>, Serializable {

    private static final long serialVersionUID = 1L;

    private Class<? extends V> _valueClass;

    @SuppressWarnings("unchecked")
    public ClassInstanceFactory(Class valueClass) {
      _valueClass = valueClass;
    }

    public V create(K key) {
      try {
        return _valueClass.newInstance();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
  }

  private static class MapImpl<K, V> implements Map<K, V> {

    private Map<K, V> _source;

    private IValueFactory<K, V> _valueFactory;

    public MapImpl(Map<K, V> source, IValueFactory<K, V> valueFactory) {
      _source = source;
      _valueFactory = valueFactory;
    }

    public void clear() {
      _source.clear();
    }

    public boolean containsKey(Object key) {
      return _source.containsKey(key);
    }

    public boolean containsValue(Object value) {
      return _source.containsValue(value);
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
      return _source.entrySet();
    }

    @SuppressWarnings("unchecked")
    public V get(Object key) {
      if (!containsKey(key))
        _source.put((K) key, createValue((K) key));
      return _source.get(key);
    }

    public boolean isEmpty() {
      return _source.isEmpty();
    }

    public Set<K> keySet() {
      return _source.keySet();
    }

    public V put(K key, V value) {
      return _source.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> t) {
      _source.putAll(t);
    }

    public V remove(Object key) {
      return _source.remove(key);
    }

    public int size() {
      return _source.size();
    }

    public Collection<V> values() {
      return _source.values();
    }

    private V createValue(K key) {
      return _valueFactory.create(key);
    }
  }

  private static class SortedMapImpl<K, V> extends MapImpl<K, V> implements
      SortedMap<K, V> {

    private SortedMap<K, V> _source;

    public SortedMapImpl(SortedMap<K, V> source,
        IValueFactory<K, V> valueFactory) {
      super(source, valueFactory);
      _source = source;
    }

    public Comparator<? super K> comparator() {
      return _source.comparator();
    }

    public K firstKey() {
      return _source.firstKey();
    }

    public SortedMap<K, V> headMap(K toKey) {
      return _source.headMap(toKey);
    }

    public K lastKey() {
      return _source.lastKey();
    }

    public SortedMap<K, V> subMap(K fromKey, K toKey) {
      return _source.subMap(fromKey, toKey);
    }

    public SortedMap<K, V> tailMap(K fromKey) {
      return _source.tailMap(fromKey);
    }
  }
}
