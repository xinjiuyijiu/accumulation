将已经“死亡”的对象空间进行回收；
什么是“死亡”的对象？没有被引用的对象；
怎么判断对象没有被引用？
- 引用计数法
- 可达性分析，当前主流的虚拟机所使用的的回收机制

可达性分析算法，将一系列的GC Roots作为出事的存活对象集合，从该集合出发，探索所有能够被该集合引用的对象，并将它们加入到该集合中，这个过程也被称为mark，而没有被探索到的对象便是死亡的可以回收的；
GC Roots包括但不限于一下几种,大概是一种指向堆内的堆外引用：
- Java方法栈帧中的局部变量
- 已加载类的静态变量
- JNI handles
- 已启动且未停止的Java线程


对象的四种引用：引用队列
- 强引用
对象只要被引用可达，则不会被GC回收
- 软引用
对象只要被引用可达，且内存足够，则不会被GC回收
- 弱引用
对象如果只是弱可达（仅被弱引用持有），则会被GC回收
Handler处理内存泄露；LifeCycle生命周期持有Activity，Fragment组件对象；LeakCanary内存泄露检测；
- 虚引用
相当于不持有任何引用，用于对象跟踪



