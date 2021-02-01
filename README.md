## Java高并发秒杀API之业务分析与DAO层

课程内容

1、SpringMVC + Spring + MyBatis使用与整合
2、秒杀类系统需求理解和实现
3、常用技术解决高并发问题

框架优势：
1. 框架易于使用和轻量级
2. 低代码侵入性
3. 成熟的社区和用户群

案例代表性：
1. 秒杀业务 具有“事物”特性
2. 秒杀/红包 类需求越来越常见

## 相关技术

1、MySQL
- 表设计
- SQL技巧
- 事务和行级锁

2、MyBatis
- DAO层设计与开发
- MyBatis合理使用
- MyBatis与Spring整合

3、Spring
- Spring IOC整合Service
- 声明式事务运用

4、SpringMVC
- Restful接口设计和使用
- 框架运作流程
- Controller开发技巧

5、前端
- 交互设计
- Bootstrap
- jQuery

6、高并发
- 高并发点和高并发分析
- 优化思路并实现

## 创建项目和依赖

JDK 1.8
logback
spring
mybatis
maven 3.2

1、Maven创建Web项目骨架

```bash
# maven3.0.5以下
mvn archetype:create \
-DgroupId=org.seckill \
-DartifactId=seckill \
-DarchetypeArtifactId=maven-archetype-webapp

# maven3.0.5及以上
mvn archetype:generate \
-DgroupId=org.seckill \
-DartifactId=seckill \
-DarchetypeArtifactId=maven-archetype-webapp
```

2、生成完项目骨架之后需要：

（1）补全缺失的目录java、test/java、test/resources

（2）需要升级servlet为3版本 web.xml可以从tomcat示例文件中拷贝

（3）需要升级pom.xml文件中的junit为4版本


3、日志框架：

- 接口规范：slf4j
- 日志实现：log4j、logback、common-logging
- 常用组合：slf4j + logback

4、完整的目录结构
```
.
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   ├── resources
    │   └── webapp
    │       ├── WEB-INF
    │       │   └── web.xml
    │       └── index.jsp
    └── test
        ├── java
        └── resources

```

5、升级Servlet web.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>

<!--从tomcat 示例文件拷贝 升级servlet版本-->
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0"
         metadata-complete="true">
</web-app>

```

6、添加依赖 pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/maven-v4_0_0.xsd">
    
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.seckill</groupId>
    <artifactId>seckill</artifactId>
    <version>1.0-SNAPSHOT</version>

    <packaging>war</packaging>

    <name>seckill Maven Webapp</name>
    <url>http://maven.apache.org</url>

    <dependencies>
        <!-- 使用junit4 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13</version>
            <scope>test</scope>
        </dependency>

        <!-- 1、日志依赖 -->
        <!--接口规范-->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.30</version>
        </dependency>

        <!--日志实现-->
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-core</artifactId>
            <version>1.2.3</version>
        </dependency>

        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.3</version>
        </dependency>

        <!-- 2、数据库依赖-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.22</version>
            <scope>runtime</scope>
        </dependency>

        <!--连接池-->
        <dependency>
            <groupId>c3p0</groupId>
            <artifactId>c3p0</artifactId>
            <version>0.9.1.2</version>
        </dependency>

        <!-- 3、Dao框架 MyBatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.6</version>
        </dependency>

        <!-- mybatis自己实现的整合依赖 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.6</version>
        </dependency>

        <!--    4、Servlet web相关 JSP -->
        <!--标签-->
        <dependency>
            <groupId>taglibs</groupId>
            <artifactId>standard</artifactId>
            <version>1.1.2</version>
        </dependency>

        <!--默认标签库-->
        <dependency>
            <groupId>jstl</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>

        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.2</version>
        </dependency>

        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
        </dependency>

        <!--    5、Spring依赖-->
        <!-- (1) Spring核心-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>5.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>5.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.3.0</version>
        </dependency>

        <!-- (2) Spring Dao层依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-jdbc</artifactId>
            <version>5.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>5.3.0</version>
        </dependency>

        <!-- (3) Spring Web层依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>5.3.0</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.3.0</version>
        </dependency>

        <!-- (4) Spring test 依赖-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>5.3.0</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>seckill</finalName>
    </build>
</project>

```

## 秒杀业务分析

1、秒杀业务分析

业务核心：对库存的处理

用户对库存的业务分析

（减库存 + 记录购买明细）完整事务->数据落地
- 完整事务：没有事务会出现超卖或少卖
- 数据落地：MySQL(事务机制)或NoSQL

用户购买行为，记录秒杀成功信息
- 谁购买成功了
- 成功的时间
- 付款发货信息

2、MySQL实现秒杀难点分析

事务 + 行级锁

事务
```
start Transaction
update 库存数据量 （竞争）
insert 购买明细
commit
```

行级锁

```sql
-- 执行 
update table set num = num - 1 where id = :id and num > 1

-- 等待 
update table set num = num - 1 where id = :id and num > 1
```


3、实现哪些秒杀功能

秒杀功能
- 秒杀接口暴露
- 执行秒杀
- 相关查询

开发阶段
- DAO
- Service
- Web

## DAO层设计与开发

1、数据库设计与编码

Table -> Entity

秒杀项目代码

数据库 通过MyBatis映射 实体对象

MyBatis
SQL + 参数 => Entity/List

SQL:
XML实现SQL（推荐）
注解提供SQL

DAO接口实现：
Mapper自动实现DAO接口（推荐）
API编程方式实现DAO接口

 
## Spring整合MyBatis
更少的编码：只写接口，不写实现类
更少的配置：别名，配置扫描
足够的灵活：结果集自动赋值

XML提供SQL
DAO接口提供Mapper

知识点：

1. command + N 快速生成测试代码
2. @Param 运行期参数, 如果不指定，会抛出异常 BindingException
```
java运行时没有保存形参的记录

queryAll(int offset, int limit);
=》
queryAll(int arg0, int arg1);
```

## Java高并发秒杀API之Service层

Dao层工作演变为：接口设计 + SQL编写
代码和SQL分离，方便review
Dao拼接等逻辑放在Service层完成

entity包 数据库对应表结构
dto包 service层和web层数据传递

IOC的优点：

对象创建统一托管
规范的生命周期管理
灵活的依赖注入
一致的获取对象

Spring-IOC注入方式和场景
1. XML： 1.Bean实现类来自第三方类库，如：DataSource等。2.需要命名空间配置，如：context，aop，mvc等。
2. 注解：项目中自身开发使用的类，可直接在代码中使用注解如：@Service,@Controller等。
3. Java配置类： 需要通过代码控制对象创建逻辑的场景。如：自定义修改依赖类库。


声明式事务

```
=> 开启事务
修改SQL-1
修改SQL-2
修改SQL-3
=> 提交/回滚事务
```

声明式事务使用方式
1. 早期使用方式2.0  ProxyFactoryBean + XML
2. 一次配置永久生效 tx:advice+aop命名空间
3. 注解控制（推荐） 注解@Transactional


声明式事务独有概念：
传播行为 默认 传播

回滚时机：
抛出运行期异常RuntineException

使用注解控制事务方法的优点：

1. 开发团队达成一致，明确标注事务方法的编程风格
2. 保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求，或者剥离到事务方法外
3. 不是所有的方法都需要事务，如：只有一条修改操作，只读操作不需要事务



## Java高并发秒杀API之Web 层

前端交互设计
Restful：URI的设计规范
重点内容，配置，如何应用，设计和实现
Bootstrap 页面布局和样式控制 jquery：交互的实现


Representational State Transfer表述性状态转移

Restful规范

GET 查询操作
POST 添加/修改操作
PUT 修改操作
DELETE 删除操作

秒杀API的URL设计
GET /seckill/list 秒杀列表
GET /seckill/{id}/detail 详情页
GET /seckill/time/now 系统时间
POST /seckill/{id}/exposer 暴露秒杀
POST /seckill/{id}/{md5}/execution 执行秒杀


注解映射
@RequestMapping注解
（1）支持标准的URL
（2）Ant风格的URL(即?、*、**等字符)
（3）带{xxx}占位符的URL

eg:
```
/user/*  => /user/aa ; /user/bb
/user/** => /user/aa/bb
/user/{id} => /user/12
/user/{id}/detail => /user/12/detail
```

请求方法处理
1. 请求参数绑定
2. 请求方法限制
3. 请求转发和重定向
4. 数据模型赋值
5. 返回json数据
6. cookie访问


```java
package org.seckill.controller;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SeckillController {

    @Autowired
    SeckillService seckillService;

    // 返回html
    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId,
                         Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list"; // 重定向
        }

        Seckill seckill = seckillService.getById(seckillId);

        if (seckill == null) {
            return "forward:/seckill/list"; // 转发
        }

        model.addAttribute("seckill", "seckill"); // model
        return "detail"; // view
    }

    // 返回json数据，访问cookie
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.GET,
            produces = {"application/json;charset=UTF-8"}
    )
    @ResponseBody
    public SeckillResult<Exposer> exportSeckillUrl(
            @PathVariable("seckillId") Long seckillId,
            @PathVariable("md5") String md5,
            @CookieValue(value = "killPhone", required = false) Long killPhone
    ) {

        SeckillResult<Exposer> result = new SeckillResult<>();

        return result;
    }
}

```

前端页面使用bootstrap
https://www.runoob.com/bootstrap/bootstrap-environment-setup.html

下载所需要的插件 https://www.bootcdn.cn/

基于SpringBoot+Mybatis+Thymeleaf的秒杀
https://github.com/js3560750/mySecondKill

课程总结回顾

1、前端交互设计过程
- Bootstrap
- javascript模块化
- jQuery和 plugin

2、Restful接口设计
- SpringMVC
- DTO传递数据
- 注解映射驱动

## Java高并发秒杀API之高并发优化

高并发优化

1、前端控制：
- 暴露接口
- 按钮防重复

2、动静态数据分离：
- CDN缓存(CDN内容分发网络 加速用户获取数据的系统)
- 后端缓存 Redis

3、事务竞争优化：
- 减少事务锁时间


```sql
update table set num = num - 1
where id = 10 and num > 0 -- 会出现等待行锁

insert ...

commit/rollback
```

瓶颈分析
```
update 减库存 (网络延迟、GC)

insert 购买明细 (网络延迟、GC)

commit/rollback 释放行级锁
```

优化方案：将客户端过程放到MySQL服务端

1. 定制SQL方案
2. 使用存储过程
