# onenet-studio-nb-data-push

在 OneNET Studio 平台上接入 NB 设备后，完成从 Studio 向第三方应用（本应用）的数据推送，从而在应用程序中获取 NB 设备的所有数据。同时，在应用程序中监控光照传感器的值，达到预设的最大阈值后，调用 LwM2M IPSO API，自动关闭 LED 灯；达到预设的最小阈值后，调用 API 自动打开 LED 灯，从而实现节能的目的。

> 本项目基础代码由中移官方数据推送 SDK 克隆而来，使用命令 `git clone -b OneNET5.0 https://github.com/cm-heclouds/data-push`，然后再简单地封装了一下 Studio 访问 LwM2M IPSO 的 API。

## 修改参数

需要修改 *application.properties* 文件中的以下参数，才能正确运行：

- `api.authorization`: 使用OneNET Studio提供的[Token工具](https://open.iot.10086.cn/doc/iot_platform/book/device-connect&manager/device-auth.html?h=token#3)生成。需要注意的是，生成该鉴权信息的参数中包括 `et`，它代表访问过期时间，尽可能设置大一点，否则可能需要不断地修改该鉴权信息。
- `light.imei`: NB-IoT设备的IMEI号。
- `http-push.token`: 第三方应用Token，HTTP推送中配置的Token需要与之一致。
- `http-push.aesKey`: 第三方应用AesKey，HTTP推送中配置的AesKey需要与之一致。
