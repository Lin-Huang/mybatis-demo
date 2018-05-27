# 插件
# 1、插件接口
在MyBatis中使用插件，我们必须实现接口Interceptor。
```java
public interface Interceptor {
  // 它将直接覆盖你所拦截对象原有的方法，因此它是插件的核心方法。
  // Intercept里面有个参数Invocation对象，通过它可以反射调度原来对象的方法
  Object intercept(Invocation invocation) throws Throwable;

  // 作用是给被拦截对象生成一个代理对象，并返回它。target是被拦截对象
  Object plugin(Object target);

  // 允许在plugin元素中配置所需参数，方法在插件初始化的时候就被调用了一次
  void setProperties(Properties properties);

}
```

## 2、插件初始化
插件的初始化是在MyBatis初始化的时候完成的。
```java
public class XMLConfigBuilder extends BaseBuilder {
  ......
  private void pluginElement(XNode parent) throws Exception {
    if (parent != null) {
      for (XNode child : parent.getChildren()) {
        String interceptor = child.getStringAttribute("interceptor");
        Properties properties = child.getChildrenAsProperties();
        Interceptor interceptorInstance = (Interceptor) resolveClass(interceptor).newInstance();
        interceptorInstance.setProperties(properties);
        configuration.addInterceptor(interceptorInstance);
      }
    }
  }
}
```
在解析配置文件的时候，在MyBatis的上下文初始化过程中，就开始读入插件节点和我们配置的参数，同时使用反射技术生成对应的插件实例，然后调用插件方法中的setProperties方法，设置我们配置的参数，然后将插件实例保存到配置对象中，以便读取和使用它。

插件在Configuration对象中的保存：
```java
  public void addInterceptor(Interceptor interceptor) {
    interceptorChain.addInterceptor(interceptor);
  }
```

## 3、插件的代理和反射设计
插件用的是责任链模式，MyBatis的责任链是由interceptorChain去定义的。在MyBatis创建Executor执行器的时候，我们可以看到有如下的一段代码：
```java
executor = (Executor) interceptorChain.pluginAll(executor);
```
再看下Interceptor的pluginAll方法：
```java
public class InterceptorChain {
  private final List<Interceptor> interceptors = new ArrayList<Interceptor>();

  public Object pluginAll(Object target) {
    for (Interceptor interceptor : interceptors) {
      // plugin方法是生成代理对象的方法
      // 可以看出来，如果有多个插件的话，会生成多层代理的代理对象
      target = interceptor.plugin(target);
    }
    return target;
  }
  ......
}
```

MyBatis为我们提供了Plugin类用于生成代理对象。
```java
public class Plugin implements InvocationHandler {
  ......

  public static Object wrap(Object target, Interceptor interceptor) {
    Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
    Class<?> type = target.getClass();
    Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
    if (interfaces.length > 0) {
      return Proxy.newProxyInstance(
          type.getClassLoader(),
          interfaces,
          new Plugin(target, interceptor, signatureMap));
    }
    return target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      Set<Method> methods = signatureMap.get(method.getDeclaringClass());
      // 如果存在签名的拦截方法，插件的intercept方法将被调用
      if (methods != null && methods.contains(method)) {
        return interceptor.intercept(new Invocation(target, method, args));
      }
      // 否则，直接反射调度我们要执行的方法
      return method.invoke(target, args);
    } catch (Exception e) {
      throw ExceptionUtil.unwrapThrowable(e);
    }
  }
}
```

在调用插件的拦截方法时，可以看到传递了一个新创建的Invocation对象。
```java
interceptor.intercept(new Invocation(target, method, args));
```
Invocation类封装了被代理的对象、方法及其参数。
```java
public class Invocation {
  public Invocation(Object target, Method method, Object[] args) {
    this.target = target;
    this.method = method;
    this.args = args;
  }

  // 这个方法会调度被代理对象的真实方法， 所以我们通过这个方法直接调用被代理对象原来的方法
  // 如果多个插件的话，我们知道会生成多层代理对象，那么每层被代理都可以通过Invocation调用这个proceed方法，
  // 所以在多个插件的环境下，调度proceed()方法时，MyBatis总是从最后一个代理对象运行到第一个代理对象，
  // 最后是真实被拦截的对象方法被运行
  public Object proceed() throws InvocationTargetException, IllegalAccessException {
    return method.invoke(target, args);
  }
}
```