### 多模块项目注意点:
- 所有项目共用的common-util中不存放任何配置文件，只存放代码
- 而使自己的项目中的spring配置文件去引用common-util包下的类
- 运行根项目JavaWebSkeleton的install，因为它覆盖了所有的依赖
- 不同子模块之间的spring配置文件是可以自然相互引用的，共享bean容器
- 每个子模块所需的配置文件放在自己的resources目录下，不要使用其他子模块的配置文件
- 每个Web项目共用common-util，以及自己的core（Service）、dao、domain模块，都打为jar包
- 前台web模块是foreground子模块，打为war包；后台web模块是background子模块，也打为war包
- 前台和后台模块共用一套的service、dao，但controller是不同的，分别部署
- 注意web子模块的web.xml中contextConfigLocation对应的值要写为 classpath*:spring/spring-*.xml 。classpath*会把各个子模块jar包中的classes目录作为classpath，也就是可以读取到所有子模块的目录，而不仅仅是当前web子模块的目录，并且要覆盖所有的spring配置文件
- 子模块的spring配置文件不需要import也可以共享bean容器
- 非web子模块打成jar包供某个web子模块使用时，会自动读取jar包下的代码和配置文件，不需要使用插件再把jar包解压。
- 最终打包结果放置在web子模块下，是一个war包。war包的WEB-INF/lib下有其他依赖的子模块的jar包，运行时会自动解压，如果web.xml配置了读入子模块的spring配置文件的路径，那么启动时会读入这些spring配置文件，与web子模块的spring配置文件协同工作，而不需额外的配置。
- 打好的war包的名字中不能有中划线
- 每个子模块中都要有一个log4j的配置文件
- src/test/java下的包名与待测试的类包名一致，使用idea的alt+enter快速生成测试类模板
- SpringTest的web包下的测试类要加一个@WebAPPConfiguration的注解

### 各模块的功能:
- common-util:各个项目都可以依赖，主要放一些轮子、公共配置代码和工具类
- sample:这是一个完整的Web项目的模块
- sample-domain:存放了实体类DO，数据传输对象DTO，枚举类，枚举的TypeHandler相关
- sample-dao:存放了sample项目的mybatis代码
- sample-core:存放了安全、工具、定时任务、异常、校验器、redis代码和相应的配置文件 以及主体业务逻辑
- sample-web-foreground:存放了网站前台的API
- sample-web-background:存放了网站后台的API

### 如果想将此项目作为脚手架使用，需要修改的地方
- common-util:
    - aop包下的日志切面，修改包名
    - config包下的Swagger配置，修改扫描的包名
- sample-domain:
    - 包名
- sample-dao:
    - 包名
    - 配置文件:
        - mybatis目录下的generator配置文件，修改mysql的jar包的路径；数据库的配置
        - mybatis目录下的mybatis-config配置文件，TypeHandler去掉或修改包名
        - spring目录下各个配置文件的扫描包和涉及到的包名
        - generatorConfig中的包名和表名
        - jdbc中的数据库配置
        - redis中的配置信息
- sample-core：
    - 包名
    - 配置文件:
        - spring目录下各个配置文件的扫描包和涉及到的包名
        - log4j的日志目录
        - token中的密钥
- sample-web-foreground:
    - 包名
    - 配置文件:
        - spring目录下配置文件的扫描包

        

### 开发事项
- 如果想要添加一份邮件模板，那么需要的core子模块的mailSubject.properties中添加subject=邮件主题，然后在emailTemplates目录下添加一个名为之前subject的html文件。html中使用占位符表示待加入的参数
  然后调用MailService的sendHTML方法，其中的subject参数就是之前的subject。（subject出现在三个地方，mailSubject.properties的key，html文件名，sendHTML的第二个参数） 


### 登录 Shiro+JWT（失败）
- 当访问时，一定会经过filter，过滤器会判断是否是登录请求（匹配loginURL）
    - 如果是登录，那么会根据传来的用户标识和密码来生成UsernamePasswordToken，
      然后会将这个UsernamePasswordToken(继承自AuthenticationToken)交给FormRealm，它会取出其中的用户名进行查询。
      
      如果查到结果，那么直接返回一个SimpleAccount(继承自AuthenticationInfo)，也可以是SimpleAuthenticationInfo。
      构造参数为用户对象(principle)，用户密码(credentials)，并添加用户权限；
      如果查不到结果，那么返回null。
      
      如果返回null，那么会抛出AuthenticationException异常
      
      如果返回不为null，那么校验UsernamePasswordToken和AuthenticationInfo中的credentials是否一致，不一致的话，会抛出IncorrectCredentialsException。
      如果一致，那么会将token和AuthenticationInfo封装到Subject中，然后返回Subject。
      
      如果之前都没有抛出AuthenticationException，那么拦截器放行。
      放行完了之后，进入到controller中的login方法，此时用户身份一定是校验完成的。
      
      此时获取Subject时，里面就有之前封装进去的token和AuthenticationInfo，调用getPrinciple方法可以拿到用户对象。
      然后调用JWT的TokenManager的createToken方法，得到JWT的token(通过userId构造)，然后将其返回给客户端(可以在这里将token加入缓存)。
    
    - 如果不是登录，那么会从响应头中取得发送来的token。
      如果没有携带token，那么拦截器将该请求拦截，并通知客户端进行登录。
      如果携带token，那么解析该token，得到userId，并将userId和该token封装到JWTAuthenticationToken(继承自AuthenticationToken)。
      
      然后会将该token交由JWTRealm来进行验证。JWTRealm会利用userId查询数据库(感觉redis也可以代替)，如果查询得到的用户不为空，
      且tokenManager的checkToken返回为真(token没有过期，格式正确等)，那么和之前一样，
      返回一个SimpleAccount(继承自AuthenticationInfo)，构造参数为用户对象(principle)，用户token(credentials)，并添加用户权限；
      否则会返回null。
      
      如果返回null，那么会抛出AuthenticationException异常。
      如果返回不为null，那么会校验credentials，因为两个是同样的token，所以这一步不会被中止。
      然后拦截器放行，此时controller中的方法是可以通过SecurityUtils.getSubject().getPrincipal()方法得到用户对象的。

### 登录 Spring Security +JWT
   - 已登录用户验证token
        - 主要是在Filter中操作。
        从requestHeader中取得token，检查token的合法性，检查这一步可以解析出username去查数据库；
        也可以查询缓存，如果缓存中有该token，那么就没有问题，可以放行。
   
   - 未登录用户进行登录
        - 登录时要构造UsernamePasswordAuthenticationToken，用户名和密码来自于参数，然后调用AuthenticationManager的authenticate方法，
        它会去调用UserDetailsService的loadFromUsername，参数是token的username，然后比对password，检查userDetails的一些状态。
        如果一切正常，那么会返回Authentication。返回的Authentication的用户名和密码是正确的用户名和密码，并且还放入了之前查询出的Roles。
        调用getAuthentication然后调用getPrinciple可以得到之前听过UserDetailsService查询出的UserDetails
   - 在Controller中使用@PreAuthorize等注解需要在spring-web配置文件中扫描security包下的类        

