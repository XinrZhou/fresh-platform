# SpringCloud + SpringWebFlux

## Problems

### Gateway相关

> Gateway路由不生效

Gateway配置静态路由路由不生效，引入Nacos后生效，暂未找到原因

### Nacos相关
> Application failed to connect to Nacos server: "null"

1. Nacos配置文件写在`bootstrap.yml`中，导入bootstrap相关依赖（报错）
2. 修改SpringCloud版本，直接将配置写在`application.yml`中，服务启动成功并且注册至Nacos
```xml
    <!--SpringBoot版本：3.1.1-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>2022.0.3</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
        <version>2022.0.0.0-RC2</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
```

### 其它

#### 微服务启动之后没有端口号

> 服务启动之后没有端口号，但已注册至Nacos

1. 删除 `C:\Users\用户名\AppData\Local\Temp\hsperfdata_用户名`
2. 重启IDEA后解决
