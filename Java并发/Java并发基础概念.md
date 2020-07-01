CPU，内存，I/O速度不一致，导致运行效率问题，多线程并发执行可以提高效率，带来了并发问题；

# 可见性
线程A修改共享变量后，线程B能够立刻看见，称为可见性；
CPU和内存之间存在CPU缓存，多核CPU，每个线程在不同的核上，操作不同的缓存，内存中的一个变量，会在不同的核上，有不同的副本，导致并发问题；在Java中，每个线程有自己工作内存，用于保存主内存中的副本，不同副本之间的共享变量同步问题，这和CPU缓存是同一个问题；

***JVM内存模型和主内存，CPU寄存器，CPU缓存的对应性？***

# 原子性
一个或多个操作在CPU执行过程中，不会被中断，称为CPU的原子性；而在我们的实践开发中，面对高级语言编程，某个操作实际是由多个CPU指令组成，不具备CPU的原子性，使得在线程切换时，导致的并发问题；

# 有序性
编译器为了优化性能，会在不修改逻辑的情况下，改变代码执行顺序；

***双重检查锁创建单例产生的空指针问题？***

```
public class Singleton {

  static Singleton instance;

  static Singleton getInstance(){

    if (instance == null) {

      synchronized(Singleton.class) {

        if (instance == null)

          instance = new Singleton();

        }

    }

    return instance;

  }

}
```
一般理解的创建对象操作是：分配内存->初始化对象->对象地址赋值，new操作被优化后，实际步骤是：分配内存->对象地址赋值->初始化对象，所以在多线程，在对象地址赋值后，但并未初始化对象之前，拿到了非空对象，但访问其成员变量出现异常。如果给instance加上volatile关键字，禁止指令重排。

***缓存，线程，编译优化的目的是提高代码执行效率，但缓存带来的可见性问题，线程切换带来的原子性问题，编译优化带来的有序性问题会导致并发问题；***
***那么也就是说，通过禁用缓存，禁止编译优化，能够解决并发带来的可见性和有序性问题，更进一步则是，如何灵活的按需禁用***


# Java内存模型
Java内存模型规范了JVM提供按需禁用缓存和编译优化的方法；包括volatile，final，synchronized三个关键字，以及六项Happen-Before规则；

## volatile
volatile的中文意思易变的，不稳定的；该关键字能实现比synchronized更弱的同步机制，能够解决可见性和有序性的问题：
- 可见性：volatile保证了线程修改了值后，会立即同步到主内存，每次获取值时，都会从主内存刷新，相当于跳过了缓存的作用；
- 有序性：volatile保证了，会在变量赋值操作后，加入内存屏障的指令（使得内存屏障后的代码不能移动到它前面），这样该变量不会受到重排序的影响；

## Happens-Before六原则
Happens-Before原则针对JVM代码编译优化，指令重排的问题，定义了一些禁止编译优化的场景，保证并发编程的正确性；Happens-Before表示前面一个操作的结构对后续操作是可见的（可获取的）。

1. 程序的顺序性规则
   在一个线程中，按照程序顺序，前面的操作对后续任意操作可见；
2. volatile变量规则
   对一个volatile变量的写操作，对后续这个变量的读操作可见；
3. 传递性规则
   a happens-before b，b happens-before c，那么a happens-before c；
4. 管程中的锁规则
   一个锁的解锁，对于后续的该锁的加锁操作可见；
5. 线程start规则
   线程A启动线程B，线程A启动线程B之前的操作对于线程B是可见的；
6. 线程join规则
   线程A中，B.join使得A等待B线程执行完成，完成后，线程B的操作对于线程A是可见的；

## synchronized

## final
- 写final域的重排规则
 禁止对final域的写操作重排到构造函数外；保证了，对象被其它线程可见之前，已经被正确的初始化
 
- 读final域的重排规则
 在一个线程中，初次读该对象引用与初次读该对象final域，JVM会禁止重排；
# 互斥锁
# 死锁
# 等待-通知 | 生产者-消费者
# 管程

# 线程&线程的生命周期
# 线程池&Future

# 读写锁

# 乐观锁&悲观锁

# ReadWriteLock
# StampedLock

# Semaphore
# Lock&Condition

# CountDownLatch&CyclicBarrier

# Java并发容器
# 原子