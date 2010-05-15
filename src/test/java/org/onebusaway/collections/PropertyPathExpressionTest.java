package org.onebusaway.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class PropertyPathExpressionTest {

  @Test
  public void test() {

    A obj = new A();

    PropertyPathExpression expr = new PropertyPathExpression("stringValue");
    assertEquals("string-value", expr.invoke(obj));

    expr = new PropertyPathExpression("integerValue");
    assertEquals(new Integer(31), expr.invoke(obj));

    expr = new PropertyPathExpression("doubleValue");
    assertEquals(new Double(3.14), expr.invoke(obj));

    expr = new PropertyPathExpression("nullA");
    assertNull(expr.invoke(obj));

    expr = new PropertyPathExpression("depth");
    assertEquals(new Integer(0), expr.invoke(obj));

    expr = new PropertyPathExpression("a.depth");
    assertEquals(new Integer(1), expr.invoke(obj));

    expr = new PropertyPathExpression("a.a.depth");
    assertEquals(new Integer(2), expr.invoke(obj));

    expr = new PropertyPathExpression("a.doubleValue");
    assertEquals(new Double(3.14), expr.invoke(obj));

    expr = new PropertyPathExpression("dne");

    try {
      expr.invoke(obj);
      fail();
    } catch (IllegalStateException ex) {

    }

    expr = new PropertyPathExpression("nullA.depth");

    try {
      expr.invoke(obj);
      fail();
    } catch (IllegalStateException ex) {

    }

    assertEquals(new Double(3.14), PropertyPathExpression.evaluate(obj,
        "a.doubleValue"));

  }

  public static class A {

    private int _depth;

    public A() {
      _depth = 0;
    }

    public A(int depth) {
      _depth = depth;
    }

    public int getDepth() {
      return _depth;
    }

    public String getStringValue() {
      return "string-value";
    }

    public int getIntegerValue() {
      return 31;
    }

    public double getDoubleValue() {
      return 3.14;
    }

    public A getA() {
      return new A(_depth + 1);
    }

    public A getNullA() {
      return null;
    }
  }
}
