package org.onebusaway.collections.adapter;

import java.util.Map;
import java.util.Map.Entry;

class AdaptableValueMapEntry<KEY, FROM_VALUE, TO_VALUE> implements
    Map.Entry<KEY, TO_VALUE> {

  private final Entry<KEY, FROM_VALUE> _source;

  private final IAdapter<FROM_VALUE, TO_VALUE> _adapter;

  public AdaptableValueMapEntry(Map.Entry<KEY, FROM_VALUE> source,
      IAdapter<FROM_VALUE, TO_VALUE> adapter) {
    _source = source;
    _adapter = adapter;
  }

  @Override
  public KEY getKey() {
    return _source.getKey();
  }

  @Override
  public TO_VALUE getValue() {
    return AdapterLibrary.apply(_adapter, _source.getValue());
  }

  @Override
  public TO_VALUE setValue(TO_VALUE value) {
    throw new UnsupportedOperationException();
  }
}
