package me.seriouszyx.DynamicProxy;

import java.lang.Override;
import java.lang.reflect.Method;
import me.seriouszyx.base.Flyable;

public class TimeProxy implements Flyable {
  private InvocationHandler handler;

  public TimeProxy(InvocationHandler handler) {
    this.handler = handler;
  }

  @Override
  public void fly() {
    try {
    	Method method = me.seriouszyx.base.Flyable.class.getMethod("fly");
    	this.handler.invoke(this, method, null);
    } catch(Exception e) {
    	e.printStackTrace();
    }
  }
}
