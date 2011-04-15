package org.onebusaway.collections.adapter;

import java.util.AbstractList;
import java.util.List;

/**
 * Create an adapted {@link List} instance that adapts a list of type FROM to
 * type TO using a {@link IAdapter} instance. The adapted list will be immutable
 * but will reflect changes to the underlying list.
 * 
 * @author bdferris
 * 
 * @param <FROM>
 * @param <TO>
 */
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
