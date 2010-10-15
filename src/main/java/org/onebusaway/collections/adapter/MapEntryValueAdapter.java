package org.onebusaway.collections.adapter;

import java.util.Map.Entry;

class MapEntryValueAdapter<KEY, FROM_VALUE, TO_VALUE> implements
    IAdapter<Entry<KEY, FROM_VALUE>, Entry<KEY, TO_VALUE>> {

  private IAdapter<FROM_VALUE, TO_VALUE> _adapter;

  public MapEntryValueAdapter(IAdapter<FROM_VALUE, TO_VALUE> adapter) {
    _adapter = adapter;
  }

  @Override
  public Entry<KEY, TO_VALUE> adapt(Entry<KEY, FROM_VALUE> source) {
    TO_VALUE v = AdapterLibrary.apply(_adapter, source.getValue());
    return new EntryImpl<KEY, TO_VALUE>(source.getKey(), v);
  }

  private static class EntryImpl<K, V> implements Entry<K, V> {

    private final K _key;
    private final V _value;

    public EntryImpl(K key, V value) {
      _key = key;
      _value = value;
    }

    @Override
    public K getKey() {
      return _key;
    }

    @Override
    public V getValue() {
      return _value;
    }

    @Override
    public V setValue(V value) {
      throw new UnsupportedOperationException();
    }
  }

}
