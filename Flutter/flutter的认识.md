
# 简介
# 环境搭建
# 一些网站收集

生命周期
StatelessWidget
StatefulWidget
onAttache
initState
build


InheritedWidget 状态管理

Flutter的Route：


Flutter的各种动画

GetX如何进行的状态管理：
- GetBuilder
手动更新   
GetXController关联到GetBuilder，GetBuilder中添加listener，在GetXController中手动调用update方法，触发GetBuilder的更新，实际调用它的setState方法；
- Obx
响应式的状态管理，自动更新  .obs RxXX之类的响应式变量


Flutter和Native的通信方式：
- BasicMessageChannel
传递消息，双向通信，可持续的双向传递数据
- MethodChannel
方法调用，双向通信，可以相互调用对方的方法
- EventChannel
Native向Flutter发送消息，比如一些系统的变化，如传感器，网络状态。。。

Flutter Plugin：
自动注册FLutterPlugin： GeneratedPluginRegistrant在Android，Ios等平台注册插件
GeneratedPluginRegistrant.java在调用flutter dependence get后,在Android项目中生成或更新，MainActivity继承自FlutterActivity，在onCreate方法中，会通过反射调用GeneratedPluginRegistrant的registerWith方法，添加和注册plugin

UniApp Weex Plugin:
- WXModule
- WXComponent
AppHookProxy拦截Application

