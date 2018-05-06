# MyBatis的解析和运行原理 
## 构建SqlSessionFactory过程
SqlSessionFactory提供创建MyBatis的核心接口SqlSession。MyBatis采用构造模式去创建SqlSessionFactory，我们可以通过SqlSessionFactoryBuilder去构建。

- 第一步，通过XMLConfigBuilder解析配置的XML文件，读出配置参数，并将读取的数据存入这个Configuration类中。
- 第二步，使用Configuration对象去创建SqlSessionFactory。

SqlSessionFactoryBuilder的源码：
```java
public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
    try {
      XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
      // XMLConfigBuilder解析配置的XML文件，构建Configuration
      return build(parser.parse());
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
      ErrorContext.instance().reset();
      try {
        inputStream.close();
      } catch (IOException e) {
        // Intentionally ignore. Prefer previous error.
      }
    }
}

// 使用Configuration对象去创建SqlSessionFactory
public SqlSessionFactory build(Configuration config) {
    // SqlSessionFactory是一个接口，为此MyBatis提供了一个默认实现类
    return new DefaultSqlSessionFactory(config);
}
```
XMLConfigBuilder的源码：
```java
public Configuration parse() {
    if (parsed) {
      throw new BuilderException("Each XMLConfigBuilder can only be used once.");
    }
    parsed = true;
    // 解析配置文件，设置Configuration
    parseConfiguration(parser.evalNode("/configuration"));
    return configuration;
  }

```

### 构建Configuration
在XMLConfigBuilder中，MyBatis会读出所有XML配置的信息，然后将这些信息保存到Configuration类的单例中。
它会做如下初始化：
- properties全局参数
- setting设置
- typeAliases别名
- typeHandler类型处理器
- ObjectFactory对象
- plugin插件
- environment环境
- DatabaseIdProvider数据库标识
- Mapper映射器

## SqlSession运行过程
### 映射器的动态代理
Mapper映射是通过动态代理实现的，MapperProxyFactory用来生成动态代理对象。
MapperProxyFactory的源码：
```java
protected T newInstance(MapperProxy<T> mapperProxy) {
    // 动态代理
    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
}

public T newInstance(SqlSession sqlSession) {
    final MapperProxy<T> mapperProxy = new MapperProxy<T>(sqlSession, mapperInterface, methodCache);
    return newInstance(mapperProxy);
}
```

MapperProxy实现InvocationHandler接口的代理方法invoke。
```java
@Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
        // 先判断是否一个类，在这里Mapper显然是一个接口
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, args);
        // 判断是不是接口默认实现方法
      } else if (isDefaultMethod(method)) {
        return invokeDefaultMethod(proxy, method, args);
      }
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }
    // 缓存中取出MapperMethod，不存在的话，则根据Configuration初始化一个
    final MapperMethod mapperMethod = cachedMapperMethod(method);
    // 执行Mapper接口的查询或其他方法
    return mapperMethod.execute(sqlSession, args);
}

private MapperMethod cachedMapperMethod(Method method) {
    MapperMethod mapperMethod = methodCache.get(method);
    if (mapperMethod == null) {
      mapperMethod = new MapperMethod(mapperInterface, method, sqlSession.getConfiguration());
      methodCache.put(method, mapperMethod);
    }
    return mapperMethod;
  }
```

MapperMethod采用命令模式运行，并根据上下文跳转。MapperMethod在构造器初始化时会根据Configuration和Mapper的Method方法解析为SqlCommand命令。之后在execute方法，根据SqlCommand的Type进行跳转。然后采用命令模式，SqlSession通过SqlCommand执行插入、更新、查询、选择等方法vv。
```java
public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
    // 根据Configuration和Mapper的Method方法解析为SqlCommand
    this.command = new SqlCommand(config, mapperInterface, method);
    this.method = new MethodSignature(config, mapperInterface, method);
}

public Object execute(SqlSession sqlSession, Object[] args) {
    Object result;
    // 根据Type进行跳转，通过sqlSession执行相关的操作
    switch (command.getType()) {
      case INSERT: {
    	Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.insert(command.getName(), param));
        break;
      }
      case UPDATE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.update(command.getName(), param));
        break;
      }
      case DELETE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.delete(command.getName(), param));
        break;
      }
      case SELECT:
        if (method.returnsVoid() && method.hasResultHandler()) {
          executeWithResultHandler(sqlSession, args);
          result = null;
        } else if (method.returnsMany()) {
          result = executeForMany(sqlSession, args);
        } else if (method.returnsMap()) {
          result = executeForMap(sqlSession, args);
        } else if (method.returnsCursor()) {
          result = executeForCursor(sqlSession, args);
        } else {
          Object param = method.convertArgsToSqlCommandParam(args);
          result = sqlSession.selectOne(command.getName(), param);
        }
        break;
      case FLUSH:
        result = sqlSession.flushStatements();
        break;
      default:
        throw new BindingException("Unknown execution method for: " + command.getName());
    }
    if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
      throw new BindingException("Mapper method '" + command.getName() 
          + " attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
    }
    return result;
}
```

### Sqlsession下的四大对象
我们已经知道了映射器其实就是一个动态代理对象，进入到了MapperMethod的execute方法。它经过简单判断就是进入了SqlSession的删除、更新、插入、选择等方法。其实SqlSession中的Executor执行器负责调度StatementHandler、ParameterHandler、ResultHandler等来执行相关的SQL。
- StatementHandler：使用数据库的Statement（PrepareStatement)执行操作
- ParameterHandler：用于SQL对参数的处理
- ResultHandler：进行最后数据集（ResultSet)的封装返回处理

Sqlsession其实是一个接口，它有一个DefaultSqlSession的默认实现类。

**DefaultSqlSession**的源码:
```java
// Executor执行器,负责调度SQL的执行
private final Executor executor;

@Override
public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
    try {
      MappedStatement ms = configuration.getMappedStatement(statement);
      // 通过executor执行查询操作
      return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
    } finally {
      ErrorContext.instance().reset();
    }
}
```

#### Executor执行器
执行器起到了至关重要的作用，它是一个真正执行Java和数据库交互的东西。在MyBatis中存在三种执行器，我们可以在MyBatis的配置文件中进行选择。
- SIMPLE,简易执行器
- REUSE，是一种执行器重用预处理语句
- BATCH，执行器重用语句和批量更新，她是针对批量专用的执行器

SimpleExecutor源代码：
```java
public class SimpleExecutor extends BaseExecutor {
    .....
    @Override
    public <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        Statement stmt = null;
        try {
            Configuration configuration = ms.getConfiguration();
            // 根据Configuration来构建StatementHandler
            StatementHandler handler = configuration.newStatementHandler(wrapper, ms, parameter, rowBounds, resultHandler, boundSql);
            // 对SQL编译并对参数进行初始化
            stmt = prepareStatement(handler, ms.getStatementLog());
            // 组装查询结果返回给调用者
            return handler.<E>query(stmt, resultHandler);
        } finally {
            closeStatement(stmt);
        }
    }

  private Statement prepareStatement(StatementHandler handler, Log statementLog) throws SQLException {
    Statement stmt;
    Connection connection = getConnection(statementLog);
    // 进行预编译和基础设置
    stmt = handler.prepare(connection, transaction.getTimeout());
    // 设置参数
    handler.parameterize(stmt);
    return stmt;
  }
}
```

#### StatementHandler数据库会话器
StatementHandler就是专门处理数据库会话的。

**创建StatementHandler：**
```java
public class Configuration {
    ......
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
        statementHandler = (StatementHandler) interceptorChain.pluginAll(statementHandler);
        return statementHandler;
  }
}
```

RoutingStatementHandler其实不是我们真实的服务对象，它是通过适配模式找到对应的StatementHandler来执行。
StatementHandler分为三种：
- SimleStatementHandler
- PrepareStatementHandler
- CallableStatementHandler

在初始化RoutingStatementHandler对象的时候它会根据上下文环境来决定创建哪个StatementHandler对象。
```java
public class RoutingStatementHandler implements StatementHandler {
    ......
    private final StatementHandler delegate;

    public RoutingStatementHandler(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
    switch (ms.getStatementType()) {
      case STATEMENT:
        delegate = new SimpleStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
        break;
      case PREPARED:
        delegate = new PreparedStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
        break;
      case CALLABLE:
        delegate = new CallableStatementHandler(executor, ms, parameter, rowBounds, resultHandler, boundSql);
        break;
      default:
        throw new ExecutorException("Unknown statement type: " + ms.getStatementType());
    }
  }
}
```

数据库会话器定义了一个对象的适配器delegate，它是一个StatementHandler接口对象，构造器根据配置来适配对应的StatementHandler对象。它的作用是给实现类对象的使用提供一个统一、简易的使用适配器。此为对象的适配模式，可以让我们使用现有的类和方法对外提供服务，也可以根据实际的需求对外屏蔽一些方法，甚至加入新的服务。

以PreparedStatementHandler为例，看看它的三个主要方法:prepare、parameterize和query。
- prepare

```java
public abstract class BaseStatementHandler implements StatementHandler {
    @Override
    public Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException {
        ErrorContext.instance().sql(boundSql.getSql());
        Statement statement = null;
        try {
            // 对SQL进行了预编译
            statement = instantiateStatement(connection);
            setStatementTimeout(statement, transactionTimeout);
            setFetchSize(statement);
            return statement;
        } catch (SQLException e) {
            closeStatement(statement);
        throw e;
        } catch (Exception e) {
            closeStatement(statement);
            throw new ExecutorException("Error preparing statement.  Cause: " + e, e);
    }
  }
}
```
- parameterize和query
```java
public class PreparedStatementHandler extends BaseStatementHandler {
    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        // resultSetHandler封装结果返回
        return resultSetHandler.<E> handleResultSets(ps);
    } 

    @Override
    public void parameterize(Statement statement) throws SQLException {
        // 设置参数
        parameterHandler.setParameters((PreparedStatement) statement);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = boundSql.getSql();
        if (mappedStatement.getKeyGenerator() instanceof Jdbc3KeyGenerator) {
        String[] keyColumnNames = mappedStatement.getKeyColumns();
        if (keyColumnNames == null) {
            return connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        } else {
            return connection.prepareStatement(sql, keyColumnNames);
        }
        } else if (mappedStatement.getResultSetType() != null) {
            return connection.prepareStatement(sql, mappedStatement.getResultSetType().getValue(), ResultSet.CONCUR_READ_ONLY);
        } else {
            return connection.prepareStatement(sql);
        }
    }
}
```