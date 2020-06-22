## ThreadLocal：
同一个ThreadLocal对象，可以存储不同线程的某个值，它们互不干扰。
Looper（MessageQueue）
## Handler :
线程创建Handler需要Looper，通过Looper.prepare()创建，其实是在该方法中，给ThreadLocal存储一个创建的Looper对象,而该ThreadLocal对象用于存储线程内部的变量。

## IdleHandler？
HandlerThread：对Thread进行Looper封装，这样能够直接和Handler配合使用；一个子线程来处理收到的消息，所以就像IntentService一样，是串行处理的。