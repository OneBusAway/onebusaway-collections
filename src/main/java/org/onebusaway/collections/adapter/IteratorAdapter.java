package org.onebusaway.collections.adapter;

import java.util.Iterator;

class IteratorAdapter<FROM, TO> implements Iterator<TO> {

  private final Iterator<FROM> _it;

  private final IAdapter<FROM, TO> _adapter;

  public IteratorAdapter(Iterator<FROM> it, IAdapter<FROM, TO> adapter) {
    _it = it;
    _adapter = adapter;
  }

  public boolean hasNext() {
    return _it.hasNext();
  }

  public TO next() {
    return _adapter.adapt(_it.next());
  }

  public void remove() {
    _it.remove();
  }
}