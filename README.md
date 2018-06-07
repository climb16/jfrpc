### 分布式 RPC 框架--JFRpc
JFRpc是基于Java语言的分布式rpc框架,
###### 当前版本：1.0.0-SNAPSHOT
###### 发布日期：2018-06-07
###### 目前为发布到maven中央仓库，请自行编译使用

#### JFRpc 特点
    1. 代码量少， 使用简单， 仅180K
    2. 使用zk作为注册中心，支持分布式，客户端灵活负载均衡策略
    3. 使用netty4 + kryo 作为远程通信框架，性能优异,序列化性能高，体积小
    4. 零配置，全注解实现
#### 使用说明
##### 一、引入maven依赖
```
    <!-- 服务端 -->
    <dependency>
        <groupId>online.jfree</groupId>
        <artifactId>jfrpc-server</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <!--客户端-->
    <dependency>
        <groupId>online.jfree</groupId>
        <artifactId>jfrpc-server</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
```
##### 二、服务端实现
```
//定义服务端接口
@RpcService(serviceId = "UserService", register = RegistryEnum.ZOOKEEPER, zkAddress = "101.200.131.11:2080")
public interface UserServiceFacade {
    UserDTO getUser(Long id);
}

//实现服务端接口
@RpcServiceImpl
public class UserServiceImpl implements UserServiceFacade {
    public UserDTO getUser(Long id) {
        UserDTO user = new UserDTO();
        user.setId(id);
        user.setName("roc test");
        user.setPhone("110");
        return user;
    }
}


//发布服务
//注解服务实现类目录，目前不支持通配符，但是支持子目录扫描
@RpcScan(basePackages = "com.jfrpc.server.sample.service")
//注解服务发布端口
@RpcServer(port = 20210)
public class App {
    public static void main(String[] args) {
        RpcBootstrap.run(App.class, args);
    }
}
```

#### 客户端实现
```
public class UserServiceClient {
    public static void main(String[] args) {
        //UserServiceFacade 为服务端发布接口，建议单独打成jar包引入到客户端中
        //RpcClientProxy 创建服务接口代理
        UserServiceFacade userServiceFacade = RpcClientProxy.create(UserServiceFacade.class);
        UserDTO userDTO = userServiceFacade.getUser(100L);
        System.out.println(userDTO.toString());
    }
}
```