Service运行在后台，没有用户界面的；
- 本地服务，即service依附在主进程上（比如service和启动它的activity在同一个进程）；
- 远程服务，运行在独立的进程，不受其他进程的影响，方便为其他进程提供服务,android:process=":service" 
     
**两种service的启动方式：**
1.  通过startService启动，启动后service不会受启动它的组件的影响，即使组件销毁后，service依然能够存在，需要手动销毁service。
2. 通过bindService让组件绑定服务。通过提供一个类似客户端-服务器接口，使得组件和服务之间能够进行交互和通信。一个服务可以被多个组件所绑定，如果所有的组件都没有继续绑定该服务，那么它会被销毁。

|startService启动方式|bindService启动方式|
|-|-|
|onCreate（只有第一次创建时回调）|onCreate（只有第一次创建时回调）|
|onStartCommand|onBind （多次调用绑定服务（此时已经绑定），总共只会调用一次）|
||onRebind|
|onDestroy（需要通过stopService终止服务）|onUnbind|
||onDestory（通过unbindService解绑服务）|

**客户端**（一般指的启动Service的组件），它通过ServiceConnection监听和获取服务端；**服务端**（指的Service服务），通过创建**Binder，Messager，AIDL三种方式**实现和客户端的链接。
- 服务第一次被绑定时，会调用onBind创建并传递Binder，后面无论多次绑定，也不会调用onBind方法，因为已经存在了Binder对象，可以直接使用；
- 客户端（Activity）在绑定服务时，要注意其**生命周期**的影响，比如onCreate绑定，然后在onDestory解绑，这样该服务会在组件的整个生命周期中存在和运行；onStart绑定，onStop解绑，那么该服务只会在组件对用户可见时运行。
- 对于一个服务同时存在startService和bindService两种方式，**startService的优先级高于bindService（或者说影响力更强大）**：
比如：服务先通过bindService绑定，然后通过startService启动，此时服务会变成startService启动的状态，即使bindService的客户端解绑，也对他没有影响。同理，服务先通过startService启动在通过bindService绑定，仍然时startService状态。

onStartCommand被多次调用，如果Service被异常kill，不会调用onDestroy，所以有些资源不会被释放，需要注意重复创建资源导致的问题；

Service是运行在进程的主线程当中的，和Activity一样，所以耗时操作需要另外开辟子线程处理。
前台服务，优先级更高，存活时间和几率更高，且会在通知栏下拉框中显示通知，比如音乐播放器一般使用了该方式。只需在onStartCommand时，调用startForeground（true），并设置对应通知栏即可，然后在onDestroy中使用startForegroud（false）关闭它。

***++Q.如果服务存在启动和绑定两种方式，那么分别调用stopService和unBindService对他们有啥影响？++***
如果两种方式同时存在，stopService才会真正的销毁Service，unBindService不会影响Service

Service的隐式启动：Android5.0以上，不同应用之间服务需要通过隐式启动，同一个应用则两种都可以；

**提高Service的存活率：**

1. 启动前台服务。
2. 单独进程启用服务。
3. onStartCommand的返回值，START_STICKY
4. 提高service的优先权

**IntentService：**
继承自Service的，处理异步操作。
1. 包含了一个子线程，用来处理耗时操作。
2. 处理完耗时操作后，会自动关闭service。
3. 多次启用服务，该服务的子线程会串行的处理这些耗时操作。

本质其实是：继承Service后，添加了HandlerThread和Handler来处理耗时操作。


