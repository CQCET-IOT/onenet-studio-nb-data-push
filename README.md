# onenet-studio-nb-data-push

在 OneNET Studio 平台上接入 NB 设备后，完成从 Studio 向第三方应用（本应用）的数据推送，从而在应用程序中获取 NB 设备的所有数据。同时，在应用程序中监控光照传感器的值，达到预设的最大阈值后，调用 LwM2M IPSO API，自动关闭 LED 灯；达到预设的最小阈值后，调用 API 自动打开 LED 灯，从而实现节能的目的。

> 本项目基础代码由中移官方数据推送 SDK 克隆而来，使用命令 `git clone -b OneNET5.0 https://github.com/cm-heclouds/data-push`，然后再简单地封装了一下 Studio 访问 LwM2M IPSO 的 API。
