# HotSpot虚拟机

![title](https://raw.githubusercontent.com/xinjiuyijiu/NoteImages/master/gitnote/2020/07/21/jvm_memory-1595301442533.png)

Java class文件如何被JVM执行？
HotSpot包含两种方式将字节码翻译成可执行的机器码：
- 解释执行，逐条将机器码翻译并执行
- 即时编译JIT，将一个方法中所有的字节码编译成机器码后再执行

HotSpot同时使用了这两种方式，先解释执行，遇到反复执行的热点代码，以方法为单位，进行即时编译；

为什么使用JVM+解释执行/JIT的方式？为什么不使用机器码+编译器的方式？