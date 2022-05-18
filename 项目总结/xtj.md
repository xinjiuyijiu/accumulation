网约车项目：
- 平台介绍：
网约车头枕+后窗+车身的广告播放媒介；
- 项目组成
  - 前端：
    - 乘客小程序
    - 运维安装端
    - 司机app端
  - 后端
    - Spring Cloud微服务
  - 车载屏
    - 广告播放程序
    - App升级和监控程序
  - 区块链积分

- 车载屏端
  - 技术选型
    - Java+Kotlin+OKHttp+Rx+WebSocket
  - 车载屏物理设备
    - 与厂商对接SDK 
  - 开发中遇到的问题

- 技术问题
  - 因为设备需要一直打开，一直连接着网络，所以需要考虑如何省电，省流量
  - Websocket相关
    - socket重连逻辑，心跳逻辑
       如何合理的减少心跳？？
	重连退避：失败3次后，每次失败都增加到下一次的重连时间
    - Stomp协议
       一个建立在websocket之上的文本协议，传输的字符串文本	     
    - socket http切换
       当socket连接遇到问题：socket服务器崩溃后，使用Http的轮询来实现一些实时的功能 
  - 大量websocket设备同时连接，导致服务器达到峰值，无法及时处理
    客户端
  - 项目编译
    - 多sourceSets
    - iml sdk编译顺序
    - apk打包
    - variant filter配置
  - 多设备业务兼容
    - 充电
    - lbs
    - 大小屏
    - sdk加载 
    - flavor打包配置
    - jar包冲突，class剔除
    - 截图+root权限
      - MediaProjection+AccessibilityService
      - screencap shell命令
    - 策略模式
  - 省电，省流量
  - 设备缓存（断电）
  - 设备存储检测
  - 设备远程控制shell操作
  - 静默升级安装
  - AIDL进程间通信
  - JNI隐藏接口签名逻辑
  - 日志管理

- 一些jar包，插件包的封装：
  - 蚂蚁链合约调用jar包
  - 抖音分享登录Weex插件
  - 一键登录功能Weex插件
  - SIM卡激活关闭功能Jar包

- 日常的数据跟踪
  - 播放数据，用户数据分析
  - 设备日志抽查 
