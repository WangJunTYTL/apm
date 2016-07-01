# apm-dashboard 介绍

apm-dashboard是一个对所有依赖perf4j的项目服务进行监控数据渲染的项目。虽然官网的perf4j也内置了图表渲染功能，但它有些缺点，例如它的图表功能是
依赖google的chartApi在远程进行图表渲染然后返回一个域名是在google下的图表地址，由于google被墙,如果直接引用官网perf4j你是看不到图表的。另外它的每个监控图表只是
在项目自身中可以查看，很难运用到实际业务中，我们希望可以做到性能的数据的分析和渲染分开，可以统一的在dashboard项目查看所有机器的监控数据。

所以，现在你可以这样理解这个项目的整体架构：

__perf4j：__用于采集监控数据并进行初步的性能数据分析

__dashboard：__可以拉取perf4j在不各个机器或业务上的已经分析好性能数据，进行图表渲染。



### 使用方式

1. 加入perf4j依赖,如果你的项目已经依赖原官网perf4j项目,可以直接升级版本为`2.0-SNAPSHOT`

    ````
    <dependency>
        <groupId>org.perf4j</groupId>
        <artifactId>perf4j</artifactId>
        <version>2.0-SNAPSHOT</version>
    </dependency>
    ````
此外,`在2.0-SNAPSHOT`新增SqlLiteAppender,可以把性能数据导入到sqllite库中,有关配置可以参照perf4j-demo项目中的log4j.xml查看,更过有关
在log4j.xml配置perf4j的信息可以到网上搜索，比如：[http://www.infoq.com/cn/articles/perf4j/](http://www.infoq.com/cn/articles/perf4j/)

2. 通过web暴漏性能数据接口，在依赖项目中配置以下的servlet,在web.xml文件中加入下面配置

     ```
     <servlet>
         <servlet-name>perf4j</servlet-name>
         <servlet-class>org.perf4j.servlet.APMGraphingServlet</servlet-class>
     </servlet>
     <servlet-mapping>
         <servlet-name>perf4j</servlet-name>
         <url-pattern>/perf4j</url-pattern>
     </servlet-mapping>
     ```
   
     
3. 在dashboard项目中配置集群中所有节点，在ServerCluster.conf 文件中像下面这样加入每一个服务的监控数据拉取地址
   
    ```
    ServerCluster: {
      clusterList = [
        {
          name = perf4j-demo
          ip = 127.0.0.1
          port = 8888
          url = "/perf4j"
        }
        {
          name = perf4j-demo02
          ip = 127.0.0.1
          port = 8888
          url = "/perf4j"
        }
        {
          name = perf4j-demo03
          ip = 127.0.0.1
          port = 8888
          url = "/perf4j"
        }
      ]
    }
    ```
     
这样，dashboard项目通过配置的集群中的节点的ip、port和servlet的访问地址去拉取性能数据，然后渲染上文中介绍的图表样式     
