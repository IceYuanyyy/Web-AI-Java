#  Spring Boot 3 支付宝沙箱支付项目

## 项目介绍

这是一个基于Spring Boot 3集成支付宝沙箱支付的示例项目。通过本项目，您可以快速实现支付宝支付功能，包括支付、回调处理等。

## 技术栈

- Spring Boot 3.4.3
- Java 17
- 支付宝SDK (alipay-sdk-java 4.35.79.ALL)
- Lombok

## 项目结构
```

├── config
│ └── AlipayConfig.java # 支付宝配置类
├── controller
│ └── PayController.java # 支付控制器
├── service
│ └── AlipayService.java # 支付服务
├── resources
│ ├── application.yml # 配置文件
│ └── static
│ └── index.html # 支付测试页面

```

## 配置步骤

### 1. 获取支付宝沙箱账号

1. 登录[支付宝开放平台](https://open.alipay.com/develop/sandbox/account)
2. 进入开发者中心，找到沙箱应用
3. 获取以下信息：
    - APPID
    - 应用私钥
    - 支付宝公钥
    - 支付宝网关地址
具体参考：https://blog.csdn.net/qq_62377885/article/details/139450851
### 2. 配置密钥

推荐使用支付宝开放平台开发助手生成RSA2密钥对：
- 下载安装[支付宝开放平台开发助手](https://opendocs.alipay.com/common/02kipk)
- 选择RSA2方式生成密钥
- 将应用公钥提交到沙箱应用配置中，获取支付宝公钥

### 3. 修改配置文件

在`application.yml`中配置支付宝相关参数：

```
alipay:
  app-id: 9021000133664960
  merchant-private-key: MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDMdaxA9Sdqdh1la5b5p3EDSfNrU5jeTOxC4jFuaaQz6l1crtRfkKaeI7x/dcHvxl+og6m/WkRUmDB20gZ2MkBKGmWZdMnOGzi/51muxbrEngP4Zsd7e/VzI8okiHeg7vElni15ERhWL8oitTqSNMEOAm8nWDwL16ARGkAnKqV+7i2oSL+tjpX8BLrqtdpj0yRN373Xu/uUSVFfs9IYw3oA7lIsj48BP8Nke+K3pkAChjR1P3C9AHFaBjS0NGHWGWt9fH5saWGG+Z6ODB/gr0UVdMOSq6cDOy1nqFozrYxZ+UADaC+gasdcTV2dfGEN7QYMdJwXrtqCkBFmqAvq4LeNAgMBAAECggEBAKdY1UFhpv5baNofQrZal4SSnbtF9ERXf7WKv9zYeL7JX9tBebAyjD25kEpfDvp7SsZ55ZEqh+0Zl+lZJFLP9JDypCADWD3rR/rCmEQVsTXHBAbn0PTAU/kG3Sgjzo6ZpCUfH6cNF/dLLE5WOXd72FvoICR68sLanTgCXWnxXP3S7E+sjHnW9jjNng9sokD9pdhjE2uObrCR2mKXWwNp4C58ajAbi/k8o3bcjFT2JoN0KLH2w6d3xOEgpI5BtOnP36SRu2n1ubbKCjSNlhvXH9HLUHV/Cx+Llp8qjT5Av0LegFahOoh30nvmlz3tPOE4uT9b448he+ml300NEXLJVX0CgYEA6b4h1KmF6qLcWWJydRhgfe3JuTaI7+IQ5TpeROLyZKIyHHhaktCoNloKICZY9bVCIHmUkhfxXHqpsw2cy/GK98XGbp2IBYMYj7fgM7Wt5O8xMARP0lN40N88/Np4NkMuGX+O0p0ugYLrrfjgGMy7xbKbJfXH5Sc34AZ2M5oLi1MCgYEA3+23vOXFAUXeQXJfl8x8gEWTSDDFMRuIKTvT/FkJv7n0DJnXIR2NO/pgGxISA4+4J0DztETW0m96Vfzn3zgkeiiJCwq59aEBGa+2E9vLrDCJ2FLQo4g3Rs9N1d5ZyTPrywuW9pPrsdi/GgrwgL+7RNijlKb7dfNKmKqq1ZAPNZ8CgYEAnQshr8A2OW8ynNuICyRVsEYTQl1ho8o/j8OnkqDoOrS+9mHmeL24Rsw4AWHCY1NlGmyVZZKwl3HSknqS3nNq6w32RFuTCCEMX4MrH5LxQt9yLNC40JdElRFn6HrgSZTZZEhPrTHuDRXpQvnzlrX3ctyNYPPBWjT/VYUB5amaGAECgYA7R029hsHZLWrBvopJwAsxVbnAgZCiCFzKNZlPky5+f7Z4Y31Muer6drRcVjNImBIy6gHZteN8f6BOBQ5IzC7z26kzsBo8SOvmpdo4dZX8Njk3d+zdbaDccOJ0/FCeb806yg2jptLdGLi2JzE1lAv6FLNxFac5uTcFJSDmOiWclwKBgQDFKdE0OsjiwwaEjySUqgFvlRxjEMK9SeTcSxKLxgkXTo5IqGtXNKSiCo3VughY4Z5JoNOQgLV6qjOeu1BpvJt1EcjvouhSgiMPq0pIEGhJlGWNiUtgLMrWBX8ymJiNq8P+Orwv3Zyz+CGKKfVvjO5O5iR0UU1exNU54xeQ9zPaZQ==
  alipay-public-key: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlime4S125WNBGsIoC46YnxtUwG6Rn+YY0roM1zWu2Ki5OpCBysj57hv02tZgfQQ//WRxsp0I/LphH24jMVvVSRTvmA3q22va3Po5xtDImED2cBeBiQ4JGKf8WLyxS3Esn7GXE1pdxwvO2CGMFpokyGH01akQCs/Cw05AElEhV2y5/S6f/aNVC97a4z0SYigAJKivir0w9rj7KWeV/Ag+SAZew5L+P/UTdHw8/zEmlrExSgehd7Az/SHzBXLnzYExw31erssva6sqMpNZ2xBxFYXqPZv0yos8UGLF8UMtmYiCqc0EIhPpMAfVNMJXY69u0+s0duXqx6Vcmi/gXGksowIDAQAB
  notify-url: http://你的域名/api/pay/notify
  return-url: http://你的域名/api/pay/return
  gateway-url: https://openapi-sandbox.dl.alipaydev.com/gateway.do
  charset: UTF-8
  format: json
  sign-type: RSA2


```


## 使用说明

### 1. 启动项目
```agsl

mvn spring-boot:run
```


### 2. 访问测试页面

打开浏览器访问：`http://localhost:8080/`，可以看到支付测试页面。

### 3. 创建支付订单

- 设置金额和商品名称
- 点击"立即支付"按钮
- 页面会跳转到支付宝沙箱支付页面

### 4. 支付回调处理

项目中已实现两种回调处理：
- 同步回调：用户支付完成后，支付宝会跳转回商户页面，对应`/api/pay/return`接口
- 异步通知：支付成功后，支付宝服务器会异步通知商户，对应`/api/pay/notify`接口

## 注意事项

1. 在正式环境中使用时，需要修改支付宝网关地址为正式环境地址：`https://openapi.alipay.com/gateway.do`
2. 异步通知地址需要公网可访问，开发测试时可使用内网穿透工具（如ngrok）
3. 请妥善保管私钥信息，建议使用配置中心或环境变量等方式管理敏感信息
4. 本项目为学习和参考用途，生产环境使用前请完善异常处理和安全措施

## 参考资料

- [支付宝开放平台文档](https://opendocs.alipay.com/open)
- [SpringBoot功能模块之支付宝沙箱支付](https://blog.csdn.net/qq_62377885/article/details/139450851)

## 声明

本项目仅供学习参考，请勿用于商业用途。使用本项目进行实际支付开发时，请务必遵守支付宝的相关规定和要求。
