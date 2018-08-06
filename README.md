# 架构实战篇（十四）：Spring Boot 多缓存实战

![](https://upload-images.jianshu.io/upload_images/9260441-0b7c668f4d9c0404.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 前言
一个程序少不了对数据库的增删改查操作，我们也知道内存的速度是大大快于硬盘的速度的。当我们需要重复地获取相同的数据的时候，我们一次又一次的请求数据库或者远程服务，导致大量的时间耗费在数据库查询或者远程方法调用上，程序性能也就大打折扣，这便是数据缓存要解决的问题。

Spring boot 给我们提供了便利的缓存注解，也大大了减少了系统的耦合
![](https://upload-images.jianshu.io/upload_images/9260441-3e644a36d890d0f2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

需要解决的问题
1. 怎么用缓存减少一对多关系的频繁数据库访问
2. 怎么用缓存解决高并发的数据服务性能

下面给出一些源码和讲解

## 目录结构
![](https://upload-images.jianshu.io/upload_images/9260441-ee1970ee4f5014f3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 项目依赖配置
pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.itunion</groupId>
    <artifactId>spring-boot-cache</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>spring-boot-cache</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.ben-manes.caffeine</groupId>
            <artifactId>caffeine</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.27</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <!--<version>5.0.0</version> -->
            <version>4.1.6</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```
这里主要使用了spring boot 邮件的依赖 spring-boot-starter-cache 和 com.github.ben-manes.caffeine

## 实体类
为了方便测试我们直接使用了 mysql 自带的表, 注意这里的 toString 方法需要重写下，后面会用作缓存的Key
HelpTopic.java
```java
package com.itunion.demo.domain;

import java.io.Serializable;

public class HelpTopic implements Serializable{
  
    private Integer helpTopicId; /***/
    private String name; /***/
    private Integer helpCategoryId; /***/
    private Object description; /***/
    private Object example; /***/
    private String url; /***/

	public Integer getHelpTopicId(){
	    return this.helpTopicId;
    }
    public void setHelpTopicId(Integer helpTopicId){
	    this.helpTopicId = helpTopicId;
    }
	public String getName(){
	    return this.name;
    }
    public void setName(String name){
	    this.name = name;
    }
	public Integer getHelpCategoryId(){
	    return this.helpCategoryId;
    }
    public void setHelpCategoryId(Integer helpCategoryId){
	    this.helpCategoryId = helpCategoryId;
    }
	public Object getDescription(){
	    return this.description;
    }
    public void setDescription(Object description){
	    this.description = description;
    }
	public Object getExample(){
	    return this.example;
    }
    public void setExample(Object example){
	    this.example = example;
    }
	public String getUrl(){
	    return this.url;
    }
    public void setUrl(String url){
	    this.url = url;
    }

    @Override
    public String toString() {
        return "HelpTopic{" +
                "helpTopicId=" + helpTopicId +
                ", name='" + name + '\'' +
                ", helpCategoryId=" + helpCategoryId +
                ", description=" + description +
                ", example=" + example +
                ", url='" + url + '\'' +
                '}';
    }
}
```

## 关系实体类
HelpCategory.java
```java
package com.itunion.demo.domain;

import java.io.Serializable;

public class HelpCategory implements Serializable{
  
    private Integer helpCategoryId; /***/
    private String name; /***/
    private Integer parentCategoryId; /***/
    private String url; /***/

	public Integer getHelpCategoryId(){
	    return this.helpCategoryId;
    }
    public void setHelpCategoryId(Integer helpCategoryId){
	    this.helpCategoryId = helpCategoryId;
    }
	public String getName(){
	    return this.name;
    }
    public void setName(String name){
	    this.name = name;
    }
	public Integer getParentCategoryId(){
	    return this.parentCategoryId;
    }
    public void setParentCategoryId(Integer parentCategoryId){
	    this.parentCategoryId = parentCategoryId;
    }
	public String getUrl(){
	    return this.url;
    }
    public void setUrl(String url){
	    this.url = url;
    }

    @Override
    public String toString() {
        return "HelpCategory{" +
                "helpCategoryId=" + helpCategoryId +
                ", name='" + name + '\'' +
                ", parentCategoryId=" + parentCategoryId +
                ", url='" + url + '\'' +
                '}';
    }
}
```

## Category 业务层缓存配置

```java
package com.itunion.demo.service.impl;

import com.itunion.demo.domain.HelpCategory;
import com.itunion.demo.domain.form.HelpCategoryForm;
import com.itunion.demo.domain.vo.HelpCategoryVo;
import com.itunion.demo.repository.HelpCategoryDao;
import com.itunion.demo.service.HelpCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service("helpCategoryService")
public class HelpCategoryServiceImpl implements HelpCategoryService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HelpCategoryDao helpCategoryDao;

    public List<HelpCategoryVo> selectList(HelpCategoryForm form) {
        return helpCategoryDao.selectList(form);
    }

    public int countByForm(HelpCategoryForm form) {
        return helpCategoryDao.countByForm(form);
    }

    @Cacheable(key = "#id", value = "help_category")
    public HelpCategoryVo selectById(Serializable id) {
        log.info("query category from DB " + id);
        return helpCategoryDao.selectById(id);
    }

    public void insert(HelpCategory entity) {
        helpCategoryDao.insert(entity);
    }

    @CacheEvict(key = "#id", value = "help_category")
    public int deleteById(Serializable id) {
        return helpCategoryDao.deleteById(id);
    }

    @CacheEvict(key = "#entity.helpCategoryId", value = "help_category")
    public int updateById(HelpCategory entity) {
        return helpCategoryDao.updateById(entity);
    }

}
```
思路：把每个实体都跟表关系起来，相当于表数据的缓存，value 也跟表名一样，这里主要解决第1个问题
> @Cacheable(key = "#id", value = "help_category")
> @CacheEvict(key = "#id", value = "help_category")

## Topic 业务层缓存配置
```java
package com.itunion.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.itunion.demo.domain.HelpTopic;
import com.itunion.demo.domain.form.HelpTopicForm;
import com.itunion.demo.domain.vo.HelpTopicVo;
import com.itunion.demo.repository.HelpTopicDao;
import com.itunion.demo.service.HelpCategoryService;
import com.itunion.demo.service.HelpTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service("helpTopicService")
public class HelpTopicServiceImpl implements HelpTopicService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HelpTopicDao helpTopicDao;
    @Autowired
    private HelpCategoryService helpCategoryService;

    public List<HelpTopicVo> selectList(HelpTopicForm form) {
        log.info("query topic list " + form.toString());
        PageHelper.startPage(form);
        List<HelpTopicVo> list = helpTopicDao.selectList(form);
        for (HelpTopicVo topic : list) {
            if (topic.getHelpCategoryId() != null) {
            topic.setCategory(helpCategoryService.selectById(topic.getHelpCategoryId()));
            }
        }
        return list;
    }

    public int countByForm(HelpTopicForm form) {
        return helpTopicDao.countByForm(form);
    }

    public HelpTopicVo selectById(Serializable id) {
        log.info("query topic by id " + id);
        HelpTopicVo topic = helpTopicDao.selectById(id);
        if (topic.getHelpCategoryId() != null) {
            topic.setCategory(helpCategoryService.selectById(topic.getHelpCategoryId()));
        }
        return topic;
    }

    public void insert(HelpTopic entity) {
        helpTopicDao.insert(entity);
    }

    public int deleteById(Serializable id) {
        return helpTopicDao.deleteById(id);
    }

    public int updateById(HelpTopic entity) {
        return helpTopicDao.updateById(entity);
    }
}
```
这里的 selectList 方法循环的去查询分类对象信息，相同的分类其实都是直接存内存中取的

## Web服务配置
```java
package com.itunion.demo.controller;

import com.itunion.demo.domain.form.HelpTopicForm;
import com.itunion.demo.domain.vo.HelpTopicVo;
import com.itunion.demo.service.HelpTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/helpTopic")
public class HelpTopicController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HelpTopicService helpTopicService;

    @Cacheable(key = "'dataGrid' + #form.toString()", value = "HelpTopicController", sync = true)
    @RequestMapping("/dataGrid")
    public Object dataGrid(HelpTopicForm form) {
        long s = System.currentTimeMillis();
        List<HelpTopicVo> list = helpTopicService.selectList(form);
        log.info("query use time " + (System.currentTimeMillis() - s) + " ms");
        return list;
    }

    @RequestMapping("/{id}")
    public Object getById(@PathVariable("id") Integer id) {
        return helpTopicService.selectById(id);
    }
}
```
> @Cacheable(key = "'dataGrid' + #form.toString()", value = "HelpTopicController", sync = true)

这里主要解决第二个问题，当很多用户在3秒之内都请求了同样的服务，将会直接返回缓存的结果，而不再执行 selectList 的数据库查询

### 缓存管理配置
```java
package com.itunion.demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {
    // 项目的缓存管理类
    @Bean
    public CacheManager cacheManager() {

        SimpleCacheManager manager = new SimpleCacheManager();

        List<Cache> caches = new ArrayList<>();
        // 使用 Map 存储数据
        caches.add(new ConcurrentMapCache("help_category"));
        caches.add(new ConcurrentMapCache("help_topic"));

        // 使用 Caffeine 存储，Caffeine 在 spring boot 2.0 之后取代了优秀的 guava cache
        // expireAfterWrite 3 秒内数据没有更新就删除缓存数据， 可以理解为是数据会在缓存中存在 3 秒钟
        caches.add(new CaffeineCache("HelpTopicController", Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build()));
        caches.add(new CaffeineCache("HelpCategoryController", Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build()));

        manager.setCaches(caches);
        return manager;
    }
}
```
Spring boot 提供了 CacheManager 用来管理所有的缓存
这里我们声明了以下缓存
* new ConcurrentMapCache("help_category")
* new ConcurrentMapCache("help_topic")
* new CaffeineCache("HelpTopicController", Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build())
* new CaffeineCache("HelpCategoryController", Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build())
前两个主要是基于内存长时间缓存的，不需要经常更新
后两个主要是解决并发请求的缓存，所以是用了 CaffeineCache 的缓存策略 expireAfterWrite 3 秒内数据没有更新就删除缓存数据， 可以理解为是数据会在缓存中存在 3 秒钟

## 高并发测试
测试场景 100 个线程，每个线程请求100次

不使用缓存
![](https://upload-images.jianshu.io/upload_images/9260441-a4ba6e02c037ef4d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

使用缓存后
![](https://upload-images.jianshu.io/upload_images/9260441-88e1188e4b5c6c8a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可以看出平均响应时间提高了 14 倍多

> 一般情况下这样就够了，但是实际项目当中还要根据不同的项目架构，以及不同的业务场景来决定该用什么样解决方案

####更多精彩内容
[架构实战篇（一）：Spring Boot 整合MyBatis](https://www.jianshu.com/p/5f76bc4bb7cf)
[架构实战篇（二）：Spring Boot 整合Swagger2](https://www.jianshu.com/p/57a4381a2b45)
[架构实战篇（三）：Spring Boot 整合MyBatis(二)](https://www.jianshu.com/p/b0668bf8cf60)
[架构实战篇（四）：Spring Boot 整合 Thymeleaf](https://www.jianshu.com/p/b5a854c0e829)
[架构实战篇（五）：Spring Boot 表单验证和异常处理](https://www.jianshu.com/p/5152c065d3cb)
[架构实战篇（六）：Spring Boot RestTemplate的使用](https://www.jianshu.com/p/c96049624891)
[架构实战篇（七）：Spring Boot Data JPA 快速入门](https://www.jianshu.com/p/9beec5b84a38)
[架构实战篇（八）：Spring Boot 集成 Druid 数据源监控](https://www.jianshu.com/p/da2b1a069a2b)
[架构实战篇（九）：Spring Boot 分布式Session共享Redis](https://www.jianshu.com/p/44130d6754c3)
[架构实战篇（十三）：Spring Boot Logback 邮件通知](https://www.jianshu.com/p/9b3a3f3a7e87)

#### 关注我们

Git源码地址：https://github.com/qiaohhgz/spring-boot-cache
作者：咖啡

![](http://upload-images.jianshu.io/upload_images/9260441-fbb877c1ace32df7.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)