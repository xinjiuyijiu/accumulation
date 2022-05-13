
# 简介
# 环境搭建
# 一些网站收集

生命周期
StatelessWidget
StatefulWidget
InheritedWidget 状态管理
GetX如何进行的状态管理
Flutter的Route
Flutter如何和Native通信
Flutter的各种动画


Flutter和Native的通信方式：
- BasicMessageChannel
传递消息，双向通信，可持续的双向传递数据
- MethodChannel
方法调用，双向通信，可以相互调用对方的方法
- EventChannel
Native向Flutter发送消息，比如一些系统的变化，如传感器，网络状态。。。

Flutter Plugin：
自动注册FLutterPlugin： GeneratedPluginRegistrant在Android，Ios等平台注册插件

UniApp Weex Plugin:
WXModule
WXComponent
AppHookProxy注册插件