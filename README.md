# onenet-studio-nb-data-push

在 OneNET Studio 平台上接入 NB 设备后，完成从 Studio 向第三方应用（本应用）的数据推送，从而在应用程序中获取 NB 设备的所有数据。同时，在应用程序中监控光照传感器的值，达到预设的最大阈值后，调用 LwM2M IPSO API，自动关闭 LED 灯；达到预设的最小阈值后，调用 API 自动打开 LED 灯，从而实现节能的目的。

> 本项目基础代码由中移官方数据推送 SDK 克隆而来，使用命令 `git clone -b OneNET5.0 https://github.com/cm-heclouds/data-push`，然后再简单地封装了一下 Studio 访问 LwM2M IPSO 的 API。
> 
> 此版对接的是 OneNET Studio。若需要对接最新的 OneNET 物联网开放平台，可以参见 [OneNET-LightControl](https://github.com/CQCET-IOT/OneNET-LightControl) 项目 。
> 
## 安装依赖

本项目依赖于：

- JDK 1.8
- Maven 3.3.9
- IntelliJ IDEA 2019

### 安装配置 JDK

使用工具包中提供的 JDK1.8 安装程序，进行默认安装即可。需要注意的是，安装完成后，必须配置下面三个环境变量，以便应用开发时能够顺利找到 Java 环境。

打开 cmd 窗口，新增下面两个环境变量，其中的路径名称根据实际路径进行修改：

```
setx /m JAVA_HOME "C:\Program Files\Java\jdk1.8.0_101"
setx /m CLASSPATH ".;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar;"
```

同时修改 `PATH` 环境变量的值，如下：

```
setx /m PATH "%PATH%;%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;"
```

关闭当前 cmd 窗口，重新打开一个新的 cmd 窗口，然后输入 `java -version` 命令，如果能够看到类似如下的输出，则表示 JDK1.8 安装完成。

```
C:\Users\thinker>java -version
java version "1.8.0_101"
Java(TM) SE Runtime Environment (build 1.8.0_101-b13)
Java HotSpot(TM) 64-Bit Server VM (build 25.101-b13, mixed mode)
```

### 安装配置 Maven

使用工具包中提供的 Maven3.3.9，或者从[这里下载](http://archive.apache.org/dist/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.zip)。将压缩包解压缩到本机目录，最好是无空格的英文目录，比如 *F:\ProgramEnv\apache-maven-3.3.9*，然后设置环境变量：

```
setx /m MAVEN_HOME "F:\ProgramEnv\apache-maven-3.3.9"
setx /m PATH "%PATH%;%MAVEN_HOME\bin%"
```

新开一个 cmd 窗口，运行 `mvn -version` 命令，如果能够看到如下输出，则表示 Maven 安装成功：

```
C:\Users\thinker>mvn -version
Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-11T00:41:47+08:00)
Maven home: F:\ProgramEnv\apache-maven-3.3.9\bin\..
Java version: 1.8.0_101, vendor: Oracle Corporation
Java home: C:\Program Files\Java\jdk1.8.0_101\jre
Default locale: zh_CN, platform encoding: GBK
OS name: "windows 10", version: "10.0", arch: "amd64", family: "dos"
```

Maven 默认使用国际网络下载 Java 程序的依赖包，下载速度可能会非常慢。我们可以配置阿里云仓库以加快下载速度。打开安装目录，找到 *conf/settings.xml* 文件，在 `mirrors` 标签中加入如下配置：

```
<mirrors>
    <mirror>
        <id>alimaven</id>
        <name>aliyun maven</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <mirrorOf>central</mirrorOf>        
    </mirror>
</mirrors>
```

Maven 下载的依赖包会保存到本地，可以手动指定一个目录以便将这些依赖包放入。创建 *F:\ProgramEnv\mvnRepo* 目录，然后仍然是修改 *conf/settings.xml* 文件：

```
<localRepository>F:\ProgramEnv\mvnRepo</localRepository>
```

### 安装配置 IntelliJ IDEA

使用工具包中提供的 IntelliJ IDEA 2019 安装包，进行默认安装。在启动界面选择 "Configure->Settings"，搜索 `maven`，然后将 Maven 的实际安装路径填入，以便在 IDEA 中使用 Maven。

![IDEA-Maven-Config](https://github.com/CQCET-IOT/onenet-studio-nb-data-push/blob/main/image/IDEA-Maven-Config.png)

## 运行程序

从[这里下载](https://github.com/CQCET-IOT/onenet-studio-nb-data-push)本应用程序。在 IntelliJ IDEA 启动界面选择 "Import Project"，定位到本程序的 *pom.xml* 文件，然后打开项目。

本应用程序使用 SpringBoot 编写，采用 Maven 进行包管理，因此第一次打开会下载应用程序的所有依赖包，可能需要等待较长的时间。

打开 *JAVA\src\main\java\com\onenet\datapush\receiver\ReceiverDemo.java* 文件，点击绿色三角，则可以运行本程序。

![Run-Application](https://github.com/CQCET-IOT/onenet-studio-nb-data-push/blob/main/image/Run-Application.png)

程序启动后，默认会监听 *127.0.0.1:9999* 地址，这是一个内网地址，此时 OneNET Studio 还无法主动从外网访问该地址，因此也无法进行 HTTP 数据推送。

### 修改参数

本项目需要根据自己的实际情况，修改 *application.properties* 文件中的以下参数，才能正确运行：

- `api.authorization`: 使用 OneNET Studio 提供的[Token工具](https://open.iot.10086.cn/doc/iot_platform/book/device-connect&manager/device-auth.html?h=token#3)生成。需要注意的是，生成该鉴权信息的参数中包括 `et`，它代表访问过期时间，尽可能设置大一点，否则可能需要不断地修改该鉴权信息。
- `light.imei`: NB-IoT 设备的 IMEI 号。
- `http-push.token`: 第三方应用 Token，HTTP 推送中配置的 Token 需要与之一致。
- `http-push.aesKey`: 第三方应用 AesKey，HTTP 推送中配置的 AesKey 需要与之一致。

## OneNET Studio HTTP 数据推送配置

Studio 进行 HTTP 数据推送的前提是，推送地址必须是公网上的有效地址。一般来说，这需要经过云主机购买、域名购买、备案登记、程序部署等过程，比较复杂。开发过程中为了快速验证程序，往往会采用内网穿透工具，将本机的内网地址映射到公网上。

准备好工具以后，需要将 *127.0.0.1:9999* 映射到公网上，得到其公网地址为 *http://www.xxx.net* ，然后在 OneNET Studio 中进行设置。

![HTTP-Push-Config](https://github.com/CQCET-IOT/onenet-studio-nb-data-push/blob/main/image/HTTP-Push-Config.png)

具体的配置如下：

- 实例名称：自己取一个合理的名称。
- 推送地址：http://www.xxx.net/receive ，注意 URL 后面必须包含 /receive，它是在本应用程序中实现的，用于接收处理 Studio 的验证和数据。
- Token：自己输入一个合理的字符串。该字符串需要与配置文件中的 `http-push.token` 保持一致。
- 消息加密方式：选择明文模式，本应用程序不进行消息加密。如果选择了加密模式，则需要与配置文件中的 `http-push.aseKey` 保持一致。

配置完成以后，需要手动点击数据推送的“验证”按钮，只有验证成功了，Studio 才会向其推送数据。

## OneNET Studio 规则引擎配置

配置完数据推送以后，只是告诉了 Studio 应该完成什么动作，但是还没有告诉 Studio 在什么情况下进行数据推送。这就需要用到规则引擎。

在 Studio 上点击“应用开发->项目管理”，新建一个项目，取一个合理的名称，比如“智能灯控项目”：

![Project-Add](https://github.com/CQCET-IOT/onenet-studio-nb-data-push/blob/main/image/Project-Add.png)

然后进入“项目管理”，创建一个规则引擎。勾选所有的消息源，指定前面创建的 NB-IoT 产品，然后关联前面创建的 HTTP 数据推送。

![Rule-Engine-Config](https://github.com/CQCET-IOT/onenet-studio-nb-data-push/blob/main/image/Rule-Engine-Config.png)

配置完成后，启动该规则引擎，则当 NB-IoT 设备上下线、上报新数据时，本应用程序就能够获得这些数据，并打印在程序的控制台上。

## 根据光照度的阈值自动开关LED灯

当数据上报到本应用程序后，程序会解析其中的光照度数据，并且根据预设的阈值，对LED灯进行控制，逻辑如下：

```
/* 解析光照度，根据光照度的值调用LED控制API */
try {
    JSONObject object = new JSONObject(obj.toString());
    JSONObject illumi = getIlluminance(object);
    if (illumi != null) {
        float value = illumi.getFloat("value");
        logger.info("illuminance value: " + value);
        if (value > light.getThresholdMax()) {
            // 调用写资源API关闭LED灯
            light.turnOff();
        } else if (value < light.getThresholdMin()) {
            // 调用写资源API打开LED灯
            light.turnOn();
        }
    }
}
catch (Exception ex) {
}
```

其中，控制 LED 灯的开关，调用了 OneNET Studio 平台提供的 [LwM2M-IPSO类->即时命令API->写设备资源](https://open.iot.10086.cn/doc/iot_platform/book/api/LwM2M-IPSO/Real-API/5rt-write-device-resources.html) API。详细实现可以参考程序中的 *Light.java* 源文件。


  [1]: http://static.zybuluo.com/morgen/5oeh02mj39k8nvhecpolwut3/image_1f9apgr2e2lppi11e704me1oee9.png
  [2]: http://static.zybuluo.com/morgen/454fz1vj05rxigvrt3pe60ra/image_1f9aq3gafr8n1cqddop1d0imn4m.png
  [3]: http://static.zybuluo.com/morgen/029zrxmu5t68aj6w4sgq89fg/image_1f9aqppqn93glsb1s1d1nup1hqg13.png
  [4]: http://static.zybuluo.com/morgen/gnq1zblhheq7asfsjztfavff/image_1f9arf07o4lp1su11jk212nbau91t.png
  [5]: http://static.zybuluo.com/morgen/4swb31tq1h26mexavw14t3pl/image_1f9arin7o16fq1n5kvh21eqj166b2a.png
