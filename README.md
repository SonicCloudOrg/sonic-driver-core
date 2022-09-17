<p align="center">
  <img width="80px" src="https://raw.githubusercontent.com/SonicCloudOrg/sonic-server/main/logo.png">
</p>
<p align="center">🎉The Sonic UIAutomation Driver Core</p>
<p align="center">
  <span>English |</span>
  <a href="https://github.com/SonicCloudOrg/sonic-driver-core/blob/main/README_CN.md">  
     简体中文
  </a>
</p>
<p align="center">
  <a href="#">  
    <img src="https://img.shields.io/maven-central/v/io.github.soniccloudorg/sonic-driver-core">
  </a>
  <a href="#">  
    <img src="https://img.shields.io/github/commit-activity/m/SonicCloudOrg/sonic-driver-core">
  </a>
<a href="https://app.fossa.com/projects/git%2Bgithub.com%2FSonicCloudOrg%2Fsonic-driver-core?ref=badge_shield" alt="FOSSA Status"><img src="https://app.fossa.com/api/projects/git%2Bgithub.com%2FSonicCloudOrg%2Fsonic-driver-core.svg?type=shield"/></a>
  <a href="https://codecov.io/gh/SonicCloudOrg/sonic-driver-core">  
    <img src="https://codecov.io/gh/SonicCloudOrg/sonic-driver-core/branch/main/graph/badge.svg?token=PZ5295WQP1">
  </a>
</p>
<p align="center">
  <a href="https://github.com/SonicCloudOrg/sonic-driver-core">  
    <img src="https://www.oscs1024.com/platform/badge/SonicCloudOrg/sonic-driver-core.svg?size=large">
  </a>
</p>

### Official Website

[Sonic Official Website](https://sonic-cloud.gitee.io)

## Background

#### What is sonic ?

> Nowadays, automatic testing, remote control and other technologies have gradually matured. [Appium](https://github.com/appium/appium) can be said to be the leader in the field of automation, and [STF](https://github.com/openstf/stf) is the ancestor of remote control. A long time ago, I began to have an idea about whether to provide test solutions for all clients (Android, IOS, windows, MAC and web applications) on one platform. Therefore, sonic cloud real machine testing platform was born.

#### Vision

> Sonic's vision is to help small and medium-sized enterprises solve the problem of lack of tools and testing means in client automation or remote control.
>
>If you want to participate, welcome to join! 💪
>
>If you want to support, you can give me a star. ⭐

## What is sonic-driver-core?

sonic-driver-core can be separated from appium and interact directly with webdriveragent or uiautomator2, which reduces the communication layer of appium and makes the test faster and more stable.

## Use in Java code

### Add dependency
#### Maven Central

```xml

<dependency>
    <groupId>io.github.soniccloudorg</groupId>
    <artifactId>sonic-driver-core</artifactId>
    <version>1.1.1</version>
</dependency>
```

#### Gradle

```
implementation 'io.github.soniccloudorg:sonic-driver-core:1.1.1'
```

### Code

```java
package org.cloud.sonic.driver.ios;

import org.cloud.sonic.driver.ios.enums.IOSSelector;
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

        //more...
    }
}
```

## Sponsors

Thank you to all our sponsors!

[<img src="https://ceshiren.com/uploads/default/original/3X/7/0/70299922296e93e2dcab223153a928c4bfb27df9.jpeg" alt="霍格沃兹测试开发学社" width="500">](https://qrcode.testing-studio.com/f?from=sonic&url=https://ceshiren.com)

> [霍格沃兹测试开发学社](https://qrcode.testing-studio.com/f?from=sonic&url=https://ceshiren.com)是业界领先的测试开发技术高端教育品牌，隶属于[测吧（北京）科技有限公司](http://qrcode.testing-studio.com/f?from=sonic&url=https://www.testing-studio.com) 。学院课程由一线大厂测试经理与资深测试开发专家参与研发，实战驱动。课程涵盖 web/app 自动化测试、接口测试、性能测试、安全测试、持续集成/持续交付/DevOps，测试左移&右移、精准测试、测试平台开发、测试管理等内容，帮助测试工程师实现测试开发技术转型。通过优秀的学社制度（奖学金、内推返学费、行业竞赛等多种方式）来实现学员、学社及用人企业的三方共赢。[进入测试开发技术能力测评!](https://qrcode.testing-studio.com/f?from=sonic&url=https://ceshiren.com/t/topic/14940)

## LICENSE

[License](LICENSE)


[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2FSonicCloudOrg%2Fsonic-driver-core.svg?type=large)](https://app.fossa.com/projects/git%2Bgithub.com%2FSonicCloudOrg%2Fsonic-driver-core?ref=badge_large)
