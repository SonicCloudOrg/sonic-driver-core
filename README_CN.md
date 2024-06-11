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

## sonic-driver-core是什么？

sonic-driver-core可以脱离Appium，直接与WebDriverAgent或UIautomator2交互，减少了Appium的通信层，让测试更快更稳定。

## 在你的Java代码中使用

### 引用库
#### Maven
```xml
<dependency>
    <groupId>io.github.soniccloudorg</groupId>
    <artifactId>sonic-driver-core</artifactId>
    <version>1.1.30</version>
</dependency>
```
#### Gradle
```
implementation 'io.github.soniccloudorg:sonic-driver-core:1.1.30'
```

### 代码

```java
package org.cloud.sonic.driver.ios;

import org.cloud.sonic.driver.common.tool.SonicRespException;

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
        iosDriver.findElement(IOSSelector.XPATH, "//XCUIElementTypeTextField").click();

        //更多...
    }
}
```

## 更多例子

查看 [这里](https://github.com/SonicCloudOrg/sonic-uiautomation-example/tree/main/java-example).

## 文档

查看 [这里](https://sonic-cloud.cn/sdc/re-sdc.html).

## 赞助商

感谢所有赞助商！

[<img src="https://ceshiren.com/uploads/default/original/3X/7/0/70299922296e93e2dcab223153a928c4bfb27df9.jpeg" alt="霍格沃兹测试开发学社" width="500">](https://qrcode.testing-studio.com/f?from=sonic&url=https://ceshiren.com)

> [霍格沃兹测试开发学社](https://qrcode.testing-studio.com/f?from=sonic&url=https://ceshiren.com)是业界领先的测试开发技术高端教育品牌，隶属于[测吧（北京）科技有限公司](http://qrcode.testing-studio.com/f?from=sonic&url=https://www.testing-studio.com) 。学院课程由一线大厂测试经理与资深测试开发专家参与研发，实战驱动。课程涵盖 web/app 自动化测试、接口测试、性能测试、安全测试、持续集成/持续交付/DevOps，测试左移&右移、精准测试、测试平台开发、测试管理等内容，帮助测试工程师实现测试开发技术转型。通过优秀的学社制度（奖学金、内推返学费、行业竞赛等多种方式）来实现学员、学社及用人企业的三方共赢。[进入测试开发技术能力测评!](https://qrcode.testing-studio.com/f?from=sonic&url=https://ceshiren.com/t/topic/14940)

## 开源许可协议

[License](LICENSE)
