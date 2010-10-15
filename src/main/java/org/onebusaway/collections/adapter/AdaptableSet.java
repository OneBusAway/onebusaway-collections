package org.onebusaway.collections.adapter;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

class AdaptableSet<FROM, TO> extends AbstractSet<TO> {

  private final Set<FROM> _source;

  private final IAdapter<FROM, TO> _adapter;

  public AdaptableSet(Set<FROM> source, IAdapter<FROM, TO> adapter) {
    _source = source;
    _adapter = adapter;
  }

  @Override
  public Iterator<TO> iterator() {
    return AdapterLibrary.adaptIterator(_source.iterator(), _adapter);
  }

  @Override
  public int size() {
    return _source.size();
  }

}
