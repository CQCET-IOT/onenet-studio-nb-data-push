# onenet-studio-nb-data-push

在 OneNET Studio 平台上接入 NB 设备后，完成从 Studio 向第三方应用（本应用）进行数据推送，获取 NB 设备的所有数据。监控光照传感器的值，达到预设的阈值后，在应用中调用 LwM2M IPSO API，从而实现 LED 灯的自动开关。

> 本项目基础代码由中移官方数据推送 SDK 克隆而来，使用命令 `git clone -b OneNET5.0 https://github.com/cm-heclouds/data-push`，然后再简单地封装了一下 Studio 访问 LwM2M IPSO 的 API。
