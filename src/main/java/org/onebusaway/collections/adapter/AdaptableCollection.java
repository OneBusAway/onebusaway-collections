package org.onebusaway.collections.adapter;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

class AdaptableCollection<FROM, TO> extends AbstractCollection<TO> {

  private final Collection<FROM> _source;

  private final IAdapter<FROM, TO> _adapater;

  public AdaptableCollection(Collection<FROM> source,
      IAdapter<FROM, TO> adapater) {
    _source = source;
    _adapater = adapater;
  }

  @Override
  public Iterator<TO> iterator() {
    return AdapterLibrary.adaptIterator(_source.iterator(), _adapater);
  }

  @Override
  public int size() {
    return _source.size();
  }
}
