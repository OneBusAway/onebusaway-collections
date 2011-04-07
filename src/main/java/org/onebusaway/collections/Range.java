package org.onebusaway.collections;

public class Range {

  private double _min = Double.POSITIVE_INFINITY;

  private double _max = Double.NEGATIVE_INFINITY;

  public void addValue(double value) {
    _min = Math.min(_min, value);
    _max = Math.max(_max, value);
  }

  public void setMin(double value) {
    _min = value;
    _max = Math.max(_max, value);
  }

  public void setMax(double value) {
    _min = Math.min(_min, value);
    _max = value;
  }

  public double getMin() {
    return _min;
  }

  public double getMax() {
    return _max;
  }

  public double getRange() {
    return _max - _min;
  }

  public boolean isEmpty() {
    return _min > _max;
  }

  @Override
  public String toString() {
    return _min + " " + _max;
  }
}
