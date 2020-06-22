# Context的理解：
  Context 场景，上下文，应用环境全局信息的接口；
  
  Context的分类：
Application，Activity，Service都继承自Context；Activity直接继承自ContextThemeWrapper（涉及到用户界面的展示，包含了主题相关的接口），Application，Service直接继承自ContextWrapper；
只有Activity的Context能够show dialog，除此之外他们的	Context功能一样，区别就是：Application和Service的Context在InflateLayout时使用的时应用的Theme而不是该Acativity的Theme；Application和Service的Context在启动新的Activity时，需要创建新的Task，因为他们没有任务栈；
  Context导致的内存泄漏：
   Context被引用后，无法释放，导致的内存泄漏。其中包括：
   1.单例模式下，如果该对象持有了Context或者View（非Application Context），那么因为该单例对象的存活时间，大于Activity和Service的生命周期，导致内存泄漏；解决办法是，通过静态方法的参数直接传递Context，而不是通过构造方法传递；
   2.非静态内部类（包括匿名内部类），它们会持有外部类的引用，导致外部类无法被释放，解决办法死是，使用静态内部类，如果传入了外部内引用，需要对该引用使用弱引用，并且判断null。
3.资源未关闭，比如File，Stream，Bitmap，BroadCastReceiver,ContentObserver等，要记得不需要后关闭释放它们。
4.异步线程导致的内存泄漏问题。