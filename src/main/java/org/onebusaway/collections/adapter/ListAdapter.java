package org.onebusaway.collections.adapter;

import java.util.AbstractList;
import java.util.List;

public class ListAdapter<FROM, TO> extends AbstractList<TO> {

  private final List<FROM> _source;

  private final IAdapter<FROM, TO> _adapter;

  public ListAdapter(List<FROM> source, IAdapter<FROM, TO> adapter) {
    _source = source;
    _adapter = adapter;
  }

  @Override
  public TO get(int index) {
    FROM v = _source.get(index);
    return _adapter.adapt(v);
  }

  @Override
  public int size() {
    return _source.size();
  }
}
