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

## final不变性
- 写final域的重排规则
 禁止对final域的写操作重排到构造函数外；保证了，对象被其它线程可见之前，已经被正确的初始化；另外不能再构造函数中将final对象指向外部引用，造成“逃逸”；
 
- 读final域的重排规则
 在一个线程中，初次读该对象引用与初次读该对象final域，JVM会禁止重排；

# 互斥锁
线程切换导致了原子性问题，如果能够保证共享变量修改的互斥性，即同一时刻只有一个线程执行；**解决原子性问题，就是保证中间状态对外不可见；**
临界区：表示一段需要互斥执行的代码；
锁和受保护资源是1：N的关系，一把锁可以保护多个资源，但多把锁不能保护一个资源；

## synchronized
 - 对象锁：某个实例化对象作为被保护资源
       --- 方法锁：成员方法添加synchronized关键字，默认对this对象加锁，该方法成为临界区；
       --- 同步代码块：方法中的代码块，对某个对象加锁，该代码块成为临界区；	
 - 类锁：某个类的Class对象作为被保护资源
       --- 方法锁：静态方法添加synchronized关键字，默认对该类的Class对象加锁，该方法成为临界区；
       --- 同步代码块：方法中的代码块，对某个类的Class对象加锁，该代码块成为临界区；

***如何使用锁保护多个资源***
- 对于受保护的多个资源，如果他们之间没有必然联系，每个资源使用单独的锁，这种精细化的操作，能够提升并发效率，这种锁也叫作细粒度锁；
- 对于受保护的多个资源，如果他们之间有关联，使用能覆盖所有资源的锁，比如类锁，这些资源会共享该锁；

# 死锁
对多个受保护的资源，使用单独的锁进行保护，如果这些资源之间存在相互依赖（竞争），导致死锁的产生；
死锁：一组互相竞争资源的线程相互等待，导致“永久”阻塞的现象；

## 预防死锁
死锁发生的四个必要条件：
1. 互斥，共享资源A只能被某一个线程占用，不能同时被多个线程占有；
2. 占有且等待，线程T1已经取得共享资源A，在等待共享资源B，不释放共享资源A；
3. 不可抢占，其它线程不能强行抢占线程T1占有的资源；
4. 循环等待：线程T1等待线程T2所占有的共享资源B，线程T2等待线程T1所占有的共享资源A；

因此，只要破坏这四个必要条件中的任意一个，则必然不会发生死锁，对于条件1，我们使用锁就是为了产生互斥，所有不考虑，所以需要破坏条件2,3,4来解决死锁：
- 占有且等待，让线程获取所有的共享资源，不再等待；
- 不可抢占，线程主动释放占有的共享资源；Java的Lock类
- 循环等待，打破循环，按照某种规则，依次获取共享资源；

# 等待-通知 | 生产者-消费者
使用循环等待的方式，可以解决占有且等待的问题，但如果并发冲突高，等待时间过长时，会严重影响CPU效率；Java中synchronized配合wait，notify，notifyAll方法能实现等待-通知机制，解决这一问题；

 当一个线程进入临界区后，由于某些条件不满足，需要进入等待状态，调用wait方法，线程会被阻塞，并添加到锁的等待队列中，，同时线程会释放持有的锁，使得其他线程有机会获取该锁，进入临界区；当线程再次满足某些条件是，通过调用notify或notifyAll方法，通知等待队列中的线程，告诉它条件**曾经满足过**，可以尝试获得锁（可能再次失败）；

# 并发带来的问题
- 安全性问题
  - 多个线程同时访问同一个数据，会出现并发问题，这叫做数据竞争；
  - 多个线程的执行顺序会导致不同的结果，即结果依赖线程执行顺序，这叫做竞态条件；
  
 数据竞争和竞态条件问题通过互斥解决，即通过锁；
- 活跃性问题
   前面提到了共享数据竞争导致的死锁问题，其实还可能导致其他问题：
  - 活锁，线程之间同时释放共享数据，企图让对方获取所有资源，导致无法运行；
  - 饥饿，线程一直无法获取所需的共享资源，而无法执行；
  
  活锁问题，可以通过某种规则，控制释放时机，使得对方能获取所有的资源；饥饿问题，可以通过公平锁，保证所有线程都能公平有序的获取共享资源；	
- 性能问题
  为了解决并发问题，使用了锁，但反过来，使用锁会影响并发的效率；解决锁带来的性能问题，或者说更好的提高锁的性能：
   - 使用无锁算法和数据结构；比如TLS，Atomic，乐观锁，copy-on-write
   - 减少锁持有时间；使用细粒度的锁，比如读写锁，分段锁
   
  性能的衡量，包括三个方面：
  - 吞吐量：单位时间处理的请求数量
  - 延迟：响应时间的大小
  - 并发量：能同时处理的请求数量，并发量≈吞吐量*延迟，也就是说在系统性能一定的情况下，延迟越高，则并发量越大

# 管程
  管程指的是，管理共享变量以及对共享变量的操作过程，让他们支持并发；
  管程和信号量是等价的；
## MESA模型
![title](https://raw.githubusercontent.com/xinjiuyijiu/NoteImages/master/gitnote/2020/07/02/mesa_model-1593673888243.png)
管程中封装了共享变量以及对共享变量的操作，如图的管程MESA模型中，封装了共享变量V，共享变量的操作方法X，Y，以及引入了条件变量和锁；
```
public class ArrayBlockingQueue<E>...{
    /** The queued items */
    final Object[] items;

    /** Main lock guarding all access */
    final ReentrantLock lock;

    /** Condition for waiting takes */
    private final Condition notEmpty;

    /** Condition for waiting puts */
    private final Condition notFull;

    public void put(E e) throws InterruptedException {
        Objects.requireNonNull(e);
        final ReentrantLock lock = this.lock;
	// 加锁
        lock.lockInterruptibly();
        try {
	    // 循坏判断队列是否已满
            while (count == items.length)
		// 如果队列已满，需要等待notFull条件变量的等待队列，知道队列有空余位
		// 置插入
                notFull.await();
	    // 队列入队操作
            enqueue(e);
        } finally {
	    // 入队完成后解锁
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            while (count == 0)
                notEmpty.await();
            return dequeue();
        } finally {
            lock.unlock();
        }
    }

    private void enqueue(E x) {
        // assert lock.getHoldCount() == 1;
        // assert items[putIndex] == null;
        final Object[] items = this.items;
        items[putIndex] = x;
        if (++putIndex == items.length) putIndex = 0;
        count++;
	// 入队操作成功后，表示队列非空，需要通知非空条件变量的等待队列，可以进入入口
	// 等待队列
        notEmpty.signal();
    }

    private E dequeue() {
        // assert lock.getHoldCount() == 1;
        // assert items[takeIndex] != null;
        final Object[] items = this.items;
        @SuppressWarnings("unchecked")
        E x = (E) items[takeIndex];
        items[takeIndex] = null;
        if (++takeIndex == items.length) takeIndex = 0;
        count--;
        if (itrs != null)
            itrs.elementDequeued();
        notFull.signal();
        return x;
    }
	...
}
```
上面的代码是Java实现的阻塞队列，它很好的反映了MESA管程模型，items是我们的共享变量，lock是可重入锁，实现互斥同步，notEmpty和notFull是两个条件变量，用来实现items数组空和满的状态，put和take方法是共享变量的操作方法；

```
while(条件不满足) {
  条件变量.await();
}
```
MESA管程的编程范式，当线程进入入口等待队列后，不会立即马上执行，还需要重新判断是否满足入口条件（因为可能此时已经变了），此时线程执行在while循环中，可以满足这一操作；
在线程T1中尝试通知线程T2唤醒，T1会继续执行，而T2会进入入口等待队列，知道满足条件，T2才会执行；

**notifyAll优于notify?**
使用notify唤醒指定的线程，可能因为唤醒失败，导致其它线程永远无法唤醒；而使用notifyAll通知所有的线程，尝试唤醒其中一个线程；

synchronized是Java内置的管程方案，能够自动加锁，解锁，但只有一个条件变量，和notify，wait，notifyAll使用，共同实现管程；

Lock+Condition是Java SDK提供的管程方案，需要手动加锁，解锁，但可以任意设置条件变量，更加灵活;

# 线程&线程的生命周期
# 线程池&Future

# Lock&Condition
java提供的两种管程方案，synchronized，Object的notify，notifyAll，wait以及Lock&Condition，Lock代替了synchronized互斥锁的功能，Condition代替了Object的notify，notifyAll，wait的条件同步功能，作为粒度更细，更灵活的互斥锁方案，Lock解决了并发中互斥的问题，Condition解决了并发中同步的问题；
synchronized管程方案，无法解决死锁问题，而Lock锁，通过破坏不可抢占条件，则能很好的解决：
- 支持超时  线程一段时间内获取锁失败，能够释放持有的锁
- 相应中断操作  阻塞的线程，收到中断信号后，能够唤醒它，并且有机会释放持有的锁
- 非阻塞的获取锁  获取锁失败，不会进入阻塞状态，返回失败，并且有机会释放持有的锁

可重入锁：线程可以重复的获取同一把锁
公平锁与非公平锁：等待队列中的线程被唤醒时，公平锁，会按照等待时长，选择排队最久的线程唤醒；非公平锁则

# 读写锁

# 乐观锁&悲观锁

# ReadWriteLock
# StampedLock

# Semaphore


# CountDownLatch&CyclicBarrier

# Java并发容器
# 原子