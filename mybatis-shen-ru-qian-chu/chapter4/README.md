# chapter4
## 自动映射
**mybatis-config.xml**

autoMappingBehavior
mapUnderscoreToCamelCase 

## 传递多个参数
**mapper/roleMapper.xml**

- 使用Map传递参数
- 使用注解方式传递参数
- 使用JavaBean传递参数

## insert 主键回填和自定义
**mapper/roleMapper.xml**

useGeneratedKeys
keyProperty

## sql元素
**mapper/roleMapper.xml**

```xml
<sql></sql>
```

## 级联
**mapper/studentMapper.xml**
**mapper/studentSelfcardMapper.xml**

- association 一对一关系
- collection 一对多关系
- discriminator 鉴别器