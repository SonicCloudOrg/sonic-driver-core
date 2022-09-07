<p align="center">
  <img width="80px" src="https://raw.githubusercontent.com/SonicCloudOrg/sonic-server/main/logo.png">
</p>
<p align="center">🎉Sonic UI自动化Driver核心</p>
<p align="center">
  <a href="https://github.com/SonicCloudOrg/sonic-driver-core/blob/main/README.md">  
    English
  </a>
  <span>| 简体中文</span>
</p>
<p align="center">
  <a href="#">  
    <img src="https://img.shields.io/maven-central/v/io.github.soniccloudorg/sonic-driver-core">
  </a>
  <a href="#">  
    <img src="https://img.shields.io/github/commit-activity/m/SonicCloudOrg/sonic-driver-core">
  </a>
  <a href="https://codecov.io/gh/SonicCloudOrg/sonic-driver-core">  
    <img src="https://codecov.io/gh/SonicCloudOrg/sonic-driver-core/branch/main/graph/badge.svg?token=PZ5295WQP1">
  </a>
</p>
<p align="center">
  <a href="https://github.com/SonicCloudOrg/sonic-driver-core">  
    <img src="https://www.oscs1024.com/platform/badge/SonicCloudOrg/sonic-driver-core.svg?size=large">
  </a>
</p>

### 官方网站
[Sonic Official Website](https://sonic-cloud.gitee.io)
## 背景

#### 什么是Sonic？

> 如今，自动化测试、远程控制等技术已经逐渐成熟。其中 [Appium](https://github.com/appium/appium) 在自动化领域可以说是领头者，[STF](https://github.com/openstf/stf) 则是远程控制的始祖。很久前就开始有了一个想法，是否可以在一个平台上，提供解决所有客户端（Android、iOS、Windows、Mac、Web应用）的测试方案，于是，Sonic云真机测试平台由此诞生。

#### 愿景

> Sonic当前的愿景是能帮助中小型企业解决在客户端自动化或远控方面缺少工具和测试手段的问题。
>
>  如果你想参与其中，欢迎加入！💪
>
> 如果你想支持，可以给我一个star。⭐

## sonic-driver-core是什么？

sonic-driver-core可以脱离Appium，直接与WebDriverAgent或UIautomator2交互，减少了Appium的通信层，让测试更快更稳定。

## 在你的Java代码中使用

### 引用库
#### Maven
```xml
<dependency>
    <groupId>io.github.soniccloudorg</groupId>
    <artifactId>sonic-driver-core</artifactId>
    <version>1.0.16</version>
</dependency>
```
#### Gradle
```
implementation 'io.github.soniccloudorg:sonic-driver-core:1.0.16'
```

### 代码

```java
package org.cloud.sonic.core.ios;

import org.cloud.sonic.core.tool.SonicRespException;

public class MyTest {

    public void test() throws SonicRespException {
        IOSDriver iosDriver = new IOSDriver("http://localhost:8100");
        iosDriver.showLog();

        //touch
        iosDriver.swipe(100, 256, 50, 256);
        iosDriver.tap(150, 81);
        iosDriver.longPress(150, 281, 1500);
        iosDriver.performTouchAction(new TouchActions().press(50, 256).wait(50).move(100, 256).wait(10).release());

        //element
        iosDriver.findElement(IOSSelector.XPATH,"//XCUIElementTypeTextField").click();
        
        //更多...
    }
}
```

## 赞助商

感谢所有赞助商！

[<img src="https://ceshiren.com/uploads/default/original/3X/7/0/70299922296e93e2dcab223153a928c4bfb27df9.jpeg" alt="霍格沃兹测试开发学社" width="500">](https://qrcode.testing-studio.com/f?from=sonic&url=https://ceshiren.com)

> [霍格沃兹测试开发学社](https://qrcode.testing-studio.com/f?from=sonic&url=https://ceshiren.com)是业界领先的测试开发技术高端教育品牌，隶属于[测吧（北京）科技有限公司](http://qrcode.testing-studio.com/f?from=sonic&url=https://www.testing-studio.com) 。学院课程由一线大厂测试经理与资深测试开发专家参与研发，实战驱动。课程涵盖 web/app 自动化测试、接口测试、性能测试、安全测试、持续集成/持续交付/DevOps，测试左移&右移、精准测试、测试平台开发、测试管理等内容，帮助测试工程师实现测试开发技术转型。通过优秀的学社制度（奖学金、内推返学费、行业竞赛等多种方式）来实现学员、学社及用人企业的三方共赢。[进入测试开发技术能力测评!](https://qrcode.testing-studio.com/f?from=sonic&url=https://ceshiren.com/t/topic/14940)

## 开源许可协议

[License](LICENSE)
